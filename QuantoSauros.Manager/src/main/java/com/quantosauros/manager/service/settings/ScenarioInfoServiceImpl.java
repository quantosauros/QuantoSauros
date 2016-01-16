package com.quantosauros.manager.service.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.settings.ScenarioInfoDao;
import com.quantosauros.manager.model.settings.ScenarioInfoModel;

@Service("scenarioInfoService")
public class ScenarioInfoServiceImpl implements ScenarioInfoService {

	ScenarioInfoDao scenarioInfoDao;
	
	@Autowired
	public void setScenarioInfoDao(ScenarioInfoDao scenarioInfoDao){
		this.scenarioInfoDao = scenarioInfoDao;
	}
	
	@Override
	public List<ScenarioInfoModel> selectScenarioInfo() {
		// TODO Auto-generated method stub
		return scenarioInfoDao.getLists();
	}
	
	@Override
	public ScenarioInfoModel findById(String scenarioId) {
		// TODO Auto-generated method stub
		return scenarioInfoDao.getScenarioInfoById(scenarioId);
	}
	
	@Override
	public String getMaxId() {
		// TODO Auto-generated method stub
		return scenarioInfoDao.getMaxScenarioId();
	}
	
	@Override
	public void saveOrUpdate(ScenarioInfoModel scenarioInfoModel) {
		if (findById(scenarioInfoModel.getScenarioId()) == null){
			scenarioInfoModel.setScenarioId(scenarioInfoDao.getMaxScenarioId());			
			scenarioInfoDao.insertScenarioInfo(scenarioInfoModel);
		} else {
			scenarioInfoDao.updateScenarioInfo(scenarioInfoModel);
		}		
	}
	
	@Override
	public void delete(String scenarioId) {
		// TODO Auto-generated method stub
		scenarioInfoDao.deleteScenarioInfo(scenarioId);
	}
	
}
