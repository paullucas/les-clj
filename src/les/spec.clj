(ns les.spec
  (:require [clojure.spec.alpha :as s]))

(s/def :emd/name string?)

(s/def :validation/error-id string?)
(s/def :validation/message string?)

(s/def :validation/error (s/keys :req-un [:validation-error/error-id :validation-error/message]))

;; Emd is a specification written in Event Markdown.
(s/def :emd/lines string?)
(s/def :emd/errors (s/coll-of :validation/error))

(s/def :emd/entity (s/keys :req-un [:emd/lines :emd/errors]))


;; Item is a line item in an Event Markdown file.
(defrecord Item [])

;; A Comment in Event Markdown
;; Example:
;; "Order Placed"
(s/def :emd/text string?)
(s/def :emd/comment (s/keys :req-un [:emd/comment]))

;; A Parameter for an emd command
(s/def :emd/parameter (s/keys :req-un [:emd/name]))
(s/def :emd/parameters (s/coll-of :emd/parameters))

;; Command in Event Markdown language
;; Example:
;; "Place Order-> // orderId, placedDate, deliveryDate"
(s/def :emd/command (s/keys :req-un [:emd/name :emd/parameters]))

;; Event describes an emd event
(s/def :emd/event (s/keys :req-un [:emd/name :emd/parameters]))

;; Document describes an emd read model document
(s/def :emd/document (s/keys :req-un [:emd/name :emd/parameters]))

;; A Property of an emd event
(s/def :emd/property (s/keys :req-un [:emd/name]))
