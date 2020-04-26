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
(ns dda.masto-embed.app
  (:require
   [dda.masto-embed.api :as api]
   [clojure.pprint :as pprint :refer [pprint]]))

(def masto-embed "masto-embed")

(defn mastodon-config-from-document []
  (let [masto-embed (.getElementById js/document masto-embed)
        host-url (.getAttribute masto-embed "host_url")
        account-id (.getAttribute masto-embed "account_id")]
    (api/create-config account-id host-url)))

(defn render-to-document
  [input]
  (-> js/document
      (.getElementById masto-embed)
      (.-innerHTML)
      (set! input)))

(defn init []
  (api/get-account-statuses
   (mastodon-config-from-document) 
   render-to-document))