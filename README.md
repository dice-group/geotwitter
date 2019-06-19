# geotwitter
Linking LinkedGeoData with twitter

A tweet dataset [Tweets_Dataset](https://github.com/dice-group/geotwitter/blob/master/Tweets_Dataset/)  containing 44997 tweets is created using the Twitter streaming API with the following bounding boxes value of Germany.


{ 5.98865807458, 47.3024876979 }, { 15.0169958839, 54.983104153 }

The locations from the tweet are extracted using [FOX Extractor](http://aksw.org/Projects/FOX.html).

The tweet dataset [Tweets_Dataset](https://github.com/dice-group/geotwitter/blob/master/Tweets_Dataset/) is then converted into a [RDF](https://github.com/dice-group/geotwitter/blob/master/Tweets_RDF.ttl)

The Tweets_RDF is uploaded in the Sparql server http://sparql.upb.de:8890/sparql in the graph http://sageproject.org/graph/Tweet.


The endpoint is then linked to geodata using  [LIMES](http://dice.cs.uni-paderborn.de/projects/active-projects/limes/) to find tweets nearby Restaurants, Tourism Things, Hospitals, Supermarkets and Museums.





