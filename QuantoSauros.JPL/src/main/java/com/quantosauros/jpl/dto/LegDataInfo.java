package com.quantosauros.jpl.dto;

import com.quantosauros.common.date.Date;

public class LegDataInfo extends AbstractInfo {
	
	private Date _nextCouponDate;
	private double _nextCouponRate;
	private int _cumulatedAccrualDays;
	private double _cumulatedAvgCoupon;
	
	public LegDataInfo(){		
	}
	
	public LegDataInfo(Date nextCouponDate) {
		_nextCouponDate = nextCouponDate;
	}
	public Date getNextCouponDate(){
		return _nextCouponDate;
	}
	public void setNextCouponRate(double nextCouponRate){
		_nextCouponRate = nextCouponRate;
	}
	public double getNextCouponRate(){
		return _nextCouponRate;
	}
	public void setCumulatedAccrualDays(int cumAccrualDays){
		_cumulatedAccrualDays = cumAccrualDays;
	}
	public int getCumulatedAccrualDays(){
		return _cumulatedAccrualDays;
	}
	public void setCumulatedAvgCoupon(double cumAvgCoupon){
		_cumulatedAvgCoupon = cumAvgCoupon;
	}
	public double getCumulatedAvgCoupon(){
		return _cumulatedAvgCoupon;
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n");
		buf.append("==" + getClass().getName() + "==");
		
		if (_nextCouponDate != null){
			buf.append("\r\n");
			buf.append("nextCouponDate=" + _nextCouponDate + "; ");			
		}
		buf.append("nextCouponRate=" + _nextCouponRate+ "; ");
		buf.append("cumulatedAccrualDays=" + _cumulatedAccrualDays+ "; ");
		buf.append("cumulatedAvgCoupon=" + _cumulatedAvgCoupon+ "; ");		
		
		return buf.toString();
	}
}
