package com.quantosauros.manager.service.products;

import java.util.List;

import com.quantosauros.manager.model.products.InstrumentInfoModel;

public interface InstrumentInfoService {

	List<InstrumentInfoModel> getLists();
	InstrumentInfoModel getOne(String instrumentCd);
}
