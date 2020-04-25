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
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as st]
   [orchestra.core :refer-macros [defn-spec]]
   [clojure.pprint :as pprint :refer [pprint]]
   [cljs.core.async :refer [go]]
   [cljs.core.async.interop :refer-macros [<p!]]))

(s/def ::account-id string?)
(s/def ::host-url string?)

(defn-spec create-config map?
  [account-id ::account-id
   host-url ::host-url]
  {:access_token "unused"
   :account-id account-id
   :api_url (str host-url "/api/v1/") })

(defn exit-with-error [error]
  (js/console.error error)
  (js/process.exit 1))

(defn js->edn [data]
  (js->clj data :keywordize-keys true))

(defn mastodon-client [mastodon-config]
  (or (some-> mastodon-config clj->js Mastodon.)
      (exit-with-error "missing Mastodon client configuration!")))

(defn get-mastodon-timeline [mastodon-config callback]
  (.then (.get (mastodon-client mastodon-config) 
               (str "accounts/" (:account-id mastodon-config) "/statuses") 
               #js {})
         #(let [response (-> % .-data js->edn)]
            (if-let [error (:error response)]
              (exit-with-error error)
              (callback response)))))