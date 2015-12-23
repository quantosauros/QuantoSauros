package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.model.ProductOptionScheduleModel;

public interface ProductOptionScheduleDao {

	List<ProductOptionScheduleModel> selectProductOptionSchedule(HashMap<String, Object> paramMap);
}
