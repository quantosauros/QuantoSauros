package com.quantosauros.batch.model;

public class VolSurfModel {

	private String swaptionMrty;
	private String swaptionTenor;
	private String vol;
	private String ccyCd;
	private String dcf;	
	private String cntNumber;
	
    public String getSwaptionMrty() {
        return swaptionMrty;
    }     
    public void setSwaptionMrty(String swaptionMrty) {
        this.swaptionMrty = swaptionMrty;
    }    
    public String getSwaptionTenor() {
        return swaptionTenor;
    }     
    public void setSwaptionTenor(String swaptionTenor) {
        this.swaptionTenor = swaptionTenor;
    }    
    public String getVol() {
        return vol;
    }     
    public void setVol(String vol) {
        this.vol = vol;
    }    
    public String getDcf(){
    	return dcf;
    }
    public void setDcf(String dcf) {
        this.dcf = dcf;
    }
    public String getCcyCd(){
    	return ccyCd;
    }    
    public void setCcyCd(String ccyCd) {
        this.ccyCd = ccyCd;
    }
    public String getCntNumber(){
    	return cntNumber;
    }    
    public void setCntNumber(String cntNumber) {
        this.cntNumber = cntNumber;
    }
}
