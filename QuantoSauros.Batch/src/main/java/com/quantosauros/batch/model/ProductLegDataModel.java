package com.quantosauros.batch.model;

public class ProductLegDataModel {

	private String nextCouponPayDt;
	private String nextCoupon;
	private String accrualDayCnt;
	private String accumulateAvgCoupon;
	
    public String getNextCouponPayDt() {
        return nextCouponPayDt;
    }     
    public void setNextCouponPayDt(String nextCouponPayDt) {
        this.nextCouponPayDt = nextCouponPayDt;
    }    
    public String getNextCoupon(){
    	return nextCoupon;
    }    
    public void setNextCoupon(String nextCoupon) {
        this.nextCoupon = nextCoupon;
    }    
    public String getAccrualDayCnt(){
    	return accrualDayCnt;
    }
    public void setAccrualDayCnt(String accrualDayCnt) {
        this.accrualDayCnt = accrualDayCnt;
    }
    public String getAccumulateAvgCoupon(){
    	return accumulateAvgCoupon;
    }    
    public void setAccumulateAvgCoupon(String accumulateAvgCoupon) {
        this.accumulateAvgCoupon = accumulateAvgCoupon;
    }
}
