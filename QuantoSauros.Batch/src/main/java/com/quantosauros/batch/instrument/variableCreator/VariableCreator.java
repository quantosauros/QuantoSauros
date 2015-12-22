package com.quantosauros.batch.instrument.variableCreator;

import java.util.List;

import com.quantosauros.batch.dao.ProductInfoDao;
import com.quantosauros.batch.dao.ProductLegDao;
import com.quantosauros.batch.dao.ProductLegDataDao;
import com.quantosauros.batch.dao.ProductOptionScheduleDao;
import com.quantosauros.batch.dao.ProductScheduleDao;
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

	public static ProductInfo getProductInfo(ProductInfoDao productInfoDao){
		
		return new ProductInfo(
				Date.valueOf(productInfoDao.getIssueDt()),
				Date.valueOf(productInfoDao.getMrtyDt()),
				Currency.getInstance(productInfoDao.getCcyCd()));		
	}
	
	public static LegCouponInfo[] getLegCouponInfos(
			ProductInfoDao productInfoDao,
    		ProductLegDao[] productLegDaos,
    		List<ProductScheduleDao>[] productScheduleLists){
		
		LegCouponInfo[] legCouponInfos = new LegCouponInfo[productLegDaos.length];
				
		for (int legIndex = 0; legIndex < productLegDaos.length; legIndex++){
			PayRcv payRcv = TypeDef.getPayRcv(productLegDaos[legIndex].getPayRcvCd());
			String legTypeCd = productLegDaos[legIndex].getLegTypeCd();
			ProductLegDao productLegDao = productLegDaos[legIndex];
			List<ProductScheduleDao> productScheduleList = productScheduleLists[legIndex];
		
			UnderlyingType underlyingType = 
					TypeDef.getUnderlyingType(productLegDaos[legIndex].getUnderlyingType());
			ConditionType conditionType = 
					TypeDef.getConditionType(productLegDaos[legIndex].getConditionType());
						
			int undNum = TypeDef.getNumOfUnderNum(underlyingType);		
			int condiNum = TypeDef.getNumOfCondition(conditionType);
			
			//Model Type
			ModelType[] modelTypes = new ModelType[undNum];			
			String ircCd1 = productLegDaos[legIndex].getCouponIrcCd1();
			String ircCd2 = productLegDaos[legIndex].getCouponIrcCd2();
			String ircCd3 = productLegDaos[legIndex].getCouponIrcCd3();
			String undType = productLegDaos[legIndex].getUnderlyingType();
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
					tenors[0] = Vertex.getVertex(productLegDao.getCouponIrcMrtyCd1());
					swapCouponFrequencies[0] = 
							Frequency.valueOf(productLegDao.getCouponIrcCouponFreqCd1());
					rateTypes[0] = TypeDef.getRateType(productLegDao.getCouponIrcTypeCd1());
					break;
				case 2 :
					tenors[0] = Vertex.getVertex(productLegDao.getCouponIrcMrtyCd1());
					swapCouponFrequencies[0] = 
							Frequency.valueOf(productLegDao.getCouponIrcCouponFreqCd1());
					rateTypes[0] = TypeDef.getRateType(productLegDao.getCouponIrcTypeCd1());
					tenors[1] =Vertex.getVertex(productLegDao.getCouponIrcMrtyCd2());
					swapCouponFrequencies[1] = 
							Frequency.valueOf(productLegDao.getCouponIrcCouponFreqCd2());
					rateTypes[1] = TypeDef.getRateType(productLegDao.getCouponIrcTypeCd2());
					break;
				case 3 : 
					tenors[0] = Vertex.getVertex(productLegDao.getCouponIrcMrtyCd1());
					swapCouponFrequencies[0] = 
							Frequency.valueOf(productLegDao.getCouponIrcCouponFreqCd1());
					rateTypes[0] = TypeDef.getRateType(productLegDao.getCouponIrcTypeCd1());
					tenors[1] =Vertex.getVertex(productLegDao.getCouponIrcMrtyCd2());
					swapCouponFrequencies[1] = 
							Frequency.valueOf(productLegDao.getCouponIrcCouponFreqCd2());
					rateTypes[1] = TypeDef.getRateType(productLegDao.getCouponIrcTypeCd2());
					tenors[2] =Vertex.getVertex(productLegDao.getCouponIrcMrtyCd3());
					swapCouponFrequencies[2] = 
							Frequency.valueOf(productLegDao.getCouponIrcCouponFreqCd3());
					rateTypes[2] = TypeDef.getRateType(productLegDao.getCouponIrcTypeCd3());
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
				ProductScheduleDao productScheduleDao = productScheduleList.get(schIndex);
				couponType[schIndex] = TypeDef.getCouponType(productScheduleDao.getCouponType());				
				spreads[schIndex] = Double.parseDouble(productScheduleDao.getFixedCoupon());
				inCouponRates[schIndex] = Double.parseDouble(productScheduleDao.getInCoupon());
				outCouponRates[schIndex] = Double.parseDouble(productScheduleDao.getOutCoupon());
				cap[schIndex] = Double.parseDouble(productScheduleDao.getCap());
				floor[schIndex] = Double.parseDouble(productScheduleDao.getFloor());
				
				switch (undNum){
					case 1 :
						leverages[0][schIndex] = Double.parseDouble(productScheduleDao.getLeverage1());
						break;
					case 2 :
						leverages[0][schIndex] = Double.parseDouble(productScheduleDao.getLeverage1());
						leverages[1][schIndex] = Double.parseDouble(productScheduleDao.getLeverage2());
						break;
					case 3 : 
						leverages[0][schIndex] = Double.parseDouble(productScheduleDao.getLeverage1());
						leverages[1][schIndex] = Double.parseDouble(productScheduleDao.getLeverage2());
						leverages[2][schIndex] = Double.parseDouble(productScheduleDao.getLeverage3());
						break;
				}
						
				switch (condiNum){
					case 1 :
						upperLimits[0][schIndex] = Double.parseDouble(productScheduleDao.getUpperBound1());
						lowerLimits[0][schIndex] = Double.parseDouble(productScheduleDao.getLowerBound1());
						break;
					case 2 :
						upperLimits[0][schIndex] = Double.parseDouble(productScheduleDao.getUpperBound1());
						lowerLimits[0][schIndex] = Double.parseDouble(productScheduleDao.getLowerBound1());
						upperLimits[1][schIndex] = Double.parseDouble(productScheduleDao.getUpperBound2());
						lowerLimits[1][schIndex] = Double.parseDouble(productScheduleDao.getLowerBound2());
						break;
				}								
			}
			
			boolean hasCap = false;	
			boolean hasFloor = false;			
			String capFloorCd =	productLegDao.getCapFloorCd();
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
			ProductLegDao[] productLegDaos,
    		List[] productScheduleLists){
		
		LegScheduleInfo[] legScheduleInfos = 
				new LegScheduleInfo[productLegDaos.length];
		
		for (int legIndex = 0; legIndex < productLegDaos.length; legIndex++){			
			DayCountFraction dcf = DayCountFraction.valueOf(
					productLegDaos[legIndex].getDcf());
			
			List productScheduleList = productScheduleLists[legIndex];
			PaymentPeriod[] periods = new PaymentPeriod[productScheduleList.size()];
			for (int periodIndex = 0; periodIndex < productScheduleList.size(); periodIndex++){
				ProductScheduleDao productSchedule = 
						(ProductScheduleDao) productScheduleList.get(periodIndex);
				
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
			ProductLegDao[] productLegDaos){
		LegAmortizationInfo[] legAmortizationInfos = new LegAmortizationInfo[productLegDaos.length];
		
		for (int legIndex = 0; legIndex < productLegDaos.length; legIndex++){
			ProductLegDao productLegDao = productLegDaos[legIndex];
			Money principal = Money.valueOf(productLegDao.getCcyCd(), 
					Double.parseDouble(productLegDao.getNotionalPrincipal()));
			legAmortizationInfos[legIndex] = new LegAmortizationInfo(principal);
		}
		
		return legAmortizationInfos;
	}
	
	public static LegDataInfo[] getLegDataInfos(
			ProductLegDao[] productLegDaos,
			ProductLegDataDao[] productLegDataDaos){
		
		LegDataInfo[] legDataInfos = new LegDataInfo[productLegDaos.length];
		
		for (int legIndex = 0; legIndex < productLegDaos.length; legIndex++){			
			ProductLegDataDao productLegDataDao = productLegDataDaos[legIndex];
			String dateStr = productLegDataDao.getNextCouponPayDt();
			if (productLegDataDao == null || dateStr == null){
				legDataInfos[legIndex] = new LegDataInfo();
			} else {				
				legDataInfos[legIndex] = new LegDataInfo(Date.valueOf(dateStr));
				
				legDataInfos[legIndex].setNextCouponRate(
						Double.parseDouble(productLegDataDao.getNextCoupon()));
				legDataInfos[legIndex].setCumulatedAccrualDays(
	    				Integer.parseInt(productLegDataDao.getAccrualDayCnt()));
	    		legDataInfos[legIndex].setCumulatedAvgCoupon(
	    				Double.parseDouble(productLegDataDao.getAccumulateAvgCoupon()));
			}						
		}
		
		return legDataInfos;
	}
	
	public static OptionInfo getOptionInfo(ProductInfoDao productInfoDao,
    		List productOptionScheduleList){
		
		Date[] exerciseDates = new Date[productOptionScheduleList.size()];
		double[] exercisePrices = new double[productOptionScheduleList.size()];
		for (int j = 0; j < productOptionScheduleList.size(); j++){
			ProductOptionScheduleDao productOptionSchedule = 
					(ProductOptionScheduleDao) productOptionScheduleList.get(j);
			
			exerciseDates[j] = Date.valueOf(productOptionSchedule.getOptionStrtDt());
			exercisePrices[j] = Double.parseDouble(productOptionSchedule.getStrike());
		}
		
		//OptionType
		OptionType optionType = null;        		
		if (productInfoDao.getOptionTypeCd().equals("C")){
			optionType = OptionType.CALL;
		} else if (productInfoDao.getOptionTypeCd().equals("P")){
			optionType = OptionType.PUT;
		} else {
			optionType = OptionType.NONE;
		}
		
		return new OptionInfo(optionType, exerciseDates, exercisePrices);
	}
//	
//	public static QuantoInfo getQuantoInfo(ProductInfoDao productInfoDao,
//			ProductLegDao[] productLegDaos){
//				
//		String productCcy = productInfoDao.getCcyCd();
//		double[][] fxAssetCorrelations = new double[productLegDaos.length][];
//		double[][] fxVolatilities = new double[productLegDaos.length][];
//		
//		for (int legIndex = 0; legIndex < productLegDaos.length; legIndex++){
//			int undNum = 0;	
//			ProductLegDao productLegDao = productLegDaos[legIndex];
//			
//			String[] ccy = new String[] {
//					productLegDao.getCouponIrcCcy1(),
//					productLegDao.getCouponIrcCcy2(),
//					productLegDao.getCouponIrcCcy3(),
//			};
//			
//			if (ccy[2] == null){
//				undNum = 2;
//				if (ccy[1] == null){
//					undNum = 1;
//					if (ccy[0] == null){
//						undNum = 0;
//					}
//				}				
//			}
//			fxAssetCorrelations[legIndex] = new double[undNum];
//			fxVolatilities[legIndex] = new double[undNum];
//						
//			//TODO quanto correlation, fx vol
//			for (int undIndex = 0; undIndex < undNum; undIndex++){
//				if (productCcy.equals(ccy[undIndex])){
//					fxAssetCorrelations[legIndex][undIndex] = 1;
//					fxVolatilities[legIndex][undIndex] = 0;
//				} else {
//					fxAssetCorrelations[legIndex][undIndex] = 0;
//					fxVolatilities[legIndex][undIndex] = 0.1;
//				}
//			}
//		}
//		
//		return new QuantoInfo(fxAssetCorrelations, fxVolatilities);
//	}
    
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
