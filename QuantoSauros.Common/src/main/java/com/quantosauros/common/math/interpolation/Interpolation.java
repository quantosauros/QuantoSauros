package com.quantosauros.common.math.interpolation;

/**
 * 1D 보간법 인터페이스이다.
 * <p> 1D 좌표 축과 뽑기를 원하는 변수가 있다면
 * 해당 변수들을 이용해 보간된 값을 반환한다.
 * 
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @version 3.0
 */
public interface Interpolation extends java.io.Serializable {

	/**
	 * {@link Coordinates}객체에서 원하는 x 값의 y 값을 선형보간하여 추출한다.
	 * <p>
	 * 예를 들어 {@link Coordinates}가 금리 커브로서,
	 * X축, Y축에 각각 Vertex별 금리가 들어있는 경우 임의의 x축 값을 입력하면
	 * 해당 x 값이 가장 가까운 두 X축을 찾아 각 X축의 y 값을 x의 선형 비율에 따라
	 * 동일하게 보간하여 값을 추출한다.
	 */
	double getY(Coordinates [] orderedCoordinates, double x);
}
