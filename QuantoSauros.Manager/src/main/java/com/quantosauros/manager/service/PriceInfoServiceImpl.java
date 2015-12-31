package com.quantosauros.manager.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.results.PriceInfoDao;
import com.quantosauros.manager.model.results.PriceInfo;

@Service("priceInfoService")
public class PriceInfoServiceImpl implements PriceInfoService {

	PriceInfoDao priceInfoDao;
	
	@Autowired
	public void setPriceInfoDao(PriceInfoDao priceInfoDao){
		this.priceInfoDao = priceInfoDao;
	}
	
	@Override
	public List<PriceInfo> selectAllList(String procId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("procId", procId);
		
		return priceInfoDao.selectAllList(paramMap);
	}
	@Override
	public List<PriceInfo> selectListForChart(String procId, String nonCallCd, 
			String instrumentCd, String startDt, String endDt) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("procId", procId);
		paramMap.put("nonCallCd", nonCallCd);
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("startDt", startDt);
		paramMap.put("endDt", endDt);

		return priceInfoDao.selectListForChart(paramMap);
	}
}
