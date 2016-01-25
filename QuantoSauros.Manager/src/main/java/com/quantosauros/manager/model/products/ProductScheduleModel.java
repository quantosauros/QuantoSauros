package com.quantosauros.manager.model.products;

public class ProductScheduleModel {
	private String instrumentCd;
	private String payRcvCd;
	private String couponResetDt;
	private String couponStrtDt;
	private String couponEndDt;	
	private String couponPayDt;
	private String couponType;
	private String principal;
	private String upperBound1;
	private String lowerBound1;
	private String leverage1;
	private String upperBound2;
	private String lowerBound2;
	private String leverage2;
	private String leverage3;
	private String inCoupon;
	private String outCoupon;
	private String fixedCoupon;
	private String cap;
	private String floor;
	
	public void setInstrumentCd(String instrumentCd){this.instrumentCd=instrumentCd;}
	public void setPayRcvCd(String payRcvCd){this.payRcvCd=payRcvCd;}
	public void setCouponStrtDt(String couponStrtDt){this.couponStrtDt=couponStrtDt;}
	public void setCouponResetDt(String couponResetDt){this.couponResetDt=couponResetDt;}
	public void setCouponEndDt(String couponEndDt){this.couponEndDt=couponEndDt;}
	public void setCouponPayDt(String couponPayDt){this.couponPayDt=couponPayDt;}
	public void setCouponType(String couponType){this.couponType=couponType;}
	public void setPrincipal(String principal){this.principal=principal;}
	public void setUpperBound1(String upperBound1){this.upperBound1=upperBound1;}
	public void setLowerBound1(String lowerBound1){this.lowerBound1=lowerBound1;}
	public void setLeverage1(String leverage1){this.leverage1=leverage1;}
	public void setUpperBound2(String upperBound2){this.upperBound2=upperBound2;}
	public void setLowerBound2(String lowerBound2){this.lowerBound2=lowerBound2;}
	public void setLeverage2(String leverage2){this.leverage2=leverage2;}
	public void setLeverage3(String leverage3){this.leverage3=leverage3;}
	public void setInCoupon(String inCoupon){this.inCoupon=inCoupon;}
	public void setOutCoupon(String outCoupon){this.outCoupon=outCoupon;}
	public void setFixedCoupon(String fixedCoupon){this.fixedCoupon=fixedCoupon;}
	public void setCap(String cap){this.cap=cap;}
	public void setFloor(String floor){this.floor=floor;}
	public String getInstrumentCd(){return instrumentCd;}
	public String getPayRcvCd(){return payRcvCd;}
	public String getCouponStrtDt(){return couponStrtDt;}
	public String getCouponEndDt(){return couponEndDt;}
	public String getCouponResetDt(){return couponResetDt;}
	public String getCouponPayDt(){return couponPayDt;}
	public String getCouponType(){return couponType;}
	public String getPrincipal(){return principal;}
	public String getUpperBound1(){return upperBound1;}
	public String getLowerBound1(){return lowerBound1;}
	public String getLeverage1(){return leverage1;}
	public String getUpperBound2(){return upperBound2;}
	public String getLowerBound2(){return lowerBound2;}
	public String getLeverage2(){return leverage2;}
	public String getLeverage3(){return leverage3;}
	public String getInCoupon(){return inCoupon;}
	public String getOutCoupon(){return outCoupon;}
	public String getFixedCoupon(){return fixedCoupon;}
	public String getCap(){return cap;}
	public String getFloor(){return floor;}

	
}
