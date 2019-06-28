# geotwitter
Linking LinkedGeoData with twitter

A tweet dataset [Tweets_Dataset](https://github.com/dice-group/geotwitter/blob/master/Tweets_Dataset/)  containing 44997 tweets is created using the Twitter streaming API with the following bounding boxes value of Germany.


{ 5.98865807458, 47.3024876979 }, { 15.0169958839, 54.983104153 }

The locations from the tweet are extracted using [FOX Extractor](http://aksw.org/Projects/FOX.html).

The tweet dataset [Tweets_Dataset](https://github.com/dice-group/geotwitter/blob/master/Tweets_Dataset/) is then converted into  [RDF](https://github.com/dice-group/geotwitter/blob/master/Tweets_RDF.ttl)

The final RDF is then uploaded in the Sparql server http://sparql.upb.de:8890/sparql in the graph http://sageproject.org/graph/Tweet.


The endpoint is then linked to geodata using  [LIMES](http://dice.cs.uni-paderborn.de/projects/active-projects/limes/) to find tweets nearby Restaurants, Tourism Things, Hospitals, Supermarkets and Museums.

Inside the folder Linking_Museum, You can find the LIMES configuration file used for linking the tweets with the http://linkedgeodata.org/ontology/Museum class and the ouput of the linking process. 

The RSS feed from https://old.datahub.io/dataset/rss-500-nif-ner-corpus is also converted into RDF.





