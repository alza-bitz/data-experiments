(use 'clojure.repl)

(use 'alzatech.data-exp.flambo.context)

; ..ok, let's make a spark context

(def ctx (spark-context-for-app-named "friday-afternoon-examples"))

(require '[flambo.api :as f])

(def numbers (f/parallelize ctx [1 2 3 4 5])) ; parallelize an existing collection

numbers ; oops, it's lazy

(f/take numbers 3) ; action (non-lazy)

(f/defsparkfn square-of [number] (* number number)) ; define a named, serializable spark function

(f/map numbers square-of) ; oops, transformation (lazy)

; ..ok, let's look at a sample by wrapping with an action to force computation

(f/take (f/map numbers square-of) 3) ; a transform with subsequent action

(f/collect (f/map numbers square-of)) ; a transform with subsequent action
