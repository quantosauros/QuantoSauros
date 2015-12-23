package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.model.ProductLegDataModel;

public interface ProductLegDataDao {

	ProductLegDataModel selectProductLegData(HashMap<String, Object> paramMap);
	
}
