(ns les.core
  (:require #?@(:cljs [[js-yaml :as yml]
                       fs
                       [lodash.kebabcase :as lodash-kebab]])
            [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [expound.alpha :as expound]
            [les.spec]))

(defn- apply-case
  "Apply case function to x, optionally transforming string to keywords if
  keywordize-keys is true."
  [f keywordize-keys x]
  (cond
    (keyword? x) (-> x name f keyword)
    (symbol? x) (-> x str f symbol)
    (and keywordize-keys (string? x)) (-> x f keyword)
    :else (f x)))

(defn- transform-keys
  "Transform keys recursively applying the f function.

     If keywordize-keys is true, transform string keys to keywords."
  [f keywordize-keys m]
  (if (map? m)
    (walk/prewalk
     (fn [form]
       (if (map? form)
         (into {}
               (map (fn [[k v]] [(apply-case f keywordize-keys k) v]))
               form)
         form))
     m)
    m))

#?(:cljs
   (def ^{:doc "Convert keys to a more Clojure-like format. Note that this
   function also transforms string keys to keywords."
          :arglists '([m])
          :private true}
     clojurify-keys
     (partial transform-keys lodash-kebab true)))

(defn read-eml!
  "Read a file and return the eml AST."
  [file-path]
  (-> file-path
      fs/readFileSync
      yml/safeLoad
      js->clj
      clojurify-keys))

(defn print-ast-validation!
  [eml-ast]
  (print (s/explain-str :eml/ast eml-ast)))
