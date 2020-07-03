# dda-masto-embed
Embeds mastodon timline into a html page. 
* Uses JS, **no intermediate server** required,
* Download latest version at:
  * [dda-masto-embed.js](https://domaindrivenarchitecture.org/downloads/downloads/dda-masto-embed.js)
  * [dda-masto-embed.js.sha256](https://domaindrivenarchitecture.org/downloads/downloads/dda-masto-embed.js.sha256)
  * [dda-masto-embed.js.sha512](https://domaindrivenarchitecture.org/downloads/downloads/dda-masto-embed.js.sha512)
* It is **OpenSource** - published under the Apache License, Version 2.0

In order to try it, just create a html like 
```
<!doctype html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>masto-embed</title>
  </head>
  <body>
    <div id="masto-embed" 
          account_name="team"
          host_url="https://social.meissa-gmbh.de">
      Here the timeline will appear.
    </div>
    <script src="https://domaindrivenarchitecture.org/downloads/dda-masto-embed.js"></script>
  </body>
</html>
```

and you will get  sth like:
![masto-embed-example.png](doc/masto-embed-example.png)

Styling is up to you atmo, help is welcome :-)


## dev setup

```
npm install -g npx
npm install -g shadow-cljs
npm install -g source-map-support --save-dev
npm install
shadow-cljs watch frontend
```

open browser at http://localhost:8080

Connect your repl for :frontend


## run the tests

```
shadow-cljs compile test
```

## releasing
### prod release
```
#adjust version
vi shadow-cljs.edn

git commit -am "releasing"
git tag [version]
git push && git push --tag

shadow-cljs release app

shadow-cljs release app
chmod a+x mastodon-bot.js
rm -rf target/npm-build 
mkdir -p target/npm-build
cp mastodon-bot.js target/npm-build/
cp package.json target/npm-build/
cp README.md target/npm-build/
tar -cz -C target/npm-build -f target/npm-build.tgz .

npm publish ./target/npm-build.tgz --access public

# Bump version
vi shadow-cljs.edn

git commit -am "version bump" && git push
```