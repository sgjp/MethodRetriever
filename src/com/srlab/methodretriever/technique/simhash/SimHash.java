package com.srlab.methodretriever.technique.simhash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
//The code has been collected from the following link: https://github.com/vonzhou/learning-java/blob/master/src/algorithm/simhash/SimHash.java
public class SimHash {
	private String tokens;
	private BigInteger intSimHash;
	private String strSimHash;
	private int hashbits = 64; // simhash code

	public SimHash(String tokens) {
		this.tokens = tokens;
		this.intSimHash = this.simHash();
	}

	public SimHash(String tokens, int hashbits) {
		this.tokens = tokens;
		this.hashbits = hashbits;
		this.intSimHash = this.simHash();
	}

	
	public BigInteger simHash() {
		
		final int[] v = new int[this.hashbits];

		
		final StringTokenizer stringTokens = new StringTokenizer(this.tokens);
		//
		//System.out.println("Token Count: " + stringTokens.countTokens());

		while (stringTokens.hasMoreTokens()) {
			final String temp = stringTokens.nextToken();
			final BigInteger t = this.hash(temp);

			
			for (int i = 0; i < this.hashbits; i++) {
				final BigInteger bitmask = new BigInteger("1").shiftLeft(i);
				if (t.and(bitmask).signum() != 0) {
					v[i] += 1;
				} else {
					v[i] -= 1;
				}
			}
		}
		
		BigInteger fingerprint = new BigInteger("0");
		final StringBuffer simHashBuffer = new StringBuffer();
		for (int i = 0; i < this.hashbits; i++) {
			if (v[i] >= 0) {
				fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
				simHashBuffer.append("1");
			} else {
				simHashBuffer.append("0");
			}
		}
		this.strSimHash = simHashBuffer.toString();
		//System.out.println(this.strSimHash + " length "
		//		+ this.strSimHash.length());
		return fingerprint;
	}

	
	private BigInteger hash(String source) {
		if (source == null || source.length() == 0) {
			return new BigInteger("0");
		} else {
			char[] sourceArray = source.toCharArray();
			BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
			BigInteger m = new BigInteger("1000003"); 
			BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(
					new BigInteger("1"));
			for (char item : sourceArray) {
				BigInteger temp = BigInteger.valueOf((long) item);
				x = x.multiply(m).xor(temp).and(mask);
			}
			x = x.xor(new BigInteger(String.valueOf(source.length())));
			if (x.equals(new BigInteger("-1"))) {
				x = new BigInteger("-2");
			}
			return x;
		}
	}

	public int hammingDistance(SimHash other) {
		BigInteger x = this.intSimHash.xor(other.intSimHash);
		int tot = 0;

		
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}

	public int getDistance(String str1, String str2) {
		int distance;
		if (str1.length() != str2.length()) {
			distance = -1;
		} else {
			distance = 0;
			for (int i = 0; i < str1.length(); i++) {
				if (str1.charAt(i) != str2.charAt(i)) {
					distance++;
				}
			}
		}
		return distance;
	}

	public List subByDistance(SimHash simHash, int distance) {
		int numEach = this.hashbits / (distance + 1);
		List characters = new ArrayList();

		StringBuffer buffer = new StringBuffer();

		int k = 0;
		for (int i = 0; i < this.intSimHash.bitLength(); i++) {
			boolean sr = simHash.intSimHash.testBit(i);

			if (sr) {
				buffer.append("1");
			} else {
				buffer.append("0");
			}

			if ((i + 1) % numEach == 0) {
				BigInteger eachValue = new BigInteger(buffer.toString(), 2);
				System.out.println("----" + eachValue);
				buffer.delete(0, buffer.length());
				characters.add(eachValue);
			}
		}

		return characters;
	}

	
	public BigInteger getIntSimHash() {
		return intSimHash;
	}

	public String getStrSimHash() {
		return strSimHash;
	}

	/*public static void main(String[] args) {
		String s = "This is a test string for testing";

		SimHash hash1 = new SimHash(s, 64);
		System.out.println(hash1.intSimHash + "  "
				+ hash1.intSimHash.bitCount());

		// hash1.subByDistance(hash1, 3);

		s = "This is a string for";
		SimHash hash2 = new SimHash(s, 64);
		System.out.println(hash2.intSimHash + "  "
				+ hash2.intSimHash.bitCount());
		// hash1.subByDistance(hash2, 3);
		s = "This is a test string for testing yu";
		SimHash hash3 = new SimHash(s, 64);
		System.out.println(hash3.intSimHash + "  "
				+ hash3.intSimHash.bitCount());
		// hash1.subByDistance(hash3, 3);

		System.out.println("============================");
		// int dis = hash1.getDistance(hash1.strSimHash, hash2.strSimHash);

		System.out.println(hash1.hammingDistance(hash2));

		// int dis2 = hash1.getDistance(hash1.strSimHash, hash3.strSimHash);

		System.out.println(hash1.hammingDistance(hash3));
		
		SimHash hash4 = new SimHash("this is a sample test for simhash", 64);
		SimHash hash5 = new SimHash("I am a big cat learning to learn english", 64);
		SimHash hash6 = new SimHash("I love to play X", 64);
		SimHash hash7 = new SimHash("Do you wat to learn english", 64);
		SimHash hash8 = new SimHash("We live in canada in the city of saskatchewan", 64);
		SimHash hash9 = new SimHash("There was a tiger long living in that forest", 64);
		SimHash hash10 = new SimHash("I love to play X", 64);
		SimHash hash11 = new SimHash("All that glitters are not meant to be gold", 64);
		SimHash hash12 = new SimHash("yesterday night I was warching the movie Jack Reacher", 64);
		SimHash hash13 = new SimHash("Ilham has been sleeping for a long time", 64);
		SimHash hash14 = new SimHash("I love to eat X", 64);
		SimHash hash15 = new SimHash("Do not lie in your life", 64);
		SimHash hash16 = new SimHash("he is not a good man, he is a terrorist", 64);
		SimHash hash17 = new SimHash("Do you really like to eat the food", 64);
			
		SimHash hash18 = new SimHash("Never eat bad food, rather eat fresh food", 64);
		SimHash hash19 = new SimHash("How are you doing in your life", 64);
		SimHash hash20 = new SimHash("He is a great player in our country", 64);
		SimHash hash21 = new SimHash("I am a cat ", 64);
		SimHash hash22 = new SimHash("I am a cat ", 64);
		ArrayList<SimHash> list = new ArrayList();	
		list.add(hash1);
		list.add(hash2);
		list.add(hash3);
		list.add(hash4);
		list.add(hash5);
		list.add(hash6);
		list.add(hash7);
		list.add(hash8);
		list.add(hash9);
		list.add(hash10);
		list.add(hash11);
		list.add(hash12);
		list.add(hash13);
		list.add(hash14);
		list.add(hash15);
		list.add(hash16);
		list.add(hash17);
		list.add(hash18);
		list.add(hash19);
		list.add(hash20);
		list.add(hash21);
		list.add(hash22);
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		Collections.sort(list, new Comparator<SimHash>(){

			@Override
			public int compare(SimHash o1, SimHash o2) {
				BigInteger so1 = o1.intSimHash;
				BigInteger so2 = o2.intSimHash;
				System.out.println("O1: "+o1.strSimHash+"  so1:"+so1.toString(2));
				return so1.compareTo(so2);
			}
		});
		
		for(int i=0;i<list.size();i++){
			SimHash sh = list.get(i);
			System.out.println("Simhash: " + sh.simHash()+"    "+sh.tokens);
		}
		
		long startTime=System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		System.out.println("End Memory: "+Runtime.getRuntime().freeMemory());
		System.out.println("Total time: "+ (endTime-startTime));
	}*/

	
}