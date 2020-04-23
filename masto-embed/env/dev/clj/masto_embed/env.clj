(ns masto-embed.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [masto-embed.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[masto-embed started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[masto-embed has shut down successfully]=-"))
   :middleware wrap-dev})
