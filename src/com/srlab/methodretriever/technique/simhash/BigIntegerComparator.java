package com.srlab.methodretriever.technique.simhash;

import java.math.BigInteger;
import java.util.Comparator;

public class BigIntegerComparator implements Comparator<BigInteger> {

	@Override
	public int compare(BigInteger o1, BigInteger o2) {
		
		return o1.compareTo(o2);
	}

}
