package com.quantosauros.manager.model.pricer;

public class ProductInfoPricerModel {

	public String issueDt;
	public String mrtyDt;
	public String currency;
	public boolean hasPrincipalExchange;
	
	public String getIssueDt(){
		return issueDt;
	}
	public void setIssueDt(String issueDt){
		this.issueDt = issueDt;
	}
	public String getMrtyDt(){
		return mrtyDt;
	}
	public void setMrtyDt(String mrtyDt){
		this.mrtyDt = mrtyDt;				
	}
	public String getCurrency(){
		return currency;
	}
	public void setCurrency(String currency){
		this.currency = currency;
	}
	public boolean getHasPrincipalExchange(){
		return hasPrincipalExchange;
	}
	public void setHasPrincipalExchange(boolean hasPrincipalExchange){
		this.hasPrincipalExchange = hasPrincipalExchange;
	}
}
