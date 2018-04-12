(ns les.les-script
  (:require [cljs.tools.cli :as cli]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [les.core :as les]))

(enable-console-print!)

(set! s/*explain-out* (expound/custom-printer {:theme :figwheel-theme}))

;;;;;;;;;
;; CLI ;;
;;;;;;;;;

(def cli-options
  ;; An option with a required argument
  [["-v" "--validate-eml FILE" "The EML yaml file to validate"]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn print-usage
  [parsed-opts]
  (println "Usage: les-cljs")
  (println (:summary parsed-opts)))

(defn ^:export -main [& args]
  (let [parsed-opts (cli/parse-opts cljs.core/*command-line-args* cli-options)
        cli-opts (:options parsed-opts)]

    (cond
      (:errors cli-opts) (do (println (:errors cli-opts) "\n")
                             (print-usage cli-opts))

      (:validate-eml cli-opts)
      (let [eml-ast (les/read-eml! (:validate-eml cli-opts))]
        (les/print-ast-validation! eml-ast))

      (:help cli-opts) (print-usage cli-opts))))

(set! *main-cli-fn* -main)
