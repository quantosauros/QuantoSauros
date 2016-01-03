package com.quantosauros.manager.model.pricer;

public class LegDataInfoPricerModel {

	private String nextCouponDt;
	private double nextCouponRate;
	private int cumulatedAccrualDays;
	private double cumulatedAvgCoupon;
	
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
}
