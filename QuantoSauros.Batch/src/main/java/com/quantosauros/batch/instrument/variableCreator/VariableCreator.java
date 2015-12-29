package com.quantosauros.batch.instrument.variableCreator;

import java.util.List;

import com.quantosauros.batch.model.ProductInfoModel;
import com.quantosauros.batch.model.ProductLegDataModel;
import com.quantosauros.batch.model.ProductLegModel;
import com.quantosauros.batch.model.ProductOptionScheduleModel;
import com.quantosauros.batch.model.ProductScheduleModel;
import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef;
import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.RateType;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.currency.Currency;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegFixedCouponInfo;
import com.quantosauros.jpl.dto.LegFloatingCouponInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.LegStructuredCouponInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.underlying.RateUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;

public class VariableCreator {

	public static ProductInfo getProductInfo(ProductInfoModel productInfoModel){
		
		boolean hasPrincipalExchange = false;
		if (productInfoModel.getPrincipalExchCd().equals("Y")){
			hasPrincipalExchange = true;
		}
		
		return new ProductInfo(				
				Date.valueOf(productInfoModel.getIssueDt()),
				Date.valueOf(productInfoModel.getMrtyDt()),
				Currency.getInstance(productInfoModel.getCcyCd()),
				hasPrincipalExchange);		
	}
	
	public static LegCouponInfo[] getLegCouponInfos(
			ProductInfoModel productInfoModel,
    		ProductLegModel[] productLegModels,
    		List<ProductScheduleModel>[] productScheduleLists){
		
		LegCouponInfo[] legCouponInfos = new LegCouponInfo[productLegModels.length];
				
		for (int legIndex = 0; legIndex < productLegModels.length; legIndex++){
			PayRcv payRcv = TypeDef.getPayRcv(productLegModels[legIndex].getPayRcvCd());
			String legTypeCd = productLegModels[legIndex].getLegTypeCd();
			ProductLegModel productLegModel = productLegModels[legIndex];
			List<ProductScheduleModel> productScheduleList = productScheduleLists[legIndex];
		
			UnderlyingType underlyingType = 
					TypeDef.getUnderlyingType(productLegModels[legIndex].getUnderlyingType());
			ConditionType conditionType = 
					TypeDef.getConditionType(productLegModels[legIndex].getConditionType());
						
			int undNum = TypeDef.getNumOfUnderNum(underlyingType);		
			int condiNum = TypeDef.getNumOfCondition(conditionType);
			
			//Model Type
			ModelType[] modelTypes = new ModelType[undNum];			
			String ircCd1 = productLegModels[legIndex].getCouponIrcCd1();
			String ircCd2 = productLegModels[legIndex].getCouponIrcCd2();
			String ircCd3 = productLegModels[legIndex].getCouponIrcCd3();
			String undType = productLegModels[legIndex].getUnderlyingType();
			if (undType.equals(UnderlyingType.R1mR2)){
				//Spread R1 - R2
				if (ircCd1.equals(ircCd2)){
					modelTypes[0] = ModelType.HW2F;
					modelTypes[1] = ModelType.HW2F;
				} else {
					modelTypes[0] = ModelType.HW1F;
					modelTypes[1] = ModelType.HW1F;
				}
			} else if(undType.equals(UnderlyingType.R1nR2mR3)){
				//Spread R1 & (R2-R3)
				if (ircCd2.equals(ircCd3)){
					if (ircCd1.equals(ircCd2)){
						//ir1 = ir2 = ir3
						modelTypes[0] = ModelType.HW2F;
						modelTypes[1] = ModelType.HW2F;
						modelTypes[2] = ModelType.HW2F;
					} else {
						//ir1 != ir2 = ir3
						modelTypes[0] = ModelType.HW1F;
						modelTypes[1] = ModelType.HW2F;
						modelTypes[2] = ModelType.HW2F;
					}					
				} else {
					//ir1 != ir2 != ir3
					modelTypes[0] = ModelType.HW1F;
					modelTypes[1] = ModelType.HW1F;
					modelTypes[2] = ModelType.HW1F;
				}				
			} else {
				//No Spread
				if (undType.equals(UnderlyingType.R1nEQ)){
					//Equity
					modelTypes[0] = ModelType.HW1F;
					modelTypes[1] = ModelType.BS;
				} else {
					//else
					for (int undIndex = 0; undIndex < undNum; undIndex++){
						modelTypes[undIndex] = ModelType.HW1F;
					}
				}
			}			

			double[] tenors = new double[undNum];
			Frequency[] swapCouponFrequencies = new Frequency[undNum];
			RateType[] rateTypes = new RateType[undNum];
			switch (undNum){
				case 1 :
					tenors[0] = Vertex.getVertex(productLegModel.getCouponIrcMrtyCd1());
					swapCouponFrequencies[0] = 
							Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd1());
					rateTypes[0] = TypeDef.getRateType(productLegModel.getCouponIrcTypeCd1());
					break;
				case 2 :
					tenors[0] = Vertex.getVertex(productLegModel.getCouponIrcMrtyCd1());
					swapCouponFrequencies[0] = 
							Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd1());
					rateTypes[0] = TypeDef.getRateType(productLegModel.getCouponIrcTypeCd1());
					tenors[1] =Vertex.getVertex(productLegModel.getCouponIrcMrtyCd2());
					swapCouponFrequencies[1] = 
							Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd2());
					rateTypes[1] = TypeDef.getRateType(productLegModel.getCouponIrcTypeCd2());
					break;
				case 3 : 
					tenors[0] = Vertex.getVertex(productLegModel.getCouponIrcMrtyCd1());
					swapCouponFrequencies[0] = 
							Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd1());
					rateTypes[0] = TypeDef.getRateType(productLegModel.getCouponIrcTypeCd1());
					tenors[1] =Vertex.getVertex(productLegModel.getCouponIrcMrtyCd2());
					swapCouponFrequencies[1] = 
							Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd2());
					rateTypes[1] = TypeDef.getRateType(productLegModel.getCouponIrcTypeCd2());
					tenors[2] =Vertex.getVertex(productLegModel.getCouponIrcMrtyCd3());
					swapCouponFrequencies[2] = 
							Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd3());
					rateTypes[2] = TypeDef.getRateType(productLegModel.getCouponIrcTypeCd3());
					break;
			}
			
			CouponType[] couponType = new CouponType[productScheduleList.size()];
			double[][] leverages = new double[undNum][productScheduleList.size()];
			double[] spreads = new double[productScheduleList.size()];
			double[][] upperLimits = new double[condiNum][productScheduleList.size()];
			double[][] lowerLimits = new double[condiNum][productScheduleList.size()];
			double[] inCouponRates = new double[productScheduleList.size()];
			double[] outCouponRates = new double[productScheduleList.size()];					
			double[] cap = new double[productScheduleList.size()];
			double[] floor = new double[productScheduleList.size()];

			for (int schIndex = 0; schIndex < productScheduleList.size(); schIndex++){
				ProductScheduleModel productScheduleModel = productScheduleList.get(schIndex);
				couponType[schIndex] = TypeDef.getCouponType(productScheduleModel.getCouponType());				
				spreads[schIndex] = Double.parseDouble(productScheduleModel.getFixedCoupon());
				inCouponRates[schIndex] = Double.parseDouble(productScheduleModel.getInCoupon());
				outCouponRates[schIndex] = Double.parseDouble(productScheduleModel.getOutCoupon());
				cap[schIndex] = Double.parseDouble(productScheduleModel.getCap());
				floor[schIndex] = Double.parseDouble(productScheduleModel.getFloor());
				
				switch (undNum){
					case 1 :
						leverages[0][schIndex] = Double.parseDouble(productScheduleModel.getLeverage1());
						break;
					case 2 :
						leverages[0][schIndex] = Double.parseDouble(productScheduleModel.getLeverage1());
						leverages[1][schIndex] = Double.parseDouble(productScheduleModel.getLeverage2());
						break;
					case 3 : 
						leverages[0][schIndex] = Double.parseDouble(productScheduleModel.getLeverage1());
						leverages[1][schIndex] = Double.parseDouble(productScheduleModel.getLeverage2());
						leverages[2][schIndex] = Double.parseDouble(productScheduleModel.getLeverage3());
						break;
				}
						
				switch (condiNum){
					case 1 :
						upperLimits[0][schIndex] = Double.parseDouble(productScheduleModel.getUpperBound1());
						lowerLimits[0][schIndex] = Double.parseDouble(productScheduleModel.getLowerBound1());
						break;
					case 2 :
						upperLimits[0][schIndex] = Double.parseDouble(productScheduleModel.getUpperBound1());
						lowerLimits[0][schIndex] = Double.parseDouble(productScheduleModel.getLowerBound1());
						upperLimits[1][schIndex] = Double.parseDouble(productScheduleModel.getUpperBound2());
						lowerLimits[1][schIndex] = Double.parseDouble(productScheduleModel.getLowerBound2());
						break;
				}								
			}
			
			boolean hasCap = false;	
			boolean hasFloor = false;			
			String capFloorCd =	productLegModel.getCapFloorCd();
			if (capFloorCd.equals("0")){				
			} else if (capFloorCd.equals("1")){
				hasCap = true;
			} else if (capFloorCd.equals("2")){
				hasFloor = true;
			} else if (capFloorCd.equals("3")){
				hasCap = true;
				hasFloor = true;
			} 
			
			UnderlyingInfo[] underlyingInfos = new UnderlyingInfo[undNum];
			for (int undIndex = 0; undIndex < undNum; undIndex++){
				underlyingInfos[undIndex] = RateUnderlyingInfo.valueOf(
						modelTypes[undIndex], tenors[undIndex], 
						swapCouponFrequencies[undIndex], rateTypes[undIndex]);
			}
			
			if (legTypeCd.equals("FLOAT")){
				legCouponInfos[legIndex] = new LegFloatingCouponInfo(payRcv,
						couponType, underlyingType, conditionType, 
						underlyingInfos, 
						leverages, spreads);
	    	} else if (legTypeCd.equals("FIXED")){
				legCouponInfos[legIndex] = new LegFixedCouponInfo(payRcv,
						couponType, underlyingType, conditionType, 
						spreads);
	    	} else {
	    		legCouponInfos[legIndex] = new LegStructuredCouponInfo(payRcv,
						couponType, underlyingType, conditionType,
						underlyingInfos, 
						upperLimits, lowerLimits, inCouponRates, outCouponRates, 
						hasCap, cap, hasFloor, floor, 
						leverages, spreads);
	    	}					
		}		
		
		return legCouponInfos;
	}
	
	public static LegScheduleInfo[] getLegScheduleInfos(
			ProductLegModel[] productLegModels,
    		List[] productScheduleLists){
		
		LegScheduleInfo[] legScheduleInfos = 
				new LegScheduleInfo[productLegModels.length];
		
		for (int legIndex = 0; legIndex < productLegModels.length; legIndex++){			
			DayCountFraction dcf = DayCountFraction.valueOf(
					productLegModels[legIndex].getDcf());
			
			List productScheduleList = productScheduleLists[legIndex];
			PaymentPeriod[] periods = new PaymentPeriod[productScheduleList.size()];
			for (int periodIndex = 0; periodIndex < productScheduleList.size(); periodIndex++){
				ProductScheduleModel productSchedule = 
						(ProductScheduleModel) productScheduleList.get(periodIndex);
				
				periods[periodIndex] = new PaymentPeriod(
						Date.valueOf(productSchedule.getCouponResetDt()), 
						Date.valueOf(productSchedule.getCouponStrtDt()),
						Date.valueOf(productSchedule.getCouponEndDt()),
						Date.valueOf(productSchedule.getCouponPayDt()));
				
			}
			legScheduleInfos[legIndex] = new LegScheduleInfo(periods, dcf);
		}
		
		return legScheduleInfos;
	}
	
	public static LegAmortizationInfo[] getLegAmortizationInfos(
			ProductLegModel[] productLegModels){
		LegAmortizationInfo[] legAmortizationInfos = new LegAmortizationInfo[productLegModels.length];
		
		for (int legIndex = 0; legIndex < productLegModels.length; legIndex++){
			ProductLegModel productLegModel = productLegModels[legIndex];
			Money principal = Money.valueOf(productLegModel.getCcyCd(), 
					Double.parseDouble(productLegModel.getNotionalPrincipal()));
			legAmortizationInfos[legIndex] = new LegAmortizationInfo(principal);
		}
		
		return legAmortizationInfos;
	}
	
	public static LegDataInfo[] getLegDataInfos(
			ProductLegModel[] productLegModels,
			ProductLegDataModel[] productLegDataModels){
		
		LegDataInfo[] legDataInfos = new LegDataInfo[productLegModels.length];
		
		for (int legIndex = 0; legIndex < productLegModels.length; legIndex++){			
			ProductLegDataModel productLegDataModel = productLegDataModels[legIndex];
			String dateStr = productLegDataModel.getNextCouponPayDt();
			if (productLegDataModel == null || dateStr == null){
				legDataInfos[legIndex] = new LegDataInfo();
			} else {				
				legDataInfos[legIndex] = new LegDataInfo(Date.valueOf(dateStr));
				
				legDataInfos[legIndex].setNextCouponRate(
						Double.parseDouble(productLegDataModel.getNextCoupon()));
				legDataInfos[legIndex].setCumulatedAccrualDays(
	    				Integer.parseInt(productLegDataModel.getAccrualDayCnt()));
	    		legDataInfos[legIndex].setCumulatedAvgCoupon(
	    				Double.parseDouble(productLegDataModel.getAccumulateAvgCoupon()));
			}						
		}
		
		return legDataInfos;
	}
	
	public static OptionInfo getOptionInfo(ProductInfoModel productInfoModel,
    		List productOptionScheduleList){
		
		Date[] exerciseDates = new Date[productOptionScheduleList.size()];
		double[] exercisePrices = new double[productOptionScheduleList.size()];
		for (int j = 0; j < productOptionScheduleList.size(); j++){
			ProductOptionScheduleModel productOptionScheduleModel = 
					(ProductOptionScheduleModel) productOptionScheduleList.get(j);
			
			exerciseDates[j] = Date.valueOf(productOptionScheduleModel.getOptionStrtDt());
			exercisePrices[j] = Double.parseDouble(productOptionScheduleModel.getStrike());
		}
		
		//OptionType
		OptionType optionType = null;        		
		if (productInfoModel.getOptionTypeCd().equals("C")){
			optionType = OptionType.CALL;
		} else if (productInfoModel.getOptionTypeCd().equals("P")){
			optionType = OptionType.PUT;
		} else {
			optionType = OptionType.NONE;
		}
		
		return new OptionInfo(optionType, exerciseDates, exercisePrices);
	}

	public static long getRandomSeed(String instrumentCd, Date date){
		if (date == null || instrumentCd == null) 
			return System.currentTimeMillis();
		String code = instrumentCd;
		int usedCharCnt = 3;		
		if (code.length() >= usedCharCnt) {
			code = instrumentCd.substring(instrumentCd.length() - usedCharCnt);
		}

		StringBuffer seed = new StringBuffer(date.toString().substring(5)); // mmdd
		for (int i = 0; i < code.length(); ++i) {
			seed.append((int)code.charAt(i));
		}
		return Long.parseLong(seed.toString());
	}
}
