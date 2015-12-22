package com.quantosauros.jpl.instrument;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.engine.pricer.StructuredPricer;

public class StructuredProduct extends AbstractProduct {
	
	public StructuredProduct(
			Date asOfDate,
			ProductInfo productInfo,
			LegScheduleInfo[] legScheduleInfos,
			LegDataInfo[] legDataInfos,
			LegAmortizationInfo[] legAmortizationInfos,
			LegCouponInfo[] legCouponInfos,			
			OptionInfo optionInfo,
			ModelType discountHWType,
			int monitorFrequency,
			long seed,
			int simNum) {		
		
		_pricer = new StructuredPricer(
				asOfDate,
				productInfo, 
				legScheduleInfos, 
				legDataInfos, 
				legAmortizationInfos, 
				legCouponInfos, 
				optionInfo,
				
				discountHWType,
				monitorFrequency,
				seed, simNum);
		
	}
	
	public Money getPrice(
			MarketInfo[][] legMarketInfos,
			RateMarketInfo discountMarketInfo,
			double[][] correlations){
		
		_pricer.generate(legMarketInfos, discountMarketInfo, 
				correlations);
		
		return _pricer.getPrice();
	}
	
}
