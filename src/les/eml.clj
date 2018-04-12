(ns les.eml)

;; A Solution describes an event sourced system
;; It will contain bounded contexts and meta information about what environment to deploy it to.
;; It can also contain references to other bounded contexts from the 'PlayStore' (Context Store? Microservice Store?
(defrecord Solution [eml-version name contexts errors])

;; A BoundedContext is a context in which a ubiquitous language applies.
(defrecord BoundedContext [name streams read-models])

;; A Stream is a stream of events representing a transactional scope.
;; In Domain Driven Design, this is known as an "aggregate".
;; In comp sci, it can be represented as a state machine.
(defrecord Stream [name commands events])

;; An Event represents a fact that occurred as a result of a state change.
(defrecord Event [event])
(defrecord Event-Event [name properties])

;; A Property of an event.
(defrecord Property [name type is-hashed])

;; A Command in a bounded context
(defrecord Command [command])
(defrecord Command-Command [name parameters post-conditions])

;; A Parameter for a command.
(defrecord Parameter [name type rules])

;; A Readmodel which subscribes to events
(defrecord Readmodel [readmodel])
(defrecord Readmodel-Readmodel [name key subscribes-to])

; A ValidationError means that the eml structure cannot be used to generate an API.
(defrecord ValidationError [error-id context stream command event readmodel message])
