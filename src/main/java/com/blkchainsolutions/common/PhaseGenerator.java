package com.blkchainsolutions.common;

import java.util.Random;

public class PhaseGenerator {

	public static String generatePhase() {
		String str= new Random().ints(10, 97, 122).collect(StringBuilder::new,
		        StringBuilder::appendCodePoint, StringBuilder::append)
		        .toString();
		return str+System.currentTimeMillis();
	}
}
