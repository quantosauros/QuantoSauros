package com.quantosauros.common.math.interpolation;

/**
 * 선형보간법을 구현한 클래스이다.
 * 
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @since 3.0 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/

public class LinearInterpolation implements Interpolation {

	private static LinearInterpolation theInstance;
	
	private LinearInterpolation() { }
	
	/**
	 * {@code LinearInterpolation} 객체를 가져온다.
	 * <P>사용 예제:
	 * <code>LinearInterpolation interp = LinearInterpolation.getInstance();
	 * interp.getY(orderedCoordinates, x);
	 * </code>
	 * @return LinearInterpolation Instance
	 */
	public static LinearInterpolation getInstance() {
		if (theInstance == null) {
			theInstance = new LinearInterpolation();
		}
		return theInstance;
	}

	/**
	 * {@link Coordinates} 객체에서 원하는 x 값의 y 값을 선형보간하여 추출한다.
	 * <P>
	 * 예를 들어 {@link Coordinates}가 금리 커브로서,
	 * X축, Y축에 각각 Vertex별 금리가 들어있는 경우 임의의 x축 값을 입력하면
	 * 해당 x 값이 가장 가까운 두 X축을 찾아 각 X축의 y 값을 x의 선형 비율에 따라
	 * 동일하게 보간하여 값을 추출한다.
	 * 
	 * @param orderedCoordinates 선형보간되어야 할 X축, Y축 값이 들어있는 객체
	 * @param x 선형보간 하기 원하는 x값
	 * @return 선형보간되어 나온 값
	 */
	public double getY(Coordinates [] orderedCoordinates, double x) {

		if (orderedCoordinates.length == 1 || x <= orderedCoordinates[0].getX()) {
			return orderedCoordinates[0].getY();
		}
		
		if (x >= orderedCoordinates[orderedCoordinates.length - 1].getX()) {
			return orderedCoordinates[orderedCoordinates.length - 1].getY();
		}

		for (int i = 1; i < orderedCoordinates.length; ++i) {			
			double x1 = orderedCoordinates[i-1].getX();
			double y1 = orderedCoordinates[i-1].getY();
			double x2 = orderedCoordinates[i].getX();
			double y2 = orderedCoordinates[i].getY();

			if (x == x2) return y2;
			if (x > x1 && x < x2) {
				double y = (y2 - y1) / (x2 - x1) * (x - x1) + y1;
				return y;
			}
		}
		
		// 여기까지 코드가 도달할 일은 없음
		return orderedCoordinates[orderedCoordinates.length - 1].getY();
	}
	
	
}
