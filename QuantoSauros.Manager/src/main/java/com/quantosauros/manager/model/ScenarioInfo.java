package com.quantosauros.manager.model;

public class ScenarioInfo {

	private String scenarioId;
	private String scenarioNM;
	private String description;
	private String crtnTime;
	
	public boolean isNew() {
		return (this.scenarioId == null);
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
