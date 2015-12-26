package com.quantosauros.manager.dao;

import java.util.Map;

import com.quantosauros.manager.model.ProductLeg;

public interface ProductLegDao {

	void insertProductLeg(ProductLeg productLeg) throws Exception;
	void insertProductLeg(Map params) throws Exception;
	
}
