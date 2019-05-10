package geotwitter;

import twitter4j.*;

import twitter4j.Query.ResultType;
import twitter4j.Query.Unit;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeoTweetsToCsv {
	private static final String OUTPUT_FILE = "/home/suganya/Tweets/Moonie.csv";
	private static final String CONSUMER_KEY = "";
	private static final String CONSUMER_SECRET = "";
	private static final String OAUTH_ACCESS = "";
	private static final String OAUTH_SECRET = "";

	private static final int TWEETS_PER_QUERY = 100;

	private static final int MAX_QUERIES = 3;

	public static String cleanText(String text) {
		text = text.replace("\n", "\\n");
		text = text.replace("\t", "\\t");

		return text;
	}

	public static Twitter getTwitter() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET)
				.setOAuthAccessToken(OAUTH_ACCESS).setOAuthAccessTokenSecret(OAUTH_SECRET);

		return new TwitterFactory(cb.build()).getInstance();

	}

	public static void main(String[] args) {
		ArrayList<String> myUsers = new ArrayList<String>();

		int totalTweets = 0;

		long maxID = -1;

		Twitter twitter = getTwitter();
		/*
		 * if (!(args.length == 1)) {
		 * System.err.println("PLease enter the search term"); System.exit(1); } else {
		 * String searchTerm = "Germany"; }
		 */

		try {
			Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");

			RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");

			System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds\n",
					searchTweetsRateLimit.getRemaining(), searchTweetsRateLimit.getLimit(),
					searchTweetsRateLimit.getSecondsUntilReset());
			File csvFile = new File("/home/suganya/Tweets/Hello.csv");
			Writer writer = Files.newBufferedWriter(Paths.get(OUTPUT_FILE));

			try (CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);) {
				for (int queryNumber = 0; queryNumber < MAX_QUERIES; queryNumber++) {
					System.out.printf("\n\n!!! Starting loop %d\n\n", queryNumber);

					if (searchTweetsRateLimit.getRemaining() == 0) {
						System.out.printf("!!! Sleeping for %d seconds due to rate limits\n",
								searchTweetsRateLimit.getSecondsUntilReset());

						Thread.sleep((searchTweetsRateLimit.getSecondsUntilReset() + 2) * 1000l);
					}

					Query q = new Query("Germany"); // Search for tweets that contains this term
					q.setCount(TWEETS_PER_QUERY); // How many tweets, max, to retrieve
					q.resultType(ResultType.mixed); // Get all tweets
					// q.setLang("en"); // English language tweets, please

					double longitude = 10.2392578125;
					double latitude = 51.86292391360244;
					GeoLocation location = new GeoLocation(latitude, longitude);
					Unit unit = Query.KILOMETERS; // or Query.MILES;
					double radius = 9000;
					q.setGeoCode(location, radius, unit);

					if (maxID != -1) {
						q.setMaxId(maxID - 1);
					}

					QueryResult r = twitter.search(q);

					if (r.getTweets().size() == 0) {
						break; // Nothing? We must be done
					}
					String[] headerRecord = { "Name", "Text", "Latitude", "Longitude" };

					csvWriter.writeNext(headerRecord);

					for (Status s : r.getTweets()) {
						if (s.getGeoLocation() == null) {
							continue;
						}
						totalTweets++;

						if (maxID == -1 || s.getId() < maxID) {
							maxID = s.getId();
						}

						String username = s.getUser().getScreenName();
						String text = cleanText(s.getText());
						GeoLocation geolocation = s.getGeoLocation();
						double tweetLatitude = geolocation.getLatitude();
						double tweetLongitude = geolocation.getLongitude();
						String output = username + "," + text + "," + tweetLatitude + "," + tweetLongitude;

						System.out.println("the output is");
						System.out.println(new String[] { output });
						csvWriter.writeNext(new String[] { output });

					}
					searchTweetsRateLimit = r.getRateLimitStatus();

				}
			}

		} catch (Exception e) {
			System.out.println("That didn't work well...wonder why?");

			e.printStackTrace();

		}

		System.out.printf("\n\nA total of %d tweets retrieved\n", totalTweets);

	}

}