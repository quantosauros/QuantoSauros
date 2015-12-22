package com.quantosauros.common.volatility;

import java.io.Serializable;

import com.quantosauros.common.math.interpolation.Coordinates;
import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.LinearInterpolation;

/**
 * <div id="ko">
 * 좌표들로 구성된 곡선을 정의한다
 * </div>
 * <div id="en">
 * Define a curve consisting of coordinates
 * </div>.
 * 
 * @author Oh, Sung Hoon
 * @author Jae-Heon Kim
 * @since 3.0
 * 
 */
/*------------------------------------------------------------------------------
 * Implementation Note
 * - Creation: RiskCraft.Market R3, 2008
 * - Made to extend Comparable by Jae-Heon Kim on 2009.04.28
 ------------------------------------------------------------------------------*/
public class Curve implements Serializable {

	private Coordinates [] orderedCoordinates;

	private Interpolation interpolation;

	public Curve(Coordinates [] orderedCoordinates) {
		this(orderedCoordinates, LinearInterpolation.getInstance());
	}

	public Curve(Coordinates [] orderedCoordinates, Interpolation interpolation) {
		this.orderedCoordinates = orderedCoordinates;
		this.interpolation = interpolation;
	}

	public double get(double x) {
		return interpolation.getY(orderedCoordinates, x);
	}

	// Jae-Heon Kim, 2009.04.28
	public int length() {
		return orderedCoordinates.length;
	}

}
