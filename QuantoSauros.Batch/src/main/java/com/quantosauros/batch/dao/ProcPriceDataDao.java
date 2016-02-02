package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.model.ProcessDataModel;

public interface ProcPriceDataDao {
	
	List<ProcessDataModel> selectProcessData(String processDate, String procId);
	void insertPrice(HashMap<String, Object> paramMap);	
	void insertDeltaGamma(HashMap<String, Object> paramMap);
	void insertDetailData(HashMap<String, Object> paramMap);
	
	void insertPortfolioPriceData(String processDate, String procId);
	void insertPortfolioDeltaGammaData(String processDate, String procId);
}
