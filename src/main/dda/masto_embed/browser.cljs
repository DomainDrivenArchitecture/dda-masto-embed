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
(ns dda.masto-embed.browser)

(def masto-embed "masto-embed")

(defn element-from-document-by-name [name]
  (-> js/document
      (.getElementById masto-embed)
      (.getAttribute name)))

(defn host-url-from-document []
  (element-from-document-by-name "host_url"))

(defn account-name-from-document []
  (element-from-document-by-name "account_name"))

(defn replies-to-from-document []
  (element-from-document-by-name "replies_to"))

(defn filter-favorited-from-document []
  (element-from-document-by-name "filter_favorited"))

(defn render-to-document
  [input]
  (-> js/document
      (.getElementById masto-embed)
      (.-innerHTML)
      (set! input)))
