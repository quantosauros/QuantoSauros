package com.quantosauros.manager.model.products;

public class ProductModel {

	protected ProductInfoModel productInfoModel;
	protected ProductLegModel[] productLegModels;
	
	
	public void setProductInfoModel(ProductInfoModel productInfoModel){
		this.productInfoModel = productInfoModel;
	}
	public ProductInfoModel getProductInfoModel(){
		return productInfoModel;
	}	
	public void setProductLegModels(ProductLegModel[] productLegModels){
		this.productLegModels = productLegModels;
	}
	public ProductLegModel[] getProductLegModels(){
		return productLegModels;
	}
	
}
