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

(ns dda.masto-embed.to-html-test
  (:require
   [cljs.test :refer (deftest is)]
   [dda.masto-embed.to-html :as sut]
   [dda.masto-embed.data-helpers :as dh]
   [hickory.core :as h]
   [hickory.convert :as hc]
   [shadow.resource :as rc]))

(def media_attachments
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
    :text_url nil}])

(def link_prev
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
   :html ""})

; TODO: Replace this test
;(deftest test-mastodon->html 
;  (is (= [:ul {:class "list-group"}
;          '([:li {:class "list-group-item, card"}
;             [:div {:class "card-body"}
;              [:h2 {:class "card-title"} [:a {:href "https://social.meissa-gmbh.de/users/team/statuses/104183256213204298/activity"} "2020-05-17" " " "10:12:10"]]
;              [:p {:class "card-text"} "<p>We&apos;ve a new asciicast ... </p>"
;               nil]]])]
;         (sut/masto->html statuses))))

(deftest test-masto-media->html
  (is (= dh/post-with-img
         (sut/masto-media->html dh/post-base-img media_attachments))))

(deftest test-insert-link-prev
  (is (= dh/post-with-prev
         (sut/insert-link-prev dh/post-base-prev))))

(deftest test-masto-link-prev->html
  (is (= dh/filled-post-with-prev
         (sut/masto-link-prev->html dh/post-base-prev link_prev))))

