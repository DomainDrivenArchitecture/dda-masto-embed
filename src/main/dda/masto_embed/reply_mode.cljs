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
(ns dda.masto-embed.reply-mode
  (:require
   [cljs.core.async :refer [go close! put! take! timeout chan <! >!]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [hiccups.runtime :refer [render-html]]
   [cljs-time.format :as t]
   [dda.masto-embed.api :as api]
   [dda.masto-embed.infra :as infra]
   [dda.masto-embed.browser :as b]
   [dda.masto-embed.account-mode :as am]
   ))

(defn mastocard->html [card media]
  (when (some? card)
    (let [{:keys [title description image url]} card
          {:keys [type preview_url url]} (first media)]
      [:div {:class "card" :url url}
       (when (some? image)
         [:img {:class "card-img-top" :src image}])
       (when (and (some? type) (= type "image"))
         [:img {:class "card-img-top" :src preview_url}])
       [:p media]
       [:h3 {:class "card-title"} title]
       [:p {:class "card-body"} description]])))



(defn mastomedia->html [media]
  (when (some? media)
    (let [{:keys [id type preview_url url]} (first media)]
      [:div {:class "media"}
       (when (and (some? type) (= type "image"))
         [:img {:class "img-thumbnail" :width "100" :height "100"
                :src preview_url}])])))

(defn masto->html [statuses]
  [:ul {:class "list-group"}
   (map (fn [status]
          (let [{:keys [created_at card media_attachments]} status
                date (t/parse created_at)]
            [:li {:class "list-group-item, card"}
             [:div {:class "card-body row"}
              [:div {:class "col-sm"}
               [:h2 {:class "card-title"}
                [:a {:href (get-in status [:url])}
                 (t/unparse (t/formatter "dd.MM.yyyy") date) " "]]
               [:div {:class "card-text"}
                (:content status)]]
              [:div {:class "col-sm"} (mastomedia->html media_attachments)]]]))
        statuses)])

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
           (masto->html)
           (render-html)
           (b/render-to-document)))))
