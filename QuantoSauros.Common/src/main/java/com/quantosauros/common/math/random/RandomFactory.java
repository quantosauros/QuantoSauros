package com.quantosauros.common.math.random;
/**
 * @author Jae-Heon Kim
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2010.01.12
------------------------------------------------------------------------------*/
public class RandomFactory {

	public static AbstractRandom getRandom() {
		long seed = System.currentTimeMillis();
		return getRandom(AbstractRandom.JDK, seed);
	}
	
	public static AbstractRandom getRandom(int type, long seed) {
		switch (type) {
			case AbstractRandom.JDK:
				return new JdkRandom(seed);
			case AbstractRandom.MERSENNE_TWISTER_FAST:
				return new MersenneTwisterFastRandom(seed);
			default:
				return new JdkRandom(seed);
		}
	}
}
