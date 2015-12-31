package com.quantosauros.manager.dao.settings;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.settings.ProcessInfo;

@Component("processInfoDao")
public class MySqlProcessInfoDao implements ProcessInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
		
	@Override
	public List<ProcessInfo> selectProcessInfo() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectList("com.quantosauros.manager.dao.ProcessInfo.selectProcessInfo");
		} finally {
			sqlSession.close();
		}
	}
	public ProcessInfo findByProcId(String procId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("procId", procId);
			return sqlSession.selectOne("com.quantosauros.manager.dao.ProcessInfo.findByProcId", 
					paramMap);
		} finally {
			sqlSession.close();
		}
	}
	public String getMaxProcId(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectOne("com.quantosauros.manager.dao.ProcessInfo.maxProcId");
		} finally {
			sqlSession.close();
		}
	}
	@Override
	public void insertProcessInfo(ProcessInfo processInfo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("procId", processInfo.getProcId());
			paramMap.put("scenarioId", processInfo.getScenarioId());
			paramMap.put("portfolioId", processInfo.getPortfolioId());
			paramMap.put("procNM", processInfo.getProcNM());
			paramMap.put("description", processInfo.getDescription());			
			
			sqlSession.insert("com.quantosauros.manager.dao.ProcessInfo.insertProcessInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}		
	}
	@Override
	public void updateProcessInfo(ProcessInfo processInfo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("procId", processInfo.getProcId());
			paramMap.put("scenarioId", processInfo.getScenarioId());
			paramMap.put("portfolioId", processInfo.getPortfolioId());
			paramMap.put("procNM", processInfo.getProcNM());
			paramMap.put("description", processInfo.getDescription());			
			
			sqlSession.update("com.quantosauros.manager.dao.ProcessInfo.updateProcessInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}			
	}
	
	public void deleteProcessInfo(String procId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("procId", procId);
			sqlSession.delete("com.quantosauros.manager.dao.ProcessInfo.deleteProcessInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}		
	}
	
}
