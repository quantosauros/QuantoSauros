package com.quantosauros.common.math.random;

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
public class JdkRandom extends AbstractRandom {

	public JdkRandom(long seed) {
		super(seed);
		_random = new Random(seed);
	}

}
