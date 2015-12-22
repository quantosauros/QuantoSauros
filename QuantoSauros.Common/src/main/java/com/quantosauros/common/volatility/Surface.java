package com.quantosauros.common.volatility;

import java.io.Serializable;

import com.quantosauros.common.math.interpolation.BilinearInterpolation;
import com.quantosauros.common.math.interpolation.Interpolation2D;

/**
 * 
 * <div id="ko"> 
 * 3차원 공간의 곡면을 나타낸다
 * </div>
 * <div id="en">
 * The class represents a 3-dimensional surface 
 * </div>.
 * 
 * @author Jae-Heon Kim
 * @since 3.1
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.04.28
------------------------------------------------------------------------------*/
public class Surface implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Interpolation2D _interpolation;
	protected double[] _vX;
	protected double[] _vY;
	protected double[][] _mZ;
	
	private double _defaultY = 1.0;

	public Surface(double[] vx, double[] vy, double[][] mz) {
		this(vx, vy, mz, new BilinearInterpolation(vx, vy, mz));
	}

	public Surface(double[] vx, double[] vy, double[][] mz, Interpolation2D interpolation) {
		if (vx == null) {
			_vX = new double[] {_defaultY};
		} else {
			_vX = vx;
		}
		
		if (vy == null) {
			_vY = new double[] {_defaultY};
		} else {
			_vY = vy;
		}
		_mZ = mz;
		_interpolation = interpolation;
	}

	public double get(double x, double y) {
		return _interpolation.evaluate(x, y);
	}
	
	// 2011.01.28
	public double get(double[] values) {
		if (values.length == 1) {
			return _interpolation.evaluate(values[0], _defaultY);
		} else {
			return _interpolation.evaluate(values[0], values[1]);
		}
	}
	
	// 2011.01.28
	public double[] getXs() {
		return _vX;
	}
	
	// 2011.01.28
	public double[] getYs() {
		return _vY;
	}
	
	// 2011.01.28
	public double[][] getGrid() {
		return _mZ;
	}
	//added by jihoon lee, 20140917
	public void setGrid(double[][] mZ){
		_mZ = mZ;
	}
}
