package com.srlab.methodretriever.technique.bm25;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

public class BM25 {
	
	private List<BM25Method> bm25Methods;

	/** The constant k_1.*/
    private double k_1 = 1.2d;

    

    /** The parameter b.*/
    private double b;

    /** A default constructor.*/
    public BM25() {
            super();
            b = 0.75d;
    }
    
    /**
     * Uses BM25 to compute a weight for a term in a document.
     * @param tf The term frequency in the document
     * @param numberOfDocuments number of documents
     * @param docLength the document's length
     * @param averageDocumentLength average document length
     * @return the score assigned to a document with the given
     *         tf and docLength, and other preset parameters
     */
    public final double score(double tf, 
    		double numberOfDocuments, 
    		double docLength, 
    		double averageDocumentLength, 
    		double documentFrequency) {
    		
    	  
    	    double idf = Math.log((numberOfDocuments - documentFrequency + 0.5d) / (documentFrequency + 0.5d)); 
    	    double weight = (tf * (k_1 + 1d))/(tf + k_1 * ( 1d - b + b * (docLength/averageDocumentLength)));
    	    
         
            // multiply the weight with idf 
            return idf * weight;
    }
    
    public final double documentWeight(List<Integer> documentTokens, List<Integer> query, double numberOfDocuments, double averageDocumentLength){
    	
    	double weight=0;
    	double docLength = documentTokens.size();
    	
    	for (int queryToken: query) {
    	    double tf = Collections.frequency(documentTokens, queryToken);
    	    double documentFrequency = getDocumentFrequency(queryToken);
    	    weight = weight+score(tf, numberOfDocuments, docLength, averageDocumentLength, documentFrequency);
    	}
    	return weight;
    }
    
    public final List<BM25Method> rankCorpus(String corpusPath, String query, int highestTokensAmmount){
    	
    	bm25Methods = new ArrayList<BM25Method>();
    	double numberOfDocuments = new File(corpusPath).list().length;
    	
    	double averageDocumentLength = 0;
    	FileInputStream fisTargetFile;
    	
    	//Open every file, add the methods to the list and get the method length
    	for (int i=0; i < new File(corpusPath).list().length;i++){
    		try {
				fisTargetFile = new FileInputStream(new File(corpusPath+i+".txt"));
				String methodBody = IOUtils.toString(fisTargetFile, "UTF-8");
				List<Integer> tokens = getTokens(methodBody);
				bm25Methods.add(new BM25Method(tokens, methodBody));
				averageDocumentLength += tokens.size();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	averageDocumentLength = averageDocumentLength / numberOfDocuments;

    	
    	
    	//All the tokens
    	List<Integer> queryTokens = getTokens(query);
    	
    	//Only the highest IDF tokens, if a token is present several times it would be added the same number of times
    	List<Integer> highIDFQueryTokens = new ArrayList<Integer>(); 
    	
    	//The list of the highestTokensAmmount based on their IDF values
    	List<JavaToken> highestIDFTokens = getHighestIdfTokens(query, numberOfDocuments, highestTokensAmmount);
    	
    	
		for (int j=0;j<queryTokens.size();j++){
			
			for (int jj=0 ; jj<highestIDFTokens.size() ; jj++){
				if(highestIDFTokens.get(jj).token==queryTokens.get(j)){
					highIDFQueryTokens.add(queryTokens.get(j));
					break;
				}
			}
		}
		
		
		//Get the weight for each document
		for (BM25Method bm25Method : bm25Methods){
			bm25Method.setWeight(documentWeight(bm25Method.getMethodTokens(),highIDFQueryTokens,numberOfDocuments,averageDocumentLength));
		}
		
		Collections.sort(bm25Methods);
		
		return bm25Methods;
    }

    public double getDocumentFrequency (int queryToken){
    	double documentFrequency = 0;
    	
    	
    	for( BM25Method bm25Method: bm25Methods){
    		if (bm25Method.getMethodTokens().contains(queryToken))
    			documentFrequency = documentFrequency + 1;
    	}
    	return documentFrequency;
    }
    
    /**
     * Generate a tokens list for a method body.
     * @param input The method body T
     * @return The integer tokens list
     */
    private static List<Integer> getTokens(String input){
		List<Integer> tokens = new ArrayList<Integer>();

		IScanner scanner = ToolFactory.createScanner(false, false, false, false);
		scanner.setSource(input.toCharArray());
		int token;
		try {
			while(true){
				token = scanner.getNextToken();
				if (token == ITerminalSymbols.TokenNameEOF) break;
					tokens.add(token);
			}
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tokens;
	}
    
    public List<JavaToken> getHighestIdfTokens(String input, double numberOfDocuments, int ammount){
 
    	
    	List<JavaToken> tokens = new ArrayList<JavaToken>();
    	//This list is to keep track of the tokens to not add repeated ones
    	List<Integer> usedTokens = new ArrayList<Integer>();

		IScanner scanner = ToolFactory.createScanner(false, false, false, false);
		scanner.setSource(input.toCharArray());
		int token;
		try {
			while(true){
				token = scanner.getNextToken();
				if (token == ITerminalSymbols.TokenNameEOF) break;
				
				if (!usedTokens.contains(token)){
					double documentFrequency = getDocumentFrequency(token);
					double idf = Math.log((numberOfDocuments - documentFrequency + 0.5d) / (documentFrequency + 0.5d)); 
					tokens.add(new JavaToken(token,idf));
					usedTokens.add(token);
				}
			}
			
			Collections.sort(tokens);
			Collections.reverse(tokens);
			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tokens.size()>ammount){
			return tokens.subList(0, ammount);
		}
		return tokens;
    }
}