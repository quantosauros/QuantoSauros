package com.quantosauros.batch.dao;

public class InstrumentInfoDao {

	private String instrumentCd;	
	private String ccyCd;
	private String payLegTypeCd;
	private String rcvLegTypeCd;
	private String optionTypeCd;
	private String issueDt;
	private String mrtyDt;	
	
    public String getInstrumentCd() {
        return instrumentCd;
    } 
    
    public void setInstrumentCd(String instrumentCd) {
        this.instrumentCd = instrumentCd;
    }
    public String getCcyCd(){
    	return ccyCd;
    }
    
    public void setCcyCd(String ccyCd) {
        this.ccyCd = ccyCd;
    }
    public String getPayLegTypeCd(){
    	return payLegTypeCd;
    }
    
    public void setPayLegTypeCd(String payLegTypeCd) {
        this.payLegTypeCd = payLegTypeCd;
    }
    public String getRcvLegTypeCd(){
    	return rcvLegTypeCd;
    }
    
    public void setRcvLegTypeCd(String rcvLegTypeCd) {
        this.rcvLegTypeCd = rcvLegTypeCd;
    }
    public String getOptionTypeCd(){
    	return optionTypeCd;
    }    
    public void setOptionTypeCd(String optionTypeCd) {
        this.optionTypeCd = optionTypeCd;
    }
    public String getIssueDt(){
    	return issueDt;
    }    
    public void setIssueDt(String issueDt) {
        this.issueDt = issueDt;
    }
    public String getMrtyDt(){
    	return mrtyDt;
    }    
    public void setMrtyDt(String mrtyDt) {
        this.mrtyDt = mrtyDt;
    }
}
