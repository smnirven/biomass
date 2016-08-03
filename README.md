# `biomass`

Drive [Amazon Mechanical Turk](http://mturk.com) from your Clojure apps.

biomass is an implementation of the Amazon Web Services Mechanical Turk REST API in clojure.

#Info
This branch is currently under development

# Configuration

Before making any requests, be sure to set your AWS credentials

```clojure
(biomass.request/set-aws-creds {:AWSAccessKey    "aws-access-key"
                                :AWSSecretAccessKey "aws-secret-key"})
```

For using the library in sandbox mode, add `:sandbox true` to the map above.

# Usage

Amazon Mechanical Turk allocates jobs to humans in the form of "Human
Intelligence Tasks" or "HITs".

First create a HIT type and Layout using the [Requester UI](http://docs.aws.amazon.com/AWSMechTurk/latest/RequesterUI/Welcome.html).

See the [API reference](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/Welcome.html) for documentation on various operations and their parameters.

All opearations are made in the format ` (operation {params})`, where params is a map from [schemas.](src/biomass/builder/schemas.clj)

Example of creating a HIT:
```clojure
(biomass.hits/create-hit {:HITTypeId "3L55M9M850CUHK36475FRIWIOKN9OL"
                          :HITLayoutId "3H03YZA6SOB7IRBTG3CTKIC1RJF8EW"
                          :HITLayoutParameter [{:Name "name" :Value "John Doe"}
                                               {:Name "phone" :Value "000-000-000"}]
                          :LifetimeInSeconds 6000})
```

Note that the nested layout params is also a map from schemas. Multiple paramters are passed in a vector.

Sample respose:
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

For examples of parsing the response, see [test_helpers.clj](test/biomass/test_helpers.clj)

##Testing

Set the `AWS_ACCESS_KEY`, `AWS_SECRET_KEY`, and `SANDBOX_WORKER_ID` environment variables before testing.
Example:
```bash
AWS_ACCESS_KEY="access-key" AWS_SECRET_KEY="secret-key" SANDBOX_WORKER_ID="worker-id" lein test
```

## License

Copyright © 2016 Shafeeq Kunnakkadan

Distributed under the Eclipse Public License, the same as Clojure.
