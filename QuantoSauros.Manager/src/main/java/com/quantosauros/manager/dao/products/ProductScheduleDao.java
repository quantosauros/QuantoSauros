package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductSchedule;

public interface ProductScheduleDao {

	void insertProductSchedule(ProductSchedule productSchedule) throws Exception;
	void insertProductSchedule(Map params) throws Exception;
	
}
