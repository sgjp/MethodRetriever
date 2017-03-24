package com.srlab.methodretriever.technique.bm25;

import java.util.List;

public class BM25Method implements Comparable<BM25Method>{
	
	private List<Integer> tokens;
	
	private double weight;
	
	private String methodBody;

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

	public String getMethodBody() {
		return methodBody;
	}

	public void setMethodBody(String methodBody) {
		this.methodBody = methodBody;
	}

	public BM25Method(List<Integer> tokens, String methodBody) {
		super();
		this.tokens = tokens;
		this.methodBody = methodBody;
	}

	@Override
	public int compareTo(BM25Method o) {
		return Double.compare(this.getWeight(), o.getWeight());
	}
	
	
	
	

}
