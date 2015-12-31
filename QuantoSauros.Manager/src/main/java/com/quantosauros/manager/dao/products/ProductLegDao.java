package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductLeg;

public interface ProductLegDao {

	void insertProductLeg(ProductLeg productLeg) throws Exception;
	void insertProductLeg(Map params) throws Exception;
	
}
