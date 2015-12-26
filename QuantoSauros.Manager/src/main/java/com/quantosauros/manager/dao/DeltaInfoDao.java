package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.manager.model.DeltaInfo;

public interface DeltaInfoDao {
	List<DeltaInfo> selectAllList(HashMap<String, Object> paramMap) throws Exception;
	List<DeltaInfo> selectDeltaForChart(HashMap<String, Object> paramMap) throws Exception;
	
	List<DeltaInfo> selectList(HashMap<String,Object> paramMap) throws Exception;
	DeltaInfo selectOne(HashMap<String, Object> paramMap) throws Exception;	
}
