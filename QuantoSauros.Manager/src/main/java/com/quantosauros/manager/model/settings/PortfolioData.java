package com.quantosauros.manager.model.settings;

public class PortfolioData {

	protected String portfolioId;
	protected String instrumentCd;	
	protected String flag;
	protected String ccyCd;
	protected String issueDt;
	protected String mrtyDt;
	protected String payLegTypeCd;
	protected String rcvLegTypeCd;
	protected String optionTypeCd;
	protected String crtnTime;
	
	public String getPortfolioId(){
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId){
		this.portfolioId = portfolioId;
	}
	public String getInstrumentCd(){
		return instrumentCd;
	}
	public void setInstrumentCd(String instrumentCd){
		this.instrumentCd = instrumentCd;
	}
	public String getFlag(){
		return flag;
	}
	public void setFlag(String flag){
		this.flag = flag;
	}
	public String getCrtnTime(){
		return crtnTime;
	}
	public void setCrtnTime(String crtnTime){
		this.crtnTime = crtnTime;
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
	public String getPayLegTypeCd(){
		return payLegTypeCd;
	}
	public void setRcvLegTypeCd(String rcvLegTypeCd){
		this.rcvLegTypeCd = rcvLegTypeCd;
	}
	public String getRcvLegTypeCd(){
		return rcvLegTypeCd;
	}	
}
