package com.quantosauros.jpl.instrument;

import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.common.date.daycounter.DayCountConvention;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.result.Cashflow;

public class IRSProduct {

	public IRSProduct(Date asOfDate,
			ProductInfo productInfo,
			LegScheduleInfo[] legScheduleInfos,
			LegDataInfo[] legDataInfos,
			LegAmortizationInfo[] legAmortizationInfos,
			LegCouponInfo[] legCouponInfos) {
 
		
		//Cashflow 만들기
		//DF
		
		int legNum = legCouponInfos.length;
		int legIndex = 0;
		UnderlyingType undType = legCouponInfos[legIndex].getUnderlyingType();
		
		PaymentPeriod[] paymentPeriods = legScheduleInfos[legIndex].getPaymentPeriods();
		Cashflow[] cashflows = new Cashflow[paymentPeriods.length];
		
		if (undType.equals(UnderlyingType.NONE)){
			//fixed leg
			for (int periodIndex = 0; periodIndex < paymentPeriods.length; periodIndex++){
				cashflows[periodIndex] = new Cashflow(paymentPeriods[periodIndex]);
				
				if (paymentPeriods[periodIndex].isExpired(asOfDate)) {
					cashflows[periodIndex].setInterest(0);
					cashflows[periodIndex].setPrincipal(0);
					continue;
				}
				
				DayCountFraction dcf = legScheduleInfos[legIndex].getDCF();
				
				double r = legCouponInfos[legIndex].getSpreads()[periodIndex];
				double dt = paymentPeriods[periodIndex].getYearFraction(dcf);
				
				Date startDate = paymentPeriods[periodIndex].getStartDate();
				Date endDate = paymentPeriods[periodIndex].getEndDate();
				Date paymentDate = paymentPeriods[periodIndex].getPaymentDate();
				
				//Interest
				double interest = r * dt;
				cashflows[periodIndex].setInterest(interest);
				
				//Accrued Interest
				double accruedInterest = 0;
				if ((startDate.compareTo(asOfDate) <= 0)
						&& (endDate.compareTo(asOfDate) > 0)) {
					if (paymentDate.equals(endDate)) {
						double deltaT = dcf.getYearFraction(startDate, asOfDate);
						double t1 = dcf.getYearFraction(startDate, endDate);
						accruedInterest = interest * deltaT / t1;
					}
					else {
						double deltaT = dcf.getYearFraction(asOfDate, endDate);
						double t1 = dcf.getYearFraction(startDate, endDate);
						accruedInterest = - interest * deltaT / t1;
					}
					cashflows[periodIndex].setAccruedInterest(accruedInterest);
				}
				
				//Principal
				if (periodIndex == paymentPeriods.length - 1 && 
						productInfo.hasPrincipalExchange()){
					cashflows[paymentPeriods.length - 1].setPrincipal(1);
				}
			}
		} else if (undType.equals(UnderlyingType.R1)){
			//floating leg
			
		}
		
		System.out.println("abs");
		
	}
	
	public Money getPrice(
			RateMarketInfo[][] legMarketInfos,
			RateMarketInfo discountMarketInfo){
		
		
		
		return Money.valueOf("KR", 0);
	}
	
}
