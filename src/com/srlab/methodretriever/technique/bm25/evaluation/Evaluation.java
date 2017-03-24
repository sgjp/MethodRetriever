package com.srlab.methodretriever.technique.bm25.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import com.srlab.methodretriever.technique.bm25.BM25;
import com.srlab.methodretriever.technique.bm25.BM25Method;
import com.srlab.methodretriever.technique.bm25.JavaToken;
import com.srlab.methodretriever.utility.MethodExtractor;

public class Evaluation {
	
	
	public static void main(String[] args) {
		
		//The file where the results will be written. This is like the log file of the system.
		String resultsFile = "/results/resultsBM25_TEMP.txt"; 
		
		//The percentage of lines that will be used as queryMethods. This number should be less or equal to 1 (100%)
		double queryMethodLenght = 0.9;
		
		//The percentage of methods that will be used as queryMethods. This number should be less or equal to 1 (100%)
		double queryMethodsQuantity = 0.1;
		
		//The location of the source code from which the methods will be extracted
		String sourceCodePath = "/junit4-master/src";
		
		//The location where the extracted methods will be put and read.
		//This folder should be empty
		//Please note the value ends in /
		String methodsPath = "/extractedMethods/";
		
		
		
		//Extract all the methods from the source code
		//Every method will be put in a different file numerically named like this: {1.txt, 2.txt...n.txt}
		System.out.println("---Generating Methods...");
		MethodExtractor md = new MethodExtractor(new File(sourceCodePath),methodsPath);
				
		BM25 bm25 = new BM25();
		
		List<String> queryMethods = getQueryMethods (queryMethodsQuantity, queryMethodLenght, methodsPath);
		
		System.out.println("---Ranking Corpus...");
		
		for (String queryMethod : queryMethods){
			
			List<BM25Method> bm25Methods = bm25.rankCorpus(methodsPath, queryMethod,10);

			//Writing results
			try {
				FileUtils.writeStringToFile(new File(resultsFile), "\n\n***************************\nMETHOD TO FIND:\n"+queryMethod, Charset.defaultCharset(), true);
			
				//Save the first 10 methods and their weight
				for (int j=bm25Methods.size()-1; j>bm25Methods.size()-11;j--){
					FileUtils.writeStringToFile(new File(resultsFile), "\n\nMETHOD #"+(bm25Methods.size()-j)+"\n-------\n"+bm25Methods.get(j).getMethodBody()+"------\n\n"+"Weight: "+bm25Methods.get(j).getWeight()+"\n", Charset.defaultCharset(), true);
				}
				
				//This block is only to be used when the complet method is searched, to find the position where it's ranked
				/*
				for( BM25Method bm25Method: bm25Methods){
		    		if (bm25Method.getMethodBody().equals(queryMethod)){
						FileUtils.writeStringToFile(new File(resultsFile), "\nExact method Found in position: "+(bm25Methods.size()-bm25Methods.indexOf(bm25Method))+" Weight: "+bm25Method.getWeight(), Charset.defaultCharset(), true);
		    		}		
		    	}
		    	*/
			
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		System.out.println("---Done!");
	}

	private static List<String> getQueryMethods(double queryMethodsQuantity, double queryMethodLenght,
			String methodsPath) {
		
		List<String> queryMethods = new ArrayList<String>();
		FileInputStream fisTargetFile;
		
		for (int i=0; i < new File(methodsPath).list().length*queryMethodsQuantity;i++){
    		try {
    			Random rand = new Random();
				fisTargetFile = new FileInputStream(new File(methodsPath+"/"+rand.nextInt(new File(methodsPath).list().length)+".txt"));
				String methodBody = IOUtils.toString(fisTargetFile, "UTF-8");
				queryMethods.add(methodBody.substring(0, (int) Math.round(methodBody.length()*queryMethodLenght) ));
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		
		return queryMethods;
	}

	

}
