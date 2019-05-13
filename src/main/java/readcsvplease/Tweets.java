package readcsvplease;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;

public class Tweets {

	@CsvBindByName(column = " ID", required = true)
     private String id;

     @CsvBindByName(column = "NAME" , required = true)
     private String userName;

    @CsvBindByName(column = "CreatedTime", required = true)
     private String createdTime;

     @CsvBindByName(column = "Text", required = true)
     private String text;

       @CsvBindByName(column = "Latitude", required = true)
       private Double latitude;
     
     
          @CsvBindByName(column = " Longitude", required = true)
     private Double longitude;

     @CsvBindByName(column = "FavoriteCount", required = true)
     private String favoriteCount;

     @CsvBindByName(column = "Language", required = true) 
     private String language;
    

     @CsvBindByName(column = "RetweetCount", required = true) 
     private String retweetCount;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	
	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public String getFavoriteCount() {
		return favoriteCount;
	}


	public void setFavoriteCount(String favoriteCount) {
		this.favoriteCount = favoriteCount;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getRetweetCount() {
		return retweetCount;
	}


	public void setRetweetCount(String retweetCount) {
		this.retweetCount = retweetCount;
	}

	

     // Getters and setters go here.
     }