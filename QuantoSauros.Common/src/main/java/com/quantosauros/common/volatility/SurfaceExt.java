package com.quantosauros.common.volatility;

import com.quantosauros.common.math.interpolation.BilinearInterpolation;
import com.quantosauros.common.math.interpolation.Interpolation2D;


public class SurfaceExt extends Surface {
	
	private double[][] _mZ2;
	private double[][] _mZ3;
	
	private Interpolation2D _interpolation2;
	private Interpolation2D _interpolation3;
	
	public SurfaceExt(double[] vx, double[] vy, 
			double[][] mz1, double[][] mz2, double[][] mz3) {
		super(vx, vy, mz1);
		
		_interpolation2 = new BilinearInterpolation(vx, vy, mz2);
		_interpolation3 = new BilinearInterpolation(vx, vy, mz3);
		
	}
	
	public double get(double x, double y, int index){
		if (index == 1){
			return _interpolation.evaluate(x, y);
		} else if (index == 2){
			return _interpolation2.evaluate(x, y);
		} else {
			return _interpolation3.evaluate(x, y);
		}
	}
	
	public double[][] getGrid(int index) {
		if (index == 1){
			return _mZ;
		} else if (index == 2){
			return _mZ2;
		} else {
			return _mZ3;
		}
	}
	
	
}
