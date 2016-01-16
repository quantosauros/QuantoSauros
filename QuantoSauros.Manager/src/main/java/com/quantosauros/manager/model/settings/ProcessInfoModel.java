package com.quantosauros.manager.model.settings;

public class ProcessInfoModel {

	private String procId;
	private String procNM;
	private String description;	
	private String scenarioId;
	private String scenarioNM;
	private String portfolioId;
	private String portfolioNM;
	private String crtnTime;

	public boolean isNew() {
		return (this.procId == null);
	}	
	public String getProcId(){
		return procId;
	}
	public void setProcId(String procId){
		this.procId = procId;
	}
	public String getProcNM(){
		return procNM;
	}
	public void setProcNM(String procNM){
		this.procNM = procNM;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getScenarioId(){
		return scenarioId;
	}
	public void setScenarioId(String scenarioId){
		this.scenarioId = scenarioId;
	}
	public String getScenarioNM(){
		return scenarioNM;
	}
	public void setScenarioNM(String scenarioNM){
		this.scenarioNM = scenarioNM;
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
	public String getCrtnTime(){
		return crtnTime;
	}
	public void setCrtnTime(String crtnTime){
		this.crtnTime = crtnTime;
	}	
}
