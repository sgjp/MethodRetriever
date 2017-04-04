package com.srlab.methodretriever.technique.hill.evaluation;

import com.srlab.methodretriever.technique.hill.HillMetricCalculator;

//This object is mainly to order the list after generating the euclidean distance for every method
public class HillMetricMethod implements Comparable<HillMetricMethod> {
		public HillMetricCalculator mc;
		public Double euclideanDistance;
		
		private String cloneId;
		
		private String cloneClassId;
		
		

	public HillMetricMethod(HillMetricCalculator mc, Double euclideanDistance, String cloneId, String cloneClassId) {
			super();
			this.mc = mc;
			this.euclideanDistance = euclideanDistance;
			this.cloneId = cloneId;
			this.cloneClassId = cloneClassId;
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




	@Override
	public int compareTo(HillMetricMethod o) {
		return euclideanDistance.compareTo(o.euclideanDistance);
		
	}

}
