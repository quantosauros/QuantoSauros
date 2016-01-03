package com.quantosauros.manager.model.pricer;

import java.util.List;

public class LegCouponInfoPricerModelForm {

	private List<LegCouponInfoPricerModel> legCouponInfoPricerModels;
	
	public void setLegCouponInfoPricerModels(
			List<LegCouponInfoPricerModel> legCouponInfoPricerModels){
		this.legCouponInfoPricerModels = legCouponInfoPricerModels;
	}
	public List<LegCouponInfoPricerModel> getLegCouponInfoPricerModels(){
		return legCouponInfoPricerModels;
	}
}
