package com.quantosauros.manager.model.pricer;

public class InterestRateCurveModel {

	private String date;
	private String compoundFreq;
	private String dcf;
	private String[] vertex;
	private double[] rate;
	private String[] rateType;
	
	public void setDate(String date){
		this.date = date;
	}
	public String getDate(){
		return date;
	}
	public void setCompoundFreq(String compoundFreq){
		this.compoundFreq = compoundFreq;
	}
	public String getCoupondFreq(){
		return compoundFreq;
	}
	public void setDcf(String dcf){
		this.dcf = dcf;
	}
	public String getDcf(){
		return dcf;
	}
	public void setVertex(String[] vertex){
		this.vertex = vertex;
	}
	public String[] getVertex(){
		return vertex;
	}
	public void setRate(double[] rate){
		this.rate = rate;
	}
	public double[] getRate(){
		return rate;
	}
	public void setRateType(String[] rateType){
		this.rateType = rateType;
	}
	public String[] getRateType(){
		return rateType;
	}

}
