package com.quantosauros.manager.model;

public class DetailInfo {

	protected String processDt;
	protected String instrumentCd;
	protected String valueType;
	protected boolean isNonCall;
	protected String legType;
	protected int periodNum;
	protected double average;
	protected double std;
	
	public String getProcessDt(){
		return processDt;
	}
	public void setProcessDt(String processDt){
		this.processDt = processDt;
	}
	public String getInstrumentCd(){
		return instrumentCd;
	}
	public void setInstrumentCd(String instrumentCd){
		this.instrumentCd = instrumentCd;
	}
	public String getValueType(){
		return valueType;
	}
	public void setValueType(String valueType){
		this.valueType = valueType;
	}
	public boolean getIsNonCall(){
		return isNonCall;
	}
	public void setIsNonCall(String isNonCall){
		if (isNonCall.equals("0")){
			this.isNonCall = true;
		} else {
			this.isNonCall = false;
		}
	}
	public String getLegType(){
		return legType;
	}
	public void setLegType(String legType){
		this.legType = legType;
	}
	public int getPeriodNum(){
		return periodNum;
	}
	public void setPeriodNum(int periodNum){
		this.periodNum = periodNum;
	}
	public double getAverage(){
		return average;
	}
	public void setAverage(double average){
		this.average = average;
	}
	public double getStd(){
		return std;
	}
	public void setStd(double std){
		this.std = std;
	}
	
}
