package geotwitter;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

public class FindGeocoordinates {

	public String[] findGeometry(String location) {
		// Find the geo-coordinates of a place
		
		String coordinates[] = new String[3];

		final String dbpedia = "http://dbpedia.org/sparql";
		ParameterizedSparqlString queryString = new ParameterizedSparqlString( 
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
				+ "PREFIX dbo: <http://dbpedia.org/ontology/>" 
						+ "SELECT * WHERE { "
						+ " ?s a dbo:Place ."
						+ " ?s geo:lat ?lat ."
						+ " ?s geo:long ?long ."
						+ " ?s geo:geometry ?geo"
						+ "}");
	//	System.out.println("the location is"+location.replaceAll("\\s+",""));

		String iri = "http://dbpedia.org/resource/" + location.replaceAll("\\s+","");
		queryString.setIri("?s", iri);
		QueryExecution exec = QueryExecutionFactory.sparqlService(dbpedia, queryString.toString());
		ResultSet results = exec.execSelect();
		if (results.hasNext()) {
			QuerySolution soln = results.nextSolution();

			String latitude = soln.getLiteral("lat").getLexicalForm();
			String longitude = soln.getLiteral("long").getLexicalForm();
			String geometry = soln.getLiteral("geo").getLexicalForm();
			coordinates[0]=latitude;
			coordinates[1]=longitude;
			coordinates[2]=geometry;
			
			

		}
		return coordinates;


	}
}
