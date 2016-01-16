package com.quantosauros.manager.dao.settings;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.settings.PortfolioDataModel;
import com.quantosauros.manager.model.settings.PortfolioInfoModel;

@Component("portfolioInfoDao")
public class MySqlPortfolioInfoDao implements PortfolioInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}	
	
	@Override
	public List<PortfolioInfoModel> getLists() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectList("com.quantosauros.manager.dao.PortfolioInfo.selectPortfolioInfo");
		} finally {
			sqlSession.close();
		}
	}
	@Override
	public PortfolioInfoModel getOneById(String portfolioId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("portfolioId", portfolioId);
			return sqlSession.selectOne("com.quantosauros.manager.dao.PortfolioInfo.getOneById", 
					paramMap);
		} finally {
			sqlSession.close();
		}
	}
	@Override
	public String getMaxPortfolioId() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectOne("com.quantosauros.manager.dao.PortfolioInfo.getMaxPortfolioId");
		} finally {
			sqlSession.close();
		}
	}
	@Override
	public void insertPortfolioInfo(PortfolioInfoModel portfolioInfoModel) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("portfolioId", portfolioInfoModel.getPortfolioId());			
			paramMap.put("portfolioNM", portfolioInfoModel.getPortfolioNM());
			paramMap.put("description", portfolioInfoModel.getDescription());			
			
			sqlSession.insert("com.quantosauros.manager.dao.PortfolioInfo.insertPortfolioInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}	
		
	}
	@Override
	public void updatePortfolioInfo(PortfolioInfoModel portfolioInfoModel) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("portfolioId", portfolioInfoModel.getPortfolioId());			
			paramMap.put("portfolioNM", portfolioInfoModel.getPortfolioNM());
			paramMap.put("description", portfolioInfoModel.getDescription());			
			
			sqlSession.update("com.quantosauros.manager.dao.PortfolioInfo.updatePortfolioInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}	
		
	}
	@Override
	public void deletePortfolioInfo(String portfolioId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("portfolioId", portfolioId);
			sqlSession.delete("com.quantosauros.manager.dao.PortfolioInfo.deletePortfolioInfo",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}		
	}
	
	@Override
	public List<PortfolioDataModel> getDataListsById(String portfolioId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {		
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("portfolioId", portfolioId);
			return sqlSession.selectList("com.quantosauros.manager.dao.PortfolioData.selectPortfolioData", 
					paramMap);
		} finally {
			sqlSession.close();
		}
	}
		
	@Override
	public void insertPortfolioData(PortfolioDataModel portfolioDataModel) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("portfolioId", portfolioDataModel.getPortfolioId());			
			paramMap.put("instrumentCd", portfolioDataModel.getInstrumentCd());						
			
			sqlSession.insert("com.quantosauros.manager.dao.PortfolioData.insertPortfolioData",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	@Override
	public void deletePortfolioData(String portfolioId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("portfolioId", portfolioId);			
			sqlSession.delete("com.quantosauros.manager.dao.PortfolioData.deletePortfolioData",
					paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}		
	}	
}
