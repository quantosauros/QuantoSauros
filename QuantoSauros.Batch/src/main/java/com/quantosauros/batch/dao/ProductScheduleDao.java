package com.quantosauros.batch.dao;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.model.ProductScheduleModel;

public interface ProductScheduleDao {

	List<ProductScheduleModel> selectProductSchedule(HashMap<String, Object> paramMap);

}
