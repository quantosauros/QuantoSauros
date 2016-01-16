package com.quantosauros.manager.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.products.ProductInfoDao;
import com.quantosauros.manager.dao.products.ProductLegDao;
import com.quantosauros.manager.dao.products.ProductLegDataDao;
import com.quantosauros.manager.dao.products.ProductOptionScheduleDao;
import com.quantosauros.manager.dao.products.ProductScheduleDao;
import com.quantosauros.manager.model.products.ProductInfo;

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
	
	public void register(ProductInfo productInfo){
		
		productInfoDao.insertProductInfo(productInfo);		
		
	}
	
	
}
