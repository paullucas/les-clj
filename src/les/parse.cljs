(ns les.parse
  (:require [fs]
            [partial.lenses :as l]
            [lumo.repl :refer [eval]]
            [les.core :refer [read-eml!]]))

(def eml (read-eml! "./sampleEmlFile.yaml"))

(defn get-all [lens eml]
  (into [] (-> lens
               (l/collect (clj->js eml))
               (js->clj :keywordize-keys true)
               flatten)))

(def events (partial get-all #js ["contexts" l/elems "streams" l/elems "events"]))

(def commands (partial get-all #js ["contexts" l/elems "streams" l/elems "commands"]))

(def k->s (comp symbol name))

(defn event->constructor [{:keys [name properties]}]
  (list 'defrecord (k->s name) (conj (mapv #(symbol (:name %)) properties))))

(defn command->constructor [{:keys [name parameters]}]
  (list 'defrecord (k->s name) (conj (mapv #(symbol (:name %)) parameters))))

(defn eml->event-constructors [eml]
  (map #(event->constructor (:event %)) (events eml)))

(defn eml->command-constructors [eml]
  (map #(command->constructor (:command %)) (commands eml)))

(defn eval-event-constructors! [eml]
  (map eval (eml->event-constructors eml)))

(defn eval-command-constructors! [eml]
  (map eval (eml->command-constructors eml)))

(defn clj->io! [xs]
  (map (fn [x] (fs/appendFileSync "./clj-output.cljs" (str x "\n"))) xs))

(defn constructors->io! []
  (clj->io! (concat '("\n;; Events\n")
                    (eml->event-constructors eml)
                    '("\n;; Commands\n")
                    (eml->command-constructors eml))))
