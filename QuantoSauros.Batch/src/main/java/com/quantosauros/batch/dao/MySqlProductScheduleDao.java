package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.ProductScheduleModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlProductScheduleDao implements ProductScheduleDao {

	SqlSessionFactory sqlsessionFactory;
	
	public MySqlProductScheduleDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	public List<ProductScheduleModel> selectProductSchedule(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectList("ProductSchedule.getProductSchedule", paramMap);
		} finally {
			sqlSession.close();
		}		
	}
}
