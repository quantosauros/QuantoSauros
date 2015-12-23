package com.quantosauros.batch.model;

public class ProductInfoModel {

	private String issueDt;
	private String mrtyDt;
	private String ccyCd;
	private String principalExchCd;
	private String optionTypeCd;	
	
    public String getIssueDt() {
        return issueDt;
    } 
    
    public void setIuuseDt(String issueDt) {
        this.issueDt = issueDt;
    }
    
    public String getMrtyDt(){
    	return mrtyDt;
    }
    
    public void setMrtyDt(String mrtyDt) {
        this.mrtyDt = mrtyDt;
    }
    
    public String getCcyCd(){
    	return ccyCd;
    }
    
    public void setCcyCd(String ccyCd) {
        this.ccyCd = ccyCd;
    }
    
    public String getPrincipalExchCd(){
    	return principalExchCd;
    }
    
    public void setPrincipalExchCd(String principalExchCd) {
        this.principalExchCd = principalExchCd;
    }
    
    public String getOptionTypeCd(){
    	return optionTypeCd;
    }
    
    public void setOptionTypeCd(String optionTypeCd) {
        this.optionTypeCd = optionTypeCd;
    }

}
