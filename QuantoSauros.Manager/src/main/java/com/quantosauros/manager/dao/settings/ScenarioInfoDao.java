package com.quantosauros.manager.dao.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.ScenarioInfo;

public interface ScenarioInfoDao {

	List<ScenarioInfo> getLists();
	ScenarioInfo getScenarioInfoById(String scenarioId);
	String getMaxScenarioId();
	void insertScenarioInfo(ScenarioInfo scenarioInfo);
	void updateScenarioInfo(ScenarioInfo scenarioInfo);
	void deleteScenarioInfo(String scenarioId);
	
}
