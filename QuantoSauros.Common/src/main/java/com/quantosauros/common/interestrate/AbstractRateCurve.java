package com.quantosauros.common.interestrate;

import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.LinearInterpolation;
import com.quantosauros.common.volatility.Curve;

public abstract class AbstractRateCurve {

	protected Date _date;
	protected Frequency compoundFreq;	
	protected DayCountFraction dcf;	
	protected Interpolation interpolation;	
	protected double spread;
	protected ArrayList<AbstractRate> rates;	
	
	public AbstractRateCurve(Date date, ArrayList<AbstractRate> rates,
			Frequency compoundFreq, DayCountFraction dcf) {
		this._date = date;
		this.compoundFreq = compoundFreq;
		this.dcf = dcf;
		this.interpolation = LinearInterpolation.getInstance();		
		this.rates = rates;
	}
	
	public AbstractRateCurve(Date date, ArrayList<AbstractRate> rates,
			Frequency compoundFreq, DayCountFraction dcf, 
			Interpolation interpolation) {
		this._date = date;
		this.compoundFreq = compoundFreq;
		this.dcf = dcf;
		this.interpolation = interpolation;		
		this.rates = rates;
	}
	
	public AbstractRateCurve(Date date, 
			Frequency compoundFreq, DayCountFraction dcf,
			Interpolation interpolation){
		this._date = date;
		this.compoundFreq = compoundFreq;
		this.dcf = dcf;
		this.interpolation = interpolation;
		
	}
	
	public double getSpotRate(Date date){
		return getSpotRate(date, compoundFreq);
	}
	
	public double getSpotRate(double t) {
		return getSpotRate(t, compoundFreq);
	}
	
	protected double getSpotRate(Date date, Frequency targetCmpdFreq) {
		double t = dcf.getYearFraction(_date, date);
		return getSpotRate(t, targetCmpdFreq);
	}
	
	public double getSpotRate(double t, Frequency targetCmpdFreq) {
		int size = this.rates.size();
		AbstractRate[] abstractRates = new AbstractRate[size];
		for (int i = 0; i < size; i++){
			abstractRates[i] = this.rates.get(i);
		}
		Curve curve = new Curve(abstractRates, interpolation);
		
		double r = curve.get(t);
		if (compoundFreq == targetCmpdFreq) {
			return r + spread;
		}

		if (targetCmpdFreq == Frequency.NONE) {
			if (compoundFreq == Frequency.CONTINUOUS) {
				r = (Math.exp(r * t) - 1.0) / t;
			} else { // Discrete Compounding
				double m = compoundFreq.getFrequencyInYear();
				r = (Math.pow(1.0 + r / m, m * t) - 1.0) / t;
			}
		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
			if (compoundFreq == Frequency.NONE) {
				r = Math.log(1.0 + r * t) / t;
			} else { // Discrete Compounding
				double m = compoundFreq.getFrequencyInYear();
				r = m * Math.log(1.0 + r / m);
			}
		} else { // Discrete Compounding
			if (compoundFreq == Frequency.NONE) {
				double mt = targetCmpdFreq.getFrequencyInYear();
				r = mt * (Math.pow(1 + r * t, 1.0 / (mt * t)) - 1.0);
			} else if (compoundFreq == Frequency.CONTINUOUS) {
				double mt = targetCmpdFreq.getFrequencyInYear();
				r = mt * (Math.exp(r / mt) - 1);
			} else { // Discrete Compounding
				double m = compoundFreq.getFrequencyInYear();
				double mt = targetCmpdFreq.getFrequencyInYear();
				r = mt * (Math.pow(1.0 + r / m, m / mt) - 1.0);
			}
		}
		return r + spread;
	}
	
	public double getForwardRate(Date date1, Date date2){
		return getForwardRate(date1, date2, compoundFreq);
	}
	
	protected double getForwardRate(Date date1, Date date2, 
			Frequency targetCmpdFreq) {
		double t1 = dcf.getYearFraction(_date, date1);
		double t2 = dcf.getYearFraction(_date, date2);
		
		return getForwardRate(t1, t2, targetCmpdFreq);
	}
	
	protected double getForwardRate(double t1, double t2,
			Frequency targetCmpdFreq) {
		double tenor = t2 - t1;
		return getForwardRate(t1, t2, tenor, targetCmpdFreq);
	}
	
	protected double getForwardRate(double t1, double t2,
			double tenor, Frequency targetCmpdFreq) {

		//20140304 Jihoon Lee, for instantaneous forward rate
		if (t1 == t2){
			t2 += 0.0001;
			tenor = 0.0001;
		}
//		if (t1 == t2) {
//			return getSpotRate(t1, targetCmpdFreq);
//		}

		double df1 = getDiscountFactor(t1);
		double df2 = getDiscountFactor(t2);

		if (targetCmpdFreq == Frequency.NONE) { // Simple
			return (df1 / df2 - 1.0) / tenor;
		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
			return Math.log(df1 / df2) / tenor;
		}

		double m = targetCmpdFreq.getFrequencyInYear();
		return m * (Math.pow(df1 / df2, 1.0 / (m * tenor)) - 1.0);
	}
	
	public double getDiscountFactor(Date date) {
		double t = dcf.getYearFraction(_date, date);
		return getDiscountFactor(t);
	}
	
	public double getDiscountFactor(Date date, Frequency compoundFreq) {
		double t = dcf.getYearFraction(_date, date);
		double r1 = getSpotRate(t, compoundFreq);
		
		return getDiscountFactor(r1, t, compoundFreq);
	}
	
	public double getDiscountFactor(double t, Frequency compoundFreq) {		
		double r1 = getSpotRate(t, compoundFreq);
		
		return getDiscountFactor(r1, t, compoundFreq);
	}
	
	public double getDiscountFactor(double t1) {
		double r1 = getSpotRate(t1, compoundFreq);
		return getDiscountFactor(r1, t1, compoundFreq);
	}
	
	private double getDiscountFactor(double r, double t,
			Frequency targetCmpdFreq) {

		if (targetCmpdFreq == Frequency.NONE) { // Simple
			return 1.0 / (1.0 + r * t);
		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
			return Math.exp(-r * t);
		}
		// Discrete Compounding
		double m = targetCmpdFreq.getFrequencyInYear();
		return 1.0 / Math.pow(1.0 + r / m, m * t);
	}
	
	public double getForwardSwapRate(Date date, double swapMaturity) {
		return getForwardSwapRate(date, swapMaturity, compoundFreq);
	}
	
	public double getForwardSwapRate(Date date, double swapTenor,
			Frequency swapPaymentFreq) {

		double t1 = dcf.getYearFraction(_date, date);
//		double t1 = date.diff(_date)/364.0;
		double swapEffectiveT = t1;
		double swapTerminationT = swapEffectiveT + swapTenor;
		int swapPaymentCnt = (int) (swapTenor * swapPaymentFreq
				.getFrequencyInYear());

		double annuityFactor = getAnnuityFactor(swapEffectiveT,
				swapPaymentFreq, swapPaymentCnt);
		double floatingLegPrice = getDiscountFactor(swapEffectiveT)
				- getDiscountFactor(swapTerminationT);
		double swapRate = floatingLegPrice / annuityFactor;
		return swapRate;
	}
	
	public double getForwardSwapRate(Date date, double swapMaturity,
			Frequency swapPaymentFreq, Money fixedNotionalPrincipal, 
			Money floatingNotionalPrincipal) {

		double t1 = dcf.getYearFraction(_date, date);

		double swapEffectiveT = t1;
		double swapTerminationT = swapEffectiveT + swapMaturity;
		int swapPaymentCnt = (int) (swapMaturity * swapPaymentFreq
				.getFrequencyInYear());

		double annuityFactor = getAnnuityFactor(swapEffectiveT,
				swapPaymentFreq, swapPaymentCnt)
				* fixedNotionalPrincipal.getAmount();
		double floatingLegPrice = (getDiscountFactor(swapEffectiveT) - getDiscountFactor(swapTerminationT))
				* floatingNotionalPrincipal.getAmount();
		double swapRate = floatingLegPrice / annuityFactor;
		return swapRate;
	}
	
	private double getAnnuityFactor(double effectiveT, Frequency paymentFreq,
			int paymentCnt) {
		
		double tenor = 1.0 / paymentFreq.getFrequencyInYear();
		double paymentT = effectiveT;
		double annuityFactor = 0;
		for (int i = 0; i < paymentCnt; ++i) {
			paymentT += tenor;
			annuityFactor += tenor * getDiscountFactor(paymentT);
		}
		return annuityFactor;
	}
	
	public double getSumSwapDiscountFactorWithCoupon(Date date,
			double swapMaturity) {
		return getSumSwapDiscountFactorWithCoupon(date, swapMaturity,
				compoundFreq);
	}
	
	public double getSumSwapDiscountFactorWithCoupon(Date date,
			double swapMaturity, Frequency swapPaymentFreq) {
		int m = (int) swapPaymentFreq.getFrequencyInYear();
		double n = swapMaturity;
		int index = (int) (m * n);
		double sum = 0;
		for (int i = 0; i < index; ++i) {
			date = date.plusMonths(swapPaymentFreq.toMonthUnit());
			sum += getDiscountFactor(date);
		}
		return sum / (double) m;
	}
	
	public Date getDate() {
		return _date;
	}
	
	public Frequency getCompoundingFrequency() {
		return compoundFreq;
	}
	
	public DayCountFraction getDayCountFraction() {
		return dcf;
	}
	
	public double getSpread() {
		return spread;
	}
	
	public Interpolation getInterpolation(){
		return interpolation;
	}
	public void setInterpolation(Interpolation interpolation){
		this.interpolation = interpolation;
	}

	public boolean isEqual(AbstractRateCurve curve) {
		ArrayList<AbstractRate> curveSpotRates = curve.getRates();
		if(rates.size() != curveSpotRates.size()) {
			return false;
		}
		for (int i = 0; i < rates.size(); i++) {
			if(rates.get(i).compareTo(curveSpotRates.get(i)) != 0) {
				return false;
			}
			if(rates.get(i).getRate() != curveSpotRates.get(i).getRate()) {
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<AbstractRate> getRates(){
		return rates;
	}
		
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < rates.size(); ++i) {
			buf.append(rates.get(i));
			buf.append("\r\n");
		}
		if (spread != 0) {
			buf.append("[s=" + spread + "]\r\n");
		}
		return buf.toString();
	}
	public abstract AbstractRateCurve copy(double parallelShift);
	public abstract AbstractRateCurve copy(int index, double parallelShift);
}
