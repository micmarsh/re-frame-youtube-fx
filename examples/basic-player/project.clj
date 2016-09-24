(defproject basic-player "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [reagent "0.6.0"]
                 [binaryage/devtools "0.8.2"]
                 [re-frame "0.8.0"]]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :min-lein-version "2.5.3"

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies [[camel-snake-kebab "0.4.0"]]
    :plugins      [[lein-figwheel "0.5.7"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "../../src"]
     :figwheel     {:on-jsload "basic-player.core/mount-root"}
     :compiler     {:main                 basic-player.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            basic-player.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    ]}

  )
