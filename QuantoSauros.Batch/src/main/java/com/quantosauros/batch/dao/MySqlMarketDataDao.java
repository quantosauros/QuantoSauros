package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.IrCurveModel;
import com.quantosauros.batch.model.VolSurfModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlMarketDataDao implements MarketDataDao {

	SqlSessionFactory sqlsessionFactory;
	
	public MySqlMarketDataDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	public List<IrCurveModel> selectIrCurveModel(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectList("MarketData.getIrCurveFromIrcCd", paramMap);
		} finally {
			sqlSession.close();
		}	
	}
	public List<VolSurfModel> selectVolSurfModel(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectList("MarketData.getVolatilitySurfaceData", paramMap);
		} finally {
			sqlSession.close();
		}	
	}
	
	public Map<String, Object> selectSTLLegFxRate(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("MarketData.getStlLegFXRate", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	public Map<String, Object> selectHWVolatility(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("MarketData.getHullWhiteVolatility", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	public Map<String, Object> selectIRCCodesofProduct(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("MarketData.getIrcCodesofProduct", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	public List<String> selectIRCCode(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectList("MarketData.getIrcCd", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	public String selectIRCCodeFromMarketDataMap(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("MarketData.getIrcCdFromMarketDataMap", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	public String selectCcyCodeFromIrcCd(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("MarketData.getCcyCodeFromIrcCd", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	public String selectRiskFreeIrcCdFromCcyCd(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("MarketData.getRiskFreeIrcCdFromCcyCd", paramMap);
		} finally {
			sqlSession.close();
		}
	}
}
