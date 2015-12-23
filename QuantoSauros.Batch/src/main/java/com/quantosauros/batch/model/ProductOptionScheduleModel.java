package com.quantosauros.batch.model;

public class ProductOptionScheduleModel {

	private String optionTypeCd;
	private String optionStrtDt;
	private String optionEndDt;
	private String strike;
	
    public String getOptionTypeCd() {
        return optionTypeCd;
    }     
    public void setOptionTypeCd(String optionTypeCd) {
        this.optionTypeCd = optionTypeCd;
    }    
    public String getOptionStrtDt(){
    	return optionStrtDt;
    }    
    public void setOptionStrtDt(String optionStrtDt) {
        this.optionStrtDt = optionStrtDt;
    }    
    public String getOptionEndDt(){
    	return optionEndDt;
    }
    public void setOptionEndDt(String optionEndDt) {
        this.optionEndDt = optionEndDt;
    }
    public String getStrike(){
    	return strike;
    }    
    public void setStrike(String strike) {
        this.strike = strike;
    }
    
    
}
