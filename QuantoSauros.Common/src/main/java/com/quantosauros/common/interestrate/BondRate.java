package com.quantosauros.common.interestrate;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.YTMRateType;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.Vertex;

public class BondRate extends AbstractRate {

	private Frequency couponFrequency;
	private Date maturityDate;
	private Date accrualStartDate;
	private double couponRate;
	
	
	public BondRate(Vertex vertex, double rate, 
			Frequency couponFrequency, Date maturityDate, Date accrualStartDate, 
			double couponRate) {
		super(vertex, rate, YTMRateType.BOND);
		this.couponFrequency = couponFrequency;
		this.maturityDate = maturityDate;
		this.couponRate = couponRate;
		this.accrualStartDate = accrualStartDate;
	}
	
	public BondRate(Vertex vertex, double rate, YTMRateType rateType,
			Frequency couponFrequency, Date maturityDate, Date accrualStartDate, 
			double couponRate) {
		super(vertex, rate, rateType);
		this.couponFrequency = couponFrequency;
		this.maturityDate = maturityDate;
		this.couponRate = couponRate;
		this.accrualStartDate = accrualStartDate;
	}
	
	public Frequency getCouponFrequency(){
		return couponFrequency;
	}
	public Date getMaturityDate(){
		return maturityDate;
	}
	public Date getAccrualStartDate(){
		return accrualStartDate;
	}
	public double getCouponRate(){
		return couponRate;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
	
		buf.append("[" + vertex + ", " + rate + ", " + rateType + ", " +
		accrualStartDate + ", " + maturityDate + ", " + couponRate + ", " + couponFrequency + "] ");
		
		return buf.toString();
	}
}
