package com.quantosauros.common.math.random;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Jae-Heon Kim
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2010.01.12
------------------------------------------------------------------------------*/

public abstract class AbstractRandom implements Serializable {
	
	public static final int JDK = 0;
	public static final int MERSENNE_TWISTER_FAST = 1;
	
	protected long _seed;
	protected Random _random;
	
	protected AbstractRandom(long seed) {
		_seed = seed;
	}
	
	public long getSeed() {
		return _seed;
	}
	
	public double nextGaussian() {
		return _random.nextGaussian();
	}
	
	//Added by Jaeoh March 16, 2010
	//This generates a uniform random value on [0,1]
	public double nextDouble() {
		return _random.nextDouble();
	}
}
