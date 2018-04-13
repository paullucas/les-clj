(ns les.les-script
  (:require [getopts]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [les.core :as les]))

(enable-console-print!)

(set! s/*explain-out* (expound/custom-printer {:theme :figwheel-theme
                                               :print-specs? false}))

;;;;;;;;;
;; CLI ;;
;;;;;;;;;

(def cli-summaries
  [["-v" "--validate-eml FILE" "The EML yaml file to validate"]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(def cli-options
  {:v "validate-eml"})

(defn print-usage
  [summaries]
  (print "Usage: les-cljs" "\n")
  (doseq [s summaries]
    (apply print s)))

(defn ^:export -main [& args]
  (let [parsed-opts (apply getopts (clj->js
                                    [(or cljs.core/*command-line-args* [])
                                     {:alias cli-options}]))]
    (cond
      (:validate-eml parsed-opts)
      (let [eml-ast (les/read-eml! (:validate-eml parsed-opts))]
        (les/print-ast-validation! eml-ast))

      (:help parsed-opts) (print-usage cli-summaries)

      :else (print-usage cli-summaries))))

(set! *main-cli-fn* -main)
