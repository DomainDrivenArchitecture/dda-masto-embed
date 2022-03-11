; Licensed to the Apache Software Foundation (ASF) under one
; or more contributor license agreements. See the NOTICE file
; distributed with this work for additional information
; regarding copyright ownership. The ASF licenses this file
; to you under the Apache License, Version 2.0 (the
; "License"); you may not use this file except in compliance
; with the License. You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
(ns dda.masto-embed.app
  (:require
   [cljs.core.async :refer [go close! put! take! timeout chan <! >!]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [hiccups.runtime :refer [render-html]]
   [dda.masto-embed.api :as api]
   [dda.masto-embed.infra :as infra]
   [dda.masto-embed.render-bootstrap :as rb]
   ))

(def masto-embed "masto-embed")

(defn element-from-document-by-name [name]
  (-> js/document
      (.getElementById masto-embed)
      (.getAttribute name)))

(defn host-url-from-document []
  (element-from-document-by-name "host_url"))

(defn account-name-from-document []
  (element-from-document-by-name "account_name"))

(defn account-id-from-document []
  (element-from-document-by-name "account_id"))

(defn replies-to-from-document []
  (element-from-document-by-name "replies_to"))

(defn filter-favorited-from-document []
  (element-from-document-by-name "filter_favorited"))

(defn render-to-document
  [input]
  (-> js/document
      (.getElementById masto-embed)
      (.-innerHTML)
      (set! input)))

(defn find-account-id [host-url account-name]
  (let [out (chan)]
    (go
      (>! out 
          (->>
           (<p! (api/get-directory host-url))
           api/mastojs->edn
           (filter #(= account-name (:acct %)))
           (infra/debug)
           (map :id)
           first)))
    out))

(defn favorited-replies? [host-url account-name reply-id]
  (let [out (chan)]
    (go
      (>! out
          (->>
           (<p! (api/get-favorited-by host-url reply-id))
           api/mastojs->edn
           (filter #(= account-name (:acct %)))
           (empty?)
           (not))))
    out))

(defn favorited? [host-url account-name replies]
  (let [out (chan)]
    (go
      (>! out
          (loop [loc-replies replies
                 result []]
            (if (empty? loc-replies)
              result
              (recur (rest loc-replies)
                     (conj result (<! (favorited-replies? host-url account-name (first loc-replies)))))))))
  out))

(defn account-mode [host-url account-name]
  (go
    (let [account-id (or
                      (account-id-from-document)
                      (<! (find-account-id host-url account-name)))
          statuus (->
                   (<p! (api/get-account-statuses host-url account-id))
                   api/mastojs->edn)]
      (->> statuus
           (take 4)
           (rb/masto->html)
           (render-html)
           (render-to-document)))))

(defn replies-mode [host-url account-name post-id filter-favorited]
  (go
    (let [replies (->
                   (<p! (api/get-replies host-url post-id))
                   api/mastojs->edn)
          favorited (<! (favorited? host-url account-name (map :id (:descendants replies))))
          combined (map (fn [s f] {:status s :favorited f}) (:descendants replies) favorited)]
      (->> combined
           (filter #(infra/debug (or (not filter-favorited) (:favorited %))))
           (map :status)
           (infra/debug)
           (rb/masto->html)
           (render-html)
           (render-to-document)))))

(defn init []
  (let [host-url (host-url-from-document)
        account-name (account-name-from-document)
        replies-to (replies-to-from-document)
        filter-favorited (filter-favorited-from-document)]
    (if (nil? replies-to)
      (account-mode host-url account-name)
      (replies-mode host-url account-name replies-to filter-favorited))
    ))