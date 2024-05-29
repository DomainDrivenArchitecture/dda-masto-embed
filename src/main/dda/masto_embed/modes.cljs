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
(ns dda.masto-embed.modes
  (:require
   [cljs.core.async :refer [go close! put! take! timeout chan <! >!]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [hiccups.runtime :refer [render-html]]
   [cljs-time.format :as t]
   [dda.masto-embed.api :as api]
   [dda.masto-embed.infra :as infra]
   [dda.masto-embed.browser :as b]
   [dda.masto-embed.to-html :as th]
   ))

; TODO?: Functions in this ns mix business logic and api calls.
;        Should we separate better etween business and infra logic?

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

(defn find-account-id [host-url account-name]
  (let [out (chan)]
    (go
      (>! out
          (->>
           (<p! (api/get-directory host-url))
           api/mastojs->edn
           (filter #(= account-name (:acct %)))
           (map :id)
           first)))
    out))

(defn replies-mode [host-url account-name post-id filter-favorited]
  (go
    (let [replies (->
                   (<p! (api/get-replies host-url post-id))
                   api/mastojs->edn)
          favorited (<! (favorited? host-url account-name (map :id (:descendants replies))))
          combined (map (fn [s f] {:status s :favorited f}) (:descendants replies) favorited)]
      (->> combined
           (filter #(or (not filter-favorited) (:favorited %)))
           (reverse)
           (map :status)
           (th/masto->html)
           (render-html)
           (b/render-to-document)))))

(defn account-mode [host-url account-name]
  (go
    (let [account-id (<! (find-account-id host-url account-name))
          status (->
                  (<p! (api/get-account-statuses host-url account-id))
                  api/mastojs->edn)]
      (->> status
           (filter #(= nil (:reblog %)))
           (filter #(= nil (:in_reply_to_account_id %)))
           (take 4)
           (th/masto->html)
           (render-html)
           (b/render-to-document)))))
