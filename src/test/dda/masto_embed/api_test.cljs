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

(ns dda.masto-embed.api-test
  (:require
   [cljs.test :refer (deftest is)]
   [dda.masto-embed.api :as sut]))

(def statuus [{:mentions []
               :emojis []
               :tags []
               :reblog
               {:mentions []
                :emojis []
                :tags
                [{:name "hetzner"
                  :url "https://social.meissa-gmbh.de/tags/hetzner"}
                 {:name "devops", :url "https://social.meissa-gmbh.de/tags/devops"}
                 {:name "k8s", :url "https://social.meissa-gmbh.de/tags/k8s"}
                 {:name "cheap", :url "https://social.meissa-gmbh.de/tags/cheap"}]
                :reblog nil
                :replies_count 0
                :in_reply_to_account_id nil
                :reblogs_count 1
                :application nil
                :content
                "mentioned: <p>We&apos;ve a new asciicast ... </p>"
                :sensitive false
                :favourites_count 2
                :in_reply_to_id nil
                :poll nil
                :account
                {:acct "jerger"
                 :last_status_at "2020-06-14"
                 :emojis []
                 :bot false
                 :group false
                 :following_count 64
                 :avatar_static
                 "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/avatars/000/000/001/original/794ca61bfd71bbe1.jpg"
                 :fields
                 [{:name "blog"
                   :value
                   "<a href=\"https://domaindrivenarchitecture.org/\" rel=\"me nofollow noopener noreferrer\" target=\"_blank\"><span class=\"invisible\">https://</span><span class=\"\">domaindrivenarchitecture.org/</span><span class=\"invisible\"></span></a>"
                   :verified_at nil}
                  {:name "interests"
                   :value "Clojure, sci-fi, tech, DevOps, public weal"
                   :verified_at nil}
                  {:name "location"
                   :value "Reutlingen, de, eu"
                   :verified_at nil}]
                 :username "jerger"
                 :header_static
                 "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/headers/000/000/001/original/2a45f78fa1af0815.jpg"
                 :discoverable true
                 :statuses_count 135
                 :header
                 "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/headers/000/000/001/original/2a45f78fa1af0815.jpg"
                 :note
                 "<p>meissa GmbH, Maintainer, dda-pallet, Clojure, OpenSource, DevOps, DomainDrivenDesign, Demokratie, Bürgerbeteiligung, Europa, Klettern, Wandern</p>"
                 :locked false
                 :id "1"
                 :avatar
                 "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/avatars/000/000/001/original/794ca61bfd71bbe1.jpg"
                 :url "https://social.meissa-gmbh.de/@jerger"
                 :display_name "jerger"
                 :followers_count 24
                 :created_at "2019-06-02T10:41:41.919Z"}
                :card
                {:description
                 "We use our dda-k8s-crate to install kubernetes automatically with a nexux repository manager. See our GitHub page for more details :) https://github.com/DomainDrivenArchitecture/dda-k8s-crate"
                 :author_url ""
                 :width 400
                 :type "link"
                 :embed_url ""
                 :title "Automatic kubernetes installation"
                 :provider_name ""
                 :url "https://asciinema.org/a/329800"
                 :author_name ""
                 :image
                 "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/cache/preview_cards/images/000/024/180/original/8562ddca088c16e3.png"
                 :provider_url ""
                 :height 311
                 :html ""}
                :language "en"
                :id "104172691066899431"
                :url "https://social.meissa-gmbh.de/@jerger/104172691066899431"
                :media_attachments []
                :uri
                "https://social.meissa-gmbh.de/users/jerger/statuses/104172691066899431"
                :visibility "public"
                :created_at "2020-05-15T13:25:19.190Z"
                :spoiler_text ""}
               :replies_count 0
               :in_reply_to_account_id nil
               :reblogs_count 0
               :application nil
               :content
               "<p>We&apos;ve a new asciicast ... </p>"
               :sensitive false
               :favourites_count 0
               :in_reply_to_id nil
               :poll nil
               :account
               {:acct "team"
                :last_status_at "2020-05-17"
                :emojis []
                :bot false
                :group false
                :following_count 2
                :avatar_static
                "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/avatars/000/000/002/original/1aaff0c626a2ade3.png"
                :fields []
                :username "team"
                :header_static
                "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/headers/000/000/002/original/2e6a592b61a2d505.jpg"
                :discoverable true
                :statuses_count 12
                :header
                "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/headers/000/000/002/original/2e6a592b61a2d505.jpg"
                :note
                "<p>DevOps, Cloud, KI, Clojure, Java, Python, Reutlingen, Tübingen, Stuttgart, genossenschaftlich, OpenSource, TestDriven, Maintainer</p>"
                :locked false
                :id "2"
                :avatar
                "https://cf.mastohost.com/v1/AUTH_91eb37814936490c95da7b85993cc2ff/socialmeissagmbhde/accounts/avatars/000/000/002/original/1aaff0c626a2ade3.png"
                :url "https://social.meissa-gmbh.de/@team"
                :display_name "meissa-team"
                :followers_count 3
                :created_at "2019-06-02T10:50:28.976Z"}
               :card nil
               :language nil
               :id "104183256213204298"
               :url
               "https://social.meissa-gmbh.de/users/team/statuses/104183256213204298/activity"
               :media_attachments []
               :uri
               "https://social.meissa-gmbh.de/users/team/statuses/104183256213204298/activity"
               :visibility "public"
               :created_at "2020-05-17T10:12:10.403Z"
               :spoiler_text ""}])

(deftest test-mastodon-2-html 
  (is (= [:ul 
           '([:li
              [:h2 [:a {:href "https://social.meissa-gmbh.de/@team"}
                    "2020-05-17T10:12:10.403Z"]]
              "<p>We&apos;ve a new asciicast ... </p>"
              [:p ]])]
         (sut/masto->html statuus))))
