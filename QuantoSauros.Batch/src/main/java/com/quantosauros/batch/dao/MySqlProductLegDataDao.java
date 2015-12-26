package com.quantosauros.batch.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.ProductLegDataModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlProductLegDataDao implements ProductLegDataDao {
	SqlSessionFactory sqlsessionFactory;
	public MySqlProductLegDataDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	
	public ProductLegDataModel selectProductLegData(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();
		try {
			return sqlSession.selectOne("ProductLegData.getProductLegData", paramMap);
		} finally {
			sqlSession.close();
		}		
	}
	public void insertProductLegData(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();
		try {
			sqlSession.insert("ProductLegData.insertProductLegData", paramMap);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}	
	}
}
