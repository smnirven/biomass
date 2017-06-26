(ns biomass.schemas.schemas
  (:require [schema.core :as s]))

(defn maybe-sequential [schema]
  (s/if sequential? [schema] schema))

(defonce Price
  {(s/optional-key :Amount) s/Num
   :CurrencyCode s/Str
   (s/optional-key :FormattedPrice) s/Str})

(defonce notification-event-type
  (s/enum "AssignmentAccepted" "AssignmentAbandoned" "AssignmentReturned" "AssignmentSubmitted" "AssignmentRejected" "AssignmentApproved" "HITCreated" "HITExtended" "HITDisposed" "HITReviewable" "HITExpired" "Ping"))

(defonce Notification
  {:Destination s/Str
   :Transport (s/enum "Email" "SQS")
   :Version s/Str
   :EventType (maybe-sequential notification-event-type)})
