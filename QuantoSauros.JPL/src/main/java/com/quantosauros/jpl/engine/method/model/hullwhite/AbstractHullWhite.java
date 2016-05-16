package com.quantosauros.jpl.engine.method.model.hullwhite;

import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.ZeroRateCurve;

public abstract class AbstractHullWhite {

	protected double _meanReversion;
	protected double _sigma;
	protected ZeroRateCurve _termStructure;
	protected HullWhiteVolatility _vols;
	
	public AbstractHullWhite() {
		// TODO Auto-generated constructor stub
	}		
	public abstract void setSigma(double sigma);		
	public abstract double getSigma();
	public abstract double getMeanReversion();
	public abstract ZeroRateCurve getTermStructure();
	public abstract HullWhiteVolatility getHullWhiteVolatility();
	protected abstract double getA(double from, double to);
	protected abstract double getB(double from, double to, double x);
}
