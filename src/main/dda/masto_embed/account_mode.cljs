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
(ns dda.masto-embed.account-mode
  (:require
   [cljs.core.async :refer [go close! put! take! timeout chan <! >!]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [hiccups.runtime :refer [render-html]]
   [cljs-time.format :as t]
   [dda.masto-embed.api :as api]
   [dda.masto-embed.infra :as infra]
   [dda.masto-embed.browser :as b]
   ))

(defn mastocard->html [card]
  (when (some? card)
    (let [{:keys [title description image url]} card]
      [:div {:class "card" :url url}
       (when (some? image)
         [:img {:class "card-img-top" :src image}])
       [:h3 {:class "card-title"} title]
       [:p {:class "card-body"} description]])))

(defn masto->html [statuses]
  [:ul {:class "list-group"}
   (map (fn [status]
          (let [{:keys [created_at card]} status
                date (t/parse created_at)]
            [:li {:class "list-group-item, card"}
             [:div {:class "card-body"}
              [:h2 {:class "card-title"}
               [:a {:href (get-in status [:card :url])}
                (t/unparse (t/formatters :date) date) " "
                (t/unparse (t/formatters :hour-minute-second) date)]]
              [:p {:class "card-text"}
               (:content status)
               (mastocard->html card)]]]))
        statuses)])

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

; (infra/debug)

(defn account-mode [host-url account-name]
  (go
    (let [account-id (<! (find-account-id host-url account-name))
          statuus (->
                   (<p! (api/get-account-statuses host-url account-id))
                   api/mastojs->edn)]
      (->> statuus
           (filter #(= nil (:reblog %)))
           (filter #(= nil (:in_reply_to_account_id %)))
           (take 4)
           (masto->html)
           (render-html)
           (b/render-to-document)))))
