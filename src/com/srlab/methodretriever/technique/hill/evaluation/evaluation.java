package com.srlab.methodretriever.technique.hill.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.srlab.methodretriever.technique.hill.HillMetricCalculator;
import com.srlab.methodretriever.utility.MethodExtractor;

public class evaluation {
	
public static void main(String[] args) {
	
	    String resultsFile = "/results/resultsHYR.txt"; 
	    
		String queryMethod =  "private List<Exception> validateAnnotatable(T annotatable){\n" +
	            "  List<Exception> validationErrors=new ArrayList<Exception>();\n" +
	            "  for (  Annotation annotation : annotatable.getAnnotations()) {\n" +
	            "    Class<? extends Annotation> annotationType=annotation.annotationType();\n" +
	            "    if (validateWith != null) {\n" +
	            "      AnnotationValidator annotationValidator=ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);\n" +
	            "    }\n" +
	            "  }\n" +
	            "  return validationErrors;\n" +
	            "}\n" ;
		HillMetricCalculator qm = new HillMetricCalculator(queryMethod);
		List<HillMetricMethod> hillMetricList = new ArrayList<HillMetricMethod>();
		
		
		//Put each method in a different file
		System.out.println("---Generating Methods...");
		//MethodExtractor md = new MethodExtractor(new File("/junit4-master/src"),"/extractedMethods");
		
		
		System.out.println("---Processing Methods...");
		//Read every method and process it
		for (int i=0; i < new File("/extractedMethods/").list().length-1;i++){
			FileInputStream fisTargetFile;
			try {
				fisTargetFile = new FileInputStream(new File("/extractedMethods/"+i+".txt"));
				String methodBody = IOUtils.toString(fisTargetFile, "UTF-8");
				HillMetricCalculator mc = new HillMetricCalculator(methodBody);
				
				hillMetricList.add(new HillMetricMethod(mc,mc.getEuclideanDistance(qm),methodBody));
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		Collections.sort(hillMetricList);
		
	
		//Save the original method, the top 10 and their euclidean distance
		try {
			FileUtils.writeStringToFile(new File(resultsFile), queryMethod, Charset.defaultCharset(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int j=0;j<10;j++){
			try {
				FileUtils.writeStringToFile(new File(resultsFile), "\n\nMETHOD #"+(j+1)+"\n-------\n"+hillMetricList.get(j).methodBody+"------\n\n"+"EC: "+hillMetricList.get(j).euclideanDistance+"\n", Charset.defaultCharset(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			FileUtils.writeStringToFile(new File(resultsFile), "\n*********************\n" , Charset.defaultCharset(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("---Done!");
	}

}
