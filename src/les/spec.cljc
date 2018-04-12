(ns les.spec
  (:require [clojure.spec.alpha :as s]))

;;;;;;;;;
;; EMD ;;
;;;;;;;;;

(s/def :emd/name string?)

(s/def :validation/error-id string?)
(s/def :validation/message string?)

(s/def :validation/error (s/keys :req-un [:validation-error/error-id :validation-error/message]))

;; Emd is a specification written in Event Markdown.
(s/def :emd/lines string?)
(s/def :emd/errors (s/coll-of :validation/error))

(s/def :emd/entity (s/keys :req-un [:emd/lines :emd/errors]))

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
(s/def :emd/commands (s/coll-of :emd/command))

;; Event describes an emd event
(s/def :emd/event (s/keys :req-un [:emd/name :emd/parameters]))
(s/def :emd/events (s/coll-of :emd/event))

;; Document describes an emd read model document
(s/def :emd/document (s/keys :req-un [:emd/name :emd/parameters]))

;; A Property of an emd event
(s/def :emd/property (s/keys :req-un [:emd/name]))


;;;;;;;;;
;; EML ;;
;;;;;;;;;

(s/def :emd/name string?)
(s/def :emd/type string?)
(s/def :emd/key string?)
(s/def :eml/eml-version string?)

;; A Stream is a stream of events representing a transactional scope.
;; In Domain Driven Design, this is known as an "aggregate".
;; In comp sci, it can be represented as a state machine.
(s/def :eml/stream (s/keys :req-un [:eml/name :eml/commands :eml/events]))
(s/def :eml/streams (s/coll-of :eml/stream))

(s/def :eml/subscribes-to (s/coll-of string?))

(s/def :eml/readmodel (s/keys :req-un [:eml/name :eml/key :eml/subscribes-to]))
(s/def :eml/readmodels (s/coll-of :eml/readmodel))

;; A BoundedContext is a context in which a ubiquitous language applies.
(s/def :eml/context [:eml/name :eml/streams :eml/readmodels])
(s/def :eml/contexts (s/coll-of :eml/context))

(s/def :eml/rules (s/coll-of string?))
(s/def :eml/parameter (s/keys :req-un [:eml/name :eml/type :eml/rules]))
(s/def :emd/parameters (s/coll-of :emd/parameters))

(s/def :emd/postcondition string?)
(s/def :emd/postconditions (s/coll-of :emd/postcondition))

(s/def :eml/command (s/keys :req-un [:emd/name :emd/parameters :emd/postconditions]))
(s/def :eml/commands (s/coll-of :emd/command))

(s/def :emd/is-hashed boolean?)
(s/def :eml/property (s/keys :req-un [:eml/name :eml/type :eml/is-hashed]))
(s/def :eml/properties (s/coll-of :eml/property))

;; Event describes an emd event
(s/def :eml/event (s/keys :req-un [:emd/name :emd/properties]))
(s/def :eml/events (s/coll-of :emd/event))


(s/def :eml/error-id string?)
(s/def :emd/message string?)

;; A ValidationError means that the eml structure cannot be used to generate an API.
(s/def :eml/error (s/keys :req-un [:eml/error-id
                                   :eml/context
                                   :eml/stream
                                   :eml/command
                                   :eml/event
                                   :eml/readmodel
                                   :eml/message]))
(s/def :eml/errors (s/coll-of :eml/error))

;; A Solution describes an event sourced system
;; It will contain bounded contexts and meta information about what environment to deploy it to.
;; It can also contain references to other bounded contexts from the 'Play Store' (Context Store? Microservice Store?)
(s/def :eml/solution (s/keys :req-un [:eml/name :eml/contexts :eml/errors]))
