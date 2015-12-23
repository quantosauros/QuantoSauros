package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.InstrumentInfoModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlInstrumentInfoDao implements InstrumentInfoDao {
	
	SqlSessionFactory sqlsessionFactory;
	
	public MySqlInstrumentInfoDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	public List<InstrumentInfoModel> selectInstrumentInfo(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectList("InstrumentInfo.getInstrumentInfo", paramMap);
		} finally {
			sqlSession.close();
		}				
	}
	
	public List<InstrumentInfoModel> selectSpecificInstrumentInfo(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();
		try {							
			return sqlSession.selectList("InstrumentInfo.getInstrumentInfoSpeicificProduct", paramMap);
		} finally {
			sqlSession.close();
		}		
	}
	
}
