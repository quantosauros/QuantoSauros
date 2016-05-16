package com.quantosauros.batch.model;

public class IrCurveModel {

	private String factorCd;
	private String irValue;
	private String dcf;	
	private String compoundFrequency;	
	private String ccyCd;
	private String mrtyCd;	
	private String ytmRatetype;
	
	private String payCouponFrequency;
	private String rcvCouponFrequency;
	private String payDayCountConvention;
	private String rcvDayCountConvention;
	private String isBondCurve;
	
	private String issueDt;
	private String mrtyDt;
	private String couponFrequency;
	private double coupon;
	
    public String getYTMRateType() {
        return ytmRatetype;
    }     
    public void setYTMRateType(String type) {
        this.ytmRatetype = type;
    }    
	
    public String getFactorCd() {
        return factorCd;
    }     
    public void setFactorCd(String factorCd) {
        this.factorCd = factorCd;
    }    
    public String getIrValue(){
    	return irValue;
    }    
    public void setIrValue(String irValue) {
        this.irValue = irValue;
    }
    public String getDcf(){
    	return dcf;
    }
    public void setDcf(String dcf) {
        this.dcf = dcf;
    }
    public String getCompoundFrequency(){
    	return compoundFrequency;
    }    
    public void setCompoundFrequency(String compoundFrequency) {
        this.compoundFrequency = compoundFrequency;
    }
    public String getCcyCd(){
    	return ccyCd;
    }    
    public void setCcyCd(String ccyCd) {
        this.ccyCd = ccyCd;
    }
    public String getMrtyCd(){
    	return mrtyCd;
    }    
    public void setMrtyCd(String mrtyCd) {
        this.mrtyCd = mrtyCd;
    }
    public void setPayCouponFrequency(String payCouponFrequency){
    	this.payCouponFrequency = payCouponFrequency;
    }
    public String getPayCouponFrequency(){
    	return payCouponFrequency;
    }
    public void setRcvCouponFrequency(String rcvCouponFrequency){
    	this.rcvCouponFrequency = rcvCouponFrequency;
    }
    public String getRcvCouponFrequency(){
    	return rcvCouponFrequency;
    }
    public void setPayDayCountConvention(String payDayCountConvention){
    	this.payDayCountConvention = payDayCountConvention;
    }
    public String getPayDayCountConvention(){
    	return payDayCountConvention;
    }
    public void setRcvDayCountConvention(String rcvDayCountConvention){
    	this.rcvDayCountConvention = rcvDayCountConvention;
    }
    public String getRcvDayCountConvention(){
    	return rcvDayCountConvention;
    }
    public void setIsBondCurve(String isBondCurve){
    	this.isBondCurve = isBondCurve;
    }
    public String getIsBondCurve(){
    	return isBondCurve;
    }
    public void setIssueDt(String issueDt){
    	this.issueDt = issueDt;
    }
    public String getIssueDt(){
    	return issueDt;
    }
    public void setMrtyDt(String mrtyDt){
    	this.mrtyDt = mrtyDt;
    }
    public String getMrtyDt(){
    	return mrtyDt;
    }
    public void setCouponFrequency(String couponFrequency){
    	this.couponFrequency = couponFrequency;
    }
    public String getCouponFrequenc(){
    	return couponFrequency;
    }
    public void setCoupon(double coupon){
    	this.coupon = coupon;
    }
    public double getCoupon(){
    	return coupon;
    }
}
