(defproject caribou-docs "0.11.0"
  :description "The page routing ring handler for caribou"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [antler/caribou-frontend "0.11.24"]
                 [antler/caribou-admin "0.11.30"]
                 [antler/caribou-api "0.11.20"]
                 [markdown-clj "0.9.19"]]
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n"]
  :source-paths ["src" "../src"]
  :resource-paths ["resources/" "../resources/"]
  :migration-namespace caribou-docs.migrations
  :immutant {:context-path "/"}
  :ring {:handler caribou-docs.core/handler
         :servlet-name "caribou-docs-frontend"
         :init caribou-docs.core/init
         :port 32223})
