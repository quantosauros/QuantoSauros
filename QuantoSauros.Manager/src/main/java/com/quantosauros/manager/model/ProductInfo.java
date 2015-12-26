package com.quantosauros.manager.model;

public class ProductInfo {

	protected String instrumentCd;
	protected String issueDt;
	protected String mrtyDt;
	protected String ccyCd;
	protected String principalExchCd;
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
	public void setPrincipalExchCd(String principalExchCd){
		this.principalExchCd = principalExchCd;
	}
	public String getPrincipalExchCd(){
		return principalExchCd;
	}
	public void setOptionTypeCd(String optionTypeCd){
		this.optionTypeCd = optionTypeCd;
	}
	public String getOptionTypeCd(){
		return optionTypeCd;
	}	
}
