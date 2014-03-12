(defproject tictactoe-cljs "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [om "0.5.2"]]

  :plugins [[lein-cljsbuild "1.0.1"]]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {:output-to "resources/public/main.js"
                                   :output-dir "resources/public/out"
                                   :optimizations :none
                                   :source-map true}}]}
)
