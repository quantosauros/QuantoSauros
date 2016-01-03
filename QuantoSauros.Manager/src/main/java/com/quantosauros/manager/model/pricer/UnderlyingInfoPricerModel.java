package com.quantosauros.manager.model.pricer;

public class UnderlyingInfoPricerModel {

	private String modelType;
	
	//RateUnderlyingInfo
	private double tenor;
	private String swapCouponFrequency;
	private String rateType;
	
	//EquityUnderlyingInfo
	private double basePrice;
	private String riskFreeType;
	
	private String underlyingInfoFlag;
	
	public void setModelType(String modelType){
		this.modelType = modelType;
	}
	public String getModelType(){
		return this.modelType;
	}
	public void setTenor(double tenor){
		this.tenor = tenor;
	}
	public double getTenor(){
		return tenor;
	}
	public void setSwapCouponFrequency(String swapCouponFrequency){
		this.swapCouponFrequency = swapCouponFrequency;
	}
	public String getSwapCouponFrequency(){
		return swapCouponFrequency;
	}
	public void setRateType(String rateType){
		this.rateType = rateType;
	}
	public String getRateType(){
		return rateType;
	}
	public void setBasePrice(double basePrice){
		this.basePrice = basePrice;
	}
	public double getBasePrice(){
		return basePrice;
	}
	public void setRiskFreeType(String riskFreeType){
		this.riskFreeType = riskFreeType;
	}
	public String getRiskFreeType(){
		return riskFreeType;
	}
	public void setUnderlyingInfoFlag(String underlyingInfoFlag){
		this.underlyingInfoFlag = underlyingInfoFlag;
	}
	public String getUnderlyingInfoFlag(){
		//R: rate, E: equity, F: FX
		return underlyingInfoFlag;
	}
}
