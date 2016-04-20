package com.quantosauros.jpl.result;

import com.quantosauros.common.currency.Currency;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.PaymentPeriod;

public class Cashflow {

	private PaymentPeriod paymentPeriod;
	private double principal;
	private double interest;
	private double accruedInterest;
	private double probability = 1.0;
	private double discountFactor = 1.0;
	private Currency ccyCd;
	private String conditionString;
	
	public Cashflow(PaymentPeriod paymentPeriod){
		this.paymentPeriod = paymentPeriod;
	}
	
	private PaymentPeriod getPaymentPeriod(){
		return paymentPeriod;
	}
	
	public Date getResetDate(){
		return paymentPeriod.getResetDate();
	}
	public Date getStartDate(){
		return paymentPeriod.getStartDate();
	}
	public Date getEndDate(){
		return paymentPeriod.getEndDate();
	}
	public Date getPaymentDate(){
		return paymentPeriod.getPaymentDate();
	}
	public double getPrincipal(){
		return principal;
	}
	public void setPrincipal(double principal){
		this.principal = principal;
	}
	public double getInterest(){
		return interest;
	}
	public void setInterest(double interest){
		this.interest = interest;
	}
	public double getAccruedInterest(){
		return accruedInterest;
	}
	public void setAccruedInterest(double accruedInterest){
		this.accruedInterest = accruedInterest;
	}
	public boolean isExpired(Date date){
		return paymentPeriod.isExpired(date);
	}
	public double getProbability(){
		return probability;
	}
	public void setProbability(double probability){
		this.probability = probability;
	}
	public void setDiscountFactor(double discountFactor) {
		this.discountFactor = discountFactor;	
	}
	public double getDiscountFactor(){
		return this.discountFactor;
	}
	public String getCondition() {
		return conditionString;
	}
	public void setCondition(String conditionString) {
		this.conditionString = conditionString;
	}	
	public Currency getCcyCd() {
		return ccyCd;
	}
	public void setCcyCd(Currency ccyCd) {
		this.ccyCd = ccyCd;
	}
	
}
