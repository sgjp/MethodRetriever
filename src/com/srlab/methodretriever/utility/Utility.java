package com.srlab.methodretriever.utility;

public class Utility {
	
	public final static String extensions[]={".java"};
	
	public static boolean isExtensionMatches(String file){
		for(String extension:extensions){
			if(file.endsWith(extension)){
				return true;
			}
		}
		return false;
	}
}
