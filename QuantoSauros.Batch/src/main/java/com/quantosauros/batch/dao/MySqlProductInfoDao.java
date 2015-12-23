package com.quantosauros.batch.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.ProductInfoModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlProductInfoDao implements ProductInfoDao {

	SqlSessionFactory sqlsessionFactory;
	
	public MySqlProductInfoDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	public ProductInfoModel selectProductInfo(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectOne("ProductInfo.getProductInfo", paramMap);
		} finally {
			sqlSession.close();
		}	
	}
}
