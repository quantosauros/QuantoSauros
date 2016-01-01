package com.quantosauros.manager.model.settings;

public class PortfolioInfo {

	private String portfolioId;
	private String portfolioNM;
	private String description;	
	private String crtnTime;
	
	public boolean isNew() {
		return (this.portfolioId == null);
	}	
	public String getPortfolioId(){
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId){
		this.portfolioId = portfolioId;
	}
	public String getPortfolioNM(){
		return portfolioNM;
	}
	public void setPortfolioNM(String portfolioNM){
		this.portfolioNM = portfolioNM;
	}	
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getCrtnTime(){
		return crtnTime;
	}
	public void setCrtnTime(String crtnTime){
		this.crtnTime = crtnTime;
	}	
}
