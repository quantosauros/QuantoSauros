package com.quantosauros.manager.service.products;

import com.quantosauros.manager.model.products.ProductInfoModel;
import com.quantosauros.manager.model.products.ProductLegModel;
import com.quantosauros.manager.model.products.ProductOptionScheduleModel;
import com.quantosauros.manager.model.products.ProductScheduleModel;

public interface ProductRegistrationService {

	void register(ProductInfoModel productInfoModel, 
			ProductLegModel[] productLegModels, ProductScheduleModel[][] productScheduleModels,
			ProductOptionScheduleModel[] productOptionScheduleModels);
	ProductInfoModel getProductInfoModelByInstrumentCd(String instrumentCd);
}
