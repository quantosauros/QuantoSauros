package com.quantosauros.manager.dao.results;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.manager.model.results.PriceInfo;

public interface PriceInfoDao {

	List<PriceInfo> selectAllList(HashMap<String, Object> paramMap);
	List<PriceInfo> selectListForChart(HashMap<String,Object> paramMap);
	
}
