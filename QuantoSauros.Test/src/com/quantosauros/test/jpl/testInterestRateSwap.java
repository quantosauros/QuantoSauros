package com.quantosauros.test.jpl;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.test.util.TestBase;

public class testInterestRateSwap extends TestBase {

	InterestRateCurve _discountCurve;
	
	public void test(){
		
		Date issueDt = Date.valueOf("20141202");

		double[] discountRateValue = new double[] {
				0.0225,	0.024,	0.0235045,	0.0234797,	0.02353,	0.0238322,	0.0241856,	0.0251007,	0.0258179,	0.0263855,	0.0269068,	0.0273808,	0.0278061,	0.0282655,	0.0287033,	0.0292773,	0.0298588,	0.0313365,
//				0.00283699,	0.00338713,	0.00410436,	0.00492583,	0.00675428,	0.00858092,	0.01028353,	0.0118022,	0.01431868,	0.01644246,	0.01959603,	0.02252876,	0.02634999,
		}; 
		Vertex[] discountRateVertex = new Vertex[] {
				Vertex.valueOf("D1"), Vertex.valueOf("M3"),	Vertex.valueOf("M6"), Vertex.valueOf("M9"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"), Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"), Vertex.valueOf("Y5"),Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"), 	Vertex.valueOf("Y8"), Vertex.valueOf("Y9"), Vertex.valueOf("Y10"), Vertex.valueOf("Y12"), Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),
//				Vertex.valueOf("M3"),	Vertex.valueOf("M6"),	Vertex.valueOf("M9"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y2H"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y20"),		
		};
		
		
		InterestRate[] discountRates = new InterestRate[discountRateValue.length];
		for (int i = 0; i < discountRateValue.length; i++){
			discountRates[i] = new InterestRate(discountRateVertex[i], discountRateValue[i]);			
		}
		
		_discountCurve = new InterestRateCurve(issueDt, discountRates,
				Frequency.valueOf("C"), DayCountFraction.ACTUAL_365);
		
		
		double swapRate1 = _discountCurve.getForwardSwapRate(issueDt, 1);		
		log(swapRate1);
		double swapRate2 = _discountCurve.getForwardSwapRate(issueDt, 2);
		log(swapRate2);
		double swapRate3 = _discountCurve.getForwardSwapRate(issueDt, 3);
		log(swapRate3);
		
	}
}
