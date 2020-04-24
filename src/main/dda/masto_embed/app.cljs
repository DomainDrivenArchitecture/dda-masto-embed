(ns dda.masto-embed.app
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

(defn add-one [a]
  (+ a 1))

;from yogthos / mastodon-bot

(defn exit-with-error [error]
  (js/console.error error)
  (js/process.exit 1))

(defn js->edn [data]
  (js->clj data :keywordize-keys true))

(def mastodon-config 
  {:access_token "XXXX"
   :account-id "2"
   :api_url "https://social.meissa-gmbh.de/api/v1/"})

(def mastodon-client (or (some-> mastodon-config clj->js Mastodon.)
                         (exit-with-error "missing Mastodon client configuration!")))

(defn get-mastodon-timeline [callback]
  (.then (.get mastodon-client (str "accounts/" (:account-id mastodon-config) "/statuses") #js {})
         #(let [response (-> % .-data js->edn)]
            (if-let [error (:error response)]
              (exit-with-error error)
              (callback response)))))

(defn render-to-document
  [input]
  (-> js/document
      (.getElementById "mastodon-timeline")
      (.-innerHTML)
      (set! input)))

(defn init []
  (get-mastodon-timeline render-to-document))