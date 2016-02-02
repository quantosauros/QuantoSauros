package com.quantosauros.batch.model;

public class ProcessDataModel {

	private String dt;
	private String procId;
	private String instrumentCd;
	private double position;
	
	public void setDt(String dt){
		this.dt = dt;
	}
	public String getDt(){
		return dt;
	}
	public void setProcId(String procId){
		this.procId = procId;
	}
	public String getProcId(){
		return procId;
	}
	public void setInstrumentCd(String instrumentCd){
		this.instrumentCd = instrumentCd;
	}
	public String getInstrumentCd(){
		return instrumentCd;
	}
	public void setPosition(double position){
		this.position = position;
	}
	public double getPosition(){
		return position;
	}
}
