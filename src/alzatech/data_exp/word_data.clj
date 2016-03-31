(ns alzatech.data-exp.word-data
  (:require
    [faker.quote :as fq]
    [clojure.java.io :as io]))

(defn generate-quotes
  []
  (repeatedly 20 fq/rand-quote)
  )

(defn write-quotes
  [quotes file]
  (with-open [writer (clojure.java.io/writer file)]
    (doseq [quote quotes]
      (.write writer quote)
      (.newLine writer))))