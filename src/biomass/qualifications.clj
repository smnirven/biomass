(ns biomass.qualifications
  (:require [biomass.builder.schemas :as schemas]
            [schema.core :as s]))

(def qualifications-operations
  {:DisposeQualificationType {:op-string "DisposeQualificationType"
                              :schema schemas/DisposeQualificationType}

   :GetQualificationRequests {:op-string "GetQualificationRequests"
                              :schema schemas/GetQualificationRequests}

   :GetQualificationsForQualificationType {:op-string "GetQualificationsForQualificationType"
                                           :schema schemas/GetQualificationsForQualificationType}

   :GetQualificationScore {:op-string "GetQualificationScore"
                           :schema schemas/GetQualificationScore}

   :CreateQualificationType {:op-string "CreateQualificationType"
                             :schema schemas/CreateQualificationType}

   :UpdateQualificationType {:op-string "UpdateQualificationType"
                             :schema schemas/UpdateQualificationType}

   :RevokeQualification {:op-string "RevokeQualification"
                         :schema schemas/RevokeQualification}

   :GrantQualification {:op-string "GrantQualification"
                        :schema schemas/GrantQualification}

   :SearchQualificationTypes {:op-string "SearchQualificationTypes"
                              :schema schemas/SearchQualificationTypes}

   :GetQualificationType {:op-string "GetQualificationType"
                          :schema schemas/GetQualificationType}

   :RejectQualificationRequest {:op-string "RejectQualificationRequest"
                                :schema schemas/RejectQualificationRequest}

   :AssignQualification {:op-string "AssignQualification"
                         :schema schemas/AssignQualification}

   :UpdateQualificationScore {:op-string "UpdateQualificationScore"
                              :schema schemas/UpdateQualificationScore}})

(def int-val-comparators (s/enum "LessThan" "LessThanOrEqualTo" "GreaterThan" "GreaterThanOrEqualTo"
                                 "EqualTo" "NotEqualTo" "In" "NotIn"))
(def worker-locale-id "00000000000000000071")
(def locale-val-compartors (s/enum "EqualTo" "NotEqualTo" "In" "NotIn"))
(def exists-comparators (s/enum "Exists" "NotExists"))

(defmacro throw-with-msg
  [message]
  (list 'throw (list 'clojure.lang.ExceptionInfo. (list 'str "Malformed QualificationRequirement: " message) 'params)))

(defn- validate-qualification-requirement-with-integer-value
  [params]
  (s/validate int-val-comparators (:Comparator params))

  (cond
    (= (:QualificationTypeId params) worker-locale-id)
    (throw-with-msg "Cannot use IntegerValue with Worker_Locale QualificationTypeId")

    (and (sequential? (:IntegerValue params))
         (> (count (:IntegerValue params)) 15))
    (throw-with-msg "Can use up to only 15 IntegerValue elements using the In or the NotIn Comparator")))


(defn- validate-qualification-requirement-with-locale-value
  [params]
  (s/validate locale-val-compartors (params :Comparator))

  (cond
    (not= worker-locale-id (:QualificationTypeId params))
    (throw-with-msg "LocaleValue can only be used with Worker_Locale QualificationTypeId")

    (and (some #{"EqualTo" "NotEqualTo"} [(params :Comparator)])
         (sequential? (params :LocaleValue)))
    (throw-with-msg (str "Expected a single LocaleValue for Comparator " (:Comparator params)))

    (and (some #{"In" "NotIn"} [(params :Comparator)])
         (> (count (params :LocaleValue)) 30))
    (throw-with-msg "Can use up to only 30 LocaleValue elements using the In or the NotIn Comparator")))

(defn- validate-qualification-requirement-no-values
  [params]
  (try (s/validate exists-comparators (:Comparator params))
       (catch clojure.lang.ExceptionInfo e
         (throw-with-msg (str "Missing IntegerValue or LocaleValue for the " (:Comparator params) " Comparator")))))

(defn validate-qualification-requirement
  [params]
  (if (sequential? params)
    (if (every? some? (map validate-qualification-requirement params))
      params
      (throw-with-msg "Unknown error in qualification requirement"))

    (do (s/validate schemas/QualificationRequirement params)

        (when (and (:IntegerValue params) (:LocaleValue params))
          (throw-with-msg "Both IntegerValue and LocaleValue cannot be present at the same time"))

        (case (some #{:IntegerValue :LocaleValue} (keys params))
          :IntegerValue (validate-qualification-requirement-with-integer-value params)
          :LocaleValue (validate-qualification-requirement-with-locale-value params)

          (validate-qualification-requirement-no-values params))

        params)))
