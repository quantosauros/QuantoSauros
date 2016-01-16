package com.quantosauros.manager.dao.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.ScenarioInfoModel;

public interface ScenarioInfoDao {

	List<ScenarioInfoModel> getLists();
	ScenarioInfoModel getScenarioInfoById(String scenarioId);
	String getMaxScenarioId();
	void insertScenarioInfo(ScenarioInfoModel scenarioInfo);
	void updateScenarioInfo(ScenarioInfoModel scenarioInfo);
	void deleteScenarioInfo(String scenarioId);
	
}
