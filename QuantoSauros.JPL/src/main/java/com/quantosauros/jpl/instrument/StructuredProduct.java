package com.quantosauros.jpl.instrument;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.interestrate.AbstractRate;
import com.quantosauros.common.interestrate.ZeroRate;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.EquityMarketInfo;
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
	
	public ArrayList<Double> getDelta(
			MarketInfo[][] legMarketInfos,
			RateMarketInfo discountMarketInfo,
			double[][] correlations,
			int legIndex, int undIndex,
			double epsilon){
		
		ArrayList<Double> delta = new ArrayList<>();
		
		MarketInfo marketInfo = legMarketInfos[legIndex][undIndex];
		
		if (marketInfo instanceof RateMarketInfo){
			//Interest Rate Curve Delta
			RateMarketInfo rateMarketInfo = (RateMarketInfo) marketInfo;
			ZeroRateCurve interestRateCurve = rateMarketInfo.getInterestRateCurve();
			
			ArrayList<AbstractRate> interestRates = interestRateCurve.getRates();
			
			for (int vertexIndex = 0; vertexIndex < interestRates.size(); vertexIndex++){
				//UP
				ArrayList<AbstractRate> upRates = interestRateCurve.getRates();
				double upSpot = interestRates.get(vertexIndex).getRate() + epsilon;
				upRates.set(vertexIndex, new ZeroRate(interestRates.get(vertexIndex).getVertex(), upSpot)); 
				ZeroRateCurve upCurve = new ZeroRateCurve(
						interestRateCurve.getDate(),
						upRates, 
						interestRateCurve.getCompoundingFrequency(),
						interestRateCurve.getDayCountFraction());
				legMarketInfos[legIndex][undIndex] = new RateMarketInfo(
						upCurve, rateMarketInfo.getHullWhiteParameters(), 
						rateMarketInfo.getHWVolatilities());
				
				Money upPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
				
				//DOWN
				ArrayList<AbstractRate> downRates = interestRateCurve.getRates();
				double downSpot = interestRates.get(vertexIndex).getRate() - epsilon;			
				downRates.set(vertexIndex, new ZeroRate(interestRates.get(vertexIndex).getVertex(), downSpot));
				ZeroRateCurve downCurve = new ZeroRateCurve(
						interestRateCurve.getDate(),
						downRates, 
						interestRateCurve.getCompoundingFrequency(),
						interestRateCurve.getDayCountFraction());
				legMarketInfos[legIndex][undIndex] = new RateMarketInfo(
						downCurve, rateMarketInfo.getHullWhiteParameters(), 
						rateMarketInfo.getHWVolatilities());
				Money downPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
				
				//Delta
				delta.add(upPrice.subtract(downPrice).getAmount());
			}			
		} else if (marketInfo instanceof EquityMarketInfo){
			//Equity
			EquityMarketInfo equityMarketInfo = (EquityMarketInfo) marketInfo;
			double currPrice = equityMarketInfo.getCurrEquityPrice();
			
			//UP
			double upCurrPrice = currPrice * (1 + epsilon);			
			legMarketInfos[legIndex][undIndex] = new EquityMarketInfo(
					upCurrPrice, equityMarketInfo.getEquityVolatility(),
					equityMarketInfo.getEquityDividend(),
					equityMarketInfo.getRiskFreeCurve());			
			Money upPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
			
			//DOWN
			double downCurrPrice = currPrice * (1 - epsilon);			
			legMarketInfos[legIndex][undIndex] = new EquityMarketInfo(
					downCurrPrice, equityMarketInfo.getEquityVolatility(),
					equityMarketInfo.getEquityDividend(),
					equityMarketInfo.getRiskFreeCurve());			
			Money downPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
			
			delta.add(upPrice.subtract(downPrice).getAmount());
			
		}
		
		return delta;
	}
	
	public ArrayList<Double> getDiscountDelta(MarketInfo[][] legMarketInfos,
			RateMarketInfo discountMarketInfo,
			double[][] correlations,
			double epsilon){
		
		ArrayList<Double> delta = new ArrayList<>();
		
		//Interest Rate Curve Delta		
		ZeroRateCurve interestRateCurve = discountMarketInfo.getInterestRateCurve();		
		ArrayList<AbstractRate> interestRates = interestRateCurve.getRates();
		
		for (int vertexIndex = 0; vertexIndex < interestRates.size(); vertexIndex++){
			//UP
			ArrayList<AbstractRate> upRates = interestRateCurve.getRates();			
			double upSpot = interestRates.get(vertexIndex).getRate() + epsilon;
			upRates.set(vertexIndex, new ZeroRate(interestRates.get(vertexIndex).getVertex(), upSpot));
			ZeroRateCurve upCurve = new ZeroRateCurve(
					interestRateCurve.getDate(),
					upRates, 
					interestRateCurve.getCompoundingFrequency(),
					interestRateCurve.getDayCountFraction());
			discountMarketInfo = new RateMarketInfo(
					upCurve, discountMarketInfo.getHullWhiteParameters(), 
					discountMarketInfo.getHWVolatilities());
			
			Money upPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
			
			//DOWN
			ArrayList<AbstractRate> downRates = interestRateCurve.getRates();
			double downSpot = interestRates.get(vertexIndex).getRate() - epsilon;				
			downRates.set(vertexIndex, new ZeroRate(interestRates.get(vertexIndex).getVertex(), downSpot));
			ZeroRateCurve downCurve = new ZeroRateCurve(
					interestRateCurve.getDate(),
					downRates, 
					interestRateCurve.getCompoundingFrequency(),
					interestRateCurve.getDayCountFraction());
			discountMarketInfo = new RateMarketInfo(
					downCurve, discountMarketInfo.getHullWhiteParameters(), 
					discountMarketInfo.getHWVolatilities());
			Money downPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
			
			//Delta
			delta.add(upPrice.subtract(downPrice).getAmount());
		}	
		return delta;
	}
}
