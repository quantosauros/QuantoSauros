package com.quantosauros.manager.service.products;

import java.util.List;

import com.quantosauros.manager.model.products.InstrumentInfo;

public interface InstrumentInfoService {

	List<InstrumentInfo> getLists();
	InstrumentInfo getOne(String instrumentCd);
}
