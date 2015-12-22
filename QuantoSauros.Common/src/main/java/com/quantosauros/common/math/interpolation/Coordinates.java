package com.quantosauros.common.math.interpolation;

/**
 * 수학적 좌표를 표현하기 위한 인터페이스로서 이 라이브러리 내에서는 주로 
 * 금리 및 변동성과 같이 1:1로 매핑된 값을 표시하기 위해 사용ㅇ한다.
 * <p>
 * 금리의 경우: Vertex -> InterestRate
 * <br>변동성의 경우: Maturity -> Volatility, Strike -> Volatility
 * <p>와 같이 연결되는 특성이 있다.
 * <p>
 * 이때 각 값이 X, Y축이 되어 해당 X축에 해당하는 Y축 값을 알 수 있는 구조로 
 * 되어 있다.
 * <P>해당 구조를 이용해 원하는 값을 보간하는데 사용한다.
 * 
 * @author Oh, Sung Hoon
 * @author Jae-Heon Kim
 * @author Yang Jaechul (javadoc)
 * @since 3.0 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
* - Made to extend Comparable by Jae-Heon Kim on 2009.04.28
------------------------------------------------------------------------------*/
public interface Coordinates extends Comparable {

	/**
	 * X값을 리턴한다. (Vertex, Maturity, Strike...)
	 */
	double getX();
	
	/**
	 * Y값을 리턴한다. (InterestRate, Volatility)
	 */
	double getY();
}
