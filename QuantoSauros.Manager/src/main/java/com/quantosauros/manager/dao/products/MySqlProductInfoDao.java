package com.quantosauros.manager.dao.products;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.products.ProductInfoModel;

@Component("productInfoDao")
public class MySqlProductInfoDao implements ProductInfoDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public ProductInfoModel selectProductInfoByInstrumentCd(String instrumentCd){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectOne("com.quantosauros.manager.dao.ProductInfo.getProductInfoByInstrumentCd", 
					instrumentCd);
		} finally {
			sqlSession.close();
		}
	}
	
	@Override
	public void insertProductInfo(ProductInfoModel productInfoModel){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
//			Map params = new HashMap<String, String>();
//			params.put("productCd", productInfoModel.getInstrumentCd());
//			params.put("issueDt", productInfoModel.getIssueDt());
//			params.put("mrtyDt", productInfoModel.getMrtyDt());
//			params.put("productCcyCd", productInfoModel.getCcyCd());			
//			params.put("optionTypeCd", productInfoModel.getOptionTypeCd());
//			params.put("principalExchCd", productInfoModel.getPrincipalExchCd());
//			
//			sqlSession.insert("com.quantosauros.manager.dao.ProductInfo.insertProductInfo", params);
			
			sqlSession.insert("com.quantosauros.manager.dao.ProductInfo.insertProductInfo", productInfoModel);
			
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertProductInfo(Map params){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.insert("com.quantosauros.manager.dao.ProductInfo.insertProductInfo", params);
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}

}
