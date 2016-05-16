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
import com.quantosauros.common.interestrate.ZeroRate;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.test.util.TestBase;

public class testBootStrapping_USDIRS extends TestBase{

	public void test(){
		double[] spotRateValue1 = new double[] {
				0.006266,
				0.007527,
				0.00867,
				0.00965,
				0.01061,
				0.011585,
				0.012564995,
				0.013494995,
				0.014334995,
				0.0151,
				0.015804995,
				0.016460995,
				0.017074995,
				0.018437495,
				0.019834995,
				0.020534995,
				0.020929995,
				0.021115,
				0.020974995,
		};
		Vertex[] spotRateVertex1 = new Vertex[] {
				Vertex.valueOf("M3"),	
				Vertex.valueOf("Y1"),
				Vertex.valueOf("Y2"),
				Vertex.valueOf("Y3"),
				Vertex.valueOf("Y4"),
				Vertex.valueOf("Y5"),
				Vertex.valueOf("Y6"),
				Vertex.valueOf("Y7"),
				Vertex.valueOf("Y8"),
				Vertex.valueOf("Y9"),
				Vertex.valueOf("Y10"),
				Vertex.valueOf("Y11"),
				Vertex.valueOf("Y12"),
				Vertex.valueOf("Y15"),
				Vertex.valueOf("Y20"),
				Vertex.valueOf("Y25"),
				Vertex.valueOf("Y30"),
				Vertex.valueOf("Y40"),
				Vertex.valueOf("Y50"),
		};
		YTMRateType[] swapRateTypes = new YTMRateType[] {
				YTMRateType.ZERO, 
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM, 
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
				YTMRateType.YTM, YTMRateType.YTM, YTMRateType.YTM,
		};
		Frequency[] swapPayCouponFrequency = new Frequency[] {
			Frequency.valueOf("S"), 
			Frequency.valueOf("S"), Frequency.valueOf("S"), Frequency.valueOf("S"),
			Frequency.valueOf("S"), Frequency.valueOf("S"), Frequency.valueOf("S"),
			Frequency.valueOf("S"), Frequency.valueOf("S"), Frequency.valueOf("S"),
			Frequency.valueOf("S"), Frequency.valueOf("S"), Frequency.valueOf("S"),
			Frequency.valueOf("S"), Frequency.valueOf("S"), Frequency.valueOf("S"),
			Frequency.valueOf("S"), Frequency.valueOf("S"), Frequency.valueOf("S"),
		};
		Frequency[] swapRcvCouponFrequency = new Frequency[] {
			Frequency.valueOf("Q"), 
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
		};
		DayCountFraction[] swapPayDcf = new DayCountFraction[] {
			DayCountFraction.D30_360, 
			DayCountFraction.D30_360, DayCountFraction.D30_360, DayCountFraction.D30_360,
			DayCountFraction.D30_360, DayCountFraction.D30_360, DayCountFraction.D30_360,
			DayCountFraction.D30_360, DayCountFraction.D30_360, DayCountFraction.D30_360,
			DayCountFraction.D30_360, DayCountFraction.D30_360, DayCountFraction.D30_360,
			DayCountFraction.D30_360, DayCountFraction.D30_360, DayCountFraction.D30_360,
			DayCountFraction.D30_360, DayCountFraction.D30_360, DayCountFraction.D30_360,
		};
		DayCountFraction[] swapRcvDcf = new DayCountFraction[] {
			DayCountFraction.ACTUAL_360, 
			DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360, 
			DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360,
			DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360,
			DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360,
			DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360,
			DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360, DayCountFraction.ACTUAL_360,
		};
		
		Frequency[] swapCompoundingFrequency = new Frequency[] {
				Frequency.CONTINUOUS, 
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,
				Frequency.CONTINUOUS, Frequency.CONTINUOUS, Frequency.CONTINUOUS,				
			};
		ArrayList<AbstractRate> swapRates = new ArrayList<>();		
		for (int i = 0; i < spotRateValue1.length; i++){		
			swapRates.add(new SwapRate(spotRateVertex1[i], spotRateValue1[i], swapRateTypes[i], 
					swapPayCouponFrequency[i], swapRcvCouponFrequency[i],
					swapPayDcf[i], swapRcvDcf[i], swapCompoundingFrequency[i]));					
		}
		
		Date asOfDate = Date.valueOf("20160511"); 
		
		SwapRateCurve swapRateCurve = 
				new SwapRateCurve(asOfDate, swapRates, 
						Frequency.valueOf("C"), DayCountFraction.ACTUAL_360);
		
		BootStrapper bootStrapper = new BootStrapper(swapRateCurve);
		ZeroRateCurve zeroRateCurve = bootStrapper.calculate();
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20160815")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20170515")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20180514")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20190513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20200513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20210513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20220513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20230515")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20240513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20250513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20260513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20270513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20280515")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20310513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20360513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20410513")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20460514")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20560515")));
		log(zeroRateCurve.getDiscountFactor(Date.valueOf("20660513")));
		
		log(zeroRateCurve.toString());
		
	}
	
}
