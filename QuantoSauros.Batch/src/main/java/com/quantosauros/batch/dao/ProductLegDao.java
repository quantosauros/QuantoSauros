package com.quantosauros.batch.dao;

import java.util.HashMap;

import com.quantosauros.batch.model.ProductLegModel;

public interface ProductLegDao {

	ProductLegModel selectProductLegInfo(HashMap<String, Object> paramMap);
}
