package com.quantosauros.common.interestrate;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.YTMRateType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;

public class SwapRate extends AbstractRate {

	private Frequency payCouponFrequency;
	private Frequency rcvCouponFrequency;	
	private DayCountFraction payDcf;
	private DayCountFraction rcvDcf;
	private Frequency compoundFrequency;
	
	public SwapRate(Vertex vertex, double rate, YTMRateType rateType,
			Frequency payCouponFrequency, Frequency rcvCouponFrequency,
			DayCountFraction payDcf, DayCountFraction rcvDcf,
			Frequency compoundFrequency) {
		super(vertex, rate, rateType);
		this.payCouponFrequency = payCouponFrequency;
		this.rcvCouponFrequency = rcvCouponFrequency;
		this.payDcf = payDcf;
		this.rcvDcf = rcvDcf;
		this.compoundFrequency = compoundFrequency;
	}
	
	public Frequency getPayCouponFrequency(){
		return payCouponFrequency;
	}
	public Frequency getRcvCouponFrequency(){
		return rcvCouponFrequency;
	}	
	public DayCountFraction getPayDcf(){
		return payDcf;
	}
	public DayCountFraction getRcvDcf(){
		return rcvDcf;
	}
	public Frequency getCompoundFrequency(){
		return compoundFrequency;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
	
		buf.append("[" + vertex + ", " + rate + ", " + rateType + ", " + payCouponFrequency + ", " + payDcf + ", " + rcvCouponFrequency + ", " + rcvDcf + ", " + compoundFrequency + "] ");
		
		return buf.toString();
	}
}
