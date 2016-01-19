package com.quantosauros.manager.model.products;

public class InstrumentInfoModel {

	protected String instrumentCd;
	protected String ccyCd;
	protected String issueDt;
	protected String mrtyDt;
	protected String payLegTypeCd;
	protected String rcvLegTypeCd;
	protected String optionTypeCd;
	
	public void setInstrumentCd(String instrumentCd){
		this.instrumentCd = instrumentCd;
	}
	public String getInstrumentCd(){
		return instrumentCd;
	}
	public void setIssueDt(String issueDt){
		this.issueDt = issueDt;
	}
	public String getIssueDt(){
		return issueDt;
	}
	public void setMrtyDt(String mrtyDt){
		this.mrtyDt = mrtyDt;
	}
	public String getMrtyDt(){
		return mrtyDt;
	}
	public void setCcyCd(String ccyCd){
		this.ccyCd = ccyCd;
	}
	public String getCcyCd(){
		return ccyCd;
	}
	public void setOptionTypeCd(String optionTypeCd){
		this.optionTypeCd = optionTypeCd;
	}
	public String getOptionTypeCd(){
		return optionTypeCd;
	}	
	public void setPayLegTypeCd(String payLegTypeCd){
		this.payLegTypeCd = payLegTypeCd;
	}
	public String getPaylegTypeCd(){
		return payLegTypeCd;
	}
	public void setRcvLegTypeCd(String rcvLegTypeCd){
		this.rcvLegTypeCd = rcvLegTypeCd;
	}
	public String getRcvLegTypeCd(){
		return rcvLegTypeCd;
	}	
}
