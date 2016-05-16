package com.quantosauros.test.common;

import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.YTMRateType;
import com.quantosauros.common.calibration.BootStrapper;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.AbstractRate;
import com.quantosauros.common.interestrate.BondRate;
import com.quantosauros.common.interestrate.BondRateCurve;
import com.quantosauros.common.interestrate.ZeroRate;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.common.math.interpolation.MonotoneConvexInterpolation;
import com.quantosauros.test.util.TestBase;

public class testBootStrapping_KTB extends TestBase{

	public void test(){
		
		Vertex.valueOf("NONE");
		
		double[] spotRateValue1 = new double[] {
				0.01455,
				0.01430,
				0.01420,
				0.01412,
				0.01520,
				0.01767,
				0.01872,
				0.01907,
		};
		Vertex[] spotRateVertex1 = new Vertex[] {
				Vertex.valueOf("M3"),
				Vertex.valueOf("Y1"),
				Vertex.valueOf("Y2"),
				Vertex.valueOf("Y3"),				
				Vertex.valueOf("Y5"),				
				Vertex.valueOf("Y10"),				
				Vertex.valueOf("Y20"),
				Vertex.valueOf("Y30"),
		};
		YTMRateType[] spotRateTypes = new YTMRateType[] {
			YTMRateType.ZERO, 
			YTMRateType.BOND, YTMRateType.BOND, YTMRateType.BOND,
			YTMRateType.BOND, YTMRateType.BOND, YTMRateType.BOND, YTMRateType.BOND,  			
		};
		Frequency[] spotRateFreq = new Frequency[] {
			Frequency.valueOf("Q"), Frequency.valueOf("Q"), Frequency.valueOf("Q"),
			Frequency.valueOf("S"), Frequency.valueOf("S"), Frequency.valueOf("S"),
			Frequency.valueOf("S"), Frequency.valueOf("S")
		};
		Date[] spotRateMaturity = new Date[] {
			Date.valueOf("20160802"), Date.valueOf("20170209"),
			Date.valueOf("20180202"), Date.valueOf("20181210"),
			Date.valueOf("20210310"), Date.valueOf("20251210"),
			Date.valueOf("20350910"), Date.valueOf("20460310"),
		};
		Date[] spotRateAccrualStartDate = new Date[] {
				Date.valueOf("20140802"), Date.valueOf("20160209"),
				Date.valueOf("20160202"), Date.valueOf("20151210"),
				Date.valueOf("20160310"), Date.valueOf("20151210"),
				Date.valueOf("20150910"), Date.valueOf("20160310"),
			};
		double[] spotRateCouponRate = new double[] {	
			0.0246, 0.0145, 0.0149, 0.0175, 0.02, 0.0225, 0.02625, 0.0200,	
		};
		
		ArrayList<AbstractRate> spotRates1 = new ArrayList<>();
		for (int i = 0; i < spotRateValue1.length; i++){
			spotRates1.add(new BondRate(
					spotRateVertex1[i], spotRateValue1[i], spotRateTypes[i],
					spotRateFreq[i], spotRateMaturity[i], spotRateAccrualStartDate[i], 
					spotRateCouponRate[i]));			
		}
		Date asOfDate = Date.valueOf("20160509"); 
		
		
		BondRateCurve bondRateCurve = 
				new BondRateCurve(asOfDate, spotRates1, 
						Frequency.valueOf("C"), DayCountFraction.ACTUAL_365);
//		BootStrapper bootStrapper = new BootStrapper(bondRateCurve);
		BootStrapper bootStrapper = new BootStrapper(bondRateCurve, MonotoneConvexInterpolation.getInstance());
		
		ZeroRateCurve zeroRateCurve = bootStrapper.calculate();
		
		ArrayList<AbstractRate> zeroRates = zeroRateCurve.getRates();

		System.out.println(zeroRateCurve.getDiscountFactor(Date.valueOf("20160802")));
		System.out.println(zeroRateCurve.getDiscountFactor(Date.valueOf("20170209")));
		System.out.println(zeroRateCurve.getDiscountFactor(Date.valueOf("20180202")));
		System.out.println(zeroRateCurve.getDiscountFactor(Date.valueOf("20181210")));
		System.out.println(zeroRateCurve.getDiscountFactor(Date.valueOf("20210310")));
		System.out.println(zeroRateCurve.getDiscountFactor(Date.valueOf("20251210")));
		System.out.println(zeroRateCurve.getDiscountFactor(Date.valueOf("20350910")));
		
		for (int i = 0; i < zeroRates.size(); i++){
			System.out.println(zeroRates.get(i));
		}
		
		Date forwardStartDate = asOfDate.plusDays(7);
		
		
		Date endDate = asOfDate.plusYears(30);
		
		while (endDate.diff(forwardStartDate) > 0){
			
			forwardStartDate = forwardStartDate.plusDays(3);
			Date forwardEndDate = forwardStartDate.plusMonths(3);
		
//			double forwardRate = zeroRateCurve.getForwardRate(forwardStartDate, forwardEndDate);
			double forwardRate = zeroRateCurve.getForwardSwapRate(forwardStartDate, 3.0, Frequency.valueOf("Q"));
			
			double dt = zeroRateCurve.getDayCountFraction().getYearFraction(asOfDate, forwardStartDate);
			
			log(forwardStartDate + ": " + forwardEndDate + ": " + dt + ": " + forwardRate);
		}
		
		
		
//		InterestRateCurve zeroCurve = SpotCurveCalculator.calculate(structuredLegCurve1);
//		zeroCurve.setCompoundingFrequency(Frequency.NONE);
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20160510")));
//		
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20160802")));
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20170209")));
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20180202")));
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20181210")));
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20210310")));		
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20251210")));
//		log(zeroCurve.getDiscountFactor(Date.valueOf("20350910")));
//		
//		log(zeroCurve.toString());
		
//		for (int i = 0; i < zeroCurve.)
		
	}
	
}
