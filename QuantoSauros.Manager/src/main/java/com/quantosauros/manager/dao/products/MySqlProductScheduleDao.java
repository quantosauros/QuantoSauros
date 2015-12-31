package com.quantosauros.manager.dao.products;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.products.ProductSchedule;

@Component("productScheduleDao")
public class MySqlProductScheduleDao implements ProductScheduleDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductSchedule(ProductSchedule productSchedule) 
			throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Map params = new HashMap<String, String>();
			params.put("instrumentCd", productSchedule.getInstrumentCd());
			params.put("payRcvCd", productSchedule.getPayRcvCd());
			//TODO coupon reset date
			params.put("couponResetDt", productSchedule.getCouponStrtDt());
			params.put("startDate", productSchedule.getCouponStrtDt());			
			params.put("endDate", productSchedule.getCouponEndDt());
			params.put("paymentDate", productSchedule.getCouponPayDt());
			params.put("CouponType", productSchedule.getCouponType());
			params.put("principal", productSchedule.getPrincipal());
			params.put("UpperLimit1", productSchedule.getUpperBound1());
			params.put("LowerLimit1", productSchedule.getLowerBound1());
			params.put("Leverage1", productSchedule.getLeverage1());
			params.put("UpperLimit2", productSchedule.getUpperBound2());
			params.put("LowerLimit2", productSchedule.getLowerBound2());
			params.put("Leverage2", productSchedule.getLeverage2());
			params.put("Leverage3", productSchedule.getLeverage3());
			params.put("InCouponRate", productSchedule.getInCoupon());
			params.put("OutCouponRate", productSchedule.getOutCoupon());
			params.put("Spread", productSchedule.getFixedCoupon());
			params.put("cap", productSchedule.getCap());
			params.put("floor", productSchedule.getFloor());
						
			sqlSession.insert(
					"com.quantosauros.manager.dao.ProductSchedule.insertProductSchedule",
					params);
			
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
	}
	public void insertProductSchedule(Map params) throws Exception {
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
