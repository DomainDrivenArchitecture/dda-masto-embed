# dda-masto-embed
![build](https://github.com/DomainDrivenArchitecture/dda-masto-embed/workflows/build-it/badge.svg)

[<img src="https://meissa-gmbh.de/img/community/Mastodon_Logotype.svg" width=20 alt="team@social.meissa-gmbh.de"> team@social.meissa-gmbh.de](https://social.meissa-gmbh.de/@team) | [Website & Blog](https://domaindrivenarchitecture.org)

## in brief
Embeds mastodon timeline into a html page. 
* Uses JS, **no intermediate server** required,
* example at [meissa-gmbh.de](https://meissa-gmbh.de/pages/news/)
* Download latest version at:
  * [dda-masto-embed.js](https://domaindrivenarchitecture.org/downloads/downloads/dda-masto-embed.js)
  * [dda-masto-embed.js.sha256](https://domaindrivenarchitecture.org/downloads/downloads/dda-masto-embed.js.sha256)
  * [dda-masto-embed.js.sha512](https://domaindrivenarchitecture.org/downloads/downloads/dda-masto-embed.js.sha512)
* It is **OpenSource** - published under the Apache License, Version 2.0

## how it looks
![masto-embed-example.png](doc/masto-embed-example.png)

## how to try it out
In order to try it, just create a html like 
```
<!doctype html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>masto-embed</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" 
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" 
          crossorigin="anonymous">
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

We use bootstrap for rough styling. More styling is up to you at the moment, help is welcome :-)


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
vi package.json
git commit -am 'releasing'
git tag -am 'releasing' <version>
git push --follow-tags

# Bump version
vi package.json
git commit -am "version bump" && git push
```
