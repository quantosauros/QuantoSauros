package com.quantosauros.manager.dao.products;

import java.util.Map;

import com.quantosauros.manager.model.products.ProductInfoModel;

public interface ProductInfoDao {

	ProductInfoModel selectProductInfoByInstrumentCd(String instrumentCd);
	void insertProductInfo(ProductInfoModel productInfoModel);
	void insertProductInfo(Map params);
}
