; RESTART REPL

(use 'clojure.repl)

(use 'alzatech.data-exp.sparkling.context)

; make a spark context

(def ctx (spark-context-for-app-named "friday-afternoon-word-freq-distributed"))

(require '[sparkling.core :as sc])

; create a rdd of lines

(def lines (sc/text-file ctx "data/quotes.txt"))

; how many lines?

(sc/count lines) ; action (non-lazy)

; ok.. how to split a line into words?

(require '[clojure.string :as s])

(s/split (sc/first lines) #" ") ; action (non-lazy)

; ..but we want to split *every* line into words, 
; flattened into a single seq, in order to process all the words sequentially..

(sc/flat-map (fn [line] (s/split line #" ")) lines) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(def words (sc/flat-map (fn [line] (s/split line #" ")) lines))

(sc/take 2 words) ; wrap with action (non-lazy)

; ok.. we need to go from words to word-count key-value pairs (or tuples), 
; so that we can reduce by key

(require '[sparkling.destructuring :as sd])

(sc/map-to-pair (fn [word] (sd/tuple word 1)) words) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(def word-count-pairs (sc/map-to-pair (fn [word] (sd/tuple word 1)) words))

(sc/take 2 word-count-pairs) ; wrap with action (non-lazy)

; ok.. now we want to reduce word-count pairs by key, to get their frequency..

(sc/reduce-by-key (fn [count1 count2] (+ count1 count2)) word-count-pairs) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(def word-counts (sc/reduce-by-key (fn [count1 count2] (+ count1 count2)) word-count-pairs))

(sc/take 5 word-counts) ; wrap with action (non-lazy)

