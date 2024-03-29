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

(ns dda.masto-embed.reply-mode-test
  (:require
   [cljs.test :refer (deftest is)]
   [dda.masto-embed.reply-mode :as sut]))

(def statuses [{:mentions
                [{:id "2",
                  :username "team",
                  :url "https://social.meissa-gmbh.de/@team",
                  :acct "team"}],
                :emojis [],
                :tags [],
                :reblog nil,
                :replies_count 0,
                :in_reply_to_account_id "2",
                :reblogs_count 0,
                :application nil,
                :content
                "<p><span class=\"h-card\"><a href=\"https://social.meissa-gmbh.de/@team\" class=\"u-url mention\">@<span>team</span></a></span> Hier mein erstes Bild :-)</p>",
                :sensitive false,
                :favourites_count 2,
                :in_reply_to_id "107937234506182462",
                :poll nil,
                :account
                {:acct "jerger",
                 :last_status_at "2022-03-11",
                 :emojis [],
                 :bot false,
                 :group false,
                 :following_count 220,
                 :avatar_static
                 "https://cdn.masto.host/socialmeissagmbhde/accounts/avatars/000/000/001/original/794ca61bfd71bbe1.jpg",
                 :fields
                 [{:name "blog",
                   :value
                   "<a href=\"https://domaindrivenarchitecture.org/\" rel=\"me nofollow noopener noreferrer\" target=\"_blank\"><span class=\"invisible\">https://</span><span class=\"\">domaindrivenarchitecture.org/</span><span class=\"invisible\"></span></a>",
                   :verified_at nil}
                  {:name "interests",
                   :value "Clojure, sci-fi, tech, DevOps, public weal",
                   :verified_at nil}
                  {:name "location", :value "Reutlingen, de, eu", :verified_at nil}
                  {:name "OpenPGP",
                   :value "4BBFF05ED0F97346DF15033D1A2CCFAF0C8C2BB6",
                   :verified_at nil}],
                 :username "jerger",
                 :header_static
                 "https://cdn.masto.host/socialmeissagmbhde/accounts/headers/000/000/001/original/2a45f78fa1af0815.jpg",
                 :discoverable true,
                 :statuses_count 295,
                 :header
                 "https://cdn.masto.host/socialmeissagmbhde/accounts/headers/000/000/001/original/2a45f78fa1af0815.jpg",
                 :note
                 "<p>meissa GmbH, Maintainer, dda-pallet, Clojure, OpenSource, DevOps, DomainDrivenDesign, Demokratie, Bürgerbeteiligung, Europa, Klettern, Wandern</p>",
                 :locked false,
                 :id "1",
                 :avatar
                 "https://cdn.masto.host/socialmeissagmbhde/accounts/avatars/000/000/001/original/794ca61bfd71bbe1.jpg",
                 :url "https://social.meissa-gmbh.de/@jerger",
                 :display_name "jerger",
                 :followers_count 58,
                 :created_at "2019-06-02T00:00:00.000Z"},
                :card nil,
                :language nil,
                :id "107937257700481042",
                :url "https://social.meissa-gmbh.de/@jerger/107937257700481042",
                :media_attachments
                [{:description nil,
                  :meta
                  {:original
                   {:width 1247,
                    :height 1663,
                    :size "1247x1663",
                    :aspect 0.7498496692723993},
                   :small
                   {:width 346,
                    :height 461,
                    :size "346x461",
                    :aspect 0.7505422993492408}},
                  :type "image",
                  :blurhash "UIJkAFt,%gIv}~9tIUWYOvRkD*xZw6$hRj%1",
                  :preview_url
                  "https://cdn.masto.host/socialmeissagmbhde/media_attachments/files/107/937/248/257/634/217/small/923e75c7684a2c31.jpg",
                  :preview_remote_url nil,
                  :id "107937248257634217",
                  :url
                  "https://cdn.masto.host/socialmeissagmbhde/media_attachments/files/107/937/248/257/634/217/original/923e75c7684a2c31.jpg",
                  :remote_url nil,
                  :text_url nil}],
                :uri
                "https://social.meissa-gmbh.de/users/jerger/statuses/107937257700481042",
                :edited_at nil,
                :visibility "public",
                :created_at "2022-03-11T09:44:07.235Z",
                :spoiler_text ""}])

(deftest test-mastodon->html 
  (is (= [:ul {:class "list-group"} 
          '([:li {:class "list-group-item, card"} 
             [:div {:class "card-body row"} 
              [:div {:class "col-sm"} 
               [:h2 {:class "card-title"} 
                [:a {:href "https://social.meissa-gmbh.de/@jerger/107937257700481042"} "2022-03-11" " " "09:44:07"]] 
               [:div {:class "card-text"} "<p><span class=\"h-card\"><a href=\"https://social.meissa-gmbh.de/@team\" class=\"u-url mention\">@<span>team</span></a></span> Hier mein erstes Bild :-)</p>"]] 
              [:div {:class "col-sm"} 
               [:div {:class "media"} 
                [:img {:class "img-thumbnail", :width "100", :height "100", :src "https://cdn.masto.host/socialmeissagmbhde/media_attachments/files/107/937/248/257/634/217/small/923e75c7684a2c31.jpg"}]]]]])]
         (sut/masto->html statuses))))
