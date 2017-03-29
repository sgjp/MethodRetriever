package com.srlab.methodretriever.technique.bm25;

import java.util.List;

public class BM25Method implements Comparable<BM25Method>{
	
	private List<Integer> tokens;
	
	private double weight;
	
	private String cloneId;
	
	private String cloneClassId;


	public String getCloneId() {
		return cloneId;
	}

	public void setCloneId(String cloneId) {
		this.cloneId = cloneId;
	}

	public String getCloneClassId() {
		return cloneClassId;
	}

	public void setCloneClassId(String cloneClassId) {
		this.cloneClassId = cloneClassId;
	}


	public List<Integer> getMethodTokens() {
		return tokens;
	}

	public void setTokens(List<Integer> tokens) {
		this.tokens = tokens;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}


	public BM25Method(List<Integer> tokens) {
		super();
		this.tokens = tokens;
	}
	

	public BM25Method(List<Integer> tokens, String cloneId, String cloneClassId) {
		super();
		this.tokens = tokens;
		this.cloneId = cloneId;
		this.cloneClassId = cloneClassId;
	}

	@Override
	public int compareTo(BM25Method o) {
		return Double.compare(this.getWeight(), o.getWeight());
	}
	
	
	
	

}
