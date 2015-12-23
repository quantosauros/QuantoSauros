package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.quantosauros.batch.model.ProductOptionScheduleModel;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class MySqlProductOptionScheduleDao implements ProductOptionScheduleDao {

	SqlSessionFactory sqlsessionFactory;
	
	public MySqlProductOptionScheduleDao() {
		sqlsessionFactory = SqlMapClient.getSqlSessionFactory();
	}
	
	public List<ProductOptionScheduleModel> selectProductOptionSchedule(
			HashMap<String, Object> paramMap) {
		SqlSession sqlSession = sqlsessionFactory.openSession();		
		try {
			return sqlSession.selectList("ProductOptionSchedule.getProductOptionSchedule",
					paramMap);
		} finally {
			sqlSession.close();
		}		
	}

}
