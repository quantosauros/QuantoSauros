package com.quantosauros.test.jpl;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.currency.Currency;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegFixedCouponInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.instrument.IRSProduct;
import com.quantosauros.test.util.TestBase;

public class testIRS extends TestBase{

	public void test(){
		
		Date asOfDate = Date.valueOf("20131202");
		
		Date issueDate =  Date.valueOf("20131202");
		Date maturityDate = Date.valueOf("20141202");
		Currency currency = Currency.getInstance("KR");
		
		Date[] startDates = new Date[]{
				Date.valueOf("20131202"), Date.valueOf("20140303"), Date.valueOf("20140602"), Date.valueOf("20140902"), 
		};
		
		Date[] endDates = new Date[]{
				Date.valueOf("20140303"), Date.valueOf("20140602"), Date.valueOf("20140902"), Date.valueOf("20141202"),  
		};
		
		DayCountFraction dcf = DayCountFraction.ACTUAL_365;
		
		Money principal = Money.valueOf("KR", 10000);
		
		int legNum = 2;
		
				
		//PRODUCT INFO
		ProductInfo productInfo = new ProductInfo(
				issueDate, maturityDate, currency, true);
				
		//LEG SCHEDULE INFO
		LegScheduleInfo[] legScheduleInfos = new LegScheduleInfo[legNum];
		
		PaymentPeriod[] periods1 = new PaymentPeriod[startDates.length];
		for (int i = 0; i < startDates.length; i++){
			periods1[i] = new PaymentPeriod(
					startDates[i], startDates[i], endDates[i], endDates[i]);
		}
		
		legScheduleInfos[0] = new LegScheduleInfo(periods1, dcf);
				
		//LEG DATA INFO
		LegDataInfo[] legDataInfos = new LegDataInfo[legNum];
		Date nextCouponDate = Date.valueOf("20140303");
		legDataInfos[0] = new LegDataInfo(nextCouponDate);
		
		//LEG AMORTIZATION INFO
		LegAmortizationInfo[] legAmortizationInfos = new LegAmortizationInfo[legNum];
		legAmortizationInfos[0] = new LegAmortizationInfo(principal);		
		
		//LEG COUPON INFO
		LegCouponInfo[] legCouponInfos = new LegCouponInfo[legNum];
		
		CouponType[] couponType = new CouponType[startDates.length];
		UnderlyingType underlyingType = UnderlyingType.NONE;
		ConditionType conditionType = ConditionType.NONE;
		double[] fixedRates = new double[startDates.length];
		PayRcv payRcv = PayRcv.PAY;
		
		for (int i = 0; i < startDates.length; i++){
			couponType[i] = CouponType.FIXED;
			fixedRates[i] = 0.02733;		
		}
		
		
		legCouponInfos[0] = new LegFixedCouponInfo(payRcv, 
				couponType, underlyingType, conditionType, fixedRates);
		
		
		IRSProduct irsProduct = new IRSProduct(asOfDate, 
				productInfo, legScheduleInfos, legDataInfos, 
				legAmortizationInfos, legCouponInfos);
		
		
	}	
}
