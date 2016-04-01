(defproject data-experiments-clj "0.1.0-SNAPSHOT"
  :description "Various data processing experiments with Apache Spark"
  :url "https://github.com/alzadude/data-experiments"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:aot :all}
             :uberjar {:aot :all}
             :provided {:dependencies
                        [[org.apache.spark/spark-core_2.10 "1.6.0"]]}}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [gorillalabs/sparkling "1.2.3"]
                 [yieldbot/flambo "0.7.1"]
                 [helpshift/faker "0.2.0"]])
