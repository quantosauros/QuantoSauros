package com.quantosauros.manager.dao;

import java.util.Map;

import com.quantosauros.manager.model.ProductOptionSchedule;

public interface ProductOptionScheduleDao {

	void insertProductOptionSchedule(ProductOptionSchedule productOptionSchedule) throws Exception;

	void insertProductOptionSchedule(Map params) throws Exception;

}
