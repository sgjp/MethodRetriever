package com.srlab.methodretriever.technique.hill.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.opencsv.CSVReader;
import com.srlab.methodretriever.technique.hill.HillMetricCalculator;
import com.srlab.methodretriever.utility.CloneMethod;

public class evaluation {
	
	//The location where the extracted methods will be put and read.
	//This folder should be empty
	//Please note the value ends in /
	public static String methodsPath = "/Users/jsanchez/Documents/jdk_source_function_clones_code/";
		
	public static String cloneClassInfoPath = "/Users/jsanchez/Documents/jdk_source_function_clones_code/CloneClassInfo.txt";
	
	
	//The percentage of lines that will be used as queryMethods. This number should be less or equal to 1 (100%)
	public static double queryMethodLenght = 0.75;
			
	//The percentage of methods that will be used as queryMethods. This number should be less or equal to 1 (100%)
	public static double queryMethodsQuantity = 0.0139;
	
	//The location of the source code from which the methods will be extracted
	//String sourceCodePath = "/junit4-master/src";
			
	//Number of top methods to recommend (top-k)
	public static int kMethods = 5;
	
	//The file where the results will be written. This is like the log file of the system.
	static String resultsFile = "/results/resultsHYR_TEMP.txt"; 
	
public static void main(String[] args) {
	
		//Put each method in a different file
		//System.out.println("---Generating Methods...");
		//MethodExtractor md = new MethodExtractor(new File("/junit4-master/src"),"/extractedMethods");
	
		List<CloneMethod> cloneTestMethods = getTestMethods (queryMethodsQuantity);
		List<CloneMethod> cloneMethods = getAllMethods (cloneTestMethods);
		
		System.out.println("---Ranking Corpus...");
		System.out.println("---Generating Feature vectors...");
		//This structure will contain every hillmetriccalculator (Feature Vector) for the methods
		List<HillMetricCalculator> hillMetricCalculatorList = new ArrayList<HillMetricCalculator>();
		for (CloneMethod cloneMethod: cloneMethods){
			try{
			FileInputStream fisTargetFile;
			fisTargetFile = new FileInputStream(new File(methodsPath+cloneMethod.getCloneId()+"_"+cloneMethod.getCloneClassId()+".txt"));
			String methodBody = IOUtils.toString(fisTargetFile, "UTF-8");
			//System.out.println(cloneMethods.get(i).getCloneId()+"_"+cloneMethods.get(i).getCloneClassId());
			fisTargetFile.close();
			
			HillMetricCalculator mc = new HillMetricCalculator(methodBody, 1.0, cloneMethod.getCloneId(), cloneMethod.getCloneClassId() );
			hillMetricCalculatorList.add(mc);
			
			}catch (FileNotFoundException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassCastException e){
				//Some Methods are not being parsed, probably because the JDT version compiles up to Java 7 and some methods are Java 8, for now, those methods are being skipped as the don't represent an important number
			}
		}
		
	
		
		System.out.println("---"+cloneTestMethods.size()+" Methods To be tested----");
	
	    //This is to keep track of the methods recommended that are clones
		int numberOfMethodsFound = 0;
		
		for (CloneMethod cloneTestMethod : cloneTestMethods){
			
			System.out.println("Testing Method #"+ cloneTestMethods.indexOf(cloneTestMethod)+ " Of "+cloneTestMethods.size() +" "+cloneTestMethod.getCloneId()+"_"+cloneTestMethod.getCloneClassId());
			List<HillMetricMethod> hillMetricList = getRankedCorpus(cloneTestMethod, cloneMethods, hillMetricCalculatorList);
			if(hillMetricList.size()==0) continue;
			
			//For each testMethod, go through the top-k recomendations and validate if they're clones or not
			boolean methodFoundFlag = false;
			for (int j=0; j<kMethods;j++){
				System.out.println("TOP "+j+" : "+hillMetricList.get(j).getCloneId()+"_"+hillMetricList.get(j).getCloneClassId());
				if (cloneTestMethod.getCloneClassId().equals(hillMetricList.get(j).getCloneClassId())){
					methodFoundFlag = true;
				}
			}
			//The flag and this count is to only count once if the method is found among the top-k methods, if two clones are find, only count once
			if (methodFoundFlag) numberOfMethodsFound++;
		}
		
		double precision = (double) numberOfMethodsFound/(double) cloneTestMethods.size();
		
		System.out.println("---Writing Results...");
		//Writing results
		try {
			FileUtils.writeStringToFile(new File(resultsFile),"\n"+kMethods+","+cloneMethods.size()+","+cloneTestMethods.size()+","+queryMethodLenght+","+numberOfMethodsFound+","+precision, Charset.defaultCharset(), true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("---Done!");
		
	}


private static List<HillMetricMethod> getRankedCorpus(CloneMethod cloneTestMethod, List<CloneMethod> cloneMethods, List<HillMetricCalculator> hillMetricCalculatorList) {
	List<HillMetricMethod> hillMetricList = new ArrayList<HillMetricMethod>();
	
	String query="";
	try {
		query = IOUtils.toString(new FileInputStream(new File(methodsPath+cloneTestMethod.getCloneId()+"_"+cloneTestMethod.getCloneClassId()+".txt")), "UTF-8");
	} catch (FileNotFoundException e){ //When the file is not found, check if it exists with another CloneClassId
		System.out.println(cloneTestMethod.getCloneId()+"_"+cloneTestMethod.getCloneClassId() + " Not Found. Searching with another CodeClassId");
		for (final File fileEntry : new File(methodsPath).listFiles()) {
			if(fileEntry.getName().substring(0, fileEntry.getName().indexOf("_")).equals(cloneTestMethod.getCloneId())){
				System.out.println("FOUND: " + fileEntry.getName());
				try {
					query = IOUtils.toString(new FileInputStream(fileEntry),"UTF-8");
				} catch (Exception e1) {
					e1.printStackTrace();
					return hillMetricList;
				} 
				break;
			}
		}
		
	} catch (Exception e1) {
		e1.printStackTrace();
		return hillMetricList;
	}
	
	
	try{
		HillMetricCalculator qm = new HillMetricCalculator(query, queryMethodLenght, cloneTestMethod.getCloneId(), cloneTestMethod.getCloneClassId());
		for (int i=0; i < hillMetricCalculatorList.size();i++){
			try {
				HillMetricCalculator mc = hillMetricCalculatorList.get(i);
				hillMetricList.add(new HillMetricMethod(mc,mc.getEuclideanDistance(qm),mc.getCloneId(),mc.getCloneClassId()));

			} catch (ClassCastException e){
				e.printStackTrace();
				return hillMetricList;
				//Some Methods are not being parsed, probably because the JDT version compiles up to Java 7 and some methods are Java 8, for now, those methods are being skipped as the don't represent an important number
			}
		}
		
	}catch (ClassCastException e){
		e.printStackTrace();
		return hillMetricList;
		//Some Methods are not being parsed, probably because the JDT version compiles up to Java 7 and some methods are Java 8, for now, those methods are being skipped as they don't represent an important number
	}
	
	
	
	Collections.sort(hillMetricList);
	return hillMetricList;
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
	int numberOfMethods = StringUtils.countMatches(content, ",");
	
	int numberOfTestCases = (int) Math.round(numberOfMethods * queryMethodsQuantity);
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

//Get all the methods except by the ones that will be tested.
	private static List<CloneMethod> getAllMethods(List<CloneMethod> cloneTestMethods) {
		
		//Go through the test methods and add their clone ids to a list
		List<String> testClonesIds = new ArrayList<String>();
		for (CloneMethod cloneTestMethod : cloneTestMethods){
			testClonesIds.add(cloneTestMethod.getCloneId());
		}
		List<CloneMethod> cloneMethods = new ArrayList<CloneMethod>();
		
		for (final File fileEntry : new File(methodsPath).listFiles()) {
			if(fileEntry.getName().contains("_")){ //Only method files
				String [] splitedName = fileEntry.getName().split("_");
				String cloneId = splitedName[0];
				String cloneClassId = splitedName[1];
				if (testClonesIds.contains(cloneId)){ // Avoid cloneIds already present in the test list
					continue;
				}
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


