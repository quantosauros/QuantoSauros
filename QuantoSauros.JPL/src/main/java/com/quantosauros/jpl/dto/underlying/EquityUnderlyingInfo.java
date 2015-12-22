package com.quantosauros.jpl.dto.underlying;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.RiskFreeType;

public class EquityUnderlyingInfo extends UnderlyingInfo {

	protected double _basePrice;	
	protected RiskFreeType _riskFreeType;
	
	public EquityUnderlyingInfo(ModelType modelType, double basePrice,
			RiskFreeType riskFreeType) {
		this._modelType = modelType;
		this._basePrice = basePrice;
		this._riskFreeType = riskFreeType;
	}
	
	public static EquityUnderlyingInfo valueOf(ModelType modelType, double basePrice,
			RiskFreeType riskFreeType){
		return new EquityUnderlyingInfo(modelType, basePrice, riskFreeType);
	}
	
	public double getBasePrice(){
		return _basePrice;
	}
	
	public RiskFreeType getRiskFreeType(){
		return _riskFreeType;
	}
}
