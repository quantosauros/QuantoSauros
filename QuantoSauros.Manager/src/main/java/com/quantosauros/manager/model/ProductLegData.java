package com.quantosauros.manager.model;

public class ProductLegData {

	private String dt;
	private String instrumentCd;
	private String payRcvCd;
	private String nextCouponPayDt;
	private String nextCoupon;
	private String accrualDayCnt;
	private String accumulateAvgCoupon;
	
	public void setDt(String dt){this.dt=dt;}
	public void setInstrumentCd(String instrumentCd){this.instrumentCd=instrumentCd;}
	public void setPayRcvCd(String payRcvCd){this.payRcvCd=payRcvCd;}
	public void setNextCouponPayDt(String nextCouponPayDt){this.nextCouponPayDt=nextCouponPayDt;}
	public void setNextCoupon(String nextCoupon){this.nextCoupon=nextCoupon;}
	public void setAccrualDayCnt(String accrualDayCnt){this.accrualDayCnt=accrualDayCnt;}
	public void setAccumulateAvgCoupon(String accumulateAvgCoupon){this.accumulateAvgCoupon=accumulateAvgCoupon;}
	public String getDt(){return dt;}
	public String getInstrumentCd(){return instrumentCd;}
	public String getPayRcvCd(){return payRcvCd;}
	public String getNextCouponPayDt(){return nextCouponPayDt;}
	public String getNextCoupon(){return nextCoupon;}
	public String getAccrualDayCnt(){return accrualDayCnt;}
	public String getAccumulateAvgCoupon(){return accumulateAvgCoupon;}

}
