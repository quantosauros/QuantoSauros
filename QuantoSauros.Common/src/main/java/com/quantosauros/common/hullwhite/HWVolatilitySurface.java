package com.quantosauros.common.hullwhite;

import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.volatility.SurfaceExt;
import com.quantosauros.common.volatility.Volatility;
import com.quantosauros.common.volatility.VolatilitySurface;

public class HWVolatilitySurface extends VolatilitySurface {

	
	public HWVolatilitySurface(HWVolatilityCurve[] volCurves, 
			Date date, DayCountFraction dcf) {
		super();
		
		_volCurves = volCurves;
		int nCurves = volCurves.length;
		// TODO:: Ensure all the curves have the same length
		int lenCurve = volCurves[0].length();
		double[] vx = new double[nCurves];
		double[] vy = new double[lenCurve];
		double[][] mz1 = new double[nCurves][lenCurve];
		double[][] mz2 = new double[nCurves][lenCurve];
		double[][] mz3 = new double[nCurves][lenCurve];
		for (int n = 0; n < nCurves; n++) {
			HWVolatilityCurve curve = volCurves[n];
			Vertex swapMaturity = curve.getSwapMaturity();
			vx[n] = swapMaturity.getVertex(curve.getDayCountFraction());
			//System.out.println("vx[" + n + "] = " + vx[n]);
			Volatility[] vols = curve.getVolatilities();
			int nVols = vols.length;
			for (int k = 0; k < nVols; k++) {
				HWVolatility vol = (HWVolatility)vols[k];
				if (n == 0) {
					vy[k] = vol.getX();
					//System.out.println("vy[k] = " + vy[k]);
				}
				mz1[n][k] = vol.getY();
				mz2[n][k] = vol.getY2();
				mz3[n][k] = vol.getY3();
				//System.out.println("mz[" + n + "][" + k + "] = " + mz[n][k]);
			}
		}
		_surface = new SurfaceExt(vx, vy, mz1, mz2, mz3);
	}
	public double getVol(double tenor, double maturity, int index){
		return ((SurfaceExt)_surface).get(tenor, maturity, index);
	}
	public String toString(){
		return super.toString();
	}
	
}
