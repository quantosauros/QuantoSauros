package com.quantosauros.jpl.engine.method.montecarlo.motion;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.distribution.NormalDistributionRandomGenerator;
import com.quantosauros.jpl.dto.market.EquityMarketInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;

public class MotionGenerator {

	public static GeneralizedWienerMotion getInstance(
			double tenor, double dt,
			double startTenor, double exerciseTenor,
			DayCountFraction dcf,
			ModelType modelType,
			MarketInfo marketInfo,			
			NormalDistributionRandomGenerator randomGen){
				
		if (modelType.equals(ModelType.HW1F)){
			
			return new OrnsteinUhlenbeckMotion(tenor, dt, startTenor, exerciseTenor, dcf,
					modelType,
					((RateMarketInfo)marketInfo).getHullWhiteParameters(),
					((RateMarketInfo)marketInfo).getHWVolatilities()[0],					
					((RateMarketInfo)marketInfo).getInterestRateCurve(), 
					randomGen);
			
		} else if (modelType.equals(ModelType.HW2F)){
			return new OrnsteinUhlenbeckMotion2F(tenor, dt, startTenor, exerciseTenor, dcf,
					modelType,
					((RateMarketInfo)marketInfo).getHullWhiteParameters(),
					((RateMarketInfo)marketInfo).getHWVolatilities(),					
					((RateMarketInfo)marketInfo).getInterestRateCurve(), 
					randomGen);
		} else if (modelType.equals(ModelType.BS)){
			return new GeometricBrownianMotion(tenor, dt, startTenor, exerciseTenor, dcf, 
					modelType, 
					//TODO riskFree Rate
					((EquityMarketInfo)marketInfo).getEquityDividend(), 
					((EquityMarketInfo)marketInfo).getEquityVolatility(), 
					randomGen);			
		} else {
			return null;
		}
	}
}
