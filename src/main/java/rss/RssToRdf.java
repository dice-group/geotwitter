package rss;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import geotwitter.FindGeocoordinates;
import geotwitter.FoxExtractEntities;




public class RssToRdf {
	static String SR = "http://sageproject.org/resource#";
	static String SO = "http://sageproject.org/ontology#";



public static void main(String[] args) throws IOException {
	
	String fileName = "/home/suganya/TheFinalOutput_RSS.ttl";
	FileWriter out = new FileWriter(fileName) ;
	
    final String dcat = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
    Model model = ModelFactory.createDefaultModel();
    model.read("/home/suganya/RSS-500.ttl");
    Resource datasetType = model.getResource( dcat + "RFC5147String" );
    
    ResIterator datasets = model.listSubjectsWithProperty( RDF.type, datasetType );
	Property has_test = model.createProperty(dcat + "isString");
	FoxExtractEntities fox=new FoxExtractEntities();
	FindGeocoordinates geo=new FindGeocoordinates();
	

    while ( datasets.hasNext() ) {
      Resource dataset = datasets.next();
      if(dataset.hasProperty(has_test))
      {
 System.out.println("inside");

      ArrayList<String> locations=new ArrayList<String>();
		String coordinates[] = new String[3];
		Property has_geometry = model.createProperty(dcat + "has_geometry");
		Property has_location = model.createProperty(dcat + "has_location");
		Property has_latitude = model.createProperty(dcat + "has_latitude");

		Property has_longitude = model.createProperty(dcat + "has_longitude");
		


		locations=fox.extractFromFox(dataset.getRequiredProperty(has_test).getObject().toString());
		for(int i=0;i<locations.size();i++)
		{


			Resource locationUri = model.createResource(SO  + locations.get(i));
			Resource locationtype = model.createResource(SO + "Location");

			dataset.addProperty(has_location, locationUri);
			coordinates=geo.findGeometry(locations.get(i));
		


			if(coordinates[0]!=null&&coordinates[1]!=null&&coordinates[2]!=null)
			{
				locationUri
				.addProperty(has_latitude, coordinates[0])
				.addProperty(has_longitude, coordinates[1])
				.addProperty(has_geometry,coordinates[2])
				.addProperty(RDF.type, locationtype)
				.addProperty(RDFS.label, locations.get(i));
			}
			dataset.addProperty(has_location, locationUri);

			

		}
	
		

      StmtIterator stmts  = dataset.listProperties();
     
    }
    }
	RDFDataMgr.write(System.out, model, Lang.TURTLE);
	model.write(out, "TURTLE");
	System.out.println("written");


  }
}