# dda-masto-embed

![build](https://github.com/DomainDrivenArchitecture/dda-masto-embed/workflows/build-it/badge.svg)

[<img src="https://meissa-gmbh.de/img/community/Mastodon_Logotype.svg" width=20 alt="team@social.meissa-gmbh.de"> team@social.meissa-gmbh.de](https://social.meissa-gmbh.de/@team) | [Website & Blog](https://domaindrivenarchitecture.org)

## In brief

dda-masto-embed embedd either your timeline or replies to a specific post on your website.

* Uses JS, **no intermediate server** required,
* in replies mode you can decide to show only favorited replies in order to do upfront moderation
* example for embedding a timeline at [meissa.de/news](https://meissa.de/news/)
* example for embedding replies to a specific post at [meissa.de/sustainibility_microplastic](https://meissa.de/about-meissa/03plastik-aktion/)
* Download latest version at:
  * [dda-masto-embed.js](https://repo.prod.meissa.de/attachments/fd727528-0ab4-42d1-bade-8ff129997315)
  * [dda-masto-embed.js.sha256](https://repo.prod.meissa.de/attachments/fd727528-0ab4-42d1-bade-8ff129997315)
  * [dda-masto-embed.js.sha512](https://repo.prod.meissa.de/attachments/fd727528-0ab4-42d1-bade-8ff129997315)
* It is **OpenSource** - published under the Apache License, Version 2.0

### css and html as base

Uses a generalized HTML structure with descriptive classes and css grid for styling.
The structure in the css follows the html structure. Both accound mode and replies mode are styled there.

Re-styling your timeline should now be a breeze.

## Include a timeline

Including a timeline needs the following html. The div with id `masto-embed` configures the timeline to be shown.
We use bootstrap for rough styling. More styling is up to you at the moment, help is welcome :-)

```html
<!doctype html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>masto-embed</title>
    <link rel="stylesheet" href="post.css">
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

Reference:

* `id` has to be `masto-embed`
* `account_name` is the name of your account.
* `host_url` the url of your mastodon instance.
* Use the `post.css` from the src/main/resources or the public folder. 

## Using in reply mode

Including replies of one of your posts will work as follows:

```html
<!doctype html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>masto-embed</title>
    <link rel="stylesheet" href="post.css">
  </head>
  <body>
    <div id="masto-embed" 
          account_name="team"
          replies_to="112432461907918517"
          filter_favorited=false
          host_url="https://social.meissa-gmbh.de">
      Here the timeline will appear.
    </div>
    <script src="https://domaindrivenarchitecture.org/downloads/dda-masto-embed.js"></script>
  </body>
</html>
```

Reference:

* `id` has to be `masto-embed`
* `account_name` is the name of your account.
* `host_url` the url of your mastodon instance.
* `replies_to` the id of your post.
* `filter_favorited=<true|false>` true will show only favorited replies, false will show every answer.

## Dev setup

```bash
npm install -g npx
npm install -g shadow-cljs
npm install -g source-map-support --save-dev
npm install
shadow-cljs watch frontend
```

open browser at http://localhost:8080

Connect your repl for :frontend

## Run the tests

```bash
shadow-cljs compile test
```

## Releasing

### prod release

```bash
#adjust version
vi package.json
git commit -am 'releasing'
git tag -am 'releasing' <version>
git push --follow-tags

# Bump version
vi package.json
git commit -am "version bump" && git push
```

## Development & mirrors

Development happens at: https://repo.prod.meissa.de/meissa/dda-masto-embed

Mirrors are:

* https://codeberg.org/meissa/dda-masto-embed (issues and PR)
* https://gitlab.com/domaindrivenarchitecture/dda-masto-embed (CI issues and PR)
* https://github.com/DomainDrivenArchitecture/dda-masto-embed

## License

Copyright © 2023 meissa GmbH
Licensed under the [Apache License, Version 2.0](LICENSE) (the "License")
Pls. find licenses of our subcomponents [here](doc/SUBCOMPONENT_LICENSE)
