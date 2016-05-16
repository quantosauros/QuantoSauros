package com.quantosauros.test.common;

import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.YTMRateType;
import com.quantosauros.common.calibration.BootStrapper;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.AbstractRate;
import com.quantosauros.common.interestrate.SwapRate;
import com.quantosauros.common.interestrate.SwapRateCurve;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.common.math.interpolation.MonotoneConvexInterpolation;
import com.quantosauros.test.util.TestBase;

public class testMonotoneConvexInterpolation extends TestBase{

	public void test(){
		double[] spotRateValue1 = new double[] {
				0.0159,
				0.0161,
				0.01525,
				0.01465,
				0.014275,
				0.013725,
				0.013925,
				0.0142,
				0.014475,
				0.015075,
				0.015975,
				0.0165,
				0.01675,
				0.017075,
		};
		Vertex[] spotRateVertex1 = new Vertex[] {
				Vertex.valueOf("NONE"), Vertex.valueOf("M3"),
				Vertex.valueOf("M6"), Vertex.valueOf("M9"),
				Vertex.valueOf("Y1"), Vertex.valueOf("Y2"), Vertex.valueOf("Y3"),
				Vertex.valueOf("Y4"), Vertex.valueOf("Y5"), Vertex.valueOf("Y7"),
				Vertex.valueOf("Y10"), Vertex.valueOf("Y12"), Vertex.valueOf("Y15"),
				Vertex.valueOf("Y20"),
		};
		YTMRateType[] swapRateTypes = new YTMRateType[] {
				YTMRateType.ZERO, YTMRateType.ZERO,
				YTMRateType.YTM, YTMRateType.YTM,  
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
				YTMRateType.YTM,
				
		};
		Frequency[] swapPayCouponFrequency = new Frequency[] {
			Frequency.valueOf("Q"), Frequency.valueOf("Q"),  
			Frequency.valueOf("Q"), Frequency.valueOf("Q"),   
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"),
		};
		Frequency[] swapRcvCouponFrequency = new Frequency[] {
				Frequency.valueOf("Q"), Frequency.valueOf("Q"),  
				Frequency.valueOf("Q"), Frequency.valueOf("Q"),   
				Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
				Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
				Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
				Frequency.valueOf("Q"),  
		};
		DayCountFraction[] swapPayDcf = new DayCountFraction[] {
			DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365,
			DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, 
			DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, 
			DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365,
			DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365,
			DayCountFraction.ACTUAL_365, 
		};
		DayCountFraction[] swapRcvDcf = new DayCountFraction[] {
				DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365,
				DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, 
				DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, 
				DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365,
				DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365, DayCountFraction.ACTUAL_365,
				DayCountFraction.ACTUAL_365,  
		};
		Frequency[] swapCompoundFrequency = new Frequency[] {
				Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, 
		};
		ArrayList<AbstractRate> swapRates = new ArrayList<>();		
		for (int i = 0; i < spotRateValue1.length; i++){
			swapRates.add(new SwapRate(spotRateVertex1[i], spotRateValue1[i], swapRateTypes[i], 
					swapPayCouponFrequency[i], swapRcvCouponFrequency[i],
					swapPayDcf[i], swapRcvDcf[i], swapCompoundFrequency[i]));						
		}
		
		Date asOfDate = Date.valueOf("20160512"); 
		
		SwapRateCurve swapRateCurve = 
				new SwapRateCurve(asOfDate, swapRates, 
						Frequency.valueOf("C"), DayCountFraction.ACTUAL_365);
		
		BootStrapper bootStrapper = new BootStrapper(swapRateCurve, MonotoneConvexInterpolation.getInstance());
//		BootStrapper bootStrapper = new BootStrapper(swapRateCurve, LinearInterpolation.getInstance());
		ZeroRateCurve zeroRateCurve = bootStrapper.calculate();

		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20160513")));		
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20160816")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20161114")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20170213")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20170515")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20180514")));		
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20190513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20200513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20210513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20230515")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20260513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20280515")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20310513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20360513")));
//		
		log(zeroRateCurve.toString());

		Date forwardStartDate = asOfDate.plusDays(7);
		
		
		Date endDate = asOfDate.plusYears(20);
		
		while (endDate.diff(forwardStartDate) > 0){
			
			forwardStartDate = forwardStartDate.plusDays(3);
			Date forwardEndDate = forwardStartDate.plusMonths(3);
						
			double forwardSwapRate = zeroRateCurve.getForwardSwapRate(forwardStartDate, 7.0, Frequency.valueOf("Q"));
			double forwardRate = zeroRateCurve.getForwardRate(forwardStartDate, forwardEndDate);
			
			double dt = zeroRateCurve.getDayCountFraction().getYearFraction(asOfDate, forwardStartDate);
			
//			log(forwardStartDate + ": " + forwardEndDate + ": " + dt + ": " + forwardRate);
//			log(forwardStartDate + ": " + forwardEndDate + ": " + dt + ": " + forwardSwapRate);
			log(forwardStartDate + ": " + forwardEndDate + ": " + dt + ": " + (forwardSwapRate - forwardRate));
		}
		
	}
	
}
