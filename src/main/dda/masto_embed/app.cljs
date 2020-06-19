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
   [dda.masto-embed.api :as api]
   [cljs.core.async :refer [go close! put! take! timeout chan <! >!]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [clojure.pprint :as pprint :refer [pprint]]))

(def masto-embed "masto-embed")

(defn host-url-from-document []
  (-> js/document
      (.getElementById masto-embed)
      (.getAttribute "host_url")))
(defn account-name-from-document []

  (-> js/document
      (.getElementById masto-embed)
      (.getAttribute "account_name")))

(defn account-id-from-document []
  (-> js/document
      (.getElementById masto-embed)
      (.getAttribute "account_id")))

(defn render-to-document
  [input]
  (-> js/document
      (.getElementById masto-embed)
      (.-innerHTML)
      (set! input)))

(defn debug [elem]
  (print elem)
  elem)


(defn find-account-id [host-url account-name]
  (let [in (chan)
        out (chan)]
    (go
      (->>
       (<! in)
       (filter #(= account-name (:acct %)))
       (map :id)
       first
       debug
       (>! out))
      (do (close! in)
          (close! out))
      )
    ;(api/get-directory host-url #(go (>! in %)))
    out))

(defn init []
  (go
    (let [account-id (or 
                      (account-id-from-document)
                      (<! (find-account-id "https://social.meissa-gmbh.de" "team")))]
      (print account-id)
      (api/get-account-statuses
       (host-url-from-document)
       account-id
       render-to-document))))

(go
  (let
   [result (<p! (api/get-directory "https://social.meissa-gmbh.de"))]
    result))