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
public class MersenneTwisterFastRandom extends AbstractRandom {

	public MersenneTwisterFastRandom(long seed) {
		super(seed);
		_random = new MersenneTwisterFast(seed);
	}
}
