package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.ScenarioInfo;

@Component("scenarioDao")
public class MySqlScenarioInfoDao implements ScenarioInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public List<ScenarioInfo> getLists() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectList("com.quantosauros.manager.dao.ScenarioInfo.selectScenarioInfo");
		} finally {
			sqlSession.close();
		}	
	}
	
	@Override
	public ScenarioInfo getScenarioInfoById(String scenarioId) {
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
	public void insertScenarioInfo(ScenarioInfo scenarioInfo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();			
			paramMap.put("scenarioId", scenarioInfo.getScenarioId());
			paramMap.put("scenarioNM", scenarioInfo.getScenarioNM());			
			paramMap.put("description", scenarioInfo.getDescription());			
			
			sqlSession.insert("com.quantosauros.manager.dao.ScenarioInfo.insertScenarioInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}	
		
	}
	
	@Override
	public void updateScenarioInfo(ScenarioInfo scenarioInfo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("scenarioId", scenarioInfo.getScenarioId());
			paramMap.put("scenarioNM", scenarioInfo.getScenarioNM());			
			paramMap.put("description", scenarioInfo.getDescription());	
			
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
