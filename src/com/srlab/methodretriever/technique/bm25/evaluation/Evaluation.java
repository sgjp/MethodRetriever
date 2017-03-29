package com.srlab.methodretriever.technique.bm25.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import com.opencsv.CSVReader;
import com.srlab.methodretriever.technique.bm25.BM25;
import com.srlab.methodretriever.technique.bm25.BM25Method;
import com.srlab.methodretriever.technique.bm25.JavaToken;
import com.srlab.methodretriever.utility.CloneMethod;
import com.srlab.methodretriever.utility.MethodExtractor;

public class Evaluation {
	
	static String cloneClassInfoPath = "/Users/jsanchez/Documents/jdk_source_function_clones_code/CloneClassInfo.txt";
	
	//The location where the extracted methods will be put and read.
	//This folder should be empty
	//Please note the value ends in /
	public static String methodsPath = "/Users/jsanchez/Documents/jdk_source_function_clones_code/";
	
	static String resultsFile = "/results/resultsBM25_TEMP.txt"; 
			
	public static void main(String[] args) {
		
		//The file where the results will be written. This is like the log file of the system.
		
		
		//The percentage of lines that will be used as queryMethods. This number should be less or equal to 1 (100%)
		double queryMethodLenght = 1.0;
		
		//The percentage of methods that will be used as queryMethods. This number should be less or equal to 1 (100%)
		double queryMethodsQuantity = 0.008;
		
		//The location of the source code from which the methods will be extracted
		//String sourceCodePath = "/junit4-master/src";
		
		//Number of top methods to recommend (top-k)
		int kMethods = 5;
		
		
		
		//Extract all the methods from the source code
		//Every method will be put in a different file numerically named like this: {1.txt, 2.txt...n.txt}
		//System.out.println("---Generating Methods...");
	//	MethodExtractor md = new MethodExtractor(new File(sourceCodePath),methodsPath);
				
		
		
		List<CloneMethod> cloneTestMethods = getTestMethods (queryMethodsQuantity);
		List<CloneMethod> cloneMethods = getAllMethods ();
		
		System.out.println("---Ranking Corpus...");
		System.out.println("---"+cloneTestMethods.size()+" Methods To be tested----");
		
		BM25 bm25 = new BM25(cloneMethods, cloneTestMethods, methodsPath);
		int numberOfMethodsFound = 0;
		
		for (CloneMethod cloneMethod : cloneTestMethods){
			
			System.out.println("Testing method #"+cloneTestMethods.indexOf(cloneMethod) + cloneMethod.getCloneId()+"_"+cloneMethod.getCloneClassId());
			List<BM25Method> bm25Methods = bm25.getRankedCorpus(cloneMethod, cloneTestMethods, cloneMethods, methodsPath, 10);

			boolean methodFoundFlag = false;
			for (int j=bm25Methods.size()-1; j>bm25Methods.size()-kMethods;j--){
				if (cloneMethod.getCloneClassId().equals(bm25Methods.get(j).getCloneClassId())){
					methodFoundFlag = true;
				}
			}
			
			if (methodFoundFlag) numberOfMethodsFound++;
 			System.out.println("---Writing Results...");
			
			
		}
		double precision = (double) numberOfMethodsFound/(double) cloneTestMethods.size();
		
		//Writing results
		try {
			FileUtils.writeStringToFile(new File(resultsFile),"\n"+kMethods+","+cloneMethods.size()+","+cloneTestMethods.size()+","+numberOfMethodsFound+","+precision, Charset.defaultCharset(), true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("---Done!");
	}


	private static List<CloneMethod> getTestMethods(double queryMethodsQuantity) {
		
		List<CloneMethod> cloneMethods = new ArrayList<CloneMethod>();
		FileInputStream fisTargetFile;
		String content = "";
		
		try {
			fisTargetFile = new FileInputStream(new File(cloneClassInfoPath));
			content = IOUtils.toString(fisTargetFile, "UTF-8");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int numberOfLines = StringUtils.countMatches(content, "\n");
		int numberOfTestCases = (int) Math.round(numberOfLines * queryMethodsQuantity);
		Random rand = new Random();
		
		for (int i=0; i < numberOfTestCases ;i++){
    		try {
    			 int randomLineToRead = rand.nextInt(numberOfLines);
    			 
    			 CSVReader reader = new CSVReader(new FileReader(cloneClassInfoPath));
    		     String [] nextLine;
    		     
    		     int currentLine = 0;
    		     while ((nextLine = reader.readNext()) != null) {
    		    	 currentLine++;
    		    	 if (currentLine==randomLineToRead){
    		    		 if(nextLine.length==2){ //The line only contains one clone, so that's added to the list
    		    			 String cloneClassId = nextLine[0];
    		    			 String cloneId = nextLine[1];
    		    			 cloneMethods.add(new CloneMethod(cloneId,cloneClassId, true));
    		    		 }else{ //The line contains more than one clone, add a random one from them
    		    			 Random rnd = new Random();
    		    			 String cloneClassId = nextLine[0];
    		    			 int random = rnd.nextInt(nextLine.length-1);
    		    			 if (random==0) random++;
    		    			 String cloneId = nextLine[random];
    		    			 cloneMethods.add(new CloneMethod(cloneId,cloneClassId, true));
    		    		 }
    		    	 }  
    		     }
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		
		return cloneMethods;
	}
	
	private static List<CloneMethod> getAllMethods() {
		
		List<CloneMethod> cloneMethods = new ArrayList<CloneMethod>();
		
		for (final File fileEntry : new File(methodsPath).listFiles()) {
			if(fileEntry.getName().contains("_")){ //Only method files
				String [] splitedName = fileEntry.getName().split("_");
				String cloneId = splitedName[0];
				String cloneClassId = splitedName[1];
				if (cloneClassId.contains(".")){
					cloneMethods.add(new CloneMethod(cloneId,cloneClassId.split("\\.")[0], false));
				}else{
					cloneMethods.add(new CloneMethod(cloneId,cloneClassId, false));
				}
				
				
			}
	    }
		
		return cloneMethods;
 
	}

	

}
