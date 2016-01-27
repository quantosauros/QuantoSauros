package com.quantosauros.manager.model.products;

public class ProductModel {

	protected ProductInfoModel productInfoModel;
	protected ProductLegModel[] productLegModels;
	protected ProductScheduleModel[][] productScheduleModels;
	protected ProductOptionScheduleModel[] productOptionScheduleModels;
	
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
	public void setProductScheduleModels(ProductScheduleModel[][] productScheduleModels){
		this.productScheduleModels = productScheduleModels;
	}
	public ProductScheduleModel[][] getProductScheduleModels(){
		return this.productScheduleModels;
	}
	public void setProductOptionScheduleModels(ProductOptionScheduleModel[] productOptionScheduleModels){
		this.productOptionScheduleModels = productOptionScheduleModels;
	}
	public ProductOptionScheduleModel[] getProductOptionScheduleModels(){
		return this.productOptionScheduleModels;
	}
}
