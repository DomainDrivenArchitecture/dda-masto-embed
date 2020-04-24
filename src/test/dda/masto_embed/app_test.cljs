(ns dda.masto-embed.app-test
  (:require 
    [cljs.test :refer (deftest is)]
    [dda.masto-embed.app :as sut]))

(deftest a-failing-test
  (is (= 3
         (sut/add-one 2))))