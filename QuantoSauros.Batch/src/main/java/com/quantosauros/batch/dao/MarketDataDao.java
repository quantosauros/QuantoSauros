package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quantosauros.batch.model.IrCurveModel;

public interface MarketDataDao {

	List<IrCurveModel> selectIrCurveModel(HashMap<String, Object> paramMap);
	Map<String, Object> selectSTLLegFxRate(HashMap<String, Object> paramMap);
	Map<String, Object> selectHWVolatility(HashMap<String, Object> paramMap);
	Map<String, Object> selectIRCCodesofProduct(HashMap<String, Object> paramMap);
	List<String> selectIRCCode(HashMap<String, Object> paramMap);
	String selectIRCCodeFromMarketDataMap(HashMap<String, Object> paramMap);
	String selectCcyCodeFromIrcCd(HashMap<String, Object> paramMap);
}
