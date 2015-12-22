package com.quantosauros.common.math.interpolation;

import com.quantosauros.common.math.MathUtil;


/**
 * 2차원 인터폴레이션 추상 클래스이다.
 * <p>
 * 해당 추상 클래스가 2D Linear, Exponential ... 등 
 * Interpolation의 상위클래스가 된다.
 * 
 * @author Jae-Heon Kim
 * @author Yang Jaechul (javadoc)
 * @since 3.1
 * 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.04.28
* 
* TODO:: Check validity of ranges of input data
------------------------------------------------------------------------------*/
public abstract class AbstractInterpolation2D implements Interpolation2D {

	protected double[] _vX;
	protected double[] _vY;
	protected double[][] _mZ;
	protected boolean _doExtrapolation = false;
	protected final static int _BELOW_MIN = -1;
	protected final static int _ABOVE_MAX = -2;
	
	/**
	 * X축 Y축에 따른 Z축 값을 객체에 저장한다.
	 * 
	 * @param vx X축 배열
	 * @param vy Y축 배열
	 * @param mz Z축 배열, 보간해낼 최종 배열
	 */
	public AbstractInterpolation2D(double[] vx, double[] vy, double[][] mz) {
		_vX = vx;
		_vY = vy;
		_mZ = mz;
	}
	
	/**
	 * {@code x}, {@code y} 값이 입력되면 생성자에 저장된 X축, Y축, Z축 
	 * 값으로부터 선형 보간을 수행해 z값을 구한다.
	 * <p> 이 메소드를 상속받은 하위 클래스에 상세 내용이 구현되어 있다.
	 * 
	 * @param x 찾기를 원하는 X축의 값
	 * @param y 찾기를 원하는 Y축의 값
	 * @return 입력된 {@code x}, {@code y} 값에 따라 선형보간되어 나온 z 값
	 */
	public abstract double evaluate(double x, double y);
	
	/**
	 * X축 구간 중에서 {@code x}가 몇 번째 구간에 존재하는지 찾는다.
	 * <p>
	 * X축이
	 * 1, 2, 3, 4, 5, 6, 7, 8, 9, 10일 때
	 * x 값이 5.3이면
	 * x는 5 ~ 6 사이에 포함되므로 5번째 구간에 속한다.
	 * 배열의 index는 0부터 시작하므로, 4를 반환하게 된다.
	 * 
	 * @param x index를 찾기 원하는 X축 위의 값
	 * @return 배열의 index
	 */
	public int locateX(double x) {
		
		return locate(_vX, x);
	}

	/**
	 * Y축 구간 중에서 {@code y}가 몇 번째 구간에 존재하는지 찾는다.
	 * <p>
	 * Y축이
	 * 1, 2, 3, 4, 5, 6, 7, 8, 9, 10일 때
	 * y 값이 5.3이면
	 * y는 5 ~ 6 사이에 포함되므로 5번째 구간에 속한다. 
	 * 배열의 index는 0부터 시작하므로, 4를 반환하게 된다.
	 * 
	 * @param y index를 찾기 원하는 Y축 위의 값
	 * @return 배열의 index
	 */
	public int locateY(double y) {
		return locate(_vY, y);
	}
	
	/**
	 * X축, Y축의 범위 바깥에 x, y 값이 존재하는 경우 외삽법으로 보간할 것인지 
	 * 결정할 수 있도록 설정한다.
	 * <p>
	 * 외삽법(extrapolation)이란 어떤 변역 안에서 몇 개의 변수 값에 대한 함숫값이 알려져 있을 때 
	 * 이 변역 외의 변수 값에 대한 함숫값을 추정하는 방법이다.
	 * <p>{@code b}의 값이 {@code true}인 경우 외삽법을 사용하여 범위 바깥의 
	 * 값들을 보간하고,
	 * {@code false}인 경우 변수가 범위 바깥일 때, 해당 범위의 맨 외곽의 값을 
	 * 그대로 사용한다.
	 *  
	 * @param b 
	 */
	public void setDoExtrapolation(boolean b) {
		_doExtrapolation = b;
	}
	
	/**
	 * 주어진 구간 중에서 {@code val} 값이 몇 번째 구간에 포함되어 있는지를
	 * 구한다.
	 * <p>
	 * 생성자에 정의된 배열을 사용하는 것이 아니라 축 배열을 넘겨 받아서 
	 * 처리하는 함수이다.
	 * 
	 * @param list 검색할 구간의 배열
	 * @param val 어느 구간에 포함되어 있는지 찾으려는 값
	 * @return {@code val} 값이 포함된 구간의 index
	 */
	protected int locate(double[] list, double val) {
		// 2012.03.08
		if (list == null) {
			return _BELOW_MIN;
		}
		if (_doExtrapolation) {
			if (val <= list[0]) {
				return 0;
			} else if (val >= list[list.length - 1]) {
				return list.length - 2;
			}
		} else {
			if (val <= list[0]) {
				return _BELOW_MIN;
			} else if (val >= list[list.length - 1]) {
				return _ABOVE_MAX;
			}
		}
		return MathUtil.upperBound(list, val) - 1;
	}
}
