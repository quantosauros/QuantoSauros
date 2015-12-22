package com.quantosauros.test.jpl;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.date.Date;
import com.quantosauros.jpl.dto.LegFixedCouponInfo;
import com.quantosauros.jpl.dto.OptionInfo;

public class testEveryThing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OptionType optionType = OptionType.CALL;
		Date[] exerciseDates = new Date[1];
		exerciseDates[0] = Date.valueOf("20140501");
		double[] exercisePrices = new double[1];
		exercisePrices[0] = 1;
		
		OptionInfo optionInfo = new OptionInfo(optionType, exerciseDates, exercisePrices);
		
		System.out.println(optionInfo);
		
		CouponType[] couponType = new CouponType[]{CouponType.FIXED, CouponType.FIXED};
		UnderlyingType underlyingType = UnderlyingType.R1;
		ConditionType conditionType = ConditionType.NONE;
		double[] fixedRates = new double[] {0.025, 0.030};
		PayRcv payRcv1 = PayRcv.PAY;
		
		LegFixedCouponInfo legFixedCouponinfo = new LegFixedCouponInfo(payRcv1,
				couponType, underlyingType, conditionType, fixedRates);
		
		System.out.println(legFixedCouponinfo);
		
	}

}
