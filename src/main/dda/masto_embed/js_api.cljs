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
(ns dda.masto-embed.js-api
  (:require
   ["mastodon-api" :as Mastodon]
   [clojure.pprint :as pprint :refer [pprint]]
   [cljs.core.async :refer [go]]
   [cljs.core.async.interop :refer-macros [<p!]]))

(defn get-content-seq [response]
  (map
   #(aget % "content")
   (array-seq
    (aget response "data"))))

(defn luccas-fn []
  (let [config (js-obj "api_url" "https://social.meissa-gmbh.de/api/v1/" "access_token" "...")
        masto (new Mastodon config)
        rest-endpoint "accounts/:id/statuses"
        id-config (js-obj "id" "2")
        result (go
                 (let [response (<p! (.get masto rest-endpoint id-config))]
                   (get-content-seq response)))]
    (pprint result)
    result))
