package com.quantosauros.manager.dao.results;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.manager.model.results.DeltaInfo;

public interface DeltaInfoDao {
	
	List<DeltaInfo> selectAllList(HashMap<String, Object> paramMap);
	List<DeltaInfo> selectDeltaForChart(HashMap<String, Object> paramMap);
	List<DeltaInfo> selectDeltaForChart2(HashMap<String, Object> paramMap);	
	List<String> selectMrtyCd(HashMap<String, Object> paramMap);
	
}
