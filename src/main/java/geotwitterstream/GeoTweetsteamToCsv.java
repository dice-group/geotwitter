package geotwitterstream;


import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeoTweetsteamToCsv {
	private static final String CONSUMER_KEY = "";
	private static final String CONSUMER_SECRET = "";
	private static final String OAUTH_ACCESS = "";
	private static final String OAUTH_SECRET = "";
	private static final String OUTPUT_FILE = "/home/suganya/finaltweets.csv";

	public static String cleanText(String text) {
		text = text.replace("\n", "\\n");
		text = text.replace("\t", "\\t");
		text = text.replace(",", "");

		return text;
	}

	public static void main(String[] args) throws IOException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(CONSUMER_KEY);
		cb.setOAuthConsumerSecret(CONSUMER_SECRET);
		cb.setOAuthAccessToken(OAUTH_ACCESS);
		cb.setOAuthAccessTokenSecret(OAUTH_SECRET);


		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		 StatusListener listener = new StatusListener() {

				public void onStatus(Status status) {
					 Writer writer = null;
					try {
						writer = Files.newBufferedWriter(Paths.get(OUTPUT_FILE));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
					 String[] headerRecord = { "ID", "NAME", "Created Time", "Text", "Latitude", "Longitude", "Favorite Count",
							"Language", "Retweet Count"};
					CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR,
							CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
							CSVWriter.DEFAULT_LINE_END);

						csvWriter.writeNext(headerRecord);

						String username = status.getUser().getScreenName();

						String text = cleanText(status.getText());
						GeoLocation geolocation = status.getGeoLocation();
						double tweetLatitude = geolocation.getLatitude();
						double tweetLongitude = geolocation.getLongitude();
						String output = status.getId() + "," + username + "," + status.getCreatedAt() + "," + text + ","
								+ tweetLatitude + "," + tweetLongitude + "," + status.getFavoriteCount() + ","
								+ status.getLang() + "," + status.getRetweetCount();

						System.out.println(output);
						csvWriter.writeNext(new String[] { output });

					
				}

				@Override
				public void onException(Exception ex) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onScrubGeo(long userId, long upToStatusId) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStallWarning(StallWarning warning) {
					// TODO Auto-generated method stub
					
				}

				
				



			};


		double[][] locations = { { 5.98865807458, 47.3024876979 }, { 15.0169958839, 54.983104153 } };
		FilterQuery query = new FilterQuery().locations(locations);// .track("Germany");

		twitterStream.addListener(listener);

		twitterStream.filter(query);

	}


}