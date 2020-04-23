(ns masto-embed.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [masto-embed.core-test]))

(doo-tests 'masto-embed.core-test)

