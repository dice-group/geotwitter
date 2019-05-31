package geotwitter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.aksw.fox.binding.FoxApi;
import org.aksw.fox.binding.FoxParameter;
import org.aksw.fox.binding.IFoxApi;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class FoxExtractEntities {
	

	public ArrayList<String>  extractFromFox(String text) throws IOException {
		final IFoxApi fox = new FoxApi().setApiURL(new URL("http://fox.cs.uni-paderborn.de:4444/fox"))
				.setTask(FoxParameter.TASK.NER).setOutputFormat(FoxParameter.OUTPUT.TURTLE)
				.setLang(FoxParameter.LANG.EN).setInput(text).send();

		final String jsonld = fox.responseAsFile();
		Model model = ModelFactory.createDefaultModel().read(IOUtils.toInputStream(jsonld, "UTF-8"), null, "TTL");
		return sparqlOnRdfFile( model);

	}

	

	public ArrayList<String> sparqlOnRdfFile(Model rdf) {
	
		ArrayList<String> locations = new ArrayList<String>();

		String queryString = "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
				+ "PREFIX dbo: <http://dbpedia.org/ontology/>" + "prefix foxo:  <http://ns.aksw.org/fox/ontology#>"
				+ "prefix schema: <http://schema.org/>" + "prefix oa:    <http://www.w3.org/ns/oa#>"
				+ "prefix foxr:  <http://ns.aksw.org/fox/resource#>"
				+ "prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "prefix dbr:   <http://dbpedia.org/resource/>" + "prefix xsd:   <http://www.w3.org/2001/XMLSchema#>"
				+ "prefix its:   <http://www.w3.org/2005/11/its/rdf#>"
				+ "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#>"
				+ "prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#>"
				+ "prefix prov:  <http://www.w3.org/ns/prov#>" + "prefix foaf:  <http://xmlns.com/foaf/0.1/>" +

				"SELECT * WHERE {" + "  ?s its:taClassRef dbo:Place ." + "  ?s nif:anchorOf ?loc ." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, rdf);
		ResultSet results = qexec.execSelect();
		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			Literal name = soln.getLiteral("loc");
			locations.add(name.toString());
		}

		return locations;

	}

}
