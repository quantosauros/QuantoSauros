package com.quantosauros.common.interestrate;

import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.LinearInterpolation;
import com.quantosauros.common.volatility.Curve;

public class ZeroRateCurve extends AbstractRateCurve {
	
	public ZeroRateCurve(Date date, ArrayList<AbstractRate> zeroRates, 
			Frequency compoundFreq, DayCountFraction dcf,
			Interpolation interpolation) {
		super(date, zeroRates, compoundFreq, dcf, interpolation);
	}
	
	public ZeroRateCurve(Date date, ArrayList<AbstractRate> zeroRates, 
			Frequency compoundFreq, DayCountFraction dcf){
		super(date, zeroRates, compoundFreq, dcf);		
	}
	
	public ZeroRateCurve(Date date, 
			Frequency compoundFreq, DayCountFraction dcf,
			Interpolation interpolation){
		super(date, compoundFreq, dcf, interpolation);
		this.rates = new ArrayList<>();
	}
	
	public ZeroRateCurve(Date date, Frequency compoundfrequency,
			DayCountFraction dcf){
		this(date, compoundfrequency, dcf, LinearInterpolation.getInstance());
		this.rates = new ArrayList<>();
	}
	
	public void putZeroRate(ZeroRate zeroRate){		
		this.rates.add(zeroRate);		
	}
	
	public void popBackZeroRate(){
		this.rates.remove(this.rates.size() - 1);
	}

	public ZeroRateCurve copy(double parallelShift){
		ArrayList<AbstractRate> shiftedRates = new ArrayList<>();
		
		for (int i = 0; i < rates.size(); ++i) {
			ZeroRate zeroRate = (ZeroRate) rates.get(i);
			shiftedRates.add(new ZeroRate(
					zeroRate.getVertex(),
					zeroRate.getRate() + parallelShift,
					zeroRate.getRateType()));
		}
		ZeroRateCurve irc = new ZeroRateCurve(_date, shiftedRates,
				compoundFreq, dcf, interpolation);
		
		return irc;
	}
	
	public ZeroRateCurve copy(int index, double parallelShift){
		ArrayList<AbstractRate> shiftedRates = new ArrayList<>();
		
		for (int i = 0; i < rates.size(); ++i) {
			ZeroRate swapRate = (ZeroRate) rates.get(i);
			if (index == i){
				shiftedRates.add(new ZeroRate(
						swapRate.getVertex(),
						swapRate.getRate() + parallelShift,
						swapRate.getRateType()));
 			} else {
 				shiftedRates.add(new ZeroRate(
 						swapRate.getVertex(),
 						swapRate.getRate(),
 						swapRate.getRateType()));
 			}
			
		}
		ZeroRateCurve irc = new ZeroRateCurve(_date, shiftedRates,
				compoundFreq, dcf, interpolation);
		
		return irc;
	}
	
}
