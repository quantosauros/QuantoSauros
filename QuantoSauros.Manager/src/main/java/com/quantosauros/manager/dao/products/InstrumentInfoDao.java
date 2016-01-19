package com.quantosauros.manager.dao.products;

import java.util.List;

import com.quantosauros.manager.model.products.InstrumentInfoModel;

public interface InstrumentInfoDao {

	List<InstrumentInfoModel> getLists();
	InstrumentInfoModel getOne(String instrumentCd);
}
