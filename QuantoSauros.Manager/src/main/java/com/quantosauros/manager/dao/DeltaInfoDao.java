package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.manager.model.DeltaInfo;

public interface DeltaInfoDao {
	List<DeltaInfo> selectAllList(HashMap<String, Object> paramMap) throws Exception;
	List<DeltaInfo> selectDeltaForChart(HashMap<String, Object> paramMap) throws Exception;
	List<DeltaInfo> selectDeltaForChart2(HashMap<String, Object> paramMap) throws Exception;
	
	List<String> selectMrtyCd(HashMap<String, Object> paramMap) throws Exception;
}
