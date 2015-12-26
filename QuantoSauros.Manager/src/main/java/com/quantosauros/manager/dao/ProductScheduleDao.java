package com.quantosauros.manager.dao;

import java.util.Map;

import com.quantosauros.manager.model.ProductSchedule;

public interface ProductScheduleDao {

	void insertProductSchedule(ProductSchedule productSchedule) throws Exception;
	void insertProductSchedule(Map params) throws Exception;
	
}
