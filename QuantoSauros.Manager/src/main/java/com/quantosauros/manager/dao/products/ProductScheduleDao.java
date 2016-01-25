package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductScheduleModel;

public interface ProductScheduleDao {

	void insertProductSchedule(ProductScheduleModel productSchedule) throws Exception;
	void insertProductSchedule(Map params) throws Exception;
	
}
