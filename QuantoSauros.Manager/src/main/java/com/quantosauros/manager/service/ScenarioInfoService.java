package com.quantosauros.manager.service;

import java.util.List;

import com.quantosauros.manager.model.ScenarioInfo;

public interface ScenarioInfoService {

	List<ScenarioInfo> selectScenarioInfo();
	ScenarioInfo findById(String scenarioId);
	String getMaxId();
	void saveOrUpdate(ScenarioInfo scenarioInfo);	
	void delete(String scenarioId);
}
