package geotwitter;

import java.util.Date;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class GeoTweets {
	public static void main(String[] args) {
		

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("");
		cb.setOAuthConsumerSecret("");
		cb.setOAuthAccessToken("");
		cb.setOAuthAccessTokenSecret("");

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		StatusListener listener = new StatusListener() {

			public void onStatus(Status status) {
				User user = status.getUser();
				// gets Username
				String username = status.getUser().getScreenName();
				GeoLocation geolocation = status.getGeoLocation();
				double tweetLatitude = geolocation.getLatitude();
				double tweetLongitude = geolocation.getLongitude();
				System.out.println("username"+" "+username+","+"latitude "+" "+tweetLatitude + "," + "longitude"+" "+ tweetLongitude +"\n");
		
				
			}

			public void onException(Exception ex) {
				// TODO Auto-generated method stub
				
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				// TODO Auto-generated method stub
				
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				// TODO Auto-generated method stub
				
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				// TODO Auto-generated method stub
				
			}

			public void onStallWarning(StallWarning warning) {
				// TODO Auto-generated method stub
				
			}

			
		};
		
		
		double[][] locations = { { 5.98865807458,47.3024876979  },
	            { 15.0169958839, 54.983104153 } };
	FilterQuery query = new FilterQuery().locations(locations).track("Germany");

	
		twitterStream.addListener(listener);


		twitterStream.filter(query);  
		System.out.println("printing username and geolocations");


	}
}