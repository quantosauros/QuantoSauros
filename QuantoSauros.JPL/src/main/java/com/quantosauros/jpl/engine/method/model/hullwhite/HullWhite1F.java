package com.quantosauros.jpl.engine.method.model.hullwhite;

import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.jpl.engine.method.formula.GeneralizedBlackScholesMertonFormula;

public class HullWhite1F extends AbstractHullWhite {
	private double _meanReversion;
	private double _sigma;
	private InterestRateCurve _termStructure;
	private HullWhiteVolatility _vols;
		
	public HullWhite1F(double meanReversion, double sigma, InterestRateCurve termStructure) {
		this._meanReversion = meanReversion;
		this._sigma = sigma;
		this._termStructure = termStructure;
	}
	public HullWhite1F(double meanReversion, HullWhiteVolatility vols, InterestRateCurve termStructure) {
		this._meanReversion = meanReversion;
		this._vols = vols;
		this._termStructure = termStructure;
	}	
	public double discountBond(double now, double maturity, double rate){
		
		return getA(now, maturity) * Math.exp(- getB(now, maturity, _meanReversion) * rate);
	}
	public double discountBondOption(OptionType type, double strike, double maturity, 
			double bondMaturity){

        double v = _sigma * getB(maturity, bondMaturity, _meanReversion) *
                Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * _meanReversion *maturity)) / _meanReversion);
//		System.out.println(v);		
        double f = _termStructure.getDiscountFactor(bondMaturity);
        double k = _termStructure.getDiscountFactor(maturity) * strike;
        
        //v에 이미 tenor가 들어가있음, 그래서 만기가 1
        GeneralizedBlackScholesMertonFormula gB = new GeneralizedBlackScholesMertonFormula(
        		type, f, k, 1, 
        		0, 0, v);
        
        return gB.getValue();        
	}
	public double discountBondOption(OptionType type, double strike, double maturity, 
			double bondStart, double bondMaturity){
		
		 double v = _sigma / (_meanReversion * Math.sqrt(2.0 * _meanReversion)) * 
		 Math.sqrt(Math.exp(-2.0 * _meanReversion * (bondStart - maturity)) -
				 Math.exp(-2.0 * _meanReversion * bondStart) - 2.0 * (Math.exp(- _meanReversion *
						 (bondStart + bondMaturity -2.0 * maturity)) - 
						 Math.exp(-_meanReversion * (bondStart + bondMaturity))) +
						 Math.exp(-2.0 * _meanReversion * (bondMaturity - maturity)) -
						 Math.exp(-2.0 * _meanReversion * bondMaturity));
		 
	        double f = _termStructure.getDiscountFactor(bondMaturity);
	        double k = _termStructure.getDiscountFactor(maturity) * strike;
	        
	        //v에 이미 tenor가 들어가있음, 그래서 만기가 1
	        GeneralizedBlackScholesMertonFormula gB = new GeneralizedBlackScholesMertonFormula(
	        		type, f, k, 1, 
	        		0, 0, v);
	        
	        return gB.getValue(); 
		
	}
	protected double getA(double from, double to){
		double discount1 = _termStructure.getDiscountFactor(from);
		double discount2 = _termStructure.getDiscountFactor(to);
		
		double dt = 1.0/365.0;
//		double forward = _termStructure.getForwardRate(from, from);
		double forward = ((_termStructure.getSpotRate(from + dt) - 
				_termStructure.getSpotRate(from)) * (from / dt)) +
				_termStructure.getSpotRate(from + dt);
		
		double temp = _sigma * getB(from, to, _meanReversion);
		
		double value = getB(from, to, _meanReversion) * forward - 
				0.25 * temp * temp * getB(0, 2.0 * from, _meanReversion);
		
		return Math.exp(value) * discount2 / discount1;
		
	}
	protected double getB(double from, double to, double x){
		
		double dt = to - from;
		
		double exponentialB = (1 - Math.exp(- x * dt)) / x;
		
		return exponentialB; 
	}
	public void setSigma(double sigma){
		this._sigma = sigma;
	}
	public double getSigma(){
		return this._sigma;
	}
	public double getMeanReversion(){
		return this._meanReversion;
	}
	public void setMeanReversion(double meanReversion){
		this._meanReversion = meanReversion;
	}
	public InterestRateCurve getTermStructure(){
		return this._termStructure;
	}
	public void setTermStructure(InterestRateCurve termStructure){
		_termStructure = termStructure;
	}
	public HullWhiteVolatility getHullWhiteVolatility(){
		if (this._vols != null){
			return this._vols;
		}
		return null;
	}
}
