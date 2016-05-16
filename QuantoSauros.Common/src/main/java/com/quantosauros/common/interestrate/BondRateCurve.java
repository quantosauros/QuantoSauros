package com.quantosauros.common.interestrate;

import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.interpolation.Interpolation;

public class BondRateCurve extends AbstractRateCurve {

	
	public BondRateCurve(Date date, ArrayList<AbstractRate> bondRates,
			Frequency compoundFreq, DayCountFraction dcf) {
		super(date, bondRates, compoundFreq, dcf);

	}
	
	public BondRateCurve(Date date, ArrayList<AbstractRate> bondRates,
			Frequency compoundFreq, DayCountFraction dcf, Interpolation interpolation) {
		super(date, bondRates, compoundFreq, dcf, interpolation);

	}
	
	public BondRate[] getBondRates(){
		int size = rates.size();
		BondRate[] bondRates = new BondRate[size];
		for (int i = 0; i < size; i++){
			bondRates[i] = (BondRate) rates.get(i);
		}
		return bondRates;
	}
	
	public BondRateCurve copy(double parallelShift){
		ArrayList<AbstractRate> shiftedRates = new ArrayList<>();
		
		for (int i = 0; i < rates.size(); ++i) {
			BondRate bondRate = (BondRate) rates.get(i);
			shiftedRates.add(new BondRate(
					bondRate.getVertex(),
					bondRate.getRate() + parallelShift,
					bondRate.getRateType(),
					bondRate.getCouponFrequency(),
					bondRate.getMaturityDate(),
					bondRate.getAccrualStartDate(),
					bondRate.getCouponRate()));
		}
		BondRateCurve irc = new BondRateCurve(_date, shiftedRates,
				compoundFreq, dcf, interpolation);
		
		return irc;
	}
	
	public BondRateCurve copy(int index, double parallelShift){
		ArrayList<AbstractRate> shiftedRates = new ArrayList<>();
		
		for (int i = 0; i < rates.size(); ++i) {
			BondRate bondRate = (BondRate) rates.get(i);
			if (index == i){
				shiftedRates.add(new BondRate(
						bondRate.getVertex(),
						bondRate.getRate() + parallelShift,
						bondRate.getRateType(),
						bondRate.getCouponFrequency(),
						bondRate.getMaturityDate(),
						bondRate.getAccrualStartDate(),
						bondRate.getCouponRate()));
 			} else {
 				shiftedRates.add(new BondRate(
 						bondRate.getVertex(),
 						bondRate.getRate(),
 						bondRate.getRateType(),
 						bondRate.getCouponFrequency(),
 						bondRate.getMaturityDate(),
 						bondRate.getAccrualStartDate(),
 						bondRate.getCouponRate()));
 			}
			
		}
		BondRateCurve irc = new BondRateCurve(_date, shiftedRates,
				compoundFreq, dcf, interpolation);
		
		return irc;
	}
}
