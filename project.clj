(defproject caribou/caribou-docs "0.12.0"
  :description "The page routing ring handler for caribou"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [caribou/caribou-frontend "0.12.12"]
                 [caribou/caribou-admin "0.12.12"]
                 [caribou/caribou-api "0.12.12"]
                 [markdown-clj "0.9.19"]]
  :plugins [[caribou/lein-caribou "2.4.6"]
            [lein-ring "0.8.6"]]
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n"]
  :source-paths ["src" "../src"]
  :resource-paths ["resources/" "../resources/"]
  :migration-namespace caribou-docs.migrations
  :immutant {:context-path "/"}
  :ring {:handler caribou-docs.core/handler
         :servlet-name "caribou-docs-frontend"
         :init caribou-docs.core/init
         :port 32223})
