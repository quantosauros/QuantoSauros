package com.quantosauros.manager.dao.products;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.products.ProductLeg;

@Component("productLegDao")
public class MySqlProductLegDao implements ProductLegDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductLeg(ProductLeg productLeg) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Map params = new HashMap<String, String>();
			params.put("instrumentCd", productLeg.getInstrumentCd());
			params.put("payRcvCd", productLeg.getPayRcvCd());
			params.put("TypeCd", productLeg.getLegTypeCd());
			params.put("UndTypeCd", productLeg.getUnderlyingType());
			params.put("CondiTypeCd", productLeg.getConditionType());
			params.put("Notional", productLeg.getNotionalPrincipal());
			params.put("CcyCd", productLeg.getCcyCd());
			params.put("DCF", productLeg.getDayCountConvention());
			params.put("CapFloorCd", productLeg.getCapFloorCd());
			params.put("IrCd1", productLeg.getCouponIrcCd1());
			params.put("IrTenor1", productLeg.getCouponIrcMrtyCd1());
			params.put("IrTypeCd1", productLeg.getCouponIrcTypeCd1());
			params.put("IrCouponFreq1", productLeg.getCouponIrcCouponFreqCd1());
			params.put("IrCd2", productLeg.getCouponIrcCd2());
			params.put("IrTenor2", productLeg.getCouponIrcMrtyCd2());
			params.put("IrTypeCd2", productLeg.getCouponIrcTypeCd2());
			params.put("IrCouponFreq2", productLeg.getCouponIrcCouponFreqCd2());
			params.put("IrCd3", productLeg.getCouponIrcCd3());
			params.put("IrTenor3", productLeg.getCouponIrcMrtyCd3());
			params.put("IrTypeCd3", productLeg.getCouponIrcTypeCd3());
			params.put("IrCouponFreq3", productLeg.getCouponIrcCouponFreqCd3());
			
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
