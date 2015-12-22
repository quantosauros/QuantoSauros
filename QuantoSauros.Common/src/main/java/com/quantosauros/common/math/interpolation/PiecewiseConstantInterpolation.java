package com.quantosauros.common.math.interpolation;

/**
 * 구간별 상수 보간을 구현한 클래스이다.
 * 
 * @author Youngseok Lee
 * @since 3.7 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2013
------------------------------------------------------------------------------*/

public class PiecewiseConstantInterpolation implements Interpolation {

	private static PiecewiseConstantInterpolation theInstance;
	
	private PiecewiseConstantInterpolation() { }
	
	/**
	 * {@code PiecewiseConstantInterpolation} 객체를 가져온다.
	 * 
	 * @return PiecewiseConstantInterpolation Instance
	 */
	public static PiecewiseConstantInterpolation getInstance() {
		if (theInstance == null) {
			theInstance = new PiecewiseConstantInterpolation();
		}
		return theInstance;
	}

	/**
	 * {@link Coordinates} 객체에서 원하는 x 값의 y 값을 구간별 상수 보간하여 추출한다.
	 * <P>
	 * 예를 들어 {@link Coordinates}가 par swap spread curve로서, X축, Y축에 각각 
	 * Vertex별 par swap spread가 들어있는 경우 임의의 vertex값을 입력하면 해당 vertex값
	 * 이후의 가장 가까운 두 vertex값을 찾아 그에 대응하는 par swap spread값을 추출한다.
	 * 
	 * @param orderedCoordinates 선형보간되어야 할 X축, Y축 값이 들어있는 객체
	 * @param x 선형보간 하기 원하는 x값
	 * @return 선형보간되어 나온 값
	 */
	public double getY(Coordinates [] orderedCoordinates, double x) {

		if (orderedCoordinates.length == 1 || x <= orderedCoordinates[0].getX()) {
			return orderedCoordinates[0].getY();
		}
		
		if (x > orderedCoordinates[orderedCoordinates.length - 1].getX()) {
			return orderedCoordinates[orderedCoordinates.length - 1].getY();
		}

		for (int i = 1; i < orderedCoordinates.length; ++i) {			
			double x1 = orderedCoordinates[i-1].getX();
			double x2 = orderedCoordinates[i].getX();
			double y2 = orderedCoordinates[i].getY();

			if (x > x1 && x <= x2) {
				return y2;
			}
		}

		return orderedCoordinates[orderedCoordinates.length - 1].getY();
	}
	
	
}
