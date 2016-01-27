package com.quantosauros.manager.dao.products;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.products.ProductScheduleModel;

@Component("productScheduleDao")
public class MySqlProductScheduleDao implements ProductScheduleDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductSchedule(ProductScheduleModel productScheduleModel){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.insert(
					"com.quantosauros.manager.dao.ProductSchedule.insertProductSchedule",
					productScheduleModel);
			
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertProductSchedule(Map params){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.insert(
					"com.quantosauros.manager.dao.ProductSchedule.insertProductSchedule",
					params);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
}
