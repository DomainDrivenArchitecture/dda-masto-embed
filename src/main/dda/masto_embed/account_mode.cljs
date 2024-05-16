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
   [dda.c4k-common.common :as cm]
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

(defn masto-media->html [html media_attachments]
  (let [{:keys [media]} media_attachments]
    (-> html
        (cm/replace-all-matching-values-by-new-value "POST_IMG_URL" media))))

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

{:mentions
 [{:id "109517000301838437",
   :username "J12t",
   :url "https://social.coop/@J12t",
   :acct "J12t@social.coop"}],
 :emojis [],
 :tags
 [{:name "federation",
   :url "https://social.meissa-gmbh.de/tags/federation"}
  {:name "test", :url "https://social.meissa-gmbh.de/tags/test"}],
 :reblog nil,
 :replies_count 1,
 :in_reply_to_account_id nil,
 :reblogs_count 0,
 :content
 "<p><span class=\"h-card\" translate=\"no\"><a href=\"https://social.coop/@J12t\" class=\"u-url mention\">@<span>J12t</span></a></span> Hi Johannes, do you have a solution for such integration tests in place / planed?</p><p><a href=\"https://codeberg.org/forgejo/forgejo/src/commit/fe3473fc8b7b51e024b1a564fc7f01e385ebfb5e/tests/integration/api_activitypub_repository_test.go#L76-L115\" target=\"_blank\" rel=\"nofollow noopener noreferrer\" translate=\"no\"><span class=\"invisible\">https://</span><span class=\"ellipsis\">codeberg.org/forgejo/forgejo/s</span><span class=\"invisible\">rc/commit/fe3473fc8b7b51e024b1a564fc7f01e385ebfb5e/tests/integration/api_activitypub_repository_test.go#L76-L115</span></a></p><p>Would love to be able to make better integration tests ...</p><p><a href=\"https://social.meissa-gmbh.de/tags/federation\" class=\"mention hashtag\" rel=\"tag\">#<span>federation</span></a> <a href=\"https://social.meissa-gmbh.de/tags/test\" class=\"mention hashtag\" rel=\"tag\">#<span>test</span></a></p>",
 :sensitive false,
 :favourites_count 1,
 :in_reply_to_id nil,
 :poll nil,
 :account
 {:acct "meissa",
  :last_status_at "2024-05-15",
  :emojis [],
  :bot false,
  :group false,
  :following_count 80,
  :avatar_static
  "https://cdn.masto.host/socialmeissagmbhde/accounts/avatars/112/400/753/820/571/578/original/fd05f46bcc0c5c69.png",
  :roles [],
  :fields
  [{:name "See also",
    :value
    "<a href=\"https://meissa.de\" target=\"_blank\" rel=\"nofollow noopener noreferrer me\" translate=\"no\"><span class=\"invisible\">https://</span><span class=\"\">meissa.de</span><span class=\"invisible\"></span></a>",
    :verified_at "2024-05-14T09:08:20.463+00:00"}
   {:name "OpenPGP",
    :value "DF43207F1ABF673D8C7F5D1D756A2A4873B93D34",
    :verified_at nil}],
  :username "meissa",
  :header_static
  "https://cdn.masto.host/socialmeissagmbhde/accounts/headers/112/400/753/820/571/578/original/915998366b020667.jpg",
  :discoverable true,
  :statuses_count 10,
  :header
  "https://cdn.masto.host/socialmeissagmbhde/accounts/headers/112/400/753/820/571/578/original/915998366b020667.jpg",
  :note
  "<p>DevOps, Cloud, KI, Clojure, Kotlin, JVM, Python &amp; k8s, Germany, Reutlingen, Tübingen, Stuttgart, genossenschaftlich, OpenSource, TestDriven, Maintainer, Forgejo, Federation</p>",
  :noindex false,
  :locked false,
  :id "112400753820571578",
  :avatar
  "https://cdn.masto.host/socialmeissagmbhde/accounts/avatars/112/400/753/820/571/578/original/fd05f46bcc0c5c69.png",
  :url "https://social.meissa-gmbh.de/@meissa",
  :uri "https://social.meissa-gmbh.de/users/meissa",
  :display_name "meissa-team",
  :followers_count 172,
  :created_at "2024-05-07T00:00:00.000Z"},
 :card
 {:description "forgejo - Beyond coding. We forge.",
  :author_url "",
  :image_description "",
  :width 290,
  :type "link",
  :embed_url "",
  :blurhash "URC3:FsU1xJS-8NvJ9$OFIS3wexEJ9n*xEbG",
  :title
  "forgejo/tests/integration/api_activitypub_repository_test.go at fe3473fc8b7b51e024b1a564fc7f01e385ebfb5e",
  :published_at nil,
  :provider_name "Codeberg.org",
  :language "en",
  :url
  "https://codeberg.org//forgejo/forgejo/src/commit/fe3473fc8b7b51e024b1a564fc7f01e385ebfb5e/tests/integration/api_activitypub_repository_test.go",
  :author_name "",
  :image
  "https://cdn.masto.host/socialmeissagmbhde/cache/preview_cards/images/000/545/643/original/199336f5aa5b9683.png",
  :provider_url "",
  :height 290,
  :html ""},
 :language "en",
 :id "112446229070164194",
 :url "https://social.meissa-gmbh.de/@meissa/112446229070164194",
 :media_attachments [],
 :uri
 "https://social.meissa-gmbh.de/users/meissa/statuses/112446229070164194",
 :edited_at nil,
 :visibility "public",
 :created_at "2024-05-15T17:14:50.257Z",
 :spoiler_text ""}

:media_attachments
[{:description "Plastikmüll gesammelt",
  :meta
  {:original
   {:width 1500, :height 2000, :size "1500x2000", :aspect 0.75},
   :small
   {:width 416,
    :height 554,
    :size "416x554",
    :aspect 0.7509025270758123}},
  :type "image",
  :blurhash "UAFiMmx^9aE1yEjEM|%N0eD%w]t7D$%NR4tR",
  :preview_url
  "https://cdn.masto.host/socialmeissagmbhde/media_attachments/files/112/432/505/467/393/505/small/0d01ddb07440328e.jpg",
  :preview_remote_url nil,
  :id "112432505467393505",
  :url
  "https://cdn.masto.host/socialmeissagmbhde/media_attachments/files/112/432/505/467/393/505/original/0d01ddb07440328e.jpg",
  :remote_url nil,
  :text_url nil}],