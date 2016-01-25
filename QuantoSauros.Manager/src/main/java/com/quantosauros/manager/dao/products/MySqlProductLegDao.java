package com.quantosauros.manager.dao.products;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.products.ProductLegModel;

@Component("productLegDao")
public class MySqlProductLegDao implements ProductLegDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductLeg(ProductLegModel productLegModel) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			
			sqlSession.insert("com.quantosauros.manager.dao.ProductLeg.insertProductLeg", productLegModel);
			
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertProductLeg(Map params) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.insert("com.quantosauros.manager.dao.ProductLeg.insertProductLeg", params);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
}
