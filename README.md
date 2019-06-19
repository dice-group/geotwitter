# geotwitter
Linking LinkedGeoData with twitter

A twitter dataset containing 44997 tweets is created using the Twitter streaming API with the following bounding boxes value of Germany.


{ 5.98865807458, 47.3024876979 }, { 15.0169958839, 54.983104153 }

The locations from the tweet are extracted using FOX Extractor.

The tweet dataset(Tweets_Dataset) is then converted into a RDF(Tweets_RDF).

The Tweets_RDF is uploaded in the Sparql server http://sparql.upb.de:8890/sparql in the graph http://sageproject.org/graph/Tweet.


The endpoint is then linked to geodata using LIMES to find tweets nearby Restaurants, Tourism Things, Hospitals, Supermarkets and Museums.





