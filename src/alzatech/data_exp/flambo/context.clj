(ns alzatech.data-exp.flambo.context
  (:require [flambo.conf :as conf]
            [flambo.api :as f]))

(defn spark-context-for-app-named
  "Returns a spark context"
  [app-name]
  (let [c (-> (conf/spark-conf)
            (conf/master "local[*]")
            (conf/app-name app-name))]
    (f/spark-context c)))
