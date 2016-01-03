package com.quantosauros.manager.model.pricer;

public class LegInfoPricerModel {

	//LegCouponInfo
	private UnderlyingInfoPricerModel[] underlyingInfoPricerModels;
	private String payRcv;
	private String underlyingType;
	private String conditionType;
	private boolean hasCap;
	private boolean hasFloor;
	
	//LegDataInfo
	private String nextCouponDt;
	private double nextCouponRate;
	private int cumulatedAccrualDays;
	private double cumulatedAvgCoupon;
	
	//LegScheduleInfo
	private String dcf;
	
	//LegAmortizationInfo
	private double principal;	
		
	public void setUnderlyingInfoPricerModels(UnderlyingInfoPricerModel[] underlyingInfoPricerModels){
		this.underlyingInfoPricerModels = underlyingInfoPricerModels;
	}
	public UnderlyingInfoPricerModel[] getUnderlyingInfoPricerModels(){
		return underlyingInfoPricerModels;
	}
	public void setPayRcv(String payRcv){
		this.payRcv = payRcv;
	}
	public String getPayRcv(){
		return payRcv; 
	}
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
	public String getNextCouponDt(){
		return nextCouponDt;
	}
	public void setNextCouponDt(String nextCouponDt){
		this.nextCouponDt = nextCouponDt;
	}
	public double getNextCouponRate(){
		return nextCouponRate;
	}
	public void setNextCouponRate(double nextCouponRate){
		this.nextCouponRate = nextCouponRate;
	}
	public int getCumulatedAccrualDays(){
		return cumulatedAccrualDays;
	}
	public void setCumulatedAccrualDays(int cumulatedAccrualDays){
		this.cumulatedAccrualDays = cumulatedAccrualDays;
	}
	public double getCumulatedAvgCoupon(){
		return cumulatedAvgCoupon;
	}
	public void setCumulatedAvgCoupon(double cumulatedAvgCoupon){
		this.cumulatedAvgCoupon = cumulatedAvgCoupon;
	}
	public void setDcf(String dcf){
		this.dcf = dcf;
	}
	public String getDcf(){
		return dcf;
	}
	public void setPrincipal(double principal){
		this.principal = principal;
	}
	public double getPrincipal(){
		return principal;
	}
}
