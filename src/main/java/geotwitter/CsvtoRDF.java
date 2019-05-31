package geotwitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class CsvtoRDF {
	static String COMMA_DELIMITER = ",";
	static Model model = ModelFactory.createDefaultModel();

	static String SO = "http://sageproject.org/ontology#";
	static String SR = "http://sageproject.org/resource#";
	static String RDFLABEL = "http://www.w3.org/2000/01/rdf-schema#label";


	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader("/home/suganya/finaltweets"));
		String line;
		String fileName = "/home/suganya/output.ttl";
		FileWriter out = new FileWriter(fileName) ;
		
		
		FoxExtractEntities fox=new FoxExtractEntities();
		FindGeocoordinates geo=new FindGeocoordinates();
		
		model.setNsPrefix("SO", SO);
		model.setNsPrefix("SR", SR);
		model.setNsPrefix("RDFLABEL", RDFLABEL);
		Double longitude=0.0,latitude=0.0;

		
		while ((line = br.readLine()) != null) {
		
			String[] values = line.split(COMMA_DELIMITER);
			Resource tweetType = model.createResource(SR + "Tweet");
			System.out.println(values[0]);
			/*System.out.println(values[1]);
			System.out.println(values[2]);
			System.out.println(values[3]);
			System.out.println(values.length);*/
			
			if(values.length>=6)
{
	

			longitude=Double.parseDouble(values[4]);

			 latitude=Double.parseDouble(values[5]);
}
			GeometryFactory geometryFactory = new GeometryFactory();
			Coordinate coord = new Coordinate(latitude,longitude);
			Geometry point = geometryFactory.createPoint(coord);
			String text=values[3].replace('#', ' ').trim();
			String id=values[0].replace('"', ' ').trim();
			//Creating Resource
			Resource tweet = model.createResource(SR + "tweet" + id);
			Resource locationtype = model.createResource(SO + "Location");
			


			
			
			//Creating properties 
			Property has_id = model.createProperty(SO + "has_id");
			Property has_geometry = model.createProperty(SO + "has_geometry");
			Property has_location = model.createProperty(SO + "has_location");
			Property has_latitude = model.createProperty(SO + "has_latitude");

			Property has_longitude = model.createProperty(SO + "has_longitude");

			ArrayList<String> locations=new ArrayList<String>();
			String coordinates[] = new String[3];

			locations=fox.extractFromFox(text);
			for(int i=0;i<locations.size();i++)
			{
				Resource locationUri = model.createResource(SR  + locations.get(i));
				tweet.addProperty(has_location, locationUri);
				coordinates=geo.findGeometry(locations.get(i));
				


				if(coordinates[0]!=null&&coordinates[1]!=null&&coordinates[2]!=null)
				{
					locationUri
					.addProperty(has_latitude, coordinates[0])
					.addProperty(has_longitude, coordinates[1])
					.addProperty(has_geometry, coordinates[2])
					.addProperty(RDF.type, locationtype)
					.addProperty(RDFS.label, locations.get(i));
				}
				tweet.addProperty(has_location, locationUri);

				

			}
			
			
			


			
			tweet.addProperty(RDF.type, tweetType).
			addProperty(RDFS.label, text).
			addProperty(has_id, id).
			addProperty(has_geometry, point.toString());
			



		}
		
		RDFDataMgr.write(System.out, model, Lang.TURTLE);
		model.write(out, "TURTLE");


	}

}
