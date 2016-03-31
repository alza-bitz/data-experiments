(use 'clojure.repl)

(require '[clojure.java.io :as io])

(require '[clojure.string :as s])

; get a reader

(def reader (io/reader "data/quotes.txt"))

; create a lazy seq of lines

(def lines (line-seq reader))

; how many lines?

(count lines)

; ok.. how to split a line into words?

(s/split (first lines) #" ")

; ok.. but we want to split *every* line into words..

(map (fn [line] (s/split line #" ")) lines)

; ok.. but we want the result flattened into a single seq, 
; in order to process all the words sequentially..

(def words (mapcat (fn [line] (s/split line #" ")) lines))

(take 2 words)

; ok.. now we want to reduce words by their frequency..

(doc reduce) ; "If val is supplied, 
; returns the result of applying f to val and the first item in coll, then 
; applying f to that result and the 2nd item, etc"

(reduce 
  (fn [word-counts word] (assoc word-counts word (inc (word-counts word 0)))) 
  {} 
  words)

(def word-counts (reduce 
                   (fn [word-counts word] (assoc word-counts word (inc (word-counts word 0)))) 
                   {} 
                   words))

; ok.. now we want to sort the reduce word counts by frequency..

(doc sort-by) ; 'keyfn' gets values to compare for sorting.. 
; "If no comparator is supplied, uses compare"

(doc compare)

(sort-by second word-counts)

; ok.. we need a 'negative of second item' keyfn 

((comp - second) ["a-word" 5])

(sort-by (comp - second) word-counts)

