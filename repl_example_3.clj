; RESTART REPL

(use 'clojure.repl)

(use 'alzatech.data-exp.flambo.context)

; make a spark context

(def ctx (spark-context-for-app-named "friday-afternoon-word-freq-distributed"))

(require '[flambo.api :as f])

; create a rdd of lines

(def lines (f/text-file ctx "data/quotes.txt"))

; how many lines?

(f/count lines) ; action (non-lazy)

; ok.. how to split a line into words?

(require '[clojure.string :as s])

(s/split (f/first lines) #" ") ; action (non-lazy)

; ..but we want to split *every* line into words, 
; flattened into a single seq, in order to process all the words sequentially..

(f/flat-map lines (f/fn [line] (s/split line #" "))) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(def words (f/flat-map lines (f/fn [line] (s/split line #" "))))

(f/take words 2) ; wrap with action (non-lazy)

; ok.. we need to go from words to word-count key-value pairs (or tuples), 
; so that we can reduce by key

(require '[flambo.tuple :as ft])

(f/map-to-pair words (f/fn [word] (ft/tuple word 1))) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(def word-count-pairs (f/map-to-pair words (f/fn [word] (ft/tuple word 1))))

(f/take word-count-pairs 2) ; wrap with action (non-lazy)

; ok.. now we want to reduce word-count pairs by key, to get their frequency..

(f/reduce-by-key word-count-pairs (f/fn [count1 count2] (+ count1 count2))) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(def word-counts (f/reduce-by-key word-count-pairs (f/fn [count1 count2] (+ count1 count2))))

(f/take word-counts 5) ; wrap with action (non-lazy)

