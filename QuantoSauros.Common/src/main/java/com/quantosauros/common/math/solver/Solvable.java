package com.quantosauros.common.math.solver;

import org.apache.commons.math3.analysis.UnivariateFunction;


/**
 * 풀어야 할 다항식을 정의한다. 
 * 
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public abstract class Solvable implements UnivariateFunction {

	/**
	 * 변수 {@code x}가 입력되는 경우 다항식의 값을 반환한다.
	 * 
	 * @param x 입력 변수
	 * @return 다항식의 값
	 */
	public double value(double x){
		return getY(x);
	}
	
	/**
	 * 변수 {@code x}가 입력되는 경우 다항식의 값을 반환한다.
	 * 
	 * @param x 입력 변수
	 * @return 다항식의 값
	 */
	public abstract double getY(double x);

}
