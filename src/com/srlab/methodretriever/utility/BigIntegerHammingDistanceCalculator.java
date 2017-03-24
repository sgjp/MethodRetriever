package com.srlab.methodretriever.utility;

import java.math.BigInteger;

public class BigIntegerHammingDistanceCalculator{

	public static int calculate(BigInteger first,BigInteger second){
		BigInteger x = first.xor(second);
		int tot = 0;
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}
	public static void main(String args[]){
		
	}
}
