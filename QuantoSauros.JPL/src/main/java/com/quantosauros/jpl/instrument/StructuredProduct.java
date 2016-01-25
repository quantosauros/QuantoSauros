package com.quantosauros.jpl.instrument;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;
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
			InterestRateCurve interestRateCurve = rateMarketInfo.getInterestRateCurve();
			
			InterestRate[] interestRates = interestRateCurve.getSpotRates().clone();
			
			for (int vertexIndex = 0; vertexIndex < interestRates.length; vertexIndex++){
				//UP
				InterestRate[] upRates = interestRateCurve.getSpotRates().clone();
				double upSpot = interestRates[vertexIndex].getRate() + epsilon;
				upRates[vertexIndex] = new InterestRate(interestRates[vertexIndex].getVertex(), upSpot);
				InterestRateCurve upCurve = new InterestRateCurve(
						interestRateCurve.getDate(),
						upRates, 
						interestRateCurve.getCompoundingFrequency(),
						interestRateCurve.getDayCountFraction());
				legMarketInfos[legIndex][undIndex] = new RateMarketInfo(
						upCurve, rateMarketInfo.getHullWhiteParameters(), 
						rateMarketInfo.getHWVolatilities());
				
				Money upPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
				
				//DOWN
				InterestRate[] downRates = interestRateCurve.getSpotRates().clone();
				double downSpot = interestRates[vertexIndex].getRate() - epsilon;				
				downRates[vertexIndex] = new InterestRate(interestRates[vertexIndex].getVertex(), downSpot);
				InterestRateCurve downCurve = new InterestRateCurve(
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
		InterestRateCurve interestRateCurve = discountMarketInfo.getInterestRateCurve();		
		InterestRate[] interestRates = interestRateCurve.getSpotRates().clone();
		
		for (int vertexIndex = 0; vertexIndex < interestRates.length; vertexIndex++){
			//UP
			InterestRate[] upRates = interestRateCurve.getSpotRates().clone();			
			double upSpot = interestRates[vertexIndex].getRate() + epsilon;
			upRates[vertexIndex] = new InterestRate(interestRates[vertexIndex].getVertex(), upSpot);
			InterestRateCurve upCurve = new InterestRateCurve(
					interestRateCurve.getDate(),
					upRates, 
					interestRateCurve.getCompoundingFrequency(),
					interestRateCurve.getDayCountFraction());
			discountMarketInfo = new RateMarketInfo(
					upCurve, discountMarketInfo.getHullWhiteParameters(), 
					discountMarketInfo.getHWVolatilities());
			
			Money upPrice = this.getPrice(legMarketInfos, discountMarketInfo, correlations);
			
			//DOWN
			InterestRate[] downRates = interestRateCurve.getSpotRates().clone();
			double downSpot = interestRates[vertexIndex].getRate() - epsilon;				
			downRates[vertexIndex] = new InterestRate(interestRates[vertexIndex].getVertex(), downSpot);
			InterestRateCurve downCurve = new InterestRateCurve(
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
