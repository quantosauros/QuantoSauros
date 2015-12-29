package com.quantosauros.batch.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlProcPriceDataDao implements ProcPriceDataDao {

	SqlSessionFactory sqlsessionFactory;
	public MySqlProcPriceDataDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
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
}
