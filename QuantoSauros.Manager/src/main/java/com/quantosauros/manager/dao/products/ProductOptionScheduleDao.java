package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductOptionSchedule;

public interface ProductOptionScheduleDao {

	void insertProductOptionSchedule(ProductOptionSchedule productOptionSchedule) throws Exception;

	void insertProductOptionSchedule(Map params) throws Exception;

}
