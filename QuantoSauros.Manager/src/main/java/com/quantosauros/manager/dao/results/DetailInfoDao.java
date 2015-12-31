package com.quantosauros.manager.dao.results;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.manager.model.results.DetailInfo;

public interface DetailInfoDao {	
	
	List<DetailInfo> selectList(HashMap<String,Object> paramMap);
	
}
