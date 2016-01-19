package com.quantosauros.manager.service.products;

import com.quantosauros.manager.model.products.ProductInfoModel;

public interface ProductRegistrationService {

	void register(ProductInfoModel productInfo);
	ProductInfoModel getProductInfoModelByInstrumentCd(String instrumentCd);
}
