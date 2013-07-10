{:logging {:loggers [{:type :stdout :level :debug}]}
 :database {:classname    "org.h2.Driver"
            :subprotocol  "h2"
            :protocol     "file"
            :path         "./"
            :database     "caribou_docs"
            :host         "localhost"
            :user         "h2"
            :password     ""}
 :controller {:namespace  "caribou-docs.controllers" :reload true}
 :nrepl {:port 43334}
 :cache-templates :never}
