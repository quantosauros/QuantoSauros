package com.quantosauros.manager.dao.products;

import java.util.List;
import java.util.Map;

import com.quantosauros.manager.model.products.ProductInfo;

public interface ProductInfoDao {
	
	void insertProductInfo(ProductInfo productInfo);
	void insertProductInfo(Map params);
}
