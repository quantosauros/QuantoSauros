package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.manager.model.PriceInfo;

public interface PriceInfoDao {

	List<PriceInfo> selectAllList(HashMap<String, Object> paramMap) throws Exception;
	List<PriceInfo> selectList(HashMap<String,Object> paramMap) throws Exception;
	PriceInfo selectOne(HashMap<String, Object> paramMap) throws Exception;
	
}
