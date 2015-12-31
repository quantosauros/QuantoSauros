package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductInfo;

public interface ProductInfoDao {
	
	void insertProductInfo(ProductInfo productInfo) throws Exception;
	void insertProductInfo(Map params) throws Exception;
}
