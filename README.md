# `biomass`

> Det. Thorn: "Who bought you?"

> Hatcher: "You're bought as soon as they pay you a salary."

> -- *Soylent Green*

Drive [Amazon Mechanical Turk](http://mturk.com) from your Clojure apps.

biomass is an implementation of the Amazon Web Services Mechanical Turk REST API in clojure.

#Build Status
[![Build Status](https://api.travis-ci.org/smnirven/biomass.png)](http://travis-ci.org/smnirven/biomass)
[![Dependency Status](https://www.versioneye.com/user/projects/52de9f66ec1375d5f7000473/badge.png)](https://www.versioneye.com/user/projects/52de9f66ec1375d5f7000473)
# Kudos
Kudos to [Robert Boyd] (https://github.com/rboyd) for the original implementation and inspiration 

# Installation

`biomass` is available as a Maven artifact from [Clojars](http://clojars.org/com.smnirven/biomass)

![Clojars Project](http://clojars.org/com.smnirven/biomass/latest-version.svg)

# Configuration

Before making any requests, be sure to set your AWS credentials

```clojure
(biomass.request/setup {:AWSAccessKey    "deadbeef"
                        :AWSSecretAccessKey "cafebabe"})
```

Whilst testing, you may find it useful to route all your requests to the AWS Mechanical Turk Sandbox environment.

```clojure
(biomass.request/setup {:AWSAccessKey    "deadbeef"
                        :AWSSecretAccessKey "cafebabe"
                        :sandbox true})
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
[Check out the wiki for more usage examples](https://github.com/smnirven/biomass/wiki)

## License

Copyright © 2014 Thomas Steffes

Distributed under the Eclipse Public License, the same as Clojure.
