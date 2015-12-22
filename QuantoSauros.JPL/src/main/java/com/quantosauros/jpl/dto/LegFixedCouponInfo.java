package com.quantosauros.jpl.dto;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.UnderlyingType;

public class LegFixedCouponInfo extends LegCouponInfo {
	
	public LegFixedCouponInfo(PayRcv payRcv, CouponType[] couponType, 
			UnderlyingType underlyingType, ConditionType conditionType,
			double[] fixedRates) {
		super(payRcv, couponType, underlyingType, conditionType);
		_spreads = fixedRates;		
	}	
	
}
