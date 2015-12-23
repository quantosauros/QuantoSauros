package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.model.ProductInfoModel;

public interface ProductInfoDao {

	ProductInfoModel selectProductInfo(HashMap<String, Object> paramMap);
	
}
