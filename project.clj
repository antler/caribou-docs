(defproject caribou-docs "0.10.0"
  :description "The page routing ring handler for caribou"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [antler/caribou-frontend "0.10.0"]
                 [antler/caribou-admin "0.10.2"]
                 [antler/caribou-api "0.10.0"]
                 [markdown-clj "0.9.19"]
                 [swank-clojure "1.4.2"]]
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n"]
  :source-paths ["src" "../src"]
  :resource-paths ["resources/" "../resources/"]
  :migration-namespace caribou-docs.migrations
  :immutant {:context-path "/"}
  :ring {:handler caribou-docs.core/handler
         :servlet-name "caribou-docs-frontend"
         :init caribou-docs.core/init
         :port 33333})
