# dda-masto-embed
Embeds mastodon timline into a html page. Uses JS, no intermediate server required.


# Setup

sudo npm install -g npx
sudo npm install -g shadow-cljs
npm install source-map-support --save-dev
npm install mastodon-api

# Development

shadow-cljs node-repl
shadow-cljs watch frontend
shadow-cljs compile test