package com.quantosauros.jpl.engine.method.model.hullwhite;

import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.jpl.engine.method.formula.GeneralizedBlackScholesMertonFormula;

public class HullWhite2F extends AbstractHullWhite {

	private double _alpha;
	private double _beta;
	private double _sigma1;
	private double _sigma2;	
	private HullWhiteVolatility _vols1;
	private HullWhiteVolatility _vols2;
	private double _correlation;
	
	/**
	 * r(t) = x(t) + y(t) + phi(t)
	 * 
	 * dx(t) = - alpha * x(t) * dt + sigma1 * dw1(t)
	 * dy(t) = - beta * y(t) * dt + sigma2 * dw2(t)
	 * dw1(t) * dw2(t) = correlation * dt
	 * 
	 * @param alpha						Process x의 mean Reversion 속도
	 * @param beta						Process y의 mean Reversion 속도
	 * @param sigma1					Process x의 변동성
	 * @param sigma2					Process y의 변동성
	 * @param correlation				Process X와 Process Y의 상관계수
	 * @param termStructure				기준금리 커브
	 */
	public HullWhite2F(double alpha, double beta, 
			double sigma1, double sigma2, double correlation, 
			ZeroRateCurve termStructure) {
		this._alpha = alpha;
		this._beta = beta;
		this._sigma1 = sigma1;
		this._sigma2 = sigma2;
		this._correlation = correlation;
		this._termStructure = termStructure;
	}
	
	public HullWhite2F(double alpha, double beta, 
			HullWhiteVolatility vols1, HullWhiteVolatility vols2, double correlation, 
			ZeroRateCurve termStructure) {
		this._alpha = alpha;
		this._beta = beta;
		this._vols1 = vols1;
		this._vols2 = vols2;
		this._correlation = correlation;
		this._termStructure = termStructure;
	}
	/**
	 * Calculate Hull-White 2F of discount Bond Price
	 * 
	 * P(t,T) = A * Exp[-B1 * rate1 - B2 * rate2]
	 *        = PM(0,T) / PM(0,t) * Exp[(V(t,T) - V(0,T) - V(0,t))/2] *
	 *          Exp[-B1 * rate1 -B2 * rate2]
	 * 
	 * @param now
	 * @param maturity
	 * @param rate1
	 * @param rate2 
	 * @return
	 */
	public double discountBond(double now, double maturity, double rate1, double rate2){
		
		return getA(now, maturity) * Math.exp(- getB(now, maturity, _alpha) *
				rate1 - getB(now, maturity, _beta) * rate2);
	}
	/**
	 * Calculate Hull-White 2F of discount Bond Option Price
	 * 
	 * @param type
	 * @param strike
	 * @param maturity
	 * @param bondMaturity
	 * @return
	 */
	public double discountBondOption(OptionType type, double strike, double maturity,
			double bondMaturity){
		
		double v = getSigmaP(maturity, bondMaturity);
        double f = _termStructure.getDiscountFactor(bondMaturity);
        double k = _termStructure.getDiscountFactor(maturity) * strike;
        
        GeneralizedBlackScholesMertonFormula gB = new GeneralizedBlackScholesMertonFormula(
        		type, f, k, 1, 0, 0, v);

        return gB.getValue();
	}
	
	/**
	 * calculate A of Hull-White 2F
	 *  
	 * A = PM(0,T) / PM(0,t) * Exp((V(t,T) - V(0,T) + V(0,t)) / 2)
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public double getA(double from, double to){
		double discount1 = _termStructure.getDiscountFactor(from);
		double discount2 = _termStructure.getDiscountFactor(to);
		
		 return  discount2 / discount1 *
				 Math.exp(0.5 * (getV(from, to) - getV(0, to) + getV(0, from)));		 
	}
	
	/**
	 * Calculate B of Hull-White 2F
	 * 
	 * B = 1 - Exp(-meanReversion * (to - from)) / meanReversion
	 * 
	 * @param from
	 * @param to			
	 * @param x				meanReversion Factor
	 * 
	 * @return
	 */
	public double getB(double from, double to, double x){
		
		double dt = to - from;		
		double exponentialB = (1 - Math.exp(-x * dt)) / x;		
		return exponentialB; 		
	}
	/**
	 * Calculate V of Hull-White 2F
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	private double getV(double from, double to){
		double t = to - from;
		double expat = Math.exp(-_alpha * t);
	    double expbt = Math.exp(-_beta * t);
	    double cx = _sigma1 / _alpha;
	    double cy = _sigma2 / _beta;
        double valuex = cx * cx * (t + (2.0 * expat - 0.5 * expat * expat - 1.5) /
        		_alpha);
        double valuey = cy * cy * (t + (2.0 * expbt - 0.5 * expbt * expbt - 1.5) /
        		_beta);
        double value = 2.0* _correlation * cx * cy * (t + (expat - 1.0) / _alpha
        		+ (expbt - 1.0) / _beta
        		- (expat*expbt-1.0) / (_alpha + _beta));
        
        return valuex + valuey + value;
	}
	/**
	 * Calculate SigmaP of Hull-White 2F
	 * 
	 * @param t
	 * @param s
	 * @return
	 */
	private double getSigmaP(double t, double s){
		double temp = 1.0 - Math.exp(-(_alpha + _beta) * t);
		double temp1 = 1.0 - Math.exp(-_alpha * (s - t));
		double temp2 = 1.0 - Math.exp(-_beta * (s - t));
		double a3 = _alpha * _alpha * _alpha;
		double b3 = _beta * _beta * _beta;
		double sigma2 = _sigma1 * _sigma1;
		double eta2 = _sigma2 * _sigma2;
		double value = 
				0.5 * sigma2 * temp1 * temp1 * (1.0 - Math.exp(-2.0 * _alpha * t)) / a3 +
				0.5 * eta2 * temp2 * temp2 * (1.0 - Math.exp(-2.0 * _beta * t))/ b3 +
				2.0 * _correlation * _sigma1 * _sigma2 / 
				(_alpha * _beta * (_alpha + _beta))*
				temp1 * temp2 * temp;
		
        return Math.sqrt(value);		
	}

	@Override
	public void setSigma(double sigma) {
		this._sigma1 = sigma;
	}
	@Override
	public double getSigma() {
		return _sigma1;
	}
	@Override
	public double getMeanReversion() {
		return _alpha;
	}
	@Override
	public ZeroRateCurve getTermStructure() {
		return _termStructure;
	}
	@Override
	public HullWhiteVolatility getHullWhiteVolatility() {
		return _vols1;
	}
	public double getMeanReversion2(){
		return _beta;
	}
	public HullWhiteVolatility getHullWhiteVolatility2() {
		return _vols2;
	}
	public double getCorrelation(){		
		return _correlation;
	}
	public double getSigma2(){
		return _sigma2;
	}
}
