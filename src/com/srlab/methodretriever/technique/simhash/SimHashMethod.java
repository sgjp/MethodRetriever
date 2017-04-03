package com.srlab.methodretriever.technique.simhash;

import java.util.List;

public class SimHashMethod implements Comparable<SimHashMethod> {

	private String cloneId;
	
	private String cloneClassId;
	
	public int hammingDistance;
	
	public SimHashMethod(String cloneId, String cloneClassId, int hammingDistance)  {
		super();
		this.hammingDistance = hammingDistance;
		this.cloneId = cloneId;
		this.cloneClassId = cloneClassId;
	}
	
	public SimHashMethod(String cloneId_cloneClassId, int hammingDistance) {
		super();
		this.hammingDistance = hammingDistance;
		this.cloneId = cloneId_cloneClassId.split("_")[0];
		this.cloneClassId = cloneId_cloneClassId.split("_")[1];
	}
	
	

	@Override
	public int compareTo(SimHashMethod o) {
		return Integer.compare(o.hammingDistance, this.hammingDistance);
	}}
