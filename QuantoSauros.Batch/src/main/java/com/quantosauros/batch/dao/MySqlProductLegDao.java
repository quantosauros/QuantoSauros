package com.quantosauros.batch.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.ProductLegModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlProductLegDao implements ProductLegDao {

	SqlSessionFactory sqlsessionFactory;
	
	public MySqlProductLegDao(){
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	public ProductLegModel selectProductLegInfo(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("ProductLegInfo.getProductLeg", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	
}
