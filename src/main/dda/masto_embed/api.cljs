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
   [cljs.core.async :refer [go]]
   [cljs.core.async.interop :refer-macros [<p!]]))

(s/def ::account-id string?)
(s/def ::host-url string?)

(defn exit-with-error [error]
  (js/console.error error)
  (js/process.exit 1))

(defn js->edn [data]
  (js->clj data :keywordize-keys true))

(defn-spec mastodon-client any? 
  [host-url ::host-url]
  (let [mastodon-config
        {:access_token "unused"
         :api_url (str host-url "/api/v1/")}]
    (some-> mastodon-config clj->js Mastodon.)))

(defn-spec get-account-statuses any?
  [host-url ::host-url
   account-id ::account-id
   callback fn?]
  (.then (.get (mastodon-client host-url) 
               (str "accounts/" account-id "/statuses") 
               #js {})
         #(let [response (-> % .-data js->edn)]
            (if-let [error (:error response)]
              (exit-with-error error)
              (callback response)))))

(defn-spec get-directory-old any? 
  [host-url ::host-url
   callback fn?]
  (.then (.get (mastodon-client host-url)
               (str "directory?local=true")
               #js {})
         #(let [response (-> % .-data js->edn)]
            (callback response))))

(defn-spec get-directory any?
  [host-url ::host-url]
  (go (let [result (<p! (.get (mastodon-client host-url)
                              (str "directory?local=true")
                              #js {}))]
        (-> result 
            .-data 
            js->edn))))
