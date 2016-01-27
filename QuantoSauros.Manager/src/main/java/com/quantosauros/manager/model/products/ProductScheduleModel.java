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
	private double upperBound1;
	private double lowerBound1;
	private double leverage1;
	private double upperBound2;
	private double lowerBound2;
	private double leverage2;
	private double leverage3;
	private double inCoupon;
	private double outCoupon;
	private double fixedCoupon;
	private double cap;
	private double floor;
	
	public void setInstrumentCd(String instrumentCd){this.instrumentCd=instrumentCd;}
	public void setPayRcvCd(String payRcvCd){this.payRcvCd=payRcvCd;}
	public void setCouponStrtDt(String couponStrtDt){this.couponStrtDt=couponStrtDt;}
	public void setCouponResetDt(String couponResetDt){this.couponResetDt=couponResetDt;}
	public void setCouponEndDt(String couponEndDt){this.couponEndDt=couponEndDt;}
	public void setCouponPayDt(String couponPayDt){this.couponPayDt=couponPayDt;}
	public void setCouponType(String couponType){this.couponType=couponType;}
	public void setPrincipal(String principal){this.principal=principal;}
	public void setUpperBound1(double upperBound1){this.upperBound1=upperBound1;}
	public void setLowerBound1(double lowerBound1){this.lowerBound1=lowerBound1;}
	public void setLeverage1(double leverage1){this.leverage1=leverage1;}
	public void setUpperBound2(double upperBound2){this.upperBound2=upperBound2;}
	public void setLowerBound2(double lowerBound2){this.lowerBound2=lowerBound2;}
	public void setLeverage2(double leverage2){this.leverage2=leverage2;}
	public void setLeverage3(double leverage3){this.leverage3=leverage3;}
	public void setInCoupon(double inCoupon){this.inCoupon=inCoupon;}
	public void setOutCoupon(double outCoupon){this.outCoupon=outCoupon;}
	public void setFixedCoupon(double fixedCoupon){this.fixedCoupon=fixedCoupon;}
	public void setCap(double cap){this.cap=cap;}
	public void setFloor(double floor){this.floor=floor;}
	public String getInstrumentCd(){return instrumentCd;}
	public String getPayRcvCd(){return payRcvCd;}
	public String getCouponStrtDt(){return couponStrtDt;}
	public String getCouponEndDt(){return couponEndDt;}
	public String getCouponResetDt(){return couponResetDt;}
	public String getCouponPayDt(){return couponPayDt;}
	public String getCouponType(){return couponType;}
	public String getPrincipal(){return principal;}
	public double getUpperBound1(){return upperBound1;}
	public double getLowerBound1(){return lowerBound1;}
	public double getLeverage1(){return leverage1;}
	public double getUpperBound2(){return upperBound2;}
	public double getLowerBound2(){return lowerBound2;}
	public double getLeverage2(){return leverage2;}
	public double getLeverage3(){return leverage3;}
	public double getInCoupon(){return inCoupon;}
	public double getOutCoupon(){return outCoupon;}
	public double getFixedCoupon(){return fixedCoupon;}
	public double getCap(){return cap;}
	public double getFloor(){return floor;}

	
}
