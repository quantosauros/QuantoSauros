package com.quantosauros.batch.instrument.daoCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.dao.ProductInfoDao;
import com.quantosauros.batch.dao.ProductLegDao;
import com.quantosauros.batch.dao.ProductLegDataDao;
import com.quantosauros.batch.mybatis.SqlMapClient;

public class AbstractDaoCreator {

	private static SqlSession _session = SqlMapClient.getSqlSession();
		
	public static ProductInfoDao getProductInfoDao(String instrumentCd){
		Map paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		ProductInfoDao productInfo = (ProductInfoDao) _session.selectOne(
				"DBCommon.getProductInfo", paramMap);
		
		return productInfo;
	}
	
	public static ProductLegDao getProductLegDao(String instrumentCd, String payRcvCd){
		Map paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);        		
		ProductLegDao productLegDao = (ProductLegDao) _session.selectOne(
				"DBCommon.getProductLeg", paramMap);
		
		return productLegDao;
	}
	
	public static ProductLegDataDao getProductLegDataDao(String instrumentCd,
			String payRcvCd, String asOfDate){
		Map paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);
		paramMap.put("dt", asOfDate);   
		ProductLegDataDao productLegDataDao = (ProductLegDataDao) 
				_session.selectOne("DBCommon.getProductLegData", paramMap);
		
		return productLegDataDao;
	}
	
	public static List getProductScheduleDao(String instrumentCd, 
			String payRcvCd){
		Map paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);
		
		List productScheduleDao = _session.selectList(
				"DBCommon.getProductSchedule", paramMap);
		
		return productScheduleDao;
	}
	
	public static List getProductOptionScheduleDao(String instrumentCd){
		Map paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		List productOptionScheduleDao = _session.selectList(
				"DBCommon.getProductOptionSchedule", paramMap);
		
		return productOptionScheduleDao;
	}
	
}
