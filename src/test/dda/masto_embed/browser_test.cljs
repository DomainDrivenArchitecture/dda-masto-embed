(ns dda.masto-embed.browser-test
  (:require
   [cljs.test :refer (deftest is)]
   [hickory.core :as h]
   [shadow.resource :as rc]
   [clojure.walk :as w]
   [dda.masto-embed.browser :as sut]))


(defn post-html []
  (h/as-hiccup
   (h/parse
    (rc/inline "dda/masto_embed/resources/post.html"))))

(defn post-html-no-media []
  (h/as-hickory
   (h/parse
    (rc/inline "dda/masto_embed/resources/post-min.html"))))

(deftest should-remove-media
  (is (= (post-html-no-media)
        (sut/remove-image-elem))))
