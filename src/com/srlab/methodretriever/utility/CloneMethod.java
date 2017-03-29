package com.srlab.methodretriever.utility;

public class CloneMethod {
	
	private String cloneId;
	
	private String cloneClassId;
	
	private boolean toTest;

	public boolean isToTest() {
		return toTest;
	}

	public void setToTest(boolean toTest) {
		this.toTest = toTest;
	}

	


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

	public CloneMethod(String cloneId, String cloneClassId, boolean toTest) {
		super();
		this.cloneId = cloneId;
		this.cloneClassId = cloneClassId;
		this.toTest = toTest;
	}
	
	

}
