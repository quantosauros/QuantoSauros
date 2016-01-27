package com.quantosauros.manager.dao.products;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.products.ProductOptionScheduleModel;

@Component("productOptionScheduleDao")
public class MySqlProductOptionScheduleDao implements ProductOptionScheduleDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductOptionSchedule(ProductOptionScheduleModel productOptionScheduleModel){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Map params = new HashMap<String, String>();
			params.put("instrumentCd", productOptionScheduleModel.getInstrumentCd());
			params.put("exerciseTypeCd", productOptionScheduleModel.getOptionTypeCd());
			params.put("exerciseStrtDt", productOptionScheduleModel.getOptionStrtDt());
			params.put("exerciseEndDt", productOptionScheduleModel.getOptionEndDt());
			params.put("exerciseStrike", productOptionScheduleModel.getStrike());
						
			sqlSession.insert(
					"com.quantosauros.manager.dao.ProductOptionSchedule.insertProductOptionSchedule",
					params);
			
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	@Override
	public void insertProductOptionSchedule(Map params){
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
