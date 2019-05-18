(defproject cljs-serverless-workshop "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :jvm-opts ["--add-modules" "java.xml.bind"]

  :middleware [cider-nrepl.plugin/middleware]
  
  :min-lein-version "2.8.0"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.908"]]

  :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-figwheel "0.5.18"]]

  :source-paths ["src"]

  :clean-targets [;;"serverless.js"
                  "target/js/compiled/prod/serverless.js"
                  "target"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :figwheel true
              :compiler {
                :main cljs-serverless-workshop.core
                :asset-path "target/js/compiled/dev"
                :output-to "target/js/compiled/cljs_serverless_workshop.js"
                :output-dir "target/js/compiled/dev"
                :target :nodejs
                :optimizations :none
                :source-map-timestamp true}}
             {:id "prod"
              :source-paths ["src"]
              :compiler {
                :output-to "target/js/compiled/prod/serverless.js"
                :output-dir "target/js/compiled/prod"
                :target :nodejs
                :optimizations :simple}}]}

  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.18"]
                                  [cider/piggieback "0.4.1"]]
                   :source-paths ["src" "dev"]
                   :repl-options {:nrepl-middleware [;;cider.nrepl/cider-middleware
                                                     cider.piggieback/wrap-cljs-repl]}}})
