package com.srlab.methodretriever.technique.hill.evaluation;

import com.srlab.methodretriever.technique.hill.HillMetricCalculator;

public class HillMetricMethod implements Comparable<HillMetricMethod> {
		public HillMetricCalculator mc;
		public Double euclideanDistance;
		public String methodBody;
		
	
		

	public HillMetricMethod(HillMetricCalculator mc, Double euclideanDistance, String methodBody) {
			super();
			this.mc = mc;
			this.euclideanDistance = euclideanDistance;
			this.methodBody = methodBody;
		}




	@Override
	public int compareTo(HillMetricMethod o) {
		
		
		return euclideanDistance.compareTo(o.euclideanDistance);
		
	}

}
