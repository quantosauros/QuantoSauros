package com.quantosauros.jpl.dto.underlying;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.RateType;

public class RateUnderlyingInfo extends UnderlyingInfo {
	
	protected double _tenor;
	protected Frequency _swapCouponFrequency;
	protected RateType _rateType;
	
	public RateUnderlyingInfo(ModelType modelType,
			double tenor, Frequency swapCouponFrequency, RateType rateType) {
		this._modelType = modelType;
		this._tenor = tenor;
		this._swapCouponFrequency = swapCouponFrequency;
		this._rateType = rateType;
	}

	public static RateUnderlyingInfo valueOf(ModelType modelType,
			double tenor, Frequency swapCouponFrequency, RateType rateType){
		return new RateUnderlyingInfo(modelType, tenor, swapCouponFrequency, rateType);
	}
	
	public double getTenor(){
		return this._tenor;
	}	
	public Frequency getSwapCouponFrequency(){
		return _swapCouponFrequency;
	}
	public RateType getRateType(){
		return _rateType;
	}
}
