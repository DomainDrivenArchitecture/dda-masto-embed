(ns dda.masto-embed.app
  (:require ["mastodon-api" :as Mastodon]
            [clojure.pprint :as pprint :refer [pprint]]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))

(defn get-content-seq [response]
  (map
   #(aget % "content")
   (array-seq
    (aget response "data"))))

(defn init []
  (let [config (js-obj "api_url" "https://social.meissa-gmbh.de/api/v1/" "access_token" "...")
        masto (new Mastodon config)
        rest-endpoint "accounts/:id/statuses"
        id-config (js-obj "id" "2")]
    (pprint
     (go
       (let [response (<p! (.get masto rest-endpoint id-config))]
         (get-content-seq response))))))

(defn add-one [a]
  (+ a 1))