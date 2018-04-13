(ns les.les-script
  (:require [clojure.pprint :as pprint]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [getopts]
            [goog.object :as gobj]
            [les.core :as les]))

(enable-console-print!)

(set! s/*explain-out* (expound/custom-printer {:theme :figwheel-theme
                                               :print-specs? false}))

;;;;;;;;;
;; CLI ;;
;;;;;;;;;

(def cli-summaries
  [["-v" "--validate-eml FILE" "The EML yaml file to validate"]
   ["-e" "--eml-to-edn FILE" "Convert EML yaml file to EDN"]
   ["-p" "--pretty-print" "Pretty prints the output"]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(def cli-options
  {:v "validate-eml"
   :e "eml-to-edn"
   :p "pretty-print"})

(defn print-usage
  [summaries]
  (print "Usage: les-cljs" "\n")
  (doseq [s summaries]
    (apply print s)))

(defn ^:export -main [& args]
  (let [parsed-opts (-> (apply getopts (clj->js
                                        [(or cljs.core/*command-line-args* [])
                                         {:alias cli-options}]))
                        (js->clj :keywordize-keys true))]
    (cond
      (:validate-eml parsed-opts)
      (let [eml-ast (les/read-eml! (:validate-eml parsed-opts))]
        (les/print-ast-validation! eml-ast))

      (:eml-to-edn parsed-opts)
      (let [eml-ast (les/read-eml! (:eml-to-edn parsed-opts))]
        (if (:pretty-print parsed-opts)
          (pprint/pprint eml-ast)
          (pr eml-ast)))

      (:help parsed-opts) (print-usage cli-summaries)

      :else (print-usage cli-summaries))))

(set! *main-cli-fn* -main)
