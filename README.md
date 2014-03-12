# Tictactoe CLJS
Tictactoe game in [Clojurescript](https://github.com/clojure/clojurescript) and [Om](https://github.com/swannodette/om).

## Play

Browse to [this page](http://michielborkent.nl/tictactoe-cljs) for a pre-built version of the game or
build the project yourself and open the file `resources/public/index.html` in a web browser.

## Build

`git clone https://github.com/borkdude/tictactoe-cljs.git` 

`cd tictactoe-cljs` 

`bower install` (only used to download React, you can do this manually or link to a CDN if you don't use bower)

`lein cljsbuild once` 

