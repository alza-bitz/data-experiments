(use 'clojure.repl)

(use 'alzatech.data-exp.sparkling.context)

; ..ok, let's make a spark context

(def ctx (spark-context-for-app-named "friday-afternoon-examples"))

(require '[sparkling.core :as sc])

(def numbers (sc/parallelize ctx [1 2 3 4 5])) ; parallelize an existing collection

numbers ; oops, it's lazy

(sc/take 3 numbers) ; action (non-lazy)

(defn square-of [number] (* number number)) ; define a named, serializable spark function

(sc/map square-of numbers) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(sc/take 3 (sc/map square-of numbers)) ; a transform with subsequent action

(sc/collect (sc/map square-of numbers)) ; a transform with subsequent action
