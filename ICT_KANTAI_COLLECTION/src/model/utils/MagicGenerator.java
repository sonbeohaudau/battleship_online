package model.utils;

import java.util.Random;

// random utils: 
// - generate random integer in range
// - generate percent
public class MagicGenerator {
	public static int getRandInt(int upperbound) {
		return new Random().nextInt(upperbound);
	}
}
