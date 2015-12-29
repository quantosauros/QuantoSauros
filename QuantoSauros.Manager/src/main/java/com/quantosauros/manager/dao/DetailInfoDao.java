package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.manager.model.DetailInfo;

public interface DetailInfoDao {	
	
	List<DetailInfo> selectList(HashMap<String,Object> paramMap);
	
}
