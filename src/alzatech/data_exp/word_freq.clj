(ns alzatech.data-exp.word-freq
  (:require [flambo.api :as fa]
            [flambo.tuple :as ft]
            [sparkling.core :as sc]
            [sparkling.serialization]
            [sparkling.destructuring :as sd]
            [clojure.string :as s]
            [clojure.java.io :as io]
            [alzatech.data-exp.flambo.context :as df]
            [alzatech.data-exp.sparkling.context :as ds])
  (:gen-class))

(defn word-freq-non-distributed 
  "Calculate word frequencies in a text file"
  [file]
  (with-open [reader (io/reader file)]
    (->>  (line-seq reader)
      (mapcat (fn [line] (s/split line #" ")))
      (reduce 
        (fn [word-counts word] (assoc word-counts word (inc (word-counts word 0)))) 
        {})
      (sort-by (comp - second))))
  )

(defn flambo-spark-context-for-word-freq []
  (df/spark-context-for-app-named "word-freq"))

(defn flambo-word-freq-distributed
  "Calculate word frequencies in a text file using Spark/Flambo"
  [file ctx]
  (-> (fa/text-file ctx file)
    (fa/flat-map (fa/fn [line] (s/split line #" ")))
    (fa/map-to-pair (fa/fn [word] (ft/tuple word 1)))
    (fa/reduce-by-key (fa/fn [count1 count2] (+ count1 count2)))
    (fa/map (ft/key-val-fn (fa/fn [word count] (ft/tuple count word))))
    (fa/sort-by-key compare false)
    (fa/map fa/untuple)
    fa/collect)
  )

(defn sparkling-spark-context-for-word-freq []
  (ds/spark-context-for-app-named "word-freq"))

(defn sparkling-word-freq-distributed
  "Calculate word frequencies in a text file using Spark/Sparkling"
  [file ctx]
  (->> (sc/text-file ctx file)
    (sc/flat-map (fn [line] (s/split line #" ")))
    (sc/map-to-pair (fn [word] (sd/tuple word 1)))
    (sc/reduce-by-key (fn [count1 count2] (+ count1 count2)))
    (sc/map-to-pair (sd/key-value-fn (fn [word count] (sd/tuple count word))))
    (sc/sort-by-key compare false)
    (sc/map (sd/key-value-fn (fn [count word] [count word])))
    sc/collect)
  )
