# dda-masto-embed
Embeds mastodon timline into a html page. Uses JS, no intermediate server required.


# Setup

To setup a basic luminus project:
* lein new luminus shipping-cljs +kee-frame

Start a leiningen repl and run fighwheel and let it connect to your application:

1) lein repl
    * user=> (start)
2) lein figwheel
3) Open http://localhost:3000/ in Browser
4) figwheel should have connected to your application

# Development

lein repl does not seem to work with clojurescript. Lein figwheel is needed to work with clojurescript. 

E.g. in your figwheel repl:



app:cljs.user=> (in-ns 'masto-embed.routing)
app:masto-embed.routing=> routes

But will lead to Unable to resolve symbol: routes in this context
in lein repl.

