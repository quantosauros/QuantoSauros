package com.quantosauros.batch.dao;

import java.util.HashMap;

public interface ProcPriceDataDao {

	String selectDataIdFromPriceByProcId(HashMap<String, Object> paramMap);
	String selectDataIdFromDeltaGammaByProcId(HashMap<String, Object> paramMap);
	void insertPriceInfo(HashMap<String, Object> paramMap);
	void insertDetailData(HashMap<String, Object> paramMap);
	void insertPrice(HashMap<String, Object> paramMap);
	
	void insertDeltaGammaInfo(HashMap<String, Object> paramMap);
	void insertDeltaGamma(HashMap<String, Object> paramMap);
}
