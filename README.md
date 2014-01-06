# `biomass`

> Det. Thorn: "Who bought you?"

> Hatcher: "You're bought as soon as they pay you a salary."

> -- *Soylent Green*

Drive [Amazon Mechanical Turk](http://mturk.com) from your Clojure apps.

# Installation

`biomass` is available as a Maven artifact from [Clojars](http://clojars.org/biomass)
```clojure
[com.smniven/biomass "0.5.0"]
```

# Configuration

Before making any requests, be sure to set your AWS credentials

```clojure
(biomass.request/set-aws-creds {:AWSAccessKey    "deadbeef"
                                :AWSSecretAccessKey "cafebabe"})
```

# Usage

Amazon Mechanical Turk allocates jobs to humans in the form of "Human
Intelligence Tasks" or "HITs".

First create a HIT type and Layout using the [Requester UI](http://docs.aws.amazon.com/AWSMechTurk/latest/RequesterUI/Welcome.html).

Example of creating a HIT:

```clojure
(let [hit-type-id     "VCZVWLDJOTFFJXXQLGXZ"
      hit-layout-id   "WMYUHDBKJKNGOAMNCNMT"
      10-minutes      (* 10 60)
      1-day           (* 24 60 60)]
  (biomass.hits/create-hit {:hit-type-id hit-type-id
                            :hit-layout-id hit-layout-id 
                            :assignment-duration 10-minutes
                            :lifetime 1-day
                            :layout-params {:layout-parameter1 "This is a variable defined in the layout"
                                            :layout-parameter2 "This is another"}})
```

## License

Copyright © 2014 Thomas Steffes

Distributed under the Eclipse Public License, the same as Clojure.
