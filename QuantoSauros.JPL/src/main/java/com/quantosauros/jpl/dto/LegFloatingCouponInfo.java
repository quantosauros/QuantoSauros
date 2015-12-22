package com.quantosauros.jpl.dto;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;

public class LegFloatingCouponInfo extends LegCouponInfo {
	
	public LegFloatingCouponInfo(PayRcv payRcv, CouponType[] couponType, 
			UnderlyingType underlyingType, ConditionType conditionType,
			UnderlyingInfo[] underlyingInfos,
//			ModelType[] modelTypes,
//			double[] tenors, Frequency[] swapCouponFrequencies, RateType[] rateTypes,
			double[][] leverages, double[] spreads) {
		super(payRcv, couponType, underlyingType, conditionType);
		_underlyingInfos = underlyingInfos;
//		_modelTypes = modelTypes;
//		_tenors = tenors;
//		_swapCouponFrequencies = swapCouponFrequencies;
//		_rateTypes = rateTypes;
		
		_leverages = leverages;
		_hasLeverage = true;
		_spreads = spreads;		
	}
}
