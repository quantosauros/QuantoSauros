package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductLegModel;

public interface ProductLegDao {

	void insertProductLeg(ProductLegModel productLegModel);
	void insertProductLeg(Map params);
	
}
