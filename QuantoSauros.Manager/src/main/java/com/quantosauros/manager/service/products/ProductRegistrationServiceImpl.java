package com.quantosauros.manager.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.products.ProductInfoDao;
import com.quantosauros.manager.dao.products.ProductLegDao;
import com.quantosauros.manager.dao.products.ProductLegDataDao;
import com.quantosauros.manager.dao.products.ProductOptionScheduleDao;
import com.quantosauros.manager.dao.products.ProductScheduleDao;
import com.quantosauros.manager.model.products.ProductInfoModel;
import com.quantosauros.manager.model.products.ProductLegModel;
import com.quantosauros.manager.model.products.ProductOptionScheduleModel;
import com.quantosauros.manager.model.products.ProductScheduleModel;

@Service("productRegistrationService")
public class ProductRegistrationServiceImpl 
	implements ProductRegistrationService {

	ProductInfoDao productInfoDao;
	ProductLegDao productLegDao;
	ProductLegDataDao productLegDataDao;
	ProductOptionScheduleDao productOptionScheduleDao;
	ProductScheduleDao productScheduleDao;
	
	@Autowired
	public void setProductInfoDao(ProductInfoDao productInfoDao){
		this.productInfoDao = productInfoDao;
	}
	
	@Autowired
	public void setProductLegDao(ProductLegDao productLegDao){
		this.productLegDao = productLegDao;
	}
	
	@Autowired
	public void setProductLegDataDao(ProductLegDataDao productLegDataDao){
		this.productLegDataDao = productLegDataDao;
	}
	
	@Autowired
	public void setProductOptionScheduleDao(ProductOptionScheduleDao productOptionScheduleDao){
		this.productOptionScheduleDao = productOptionScheduleDao;
	}
	
	@Autowired
	public void setProductScheduleDao(ProductScheduleDao productScheduleDao){
		this.productScheduleDao = productScheduleDao;
	}
	
	public void register(ProductInfoModel productInfoModel, 
			ProductLegModel[] productLegModels, ProductScheduleModel[][] productScheduleModels,
			ProductOptionScheduleModel[] productOptionScheduleModels){
		
		productInfoDao.insertProductInfo(productInfoModel);
		
		int legNum = productLegModels.length;
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			productLegDao.insertProductLeg(productLegModels[legIndex]);
			
			int periodNum = productScheduleModels[legIndex].length;
			for (int periodIndex = 0; periodIndex < periodNum; periodIndex++){
				productScheduleDao.insertProductSchedule(productScheduleModels[legIndex][periodIndex]);				
			}
		}
		if (productOptionScheduleModels != null){
			int optionNum = productOptionScheduleModels.length;
			for (int optionIndex = 0; optionIndex < optionNum; optionIndex++){
				productOptionScheduleDao.insertProductOptionSchedule(productOptionScheduleModels[optionIndex]);
			}
		}	
	}
	
	public ProductInfoModel getProductInfoModelByInstrumentCd(String instrumentCd){
		return productInfoDao.selectProductInfoByInstrumentCd(instrumentCd);
	}
	
}
