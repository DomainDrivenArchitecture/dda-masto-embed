(ns masto-embed.app
  (:require [masto-embed.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init! false)
