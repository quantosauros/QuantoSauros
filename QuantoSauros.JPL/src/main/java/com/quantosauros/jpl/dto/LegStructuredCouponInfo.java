package com.quantosauros.jpl.dto;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;

public class LegStructuredCouponInfo extends LegCouponInfo {
		
	public LegStructuredCouponInfo(PayRcv payRcv, CouponType[] couponType, 
			UnderlyingType underlyingType, ConditionType conditionType,
			UnderlyingInfo[] underlyingInfos,
//			ModelType[] modelTypes,
//			double[] tenors, Frequency[] swapCouponFrequencies, RateType[] rateTypes,
			double[][] upperLimits, double[][] lowerLimits,
			double[] inCouponRates, double[] outCouponRates,
			boolean hasCap, double[] cap, boolean hasFloor, double[] floor,
			double[][] leverages, double[] spreads) {
		super(payRcv, couponType, underlyingType, conditionType);
//		_modelTypes = modelTypes;
//		_tenors = tenors;
//		_swapCouponFrequencies = swapCouponFrequencies;
//		_rateTypes = rateTypes;
		_underlyingInfos = underlyingInfos;
		_upperLimits = upperLimits;
		_lowerLimits = lowerLimits;
		_inCouponRates = inCouponRates;
		_outCouponRates = outCouponRates;
		_hasCap = hasCap;
		_cap = cap;
		_hasFloor = hasFloor;
		_floor = floor;
		_leverages = leverages;
		_hasLeverage = true;
		_spreads = spreads;		
	}

	
}
