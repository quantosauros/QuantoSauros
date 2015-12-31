package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.ProductInfo;

@Component("productInfoDao")
public class MySqlProductInfoDao implements ProductInfoDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductInfo(ProductInfo productInfo) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Map params = new HashMap<String, String>();
			params.put("productCd", productInfo.getInstrumentCd());
			params.put("issueDt", productInfo.getIssueDt());
			params.put("mrtyDt", productInfo.getMrtyDt());
			params.put("productCcyCd", productInfo.getCcyCd());			
			params.put("optionTypeCd", productInfo.getOptionTypeCd());
			params.put("principalExchCd", productInfo.getPrincipalExchCd());
			
			sqlSession.insert("com.quantosauros.manager.dao.ProductInfo.insertProductInfo", params);
			
		} finally {
			sqlSession.close();
		}
	}
	public void insertProductInfo(Map params) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.insert("com.quantosauros.manager.dao.ProductInfo.insertProductInfo", params);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}

}
