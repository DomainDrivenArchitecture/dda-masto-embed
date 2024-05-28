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
(ns dda.masto-embed.to-html
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

(def link_preview
  [:a
   {:href "LINK_PREVIEW_URL", :class "mastodon-post-link-preview", :target "_blank"}
   [:img {:class "mastodon-post-link-image", :src "LINK_PREVIEW_IMG_URL"}]
   [:div
    {:class "mastodon-post-link-info"}
    [:h4 {:class "mastodon-post-link-title"} "LINK_PREVIEW_TITLE"]
    [:div {:class "mastodon-post-link-description"} "LINK_PREVIEW_DESC"]
    [:div {:class "mastodon-post-link-url"} "LINK_PREVIEW_URL"]]])

(defn insert [v i e] (vec (concat (subvec v 0 i) [e] (subvec v i))))

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
(defn insert-into-content [item insertion-element]
  (let [condition (if (vector? item)
                    (every? true? [(= (first item) :section)
                                  (= (:class (second item)) "mastodon-post-content")])
                    false)]
    (if condition       
        (conj item insertion-element)
      item)))

(defn insert-into-post [item index insertion-element]
  (let [condition (if (vector? item)
                    (every? true? [(= (first item) :article)
                                   (= (:class (second item)) "mastodon-post")])
                    false)]
    (if condition
        (insert item index insertion-element)
      item)))

(defn masto-media->html [html media_attachments]
  (if-let [preview-image-url (get-in media_attachments [0 :preview_url])]
      (let [image-element [:img {:class "mastodon-post-image", :src preview-image-url}]]
        (postwalk #(insert-into-content % image-element) html))
      html))

(defn insert-link-prev [html]
  (postwalk #(insert-into-post % 4 link_preview) html))

(defn masto-link-prev->html [html card]
  (let [{:keys [url image title description]} card]
    (-> html
        (insert-link-prev)
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_URL" url)
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_IMG_URL" image)
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_TITLE" title)
        (cm/replace-all-matching-values-by-new-value "LINK_PREVIEW_DESC" description))))

(defn masto-footer->html [html replies_count reblogs_count favourites_count]
  (-> html
      (cm/replace-all-matching-values-by-new-value "REPLIES_COUNT" replies_count)
      (cm/replace-all-matching-values-by-new-value "REBLOGS_COUNT" reblogs_count)
      (cm/replace-all-matching-values-by-new-value "FAVOURITES_COUNT" favourites_count)))

(defn masto->html [statuses]
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

(defn to-html [host-url account-name]
  (go
    (let [account-id (<! (find-account-id host-url account-name))
          status (->
                   (<p! (api/get-account-statuses host-url account-id))
                   api/mastojs->edn)]
      (->> status
           (filter #(= nil (:reblog %)))
           (filter #(= nil (:in_reply_to_account_id %)))
           (take 4)
           (masto->html)
           (render-html)
           (b/render-to-document)))))

; Execute in browser repl to get some infos about incoming data
(defn account-mode-debug [host-url account-name]
  (go
    (let [account-id (<! (find-account-id host-url account-name))
          status (->
                   (<p! (api/get-account-statuses host-url account-id))
                   api/mastojs->edn)]
      (->> status
           (filter #(= nil (:reblog %)))
           (filter #(= nil (:in_reply_to_account_id %)))
           (take 1)
           (infra/debug)
           ))))
