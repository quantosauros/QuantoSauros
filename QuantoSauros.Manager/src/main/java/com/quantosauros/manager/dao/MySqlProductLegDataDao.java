package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.ProductLegData;

@Component("productLegDataDao")
public class MySqlProductLegDataDao implements ProductLegDataDao{	
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void insertProductLegData(ProductLegData productLegData) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Map params = new HashMap<String, String>();
			params.put("dt", productLegData.getDt());
			params.put("instrumentCd", productLegData.getInstrumentCd());
			params.put("payRcvCd", productLegData.getPayRcvCd());
			params.put("nextCouponPayDt", productLegData.getNextCouponPayDt());
			params.put("nextCoupon", productLegData.getNextCoupon());
			params.put("accrualDayCnt", productLegData.getAccrualDayCnt());
			params.put("accumulateAvgCoupon", productLegData.getAccumulateAvgCoupon());
						
			sqlSession.insert("com.quantosauros.manager.dao.ProductLegData.insertProductLegData", params);
			
		} finally {
			sqlSession.close();
		}
	}

}
