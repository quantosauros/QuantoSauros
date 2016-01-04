package com.quantosauros.manager.dao.products;

import java.util.List;

import com.quantosauros.manager.model.products.InstrumentInfo;

public interface InstrumentInfoDao {

	List<InstrumentInfo> getLists();
	InstrumentInfo getOne(String instrumentCd);
}
