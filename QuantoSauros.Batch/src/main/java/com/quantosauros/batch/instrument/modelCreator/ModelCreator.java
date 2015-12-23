package com.quantosauros.batch.instrument.modelCreator;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.dao.MySqlProductInfoDao;
import com.quantosauros.batch.dao.MySqlProductLegDao;
import com.quantosauros.batch.dao.MySqlProductLegDataDao;
import com.quantosauros.batch.dao.MySqlProductOptionScheduleDao;
import com.quantosauros.batch.dao.MySqlProductScheduleDao;
import com.quantosauros.batch.dao.ProductInfoDao;
import com.quantosauros.batch.dao.ProductLegDao;
import com.quantosauros.batch.dao.ProductLegDataDao;
import com.quantosauros.batch.dao.ProductOptionScheduleDao;
import com.quantosauros.batch.dao.ProductScheduleDao;
import com.quantosauros.batch.model.ProductInfoModel;
import com.quantosauros.batch.model.ProductLegDataModel;
import com.quantosauros.batch.model.ProductLegModel;

public class ModelCreator {

	public static ProductInfoModel getProductInfoModel(String instrumentCd){
		ProductInfoDao productInfoDao = new MySqlProductInfoDao();
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		ProductInfoModel productInfoModel = productInfoDao.selectProductInfo(paramMap);
		
		return productInfoModel;
	}
	
	public static ProductLegModel getProductLegModel(
			String instrumentCd, String payRcvCd){
		ProductLegDao productLegDao = new MySqlProductLegDao();
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);        		
		ProductLegModel productLegModel = 
				productLegDao.selectProductLegInfo(paramMap);
		
		return productLegModel;
	}
	
	public static ProductLegDataModel getProductLegDataModel(String instrumentCd,
			String payRcvCd, String asOfDate){
		ProductLegDataDao productLegDataDao = new MySqlProductLegDataDao();
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);
		paramMap.put("dt", asOfDate);   
		ProductLegDataModel productLegDataModel = 
				productLegDataDao.selectProductLegData(paramMap);
		
		return productLegDataModel;
	}
	
	public static List getProductScheduleModel(String instrumentCd, 
			String payRcvCd){
		ProductScheduleDao productScheduleDao = new MySqlProductScheduleDao();
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);		
		List productScheduleModel = productScheduleDao.selectProductSchedule(paramMap);
		
		return productScheduleModel;
	}
	
	public static List getProductOptionScheduleModel(String instrumentCd){
		ProductOptionScheduleDao productOptionScheduleDao = 
				new MySqlProductOptionScheduleDao();
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		List productOptionScheduleModel = 
				productOptionScheduleDao.selectProductOptionSchedule(paramMap);
		
		return productOptionScheduleModel;
	}
	
}
