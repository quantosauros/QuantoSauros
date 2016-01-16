package com.quantosauros.manager.dao.settings;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.settings.ScenarioInfoModel;

@Component("scenarioDao")
public class MySqlScenarioInfoDao implements ScenarioInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public List<ScenarioInfoModel> getLists() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectList("com.quantosauros.manager.dao.ScenarioInfo.selectScenarioInfo");
		} finally {
			sqlSession.close();
		}	
	}
	
	@Override
	public ScenarioInfoModel getScenarioInfoById(String scenarioId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("scenarioId", scenarioId);
			return sqlSession.selectOne("com.quantosauros.manager.dao.ScenarioInfo.findById", 
					paramMap);
		} finally {
			sqlSession.close();
		}
	}
	
	@Override
	public String getMaxScenarioId() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectOne("com.quantosauros.manager.dao.ScenarioInfo.maxScenarioId");
		} finally {
			sqlSession.close();
		}
	}
	
	@Override
	public void insertScenarioInfo(ScenarioInfoModel scenarioInfoModel) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();			
			paramMap.put("scenarioId", scenarioInfoModel.getScenarioId());
			paramMap.put("scenarioNM", scenarioInfoModel.getScenarioNM());			
			paramMap.put("description", scenarioInfoModel.getDescription());			
			
			sqlSession.insert("com.quantosauros.manager.dao.ScenarioInfo.insertScenarioInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}	
		
	}
	
	@Override
	public void updateScenarioInfo(ScenarioInfoModel scenarioInfoModel) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("scenarioId", scenarioInfoModel.getScenarioId());
			paramMap.put("scenarioNM", scenarioInfoModel.getScenarioNM());			
			paramMap.put("description", scenarioInfoModel.getDescription());	
			
			sqlSession.update("com.quantosauros.manager.dao.ScenarioInfo.updateScenarioInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}			
	}
	
	@Override
	public void deleteScenarioInfo(String scenarioId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("scenarioId", scenarioId);
			sqlSession.delete("com.quantosauros.manager.dao.ScenarioInfo.deleteScenarioInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}	
		
	}
}
