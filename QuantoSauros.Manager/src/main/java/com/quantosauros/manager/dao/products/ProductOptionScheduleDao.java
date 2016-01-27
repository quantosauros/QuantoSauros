package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductOptionScheduleModel;

public interface ProductOptionScheduleDao {

	void insertProductOptionSchedule(ProductOptionScheduleModel productOptionScheduleModel);

	void insertProductOptionSchedule(Map params);

}
