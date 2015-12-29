package com.quantosauros.batch.dao;

import java.util.HashMap;

public interface ProcPriceDataDao {
	
	void insertPrice(HashMap<String, Object> paramMap);	
	void insertDeltaGamma(HashMap<String, Object> paramMap);
	void insertDetailData(HashMap<String, Object> paramMap);
	
}
