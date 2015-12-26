package com.quantosauros.manager.dao;

import java.util.Map;

import com.quantosauros.manager.model.ProductInfo;

public interface ProductInfoDao {
	
	void insertProductInfo(ProductInfo productInfo) throws Exception;
	void insertProductInfo(Map params) throws Exception;
}
