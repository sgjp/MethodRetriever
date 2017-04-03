package com.srlab.methodretriever.technique.simhash;

import java.util.List;

public class SimHashMethod {

	private String cloneId;
	
	private String cloneClassId;
	
	private SimHash simHash;
	
	public SimHashMethod(String methodBody, String cloneId, String cloneClassId) {
		super();
		this.simHash = new SimHash(methodBody);
		this.cloneId = cloneId;
		this.cloneClassId = cloneClassId;
	}
}
