package readcsvplease;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.opencsv.bean.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import com.vividsolutions.jts.geom.Point;

public class FinalCsvToRDF {

	static Model model = ModelFactory.createDefaultModel();
	static String SO = "http://sageproject.org/ontology#";
	static String SR = "http://sageproject.org/resource#";

	public static void main(String args[]) throws IOException {
		model.setNsPrefix("SO", SO);
		model.setNsPrefix("SR", SR);
		FileReader Reader = new FileReader("/home/suganya/sample1.csv");
		List<Tweets> beans = new CsvToBeanBuilder(Reader).withType(Tweets.class).build().parse();
		OutputStream out = new FileOutputStream("output.ttl");
		
			for(int i=0; i<beans.size();i++)
			{
				createRDF(beans.get(i).getId(),beans.get(i).getCreatedTime(),beans.get(i).getText(),beans.get(i).getUserName(),beans.get(i).getLatitude(),beans.get(i).getLongitude(),beans.get(i).getRetweetCount(),beans.get(i).getLanguage(),beans.get(i).getFavoriteCount());
				
			}
			RDFDataMgr.write(System.out, model, Lang.TTL);
		//	RDFDataMgr.write(out, model, Lang.TTL);

			
			

		}
	
	public static String cleanText(String text) {
		text = text.replace("\n", "\\n");
		text = text.replace("\t", "\\t");
		text = text.replace(",", "");

		return text;
	}

	


private static void createRDF(String id, String createdTime, String text, String userName, Double latitude,
		Double longitude, String retweetCount, String language, String favoriteCount) {		
		Property has_id = model.createProperty(SO + "has_ID");
		Property has_createdtime = model.createProperty(SO + "has_createdTime");
		Property has_username = model.createProperty(SO + "has_username");
		Property has_latitude = model.createProperty(SO + "has_latitude");
		Property has_longitude = model.createProperty(SO + "has_longitude");
		Property has_language = model.createProperty(SO + "has_language");
		Property has_retweetcount = model.createProperty(SO + "has_retweetcount");
		Property has_favoritecount = model.createProperty(SO + "has_favoriteCount");
		Property has_text = model.createProperty(SO + "has_text");


		Property has_geometry = model.createProperty(SO + "has_geometry");
		
	        GeometryFactory geometryFactory = new GeometryFactory();
	        Coordinate coord = new Coordinate(latitude, longitude);
	        Geometry point = geometryFactory.createPoint(coord);


		Resource statement = model.createResource("SR"+ ":"+id);
		Resource tweetType = model.createResource(SO + "Tweet");
		
		 
		  statement.addProperty(has_id,id);
		  statement.addProperty(has_createdtime,createdTime);

		  statement.addProperty(has_username,userName);
		  statement.addProperty(has_text,cleanText(text));

		  statement.addProperty(has_latitude,latitude.toString());
		  statement.addProperty(has_longitude,longitude.toString());
		  statement.addProperty(has_retweetcount,retweetCount);
		  statement.addProperty(has_favoritecount,favoriteCount); 
		  statement.addProperty(has_language,language);		 
		statement.addProperty(has_geometry,point.toString());
		 

		statement.addProperty(RDF.type, tweetType);



	}


}
