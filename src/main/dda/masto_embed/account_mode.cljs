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
   [cljs.core.async :refer [go chan <! >!]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [hiccups.runtime :refer [render-html]]
   [cljs-time.format :as t]
   [dda.masto-embed.api :as api]
   [dda.masto-embed.infra :as infra]
   [dda.masto-embed.browser :as b]
   [dda.c4k-common.common :as cm]
   [clojure.walk :refer [postwalk]]))

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
               [:a {:href (get-in status [:url])}
                (t/unparse (t/formatters :date) date) " "
                (t/unparse (t/formatters :hour-minute-second) date)]]
              [:p {:class "card-text"}
               (:content status)
               (mastocard->html card)]]]))
        statuses)])

(defn masto-header->html [html account created_at url]
  (let [{:keys [username display_name avatar_static]} account
        date (t/parse created_at)]
    (-> html
        (cm/replace-all-matching-values-by-new-value "AVATAR_URL" avatar_static)
        (cm/replace-all-matching-values-by-new-value "POST_URL" url)
        (cm/replace-all-matching-values-by-new-value "DISPLAY_NAME" display_name)
        (cm/replace-all-matching-values-by-new-value "ACCOUNT_NAME" (str "@" username))
        (cm/replace-all-matching-values-by-new-value "DATETIME" created_at)
        (cm/replace-all-matching-values-by-new-value "TIME" (t/unparse (t/formatter "EEEE, dd MMMM yyyy") date))
        )))

(defn masto-content->html [html content]
    (-> html
        (cm/replace-all-matching-values-by-new-value "POST_TEXT" content)))

; Meant to be used in postwalk on hiccup/hickory html-representation
(defn insert-into-class [item class insertion-element]
  (let [condition (every? true? [(map? item) 
                                 (= (:type item) :element) 
                                 (= (:attrs item) {:class class})])]
    (if condition 
      (let [content (:content item)]
        (assoc-in item [:content] (conj content insertion-element)))
      item)))

(defn masto-media->html [html media_attachments]
  (if-let [preview-image-url (get-in media_attachments [0 :preview_url])]
      (let [class-name "mastodon-post-content"
            image-element {:type :element, :attrs {:class "mastodon-post-image", :src nil}, :tag :img, :content preview-image-url}]
        (postwalk #(insert-into-class % class-name image-element) html))
      html))

(defn masto-link-prev->html [html card]
  (let [{:keys [url image title description]} card]
    (-> html 
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_URL" url)
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_IMG_URL" image)
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_TITLE" title)
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_DESC" description)
        )))

(defn masto-footer->html [html replies_count reblogs_count favourites_count]
  (-> html
      (cm/replace-all-matching-values-by-new-value "REPLIES_COUNT" replies_count)
      (cm/replace-all-matching-values-by-new-value "REBLOGS_COUNT" reblogs_count)
      (cm/replace-all-matching-values-by-new-value "FAVOURITES_COUNT" favourites_count)))

(defn masto->html2 [statuses]
  (let [html (b/post-html-hiccup)]
    (map (fn [status]
           (let [{:keys [account created_at content media_attachments replies_count reblogs_count favourites_count card url]} status]
             (-> html
                 (masto-header->html account created_at url)
                 (masto-content->html content)
                 (masto-media->html media_attachments)
                 (masto-link-prev->html card)
                 (masto-footer->html replies_count reblogs_count favourites_count))))
         statuses)))

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
           (masto->html2)
           (render-html)
           (b/render-to-document)))))

; Execute in browser repl to get some infos about incoming data
(defn account-mode-debug [host-url account-name]
  (go
    (let [account-id (<! (find-account-id host-url account-name))
          statuus (->
                   (<p! (api/get-account-statuses host-url account-id))
                   api/mastojs->edn)]
      (->> statuus
           (filter #(= nil (:reblog %)))
           (filter #(= nil (:in_reply_to_account_id %)))
           (take 1)
           (infra/debug)
           ))))
