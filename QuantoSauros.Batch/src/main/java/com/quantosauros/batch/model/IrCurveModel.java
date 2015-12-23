package com.quantosauros.batch.model;

public class IrCurveModel {

	private String factorCd;
	private String irValue;
	private String dcf;	
	private String compoundFrequency;	
	private String ccyCd;
	private String mrtyCd;	
	private String type;
	
    public String getType() {
        return type;
    }     
    public void setType(String type) {
        this.type = type;
    }    
	
    public String getFactorCd() {
        return factorCd;
    }     
    public void setFactorCd(String factorCd) {
        this.factorCd = factorCd;
    }    
    public String getIrValue(){
    	return irValue;
    }    
    public void setIrValue(String irValue) {
        this.irValue = irValue;
    }
    public String getDcf(){
    	return dcf;
    }
    public void setDcf(String dcf) {
        this.dcf = dcf;
    }
    public String getCompoundFrequency(){
    	return compoundFrequency;
    }    
    public void setCompoundFrequency(String compoundFrequency) {
        this.compoundFrequency = compoundFrequency;
    }
    public String getCcyCd(){
    	return ccyCd;
    }    
    public void setCcyCd(String ccyCd) {
        this.ccyCd = ccyCd;
    }
    public String getMrtyCd(){
    	return mrtyCd;
    }    
    public void setMrtyCd(String mrtyCd) {
        this.mrtyCd = mrtyCd;
    }
}
