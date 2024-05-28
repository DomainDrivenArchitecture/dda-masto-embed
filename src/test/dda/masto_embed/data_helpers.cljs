(ns dda.masto-embed.data-helpers)

(def post-base-img
  {:type :document,
   :content
   [{:type :element,
     :attrs nil,
     :tag :html,
     :content
     [{:type :element, :attrs nil, :tag :head, :content nil}
      {:type :element,
       :attrs nil,
       :tag :body,
       :content
       [{:type :element,
         :attrs {:class "section post-container"},
         :tag :section,
         :content
         [{:type :element,
           :attrs {:class "mastodon-post"},
           :tag :article,
           :content
           [{:type :element,
             :attrs {:class "mastodon-post-header"},
             :tag :header,
             :content
             [{:type :element, :attrs {:class "mastodon-post-avatar", :src "AVATAR_URL"}, :tag :img, :content nil}
              {:type :element,
               :attrs {:class "mastodon-post-names"},
               :tag :div,
               :content
               [{:type :element, :attrs {:class "display-name", :href "POST_URL"}, :tag :a, :content ["DISPLAY_NAME"]}
                {:type :element, :attrs {:class "account-name", :href "POST_URL"}, :tag :a, :content ["ACCOUNT_NAME"]}]}
              {:type :element,
               :attrs {:class "mastodon-post-date", :datetime "DATETIME"},
               :tag :time,
               :content ["TIME"]}]}
            {:type :element,
             :attrs {:class "mastodon-post-content"},
             :tag :section,
             :content [{:type :element, :attrs {:class "mastodon-post-text"}, :tag :p, :content ["POST_TEXT"]}]}
            {:type :element,
             :attrs {:class "mastodon-post-footer"},
             :tag :footer,
             :content
             [{:type :element,
               :attrs {:class "footer-button replies"},
               :tag :button,
               :content
               [{:type :element,
                 :attrs {:xmlns "http://www.w3.org/2000/svg", :viewbox "0 0 23.999989 18.905102", :fill "currentColor"},
                 :tag :svg,
                 :content
                 [{:type :element,
                   :attrs
                   {:d
                    "M 12.605469 0 C 6.3127313 -3.016065e-07 1.2128906 3.6227598 1.2128906 8.0917969 C 1.2082806 9.3482967 1.6202949 10.587431 2.4179688 11.708984 C 2.4578657 11.764164 2.498768 11.811806 2.5390625 11.865234 C 3.3268045 11.641832 4.3869061 11.848285 5.3300781 12.486328 C 6.3288461 13.162012 6.9253832 14.138795 6.953125 14.988281 C 7.1369873 15.068801 7.3124925 15.149004 7.5117188 15.232422 C 9.0912976 15.798243 10.836341 16.090505 12.605469 16.087891 C 16.920323 16.086691 20.863977 14.35437 22.792969 11.613281 C 23.580255 10.506333 23.991872 9.2846052 23.998047 8.0449219 C 23.962291 3.5975966 18.876044 0 12.605469 0 z M 23.998047 8.0449219 C 23.998174 8.0606359 24 8.0760629 24 8.0917969 L 24 7.9960938 C 24.00006 8.0124607 23.998147 8.0285639 23.998047 8.0449219 z M 2.9121094 12.222656 C 2.2425334 12.223796 1.667313 12.46211 1.3457031 12.9375 C 1.1638316 13.204122 1.077675 13.531936 1.09375 13.890625 C 1.1537212 14.293189 1.209808 14.432962 1.3125 14.671875 C 1.4316055 14.948973 1.7207031 15.40625 1.7207031 15.40625 C 1.9907532 15.764415 2.3364315 16.089696 2.7304688 16.355469 C 3.6907784 17.004767 4.8168668 17.230272 5.640625 16.9375 C 5.9710419 16.821946 6.2362892 16.627161 6.4160156 16.369141 C 7.0592353 15.418362 6.445179 13.878941 5.0449219 12.931641 C 4.3447931 12.457991 3.5816854 12.221516 2.9121094 12.222656 z M 1.0195312 16.197266 C 0.64478833 16.1979 0.32257415 16.331594 0.14257812 16.597656 C 0.040789845 16.746877 -0.007044805 16.928158 0.001953125 17.128906 C 0.035517005 17.35421 0.065572735 17.432694 0.12304688 17.566406 C 0.18970686 17.72149 0.3515625 17.978516 0.3515625 17.978516 C 0.50270196 18.178971 0.69743713 18.361021 0.91796875 18.509766 C 1.4554271 18.873168 2.0858405 18.99784 2.546875 18.833984 C 2.7316813 18.769352 2.8798841 18.661846 2.9804688 18.517578 C 3.3404608 17.985454 2.9965753 17.123927 2.2128906 16.59375 C 1.8210482 16.328648 1.3942742 16.196631 1.0195312 16.197266 z "},
                   :tag :path,
                   :content nil}]}
                {:type :element, :attrs {:class "count  reply-count"}, :tag :span, :content ["REPLIES_COUNT"]}]}
              {:type :element,
               :attrs {:class "footer-button retoots"},
               :tag :button,
               :content
               ["src/main/dda/masto_embed/resources/post.html"
                {:type :element,
                 :attrs {:xmlns "http://www.w3.org/2000/svg", :viewbox "0 0 24 15.292642", :fill "currentColor"},
                 :tag :svg,
                 :content
                 [{:type :element,
                   :attrs
                   {:d
                    "M 5.5533678,0 C 3.6714839,2.400492 1.6157603,5.1817846 0,7.362011 c 1.4380083,0 2.4385201,-5.881e-4 3.6172864,0.024507 v 3.36241 c 0,2.591649 -0.00735,3.641069 -0.00735,4.541214 0.9377142,0 1.8786511,0.0025 4.6000315,0.0025 h 9.6117861 c -0.967065,-1.240489 -1.863419,-2.423552 -2.791388,-3.644245 -2.391113,-0.01058 -5.4310727,0 -7.7149168,0 0,-1.389694 0.00491,-2.9515088 0.00491,-4.2863375 H 10.998903 C 9.4664195,5.0599896 7.0361202,1.897534 5.5533678,0 Z m 0.6249377,0 c 0.9523292,1.225788 1.9124438,2.5142572 2.7766839,3.6368923 2.3911146,0.010578 5.4433266,0.00491 7.7271706,0.00491 0,1.3896944 -0.0025,2.9515086 -0.0025,4.2863373 h -3.678556 c 1.532486,2.3020214 3.962784,5.4669284 5.445536,7.3644624 1.881875,-2.400496 3.9376,-5.18424 5.55336,-7.3644663 -1.438009,0 -2.440971,5.881e-4 -3.619738,-0.024507 V 4.5412139 c 0,-2.5916487 0.0098,-3.64106836 0.0098,-4.5412139 -0.937714,0 -1.881102,0 -4.602482,0 z"},
                   :tag :path,
                   :content nil}]}
                {:type :element, :attrs {:class "count retoot-count"}, :tag :span, :content ["REBLOGS_COUNT"]}]}
              {:type :element,
               :attrs {:class "footer-button likes"},
               :tag :button,
               :content
               [{:type :element,
                 :attrs {:xmlns "http://www.w3.org/2000/svg", :viewbox "0 0 24 22.799999", :fill "currentColor"},
                 :tag :svg,
                 :content
                 [{:type :element,
                   :attrs
                   {:d
                    "M 12,18.324 19.416,22.8 17.448,14.364 24,8.688 15.372,7.956 12,0 8.628,7.956 0,8.688 6.552,14.364 4.584,22.8 Z"},
                   :tag :path,
                   :content nil}]}
                {:type :element,
                 :attrs {:class "count like-count"},
                 :tag :span,
                 :content ["FAVOURITES_COUNT"]}]}]}]}]}]}]}]})

(def post-with-img
  {:type :document,
   :content
   [{:type :element,
     :attrs nil,
     :tag :html,
     :content
     [{:type :element, :attrs nil, :tag :head, :content nil}
      {:type :element,
       :attrs nil,
       :tag :body,
       :content
       [{:type :element,
         :attrs {:class "section post-container"},
         :tag :section,
         :content
         [{:type :element,
           :attrs {:class "mastodon-post"},
           :tag :article,
           :content
           [{:type :element,
             :attrs {:class "mastodon-post-header"},
             :tag :header,
             :content
             [{:type :element, :attrs {:class "mastodon-post-avatar", :src "AVATAR_URL"}, :tag :img, :content nil}
              {:type :element,
               :attrs {:class "mastodon-post-names"},
               :tag :div,
               :content
               [{:type :element, :attrs {:class "display-name", :href "POST_URL"}, :tag :a, :content ["DISPLAY_NAME"]}
                {:type :element, :attrs {:class "account-name", :href "POST_URL"}, :tag :a, :content ["ACCOUNT_NAME"]}]}
              {:type :element,
               :attrs {:class "mastodon-post-date", :datetime "DATETIME"},
               :tag :time,
               :content ["TIME"]}]}
            {:type :element,
             :attrs {:class "mastodon-post-content"},
             :tag :section,
             :content
             [{:type :element, :attrs {:class "mastodon-post-text"}, :tag :p, :content ["POST_TEXT"]}
              {:type :element,
               :attrs
               {:class "mastodon-post-image",
                :src
                "https://cdn.masto.host/socialmeissagmbhde/media_attachments/files/112/432/505/467/393/505/small/0d01ddb07440328e.jpg"},
               :tag :img,
               :content nil}]}
            {:type :element,
             :attrs {:class "mastodon-post-footer"},
             :tag :footer,
             :content
             [{:type :element,
               :attrs {:class "footer-button replies"},
               :tag :button,
               :content
               [{:type :element,
                 :attrs {:xmlns "http://www.w3.org/2000/svg", :viewbox "0 0 23.999989 18.905102", :fill "currentColor"},
                 :tag :svg,
                 :content
                 [{:type :element,
                   :attrs
                   {:d
                    "M 12.605469 0 C 6.3127313 -3.016065e-07 1.2128906 3.6227598 1.2128906 8.0917969 C 1.2082806 9.3482967 1.6202949 10.587431 2.4179688 11.708984 C 2.4578657 11.764164 2.498768 11.811806 2.5390625 11.865234 C 3.3268045 11.641832 4.3869061 11.848285 5.3300781 12.486328 C 6.3288461 13.162012 6.9253832 14.138795 6.953125 14.988281 C 7.1369873 15.068801 7.3124925 15.149004 7.5117188 15.232422 C 9.0912976 15.798243 10.836341 16.090505 12.605469 16.087891 C 16.920323 16.086691 20.863977 14.35437 22.792969 11.613281 C 23.580255 10.506333 23.991872 9.2846052 23.998047 8.0449219 C 23.962291 3.5975966 18.876044 0 12.605469 0 z M 23.998047 8.0449219 C 23.998174 8.0606359 24 8.0760629 24 8.0917969 L 24 7.9960938 C 24.00006 8.0124607 23.998147 8.0285639 23.998047 8.0449219 z M 2.9121094 12.222656 C 2.2425334 12.223796 1.667313 12.46211 1.3457031 12.9375 C 1.1638316 13.204122 1.077675 13.531936 1.09375 13.890625 C 1.1537212 14.293189 1.209808 14.432962 1.3125 14.671875 C 1.4316055 14.948973 1.7207031 15.40625 1.7207031 15.40625 C 1.9907532 15.764415 2.3364315 16.089696 2.7304688 16.355469 C 3.6907784 17.004767 4.8168668 17.230272 5.640625 16.9375 C 5.9710419 16.821946 6.2362892 16.627161 6.4160156 16.369141 C 7.0592353 15.418362 6.445179 13.878941 5.0449219 12.931641 C 4.3447931 12.457991 3.5816854 12.221516 2.9121094 12.222656 z M 1.0195312 16.197266 C 0.64478833 16.1979 0.32257415 16.331594 0.14257812 16.597656 C 0.040789845 16.746877 -0.007044805 16.928158 0.001953125 17.128906 C 0.035517005 17.35421 0.065572735 17.432694 0.12304688 17.566406 C 0.18970686 17.72149 0.3515625 17.978516 0.3515625 17.978516 C 0.50270196 18.178971 0.69743713 18.361021 0.91796875 18.509766 C 1.4554271 18.873168 2.0858405 18.99784 2.546875 18.833984 C 2.7316813 18.769352 2.8798841 18.661846 2.9804688 18.517578 C 3.3404608 17.985454 2.9965753 17.123927 2.2128906 16.59375 C 1.8210482 16.328648 1.3942742 16.196631 1.0195312 16.197266 z "},
                   :tag :path,
                   :content nil}]}
                {:type :element, :attrs {:class "count  reply-count"}, :tag :span, :content ["REPLIES_COUNT"]}]}
              {:type :element,
               :attrs {:class "footer-button retoots"},
               :tag :button,
               :content
               ["src/main/dda/masto_embed/resources/post.html"
                {:type :element,
                 :attrs {:xmlns "http://www.w3.org/2000/svg", :viewbox "0 0 24 15.292642", :fill "currentColor"},
                 :tag :svg,
                 :content
                 [{:type :element,
                   :attrs
                   {:d
                    "M 5.5533678,0 C 3.6714839,2.400492 1.6157603,5.1817846 0,7.362011 c 1.4380083,0 2.4385201,-5.881e-4 3.6172864,0.024507 v 3.36241 c 0,2.591649 -0.00735,3.641069 -0.00735,4.541214 0.9377142,0 1.8786511,0.0025 4.6000315,0.0025 h 9.6117861 c -0.967065,-1.240489 -1.863419,-2.423552 -2.791388,-3.644245 -2.391113,-0.01058 -5.4310727,0 -7.7149168,0 0,-1.389694 0.00491,-2.9515088 0.00491,-4.2863375 H 10.998903 C 9.4664195,5.0599896 7.0361202,1.897534 5.5533678,0 Z m 0.6249377,0 c 0.9523292,1.225788 1.9124438,2.5142572 2.7766839,3.6368923 2.3911146,0.010578 5.4433266,0.00491 7.7271706,0.00491 0,1.3896944 -0.0025,2.9515086 -0.0025,4.2863373 h -3.678556 c 1.532486,2.3020214 3.962784,5.4669284 5.445536,7.3644624 1.881875,-2.400496 3.9376,-5.18424 5.55336,-7.3644663 -1.438009,0 -2.440971,5.881e-4 -3.619738,-0.024507 V 4.5412139 c 0,-2.5916487 0.0098,-3.64106836 0.0098,-4.5412139 -0.937714,0 -1.881102,0 -4.602482,0 z"},
                   :tag :path,
                   :content nil}]}
                {:type :element, :attrs {:class "count retoot-count"}, :tag :span, :content ["REBLOGS_COUNT"]}]}
              {:type :element,
               :attrs {:class "footer-button likes"},
               :tag :button,
               :content
               [{:type :element,
                 :attrs {:xmlns "http://www.w3.org/2000/svg", :viewbox "0 0 24 22.799999", :fill "currentColor"},
                 :tag :svg,
                 :content
                 [{:type :element,
                   :attrs
                   {:d
                    "M 12,18.324 19.416,22.8 17.448,14.364 24,8.688 15.372,7.956 12,0 8.628,7.956 0,8.688 6.552,14.364 4.584,22.8 Z"},
                   :tag :path,
                   :content nil}]}
                {:type :element,
                 :attrs {:class "count like-count"},
                 :tag :span,
                 :content ["FAVOURITES_COUNT"]}]}]}]}]}]}]}]})

(def post-base-prev
  {:type :document,
   :content
   [{:type :element,
     :attrs nil,
     :tag :html,
     :content
     [{:type :element, :attrs nil, :tag :head, :content nil}
      {:type :element,
       :attrs nil,
       :tag :body,
       :content
       [{:type :element,
         :attrs {:class "section post-container"},
         :tag :section,
         :content
         [{:type :element,
           :attrs {:class "mastodon-post"},
           :tag :article,
           :content
           [{:type :element,
             :attrs {:class "mastodon-post-header"},
             :tag :header,
             :content
             [{:type :element, :attrs {:class "mastodon-post-avatar", :src "AVATAR_URL"}, :tag :img, :content nil}
              {:type :element,
               :attrs {:class "mastodon-post-names"},
               :tag :div,
               :content
               [{:type :element, :attrs {:class "display-name", :href "POST_URL"}, :tag :a, :content ["DISPLAY_NAME"]}
                {:type :element, :attrs {:class "account-name", :href "POST_URL"}, :tag :a, :content ["ACCOUNT_NAME"]}]}
              {:type :element,
               :attrs {:class "mastodon-post-date", :datetime "DATETIME"},
               :tag :time,
               :content ["TIME"]}]}
            {:type :element,
             :attrs {:class "mastodon-post-content"},
             :tag :section,
             :content [{:type :element, :attrs {:class "mastodon-post-text"}, :tag :p, :content ["POST_TEXT"]}]}
            {:type :element,
             :attrs {:href "LINK_PREVIEW_URL", :class "mastodon-post-link-preview", :target "_blank"},
             :tag :a,
             :content nil}]}]}]}]}]})

(def filled-post-with-prev
  {:type :document,
   :content
   [{:type :element,
     :attrs nil,
     :tag :html,
     :content
     [{:type :element, :attrs nil, :tag :head, :content nil}
      {:type :element,
       :attrs nil,
       :tag :body,
       :content
       [{:type :element,
         :attrs {:class "section post-container"},
         :tag :section,
         :content
         [{:type :element,
           :attrs {:class "mastodon-post"},
           :tag :article,
           :content
           [{:type :element,
             :attrs {:class "mastodon-post-header"},
             :tag :header,
             :content
             [{:type :element, :attrs {:class "mastodon-post-avatar", :src "AVATAR_URL"}, :tag :img, :content nil}
              {:type :element,
               :attrs {:class "mastodon-post-names"},
               :tag :div,
               :content
               [{:type :element, :attrs {:class "display-name", :href "POST_URL"}, :tag :a, :content ["DISPLAY_NAME"]}
                {:type :element, :attrs {:class "account-name", :href "POST_URL"}, :tag :a, :content ["ACCOUNT_NAME"]}]}
              {:type :element,
               :attrs {:class "mastodon-post-date", :datetime "DATETIME"},
               :tag :time,
               :content ["TIME"]}]}
            {:type :element,
             :attrs {:class "mastodon-post-content"},
             :tag :section,
             :content [{:type :element, :attrs {:class "mastodon-post-text"}, :tag :p, :content ["POST_TEXT"]}]}
            {:type :element,
             :attrs {:href "https://codeberg.org//forgejo/forgejo/src/commit/fe3473fc8b7b51e024b1a564fc7f01e385ebfb5e/tests/integration/api_activitypub_repository_test.go", :class "mastodon-post-link-preview", :target "_blank"},
             :tag :a,
             :content
             [{:type :element,
               :attrs {:class "mastodon-post-link-image", :src "https://cdn.masto.host/socialmeissagmbhde/cache/preview_cards/images/000/545/643/original/199336f5aa5b9683.png"},
               :tag :img,
               :content nil}
              {:type :element,
               :attrs {:class "mastodon-post-link-info"},
               :tag :div,
               :content
               [{:type :element, :attrs {:class "mastodon-post-link-title"}, :tag :h4, :content ["forgejo/tests/integration/api_activitypub_repository_test.go at fe3473fc8b7b51e024b1a564fc7f01e385ebfb5e"]}
                {:type :element,
                 :attrs {:class "mastodon-post-link-description"},
                 :tag :div,
                 :content ["forgejo - Beyond coding. We forge."]}
                {:type :element, :attrs {:class "mastodon-post-link-url"}, :tag :div, :content ["https://codeberg.org//forgejo/forgejo/src/commit/fe3473fc8b7b51e024b1a564fc7f01e385ebfb5e/tests/integration/api_activitypub_repository_test.go"]}]}]}]}]}]}]}]})

(def post-with-prev
  {:type :document,
   :content
   [{:type :element,
     :attrs nil,
     :tag :html,
     :content
     [{:type :element, :attrs nil, :tag :head, :content nil}
      {:type :element,
       :attrs nil,
       :tag :body,
       :content
       [{:type :element,
         :attrs {:class "section post-container"},
         :tag :section,
         :content
         [{:type :element,
           :attrs {:class "mastodon-post"},
           :tag :article,
           :content
           [{:type :element,
             :attrs {:class "mastodon-post-header"},
             :tag :header,
             :content
             [{:type :element, :attrs {:class "mastodon-post-avatar", :src "AVATAR_URL"}, :tag :img, :content nil}
              {:type :element,
               :attrs {:class "mastodon-post-names"},
               :tag :div,
               :content
               [{:type :element, :attrs {:class "display-name", :href "POST_URL"}, :tag :a, :content ["DISPLAY_NAME"]}
                {:type :element, :attrs {:class "account-name", :href "POST_URL"}, :tag :a, :content ["ACCOUNT_NAME"]}]}
              {:type :element,
               :attrs {:class "mastodon-post-date", :datetime "DATETIME"},
               :tag :time,
               :content ["TIME"]}]}
            {:type :element,
             :attrs {:class "mastodon-post-content"},
             :tag :section,
             :content [{:type :element, :attrs {:class "mastodon-post-text"}, :tag :p, :content ["POST_TEXT"]}]}
            {:type :element,
             :attrs {:href "LINK_PREVIEW_URL", :class "mastodon-post-link-preview", :target "_blank"},
             :tag :a,
             :content
             [{:type :element,
               :attrs {:class "mastodon-post-link-image", :src "LINK_PREVIEW_IMG_URL"},
               :tag :img,
               :content nil}
              {:type :element,
               :attrs {:class "mastodon-post-link-info"},
               :tag :div,
               :content
               [{:type :element, :attrs {:class "mastodon-post-link-title"}, :tag :h4, :content ["LINK_PREVIEW_TITLE"]}
                {:type :element,
                 :attrs {:class "mastodon-post-link-description"},
                 :tag :div,
                 :content ["LINK_PREVIEW_DESC"]}
                {:type :element, :attrs {:class "mastodon-post-link-url"}, :tag :div, :content ["LINK_PREVIEW_URL"]}]}]}]}]}]}]}]})