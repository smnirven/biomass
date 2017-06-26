# `biomass`

Drive [Amazon Mechanical Turk](http://mturk.com) from your Clojure apps.

Biomass is an implementation of the Amazon Web Services Mechanical Turk REST API in clojure.

Previously [smnirven/biomass](https://github.com/smnirven/biomass)

# Build Status

[![Build Status](https://travis-ci.org/shafeeq/biomass.svg?branch=operations-with-schema)](https://travis-ci.org/shafeeq/biomass)

# Kudos

Kudos to [Robert Boyd](https://github.com/rboyd) and [Thomas Steffes](https://github.com/smnirven) for the previous implementations

# Configuration

Before making any requests, be sure to set your AWS credentials

```clojure
(biomass.request/setup {:AWSAccessKey    "aws-access-key"
                        :AWSSecretAccessKey "aws-secret-key"})
```

For using the library in sandbox mode, add `:sandbox true` to the map.

# Usage

Amazon Mechanical Turk allocates jobs to humans in the form of "Human
Intelligence Tasks" or "HITs".

See the [Getting Started Guide] (http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMechanicalTurkGettingStartedGuide/Welcome.html) for an overview of Amazon Mechanical Turk for developers.

See the [API reference](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/Welcome.html) for documentation on various operations and their parameters.

All operations are made in the format ` (biomass.request/requester :operation {params})`, where `:operation` is the keyword form of an operation defined in the API, and  `params` is a map from [schemas](src/biomass/schemas/).

Example of creating a HIT:
```clojure
(biomass.request/requester :CreateHIT {:HITTypeId "3L55M9M850CUHK36475FRIWIOKN9OL"
                                       :HITLayoutId "3H03YZA6SOB7IRBTG3CTKIC1RJF8EW"
                                       :HITLayoutParameter [{:Name "name" :Value "John Doe"}
                                                            {:Name "phone" :Value "000-000-000"}]
                                       :LifetimeInSeconds 6000})
```

Note that the nested layout params is also a map from schemas. Multiple parameters are passed in a vector.

Sample response:
```clojure
{:status :success,
 :response
 [{:CreateHITResponse
   ({:OperationRequest ({:RequestId ("cb2cf94e-b2e2-448c-a2c6-41806bd2e046")})}
    {:HIT
     ({:Request ({:IsValid ("True")})}
      {:HITId ("32W3UF2EZO49LXS83EVHVUYUB0PC4T")}
      {:HITTypeId ("3L55M9M850CUHK36475FRIWIOKN9OL")})})}]}
```

The XML response is parsed to a data structure similar to above, where the tag name is a key in a map and its value is a list that contains its children nodes. See the API for specifics about various types of response formats and fields.

The `status` is always `:success` if the request returned a status code 200, irrespective of whether the request was valid for the API. Check the `:IsValid` in response to test whether the request was valid at the API level.

## More examples

Creating a HITType with qualification:
```clojure
(let [qualification {:QualificationTypeId "00000000000000000071"
                     :Comparator "In"
                     :LocaleValue [{:Country "US"}
                                   {:Country "IN"}
                                   {:Country "GB"}]
                     :RequiredToPreview false}]

  (biomass.request/requester :RegisterHITType {:Title (str "TestHITType" (time/now))
                                               :Description "Test generated hittype"
                                               :Reward {:Amount 0.50 :CurrencyCode "USD"}
                                               :AssignmentDurationInSeconds 600
                                               :Keywords "test"
                                               :QualificationRequirement qualification}))
```

Approving all submitted assignments and disposing hits:
```clojure
(defn get-ids
  [response path target-key]
  (->> [response]
       (biomass.util/find-in-response-with-path path)
       (map target-key)
       flatten))

(defn approve-assignments
  [hit-id]
  (doseq [assignment (get-ids (biomass.request/requester :GetAssignmentsForHIT {:HITId hit-id :AssignmentStatus "Submitted"})
                              [:response :GetAssignmentsForHITResponse :GetAssignmentsForHITResult :Assignment :AssignmentId]
                              :AssignmentId)]
    (biomass.request/requester :ApproveAssignment {:AssignmentId assignment :RequesterFeedback "Approved"})))

(defn dispose-hits
  []
  (doseq [hit (get-ids (biomass.request/requester :GetReviewableHITs {})
                       [:response :GetReviewableHITsResponse :GetReviewableHITsResult :HIT :HITId]
                       :HITId)]
    (approve-assignments hit)
    (biomass.request/requester :DisposeHIT {:HITId hit})))
```

## Testing

Set the `AWS_ACCESS_KEY`, `AWS_SECRET_KEY`, and `SANDBOX_WORKER_ID` environment variables before testing.
Example:
```bash
AWS_ACCESS_KEY="access-key" AWS_SECRET_KEY="secret-key" SANDBOX_WORKER_ID="worker-id" lein test
```

## License

Copyright © 2017 Shafeeq Kunnakkadan and Akshay Gupta.  
  Released under the MIT license. http://mit-license.org
