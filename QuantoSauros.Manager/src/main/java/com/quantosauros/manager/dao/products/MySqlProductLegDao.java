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
			Map params = new HashMap<String, String>();
			params.put("instrumentCd", productLegModel.getInstrumentCd());
			params.put("payRcvCd", productLegModel.getPayRcvCd());
			params.put("TypeCd", productLegModel.getLegTypeCd());
			params.put("UndTypeCd", productLegModel.getUnderlyingType());
			params.put("CondiTypeCd", productLegModel.getConditionType());
			params.put("Notional", productLegModel.getNotionalPrincipal());
			params.put("CcyCd", productLegModel.getCcyCd());
			params.put("DCF", productLegModel.getDayCountConvention());
			params.put("CapFloorCd", productLegModel.getCapFloorCd());
			params.put("IrCd1", productLegModel.getCouponIrcCd1());
			params.put("IrTenor1", productLegModel.getCouponIrcMrtyCd1());
			params.put("IrTypeCd1", productLegModel.getCouponIrcTypeCd1());
			params.put("IrCouponFreq1", productLegModel.getCouponIrcCouponFreqCd1());
			params.put("IrCd2", productLegModel.getCouponIrcCd2());
			params.put("IrTenor2", productLegModel.getCouponIrcMrtyCd2());
			params.put("IrTypeCd2", productLegModel.getCouponIrcTypeCd2());
			params.put("IrCouponFreq2", productLegModel.getCouponIrcCouponFreqCd2());
			params.put("IrCd3", productLegModel.getCouponIrcCd3());
			params.put("IrTenor3", productLegModel.getCouponIrcMrtyCd3());
			params.put("IrTypeCd3", productLegModel.getCouponIrcTypeCd3());
			params.put("IrCouponFreq3", productLegModel.getCouponIrcCouponFreqCd3());
			
			sqlSession.insert("com.quantosauros.manager.dao.ProductLeg.insertProductLeg", params);
			
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
