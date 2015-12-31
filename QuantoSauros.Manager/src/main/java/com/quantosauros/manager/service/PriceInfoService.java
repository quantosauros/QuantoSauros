package com.quantosauros.manager.service;

import java.util.List;

import com.quantosauros.manager.model.results.PriceInfo;

public interface PriceInfoService {

	List<PriceInfo> selectAllList(String procId);
	List<PriceInfo> selectListForChart(String procId, 
			String nonCallCd, String instrumentCd, String startDt, String endDt);
	
}
