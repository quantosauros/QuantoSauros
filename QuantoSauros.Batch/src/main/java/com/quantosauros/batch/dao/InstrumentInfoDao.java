package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.model.InstrumentInfoModel;

public interface InstrumentInfoDao {

	List<InstrumentInfoModel> selectInstrumentInfo(HashMap<String, Object> paramMap);
	List<InstrumentInfoModel> selectSpecificInstrumentInfo(HashMap<String, Object> paramMap);
	
}
