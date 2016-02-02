package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.ProcessDataModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlProcPriceDataDao implements ProcPriceDataDao {

	SqlSessionFactory sqlsessionFactory;
	public MySqlProcPriceDataDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	public List<ProcessDataModel> selectProcessData(String processDate, String procId){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("dt", processDate);
			paramMap.put("procId", procId);
			return sqlSession.selectList("ProcPriceData.selectProcessData", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	
	public void insertPrice(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			sqlSession.insert("ProcPriceData.insertPrice", paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertDeltaGamma(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			sqlSession.insert("ProcPriceData.insertDeltaGamma", paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertDetailData(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			sqlSession.insert("ProcPriceData.insertDetailData", paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertPortfolioPriceData(String processDate, String procId){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("dt", processDate);
			paramMap.put("procId", procId);
			sqlSession.insert("ProcPriceData.insertPortfolioPriceData", paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertPortfolioDeltaGammaData(String processDate, String procId){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("dt", processDate);
			paramMap.put("procId", procId);
			sqlSession.insert("ProcPriceData.insertPortfolioDeltaGammaData", paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
}
