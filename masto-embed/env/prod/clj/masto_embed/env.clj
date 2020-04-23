(ns masto-embed.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[masto-embed started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[masto-embed has shut down successfully]=-"))
   :middleware identity})
