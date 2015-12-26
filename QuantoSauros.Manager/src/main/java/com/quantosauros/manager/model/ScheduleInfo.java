package com.quantosauros.manager.model;

public class ScheduleInfo {

	private String startDate;
	private String endDate;
	private String paymentDate;
	
	public ScheduleInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}
	public String getStartDate(){
		return startDate;
	}
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}
	public String getEndDate(){
		return endDate;
	}
	public void setPaymentDate(String paymentDate){
		this.paymentDate = paymentDate;
	}
	public String getPaymentDate(){
		return paymentDate;
	}
		
}
