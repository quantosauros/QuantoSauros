package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.ProductOptionSchedule;

@Component("productOptionScheduleDao")
public class MySqlProductOptionScheduleDao implements ProductOptionScheduleDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductOptionSchedule(ProductOptionSchedule productOptionSchedule) 
			throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Map params = new HashMap<String, String>();
			params.put("instrumentCd", productOptionSchedule.getInstrumentCd());
			params.put("exerciseTypeCd", productOptionSchedule.getOptionTypeCd());
			params.put("exerciseStrtDt", productOptionSchedule.getOptionStrtDt());
			params.put("exerciseEndDt", productOptionSchedule.getOptionEndDt());
			params.put("exerciseStrike", productOptionSchedule.getStrike());
						
			sqlSession.insert(
					"com.quantosauros.manager.dao.ProductOptionSchedule.insertProductOptionSchedule",
					params);
			
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	@Override
	public void insertProductOptionSchedule(Map params) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.insert(
					"com.quantosauros.manager.dao.ProductOptionSchedule.insertProductOptionSchedule",
					params);			
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
}
