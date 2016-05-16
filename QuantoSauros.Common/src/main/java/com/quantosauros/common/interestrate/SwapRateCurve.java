package com.quantosauros.common.interestrate;

import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.interpolation.Interpolation;

public class SwapRateCurve extends AbstractRateCurve {

	
	public SwapRateCurve(Date date, ArrayList<AbstractRate> swapRates,
			Frequency compoundFreq, DayCountFraction dcf,
			Interpolation interpolation) {
		super(date, swapRates, compoundFreq, dcf, interpolation);
	}
	
	public SwapRateCurve(Date date, ArrayList<AbstractRate> swapRates,
			Frequency compoundFreq, DayCountFraction dcf) {
		super(date, swapRates, compoundFreq, dcf);
	}
	
	public SwapRateCurve copy(double parallelShift){
		ArrayList<AbstractRate> shiftedRates = new ArrayList<>();
		
		for (int i = 0; i < rates.size(); ++i) {
			SwapRate swapRate = (SwapRate) rates.get(i);
			shiftedRates.add(new SwapRate(
					swapRate.getVertex(),
					swapRate.getRate() + parallelShift,
					swapRate.getRateType(),
					swapRate.getPayCouponFrequency(),
					swapRate.getRcvCouponFrequency(),
					swapRate.getPayDcf(),
					swapRate.getRcvDcf(),
					swapRate.getCompoundFrequency()));
		}
		SwapRateCurve irc = new SwapRateCurve(_date, shiftedRates,
				compoundFreq, dcf, interpolation);
		
		return irc;
	}
	
	public SwapRateCurve copy(int index, double parallelShift){
		ArrayList<AbstractRate> shiftedRates = new ArrayList<>();
		
		for (int i = 0; i < rates.size(); ++i) {
			SwapRate swapRate = (SwapRate) rates.get(i);
			if (index == i){
				shiftedRates.add(new SwapRate(
						swapRate.getVertex(),
						swapRate.getRate() + parallelShift,
						swapRate.getRateType(),
						swapRate.getPayCouponFrequency(),
						swapRate.getRcvCouponFrequency(),
						swapRate.getPayDcf(),
						swapRate.getRcvDcf(),
						swapRate.getCompoundFrequency()));
 			} else {
 				shiftedRates.add(new SwapRate(
 						swapRate.getVertex(),
 						swapRate.getRate(),
 						swapRate.getRateType(),
 						swapRate.getPayCouponFrequency(),
 						swapRate.getRcvCouponFrequency(),
 						swapRate.getPayDcf(),
 						swapRate.getRcvDcf(),
 						swapRate.getCompoundFrequency()));
 			}
			
		}
		SwapRateCurve irc = new SwapRateCurve(_date, shiftedRates,
				compoundFreq, dcf, interpolation);
		
		return irc;
	}
}
