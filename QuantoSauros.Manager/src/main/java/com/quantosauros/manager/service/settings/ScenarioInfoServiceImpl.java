package com.quantosauros.manager.service.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.settings.ScenarioInfoDao;
import com.quantosauros.manager.model.settings.ScenarioInfo;

@Service("scenarioInfoService")
public class ScenarioInfoServiceImpl implements ScenarioInfoService {

	ScenarioInfoDao scenarioInfoDao;
	
	@Autowired
	public void setScenarioInfoDao(ScenarioInfoDao scenarioInfoDao){
		this.scenarioInfoDao = scenarioInfoDao;
	}
	
	@Override
	public List<ScenarioInfo> selectScenarioInfo() {
		// TODO Auto-generated method stub
		return scenarioInfoDao.getLists();
	}
	
	@Override
	public ScenarioInfo findById(String scenarioId) {
		// TODO Auto-generated method stub
		return scenarioInfoDao.getScenarioInfoById(scenarioId);
	}
	
	@Override
	public String getMaxId() {
		// TODO Auto-generated method stub
		return scenarioInfoDao.getMaxScenarioId();
	}
	
	@Override
	public void saveOrUpdate(ScenarioInfo scenarioInfo) {
		if (findById(scenarioInfo.getScenarioId()) == null){
			scenarioInfo.setScenarioId(scenarioInfoDao.getMaxScenarioId());			
			scenarioInfoDao.insertScenarioInfo(scenarioInfo);
		} else {
			scenarioInfoDao.updateScenarioInfo(scenarioInfo);
		}		
	}
	
	@Override
	public void delete(String scenarioId) {
		// TODO Auto-generated method stub
		scenarioInfoDao.deleteScenarioInfo(scenarioId);
	}
	
}
