package com.srlab.methodretriever.technique.bm25;

public class JavaToken implements Comparable<JavaToken> {

	public int token;
	public Double idf;
	
	
	public JavaToken(int token, Double idf) {
		super();
		this.token = token;
		this.idf = idf;
	}


	@Override
	public int compareTo(JavaToken o) {
		// TODO Auto-generated method stub
		return this.idf.compareTo(o.idf);
	}

}
