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

(s/def :eml/eml-version #{"0.1-alpha"})

(s/def :eml/name string?)
(s/def :eml/type string?)
(s/def :eml/key string?)

;; A Parameter for a command.
(s/def :eml/rules (s/coll-of string?))
(s/def :eml/parameter (s/keys :req-un [:eml/name :eml/type :eml/rules]))
(s/def :eml/parameters (s/coll-of :eml/parameter))

(s/def :eml/postcondition string?)
(s/def :eml/postconditions (s/coll-of :eml/postcondition))

;; A Command in a bounded context
(s/def :eml/command (s/keys :req-un [:eml/name :eml/parameters :eml/postconditions]))
(s/def :eml/command-container (s/keys :req-un [:eml/command]))
(s/def :eml/commands (s/coll-of :eml/command-container))

;; A Property of an event.
(s/def :eml/is-hashed boolean?)
(s/def :eml/property (s/keys :req-un [:eml/name :eml/type]
                             :opt-un [:eml/is-hashed]))
(s/def :eml/properties (s/coll-of :eml/property))

;; An Event represents a fact that occurred as a result of a state change.
(s/def :eml/event (s/keys :req-un [:eml/name :eml/properties]))
(s/def :eml/event-container (s/keys :req-un [:eml/event]))
(s/def :eml/events (s/coll-of :eml/event-container))

;; A Stream is a stream of events representing a transactional scope.
;; In Domain Driven Design, this is known as an "aggregate".
;; In comp sci, it can be represented as a state machine.
(s/def :eml/stream string?)
(s/def :eml/stream-entity (s/keys :req-un [:eml/stream :eml/commands :eml/events]))
(s/def :eml/streams (s/coll-of :eml/stream-entity))

(s/def :eml/subscribes-to (s/coll-of string?))

(s/def :eml/readmodel (s/keys :req-un [:eml/name :eml/key :eml/subscribes-to]))
(s/def :eml/readmodels (s/coll-of :eml/readmodel))

;; A BoundedContext is a context in which a ubiquitous language applies.
(s/def :eml/context (s/keys :req-un [:eml/name :eml/streams :eml/readmodels]))
(s/def :eml/contexts (s/coll-of :eml/context))

(s/def :eml/error-id string?)
(s/def :eml/message string?)

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
(s/def :eml/solution string?)

(s/def :eml/ast (s/keys :req-un [:eml/eml-version :eml/solution :eml/contexts]
                        :opt-un [:eml/errors]))
