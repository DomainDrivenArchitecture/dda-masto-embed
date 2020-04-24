# dda-masto-embed
Embeds mastodon timline into a html page. Uses JS, no intermediate server required.


# Setup

sudo npm install -g npx
sudo npm install -g source-map-support
sudo npm install -g shadow-cljs

# Development

shadow-cljs node-repl
shadow-cljs watch frontend
shadow-cljs compile test