package com.quantosauros.jpl.instrument;

import java.util.ArrayList;
import java.util.Map;

import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.hullwhite.HWVolatilitySurface;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.engine.pricer.AbstractPricer;
import com.quantosauros.jpl.result.AbstractResult;

public abstract class AbstractProduct {

	protected AbstractPricer _pricer;
	
	public AbstractProduct() {}
	
	public abstract Money getPrice(
			MarketInfo[][] legMarketInfos,
			RateMarketInfo discountMarketInfo,
			double[][] correlations);
	
	public Money getLegPrice(int legIndex){
		return _pricer.getLegPrice(legIndex);
	}	
	public int getPeriodNum(){
		return _pricer.getPeriodNum();
	}	
	public ArrayList<ArrayList<Double>> getTenors(){
		return _pricer.getTenors(); 
	}
	public int[] getExerciseIndex(){
		return _pricer.getExerciseIndex();
	}	
	public void setExerciseIndex(int[] exerciseIndex){
		_pricer.setExerciseIndex(exerciseIndex);
	}
	public int getDeferredCouponResetIndex(){
		return _pricer.getDeferredCouponResetIndex();
	}	
	public double[][] getPayOffs(int legIndex){
		return _pricer.getPayOffs(legIndex);
	}	
	public double[][] getDiscounts(){
		return _pricer.getDiscounts();
	}
	public double[][][] getRefRates(int legIndex, int undIndex){
		return _pricer.getRefRates(legIndex, undIndex);
	}	
	public boolean getHasExercise(){
		return _pricer.getHasExercise();
	}	
	public Money getPrincipal(int legIndex){
		return _pricer.getPrincipal(legIndex);
	}	
	public DayCountFraction getDCF(int legIndex){
		return _pricer.getDCF(legIndex);
	}	
	public double[][] getLowerLimits(int legIndex){
		return _pricer.getLowerLimits(legIndex);
	}	
	public double[][] getUpperLimits(int legIndex){
		return _pricer.getUpperLimits(legIndex);
	}	
	public double[] getInCouponRates(int legIndex){
		return _pricer.getInCouponRates(legIndex);
	}	
	public double[] getOutCouponRates(int legIndex){
		return _pricer.getOutCouponRates(legIndex);		
	}
	public double getReferenceTenor(int legIndex, int underlyingIndex){
		return _pricer.getReferenceTenor(legIndex, underlyingIndex);
	}	
	public boolean[][] getRestrictionInfo(){
		return _pricer.getRestrictionInfo();
	}	
	public double[] getLeverage(int legIndex, int underlyingIndex){
		return _pricer.getLeverage(legIndex, underlyingIndex);
	}	
	public int[] getMonitorFrequencies(){
		return _pricer.getMonitorFrequencies();
	}	
	public void setHasExercise(boolean hasExercise){
		_pricer.setHasExercise(hasExercise);
	}
	public double[][] getCoupon(int legIndex){
		return _pricer.getCoupon(legIndex);
	}
	public double[][] getTenor(int legIndex){
		return _pricer.getTenor(legIndex);
	}
	public int[] getUnderlyingNum(){
		return _pricer.getUnderlyingNum();
	}
	public int getSimNum(){
		return _pricer.getSimNum();
	}
	public int getLegNum(){
		return _pricer.getLegNum();
	}
	public Map getResults(){		
		Map result = (new AbstractResult()).getResults(this);
		return result;
	}
	public int getPayIndex(){
		return _pricer.getPayIndex();
	}
	public int getRcvIndex(){
		return _pricer.getRcvIndex();
	}
	public double[][] getLSMCParams(int legIndex){
		return _pricer.getLSMCParams(legIndex);
	}
	public boolean hasPrincipalExchange(){
		return _pricer.hasPrincipalExchange();
	}
}
