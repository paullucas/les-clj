(ns les.emd)

;; Emd is a specification written in Event Markdown.
(defrecord Emd [lines errors])

;; Item is a line item in an Event Markdown file.
(defrecord Item [])

;; A Comment in Event Markdown
;; Example:
;; "Order Placed"
(defrecord Comment [text])

;; Command in Event Markdown language
;; Example:
;; "Place Order-> // orderId, placedDate, deliveryDate"
(defrecord Command [name parameters])

;; A Parameter for an emd command
(defrecord Parameter [name])

;; Event describes an emd event
(defrecord Event [name properties])

;; Document describes an emd read model document
(defrecord Document [name properties])

;; A Property of an emd event
(defrecord Property [name])

;; A EmdValidationError means that the event markdown structure cannot be used to generate event markup.
(defrecord EmdValidationError [error-id message])
