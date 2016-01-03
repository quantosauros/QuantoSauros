package com.quantosauros.manager.model.pricer;

public class LegCouponInfoPricerModel {

	private String underlyingType;
	private String conditionType;
	private String payRcv;
	private boolean hasCap;
	private boolean hasFloor;
		
	//[period]
	private double[] cap;
	private double[] floor;
	private double[] spreads;
	private String[] couponType;
	private double[] inCouponRates;
	private double[] outCouponRates;
		
	//[underlying]
	private UnderlyingInfoPricerModel[] underlyingInfoPricerModels;
	//private double[][] _leverages;
	
	//[condition]
	//private double[][] upperLimits;
	//private double[][] lowerLimits;
	
	public void setUnderlyingType(String underlyingType){
		this.underlyingType = underlyingType;
	}
	public String getUnderlyingType(){
		return underlyingType;
	}
	public void setConditionType(String conditionType){
		this.conditionType = conditionType;
	}
	public String getConditionType(){
		return conditionType;
	}
	public void setPayRcv(String payRcv){
		this.payRcv = payRcv;
	}
	public String getPayRcv(){
		return payRcv; 
	}
	public void setHasCap(boolean hasCap){
		this.hasCap = hasCap;
	}
	public boolean getHasCap(){
		return hasCap;
	}
	public void setHasFloor(boolean hasFloor){
		this.hasFloor = hasFloor;
	}
	public boolean getHasFloor(){
		return hasFloor;
	}
	public void setCap(double[] cap){
		this.cap = cap;
	}
	public double[] getCap(){
		return cap;
	}
	public void setFloor(double[] floor){
		this.floor = floor;
	}
	public double[] getFloor(){
		return floor;
	}
	public void setSpreads(double[] spreads){
		this.spreads = spreads;
	}
	public double[] getSpreads(){
		return spreads;
	}
	public void setCouponType(String[] couponType){
		this.couponType = couponType;
	}
	public String[] getCouponType(){
		return couponType;
	}
	public void setInCouponRates(double[] inCouponRates){
		this.inCouponRates = inCouponRates;
	}
	public double[] getInCouponRates(){
		return inCouponRates;
	}
	public void setOutCouponRates(double[] outCouponRates){
		this.outCouponRates = outCouponRates;
	}
	public double[] getOutCouponRates(){
		return outCouponRates;
	}
	public void setUnderlyingInfoPricerModels(UnderlyingInfoPricerModel[] underlyingInfoPricerModels){
		this.underlyingInfoPricerModels = underlyingInfoPricerModels;
	}
	public UnderlyingInfoPricerModel[] getUnderlyingInfoPricerModels(){
		return underlyingInfoPricerModels;
	}
	
}
