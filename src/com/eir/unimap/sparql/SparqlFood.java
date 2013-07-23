/** Author: Eirini Televantou* This class was developed by following an online tutorial: http://monead.com/blog/?p=1420 
 *  Year: 2013
 *  Organization: University of Southampton
 *  **/
package com.eir.unimap.sparql;


import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;

/**
 * Demonstrate some SPARQL operations using ARQoid
 * 
 * Built in support of a series of blog entries in the use of
 * ARQoid.
 * 
 * @version 1.0 - 20111129
 * @author David Read
 */
public class SparqlFood {
    /**
     * A trivial use of ARQoid to output CSV report of results from a SPARQL
     * endpoint.
     */
    public List<String> queryRemoteSparqlEndpoint() {
        /**
         * Use the SPARQL engine and report the results
         * 
         * @return The number of resulting rows
         */

        // Set the query
        String queryString = "PREFIX spacerel: <http://data.ordnancesurvey.co.uk/ontology/spatialrelations/>" 
        	+"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" 
        	+"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" 
        	+"PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>"
        	+"PREFIX aa: <http://purl.org/goodrelations/v1#>"
            +"SELECT DISTINCT ?label ?long ?lat ?building_number ?oilabel " 
            + "WHERE {"
            + "<http://id.southampton.ac.uk/dataset/catering> <http://rdfs.org/ns/void#dataDump> ?graph. "
            + "<http://id.southampton.ac.uk/dataset/places> <http://rdfs.org/ns/void#dataDump> ?graph1."
            + "GRAPH ?graph {"
            + "?uri <http://www.w3.org/2000/01/rdf-schema#label> ?label. "
            + "?uri a <http://purl.org/goodrelations/v1#LocationOfSalesOrServiceProvisioning>. "
            + "?uri spacerel:within ?building. "
            +"  ?ofering a <http://purl.org/goodrelations/v1#ProductOrServicesSomeInstancesPlaceholder>."
            +" ?ofering <http://www.w3.org/2000/01/rdf-schema#label> ?olabel. "
            +" ?offf aa:availableAtOrFrom ?uri."
            +"  ?offf <http://www.w3.org/2000/01/rdf-schema#label> ?oilabel."
            +" FILTER (?oilabel = ?olabel).}"
            + "GRAPH ?graph1 {"
            + "?building skos:notation ?building_number. "
            + "?building geo:lat ?lat."
            + "?building geo:long ?long."
           
            + "}"
            + "}";

        // Set the SPARQL endpoint URI
        String sparqlEndpointUri = "http://sparql.data.southampton.ac.uk/";

        // Create a Query instance
        Query query = QueryFactory.create(queryString, Syntax.syntaxSPARQL);

        // Limit the number of results returned
        // Setting the limit is  optional - default is unlimited
       // query.setLimit(10);

        // Set the starting record for results returned
        // Setting the limit is optional - default is 1 (and it is 1-based)
       // query.setOffset(1);

        // Query uses an external SPARQL endpoint for processing
        // This is the syntax for that type of query
        QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpointUri, query);

        // Execute the query and obtain results
        ResultSet resultSet = qe.execSelect();

        // Setup a place to house results for output
        StringBuffer results = new StringBuffer();

        // Get the column names (the aliases supplied in the SELECT clause)
        List<String> columnNames = resultSet.getResultVars();
        
       
        List<String> results1 = new ArrayList<String>();
        
          
        // Iterate through all resulting rows
        while (resultSet.hasNext()) {
            // Get the next result row
            QuerySolution solution = resultSet.next();

            // Iterate through the columns
            for (String var : columnNames) {
                // Add the column label to the StringBuffer
                //results.append(var + ": ");

                // Add the returned row/column data to the StringBuffer
                
                // Data value will be null if optional and not present
                if (solution.get(var) == null) {
                    results.append("{null}");
                    results1.add("null");
                // Test whether the returned value is a literal value
                } else if (solution.get(var).isLiteral()) {
                    results.append(solution.getLiteral(var).toString());
                    results1.add(solution.getLiteral(var).toString());
                // Otherwise the returned value is a URI
                } else {
                    results.append(solution.getResource(var).getURI().toString());
                    results1.add(solution.getResource(var).getURI().toString());
                }
                results.append('\n');
            }
            results.append("-----------------\n");
        }

        // Important - free up resources used running the query
        qe.close();
        
        // Return the results as a String
        return results1;
    }
    public List<String> getType() {
    	 String queryString = "PREFIX spacerel: <http://data.ordnancesurvey.co.uk/ontology/spatialrelations/>" 
    	        	+"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" 
    	        	+"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" 
    	        	+"PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>"
    	            +"SELECT DISTINCT  ?offerlabel " 
    	            + "WHERE {"
    	            + "<http://id.southampton.ac.uk/dataset/catering> <http://rdfs.org/ns/void#dataDump> ?graph. "
    	            + "<http://id.southampton.ac.uk/dataset/places> <http://rdfs.org/ns/void#dataDump> ?graph1."
    	            + "GRAPH ?graph {"
    	            + "?uri <http://www.w3.org/2000/01/rdf-schema#label> ?label. "
    	            + "?uri a <http://purl.org/goodrelations/v1#LocationOfSalesOrServiceProvisioning>. "
    	            + "?uri spacerel:within ?building. "
    	            +" ?offers a <http://purl.org/goodrelations/v1#ProductOrServicesSomeInstancesPlaceholder>."
    	            +"?offers   <http://www.w3.org/2000/01/rdf-schema#label> ?offerlabel. "
    	          
    	            + "}"
    	            + "}";

        // Set the SPARQL endpoint URI
        String sparqlEndpointUri = "http://sparql.data.southampton.ac.uk/";

        // Create a Query instance
        Query query = QueryFactory.create(queryString, Syntax.syntaxSPARQL);

        // Limit the number of results returned
        // Setting the limit is  optional - default is unlimited
       // query.setLimit(10);

        // Set the starting record for results returned
        // Setting the limit is optional - default is 1 (and it is 1-based)
       // query.setOffset(1);

        // Query uses an external SPARQL endpoint for processing
        // This is the syntax for that type of query
        QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpointUri, query);

        // Execute the query and obtain results
        ResultSet resultSet = qe.execSelect();

        // Setup a place to house results for output
        StringBuffer results = new StringBuffer();

        // Get the column names (the aliases supplied in the SELECT clause)
        List<String> columnNames = resultSet.getResultVars();
        
       
        List<String> results1 = new ArrayList<String>();
        
          
        // Iterate through all resulting rows
        while (resultSet.hasNext()) {
            // Get the next result row
            QuerySolution solution = resultSet.next();

            // Iterate through the columns
            for (String var : columnNames) {
                // Add the column label to the StringBuffer
                //results.append(var + ": ");

                // Add the returned row/column data to the StringBuffer
                
                // Data value will be null if optional and not present
                if (solution.get(var) == null) {
                    results.append("{null}");
                    results1.add("null");
                // Test whether the returned value is a literal value
                } else if (solution.get(var).isLiteral()) {
                    results.append(solution.getLiteral(var).toString());
                    results1.add(solution.getLiteral(var).toString());
                // Otherwise the returned value is a URI
                } else {
                    results.append(solution.getResource(var).getURI().toString());
                    results1.add(solution.getResource(var).getURI().toString());
                }
                results.append('\n');
            }
            results.append("-----------------\n");
        }

        // Important - free up resources used running the query
        qe.close();
        
        // Return the results as a String
        return results1;
    }



}
