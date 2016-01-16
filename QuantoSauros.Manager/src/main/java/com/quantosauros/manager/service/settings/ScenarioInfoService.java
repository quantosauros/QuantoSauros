package com.quantosauros.manager.service.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.ScenarioInfoModel;

public interface ScenarioInfoService {

	List<ScenarioInfoModel> selectScenarioInfo();
	ScenarioInfoModel findById(String scenarioId);
	String getMaxId();
	void saveOrUpdate(ScenarioInfoModel scenarioInfo);	
	void delete(String scenarioId);
}
