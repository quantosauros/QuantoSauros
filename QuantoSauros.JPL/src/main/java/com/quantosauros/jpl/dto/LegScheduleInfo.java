package com.quantosauros.jpl.dto;

import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;

public class LegScheduleInfo extends AbstractInfo{
	
	private DayCountFraction _dcf;
	private PaymentPeriod[] _periods;
	
	public LegScheduleInfo(PaymentPeriod[] periods, DayCountFraction dcf) {
		_periods = periods;
		_dcf = dcf;
	}
	
	public PaymentPeriod[] getPaymentPeriods(){
		return _periods;
	}
	public void setPaymentPeriods(PaymentPeriod[] paymentPeriods){
		_periods = paymentPeriods;
	}
	public DayCountFraction getDCF(){
		return _dcf;
	}
	public void setDCF(DayCountFraction dcf){
		this._dcf = dcf;
	}
}
