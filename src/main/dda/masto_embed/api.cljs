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
(ns dda.masto-embed.api
  (:require
   ["mastodon-api" :as Mastodon]
   [dda.masto-embed.infra :as infra]
   [cljs-time.format :as t]
   [clojure.spec.alpha :as s]
   [orchestra.core :refer-macros [defn-spec]]))

(s/def ::account-id string?)
(s/def ::host-url string?)

(defn mastojs->edn [response]
  (-> response .-data infra/js->edn))

(defn-spec mastodon-client any? 
  [host-url ::host-url]
  (let [mastodon-config
        {:access_token "unused"
         :api_url (str host-url "/api/v1/")}]
    (some-> mastodon-config clj->js Mastodon.)))

(defn-spec get-account-statuses any?
  [host-url ::host-url
   account-id ::account-id]
  (.get (mastodon-client host-url)
        (str "accounts/" account-id "/statuses")
        #js {}))

(defn get-replies
  [host-url
   status-id]
  (.get (mastodon-client host-url)
        (str "statuses/" status-id "/context")
        #js {}))

(defn get-favorited-by
  [host-url
   status-id]
  (.get (mastodon-client host-url)
        (str "statuses/" status-id "/favourited_by")
        #js {}))

(defn-spec get-directory any?
  [host-url ::host-url]
  (.get (mastodon-client host-url)
        (str "directory?local=true")
        #js {}))