package com.quantosauros.common.calibration;

import java.io.Serializable;
import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;

/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2014
* - Creator: Jihoon Lee
------------------------------------------------------------------------------*/
/**
 * {@code SpotCurveCalculator} 클래스는 YTM 금리커브에서 BootStrapping을 이용하여 Spot금리커브를
 * 도출하는 클래스이다.
 * 국가코드와 입수된 YTM금리 시장타입을 이용하여 시장 컨벤션을 결정하고, 결정된 시장 컨벤션을 이용하여 알맞은 Spot금리커브를
 * 도출한다.
 * Bootstrapping에서 세부적인 방법론은 Discount Factor를 Linear Interpolation을 이용한 방법론을 
 * 사용하였다.
 * 
 * @author Jihoon Lee 
 *
 */
public class SpotCurveCalculator implements Serializable {

	public static InterestRateCurve calculate(InterestRateCurve ytmCurve){
		
		InterestRate[] ytmRates = ytmCurve.getSpotRates();
		InterestRate[] spotRates = new InterestRate[ytmRates.length];
		
		Date asOfDate = ytmCurve.getDate();
		DayCountFraction dcf = ytmCurve.getDayCountFraction();
		Frequency couponFrequency = Frequency.valueOf("Q");
		BusinessDayConvention businessDayConvention = 
				BusinessDayConvention.MODIFIED_FOLLOWING;
		int settlementDay = 2;		
		Calendar calendar = CalendarFactory.getInstance("KR", 0);
		
		Vertex ytmStartVertex = ytmRates[ytmRates.length - 1].getVertex();
		for (int i = 0; i < ytmRates.length; i++){			
			if (ytmRates[i].getFactorCode().equals("YTM")){
				ytmStartVertex = ytmRates[i].getVertex();
				break;
			}
		}
		if (ytmStartVertex.equals(ytmRates[ytmRates.length - 1].getVertex())){
			return ytmCurve;
		}
		
		//settlement Date 계산
		Date settlementDate = calendar.adjustDate(
				asOfDate.plusDays(settlementDay), businessDayConvention);
		
		//tenor 계산
		ArrayList<Double> tenorArray = new ArrayList<Double>();
		
		double[] tenor = new double[ytmRates.length];
		for (int i = 0; i < ytmRates.length; i++){
			Vertex vertex = ytmRates[i].getVertex();
			Date vertexDate = vertex.getDate(settlementDate);
			Date adjustedVertexDate = calendar.adjustDate(vertexDate, 
					businessDayConvention);
			tenor[i] = dcf.getYearFraction(asOfDate, adjustedVertexDate);
		}
		
		//bootstrapping 계산
		double[] df = new double[ytmRates.length];
		ArrayList<Double> DFArray = new ArrayList<Double>();
		boolean YTMCurveStart = false;
		int tmpInt = 0;
		for (int i = 0; i < ytmRates.length; i++){
			Vertex vertex = ytmRates[i].getVertex();
			double swapRate = ytmRates[i].getRate();
			if (ytmStartVertex.getVertex() <= vertex.getVertex()){
				YTMCurveStart = true;
			}
//					if (ytmStartVertex.equals(vertex) && !YTMCurveStart){
//						YTMCurveStart = true;
//					}			
			double spotRate = 0;			
			
			if (YTMCurveStart){
				//YTM
				int numOfSwapTenor = (int) (vertex.getVertex() *
						couponFrequency.getFrequencyInYear());	
				if (numOfSwapTenor == 1){
					spotRate = ytmRates[i].getRate();
					df[i] = Math.exp(-spotRate * tenor[i]);
					DFArray.add(Math.exp(-spotRate * tenor[i]));
				} else {
					double[] tmpTenor = new double[numOfSwapTenor];
					double[] tmpDF = new double[numOfSwapTenor];
					
					Vertex initialVertex = Vertex.valueOf("NONE");
					int tmpIndex = 0;
					for (int j = 0; j < numOfSwapTenor; j++){
						double yearFraction = couponFrequency.getInterval() * (j + 1);
						Vertex tmpVertex = initialVertex.addVertex(yearFraction);
						Date vertexDate = tmpVertex.getDate(settlementDate);
						Date adjustedVertexDate = calendar.adjustDate(vertexDate, 
								businessDayConvention);
						tmpTenor[j] = dcf.getYearFraction(asOfDate, adjustedVertexDate);
						if (!tenorArray.contains(tmpTenor[j])){
							tenorArray.add(tmpTenor[j]);
						}
						//tenor 와 tmpTenor를 비교하여 같은값인 tenor의 index에 해당하는 df값을 tmpDF에 입력					

						double targetTenor = tmpTenor[j];
//								double frequencyInYear = couponFrequency.getFrequencyInYear();
//								double tmp = Math.round(targetTenor * frequencyInYear * (j + 1)) / (frequencyInYear* (j + 1));
//								boolean flagForException = tmp < ytmStartVertex.getVertex()
//										 ? true : false;						
						
						int index = tenorArray.indexOf(targetTenor);
						if (DFArray.size() > index){
							tmpDF[tmpIndex] = DFArray.get(index);
							tmpIndex++;
						} else if (i == tmpInt){
							spotRate = ytmRates[i].getRate();
							//df[i] = Math.exp(-spotRate * targetTenor);
							DFArray.add(Math.exp(-spotRate * targetTenor));
							tmpDF[tmpIndex] = Math.exp(-spotRate * targetTenor);
//									double exceptionalDF = Math.exp(-ytmRates[i].getRate() * targetTenor);
//									DFArray.add(exceptionalDF);
//									tmpDF[tmpIndex] = exceptionalDF;
							tmpIndex++;
						}
						 
					}	
//							//예외처리
//							if (tmpIndex == 0){
//								for (int m = 0; m < tmpDF.length - 1; m++){
//									tmpDF[m] = Math.exp(- swapRate * tmpTenor[m]);
//								}
//								tmpIndex = tmpDF.length - 1;
//							}
					double coefficientOfUnknown = 1; 
					double coefficientOfDF = 1;
					int swapIndex = tmpDF.length - tmpIndex;
					
					for (int l = 0; l < tmpIndex; l++){
						double couponTenor = (l == 0) ? tmpTenor[l] : (tmpTenor[l] - tmpTenor[l-1]);
						coefficientOfDF +=  - swapRate * tmpDF[l] * couponTenor;					
					}
					
					double sumOfTenors = (tmpTenor[tmpIndex + swapIndex - 1] - tmpTenor[tmpIndex - 1]);
					double knownDF = tmpDF[tmpIndex - 1];
					for (int j = 0; j < swapIndex; j++){
						double couponTenor = tmpTenor[tmpIndex + j] - tmpTenor[tmpIndex + j - 1];
						double interpolationTenor = tmpTenor[tmpIndex + j] - tmpTenor[tmpIndex - 1];
						coefficientOfUnknown += swapRate * couponTenor * interpolationTenor / sumOfTenors;
						coefficientOfDF += + swapRate * couponTenor * interpolationTenor * knownDF / sumOfTenors 
								- swapRate * couponTenor * knownDF;
					}
					double unknownDF = coefficientOfDF / coefficientOfUnknown;
					for (int j = 0; j < swapIndex; j++){
						double interpolationTenor = tmpTenor[tmpIndex + j] - tmpTenor[tmpIndex - 1];
						tmpDF[tmpIndex + j] =  interpolationTenor * (unknownDF - knownDF) / sumOfTenors + knownDF;
						DFArray.add(tmpDF[tmpIndex + j]);						
					}
					
					df[i] = coefficientOfDF / coefficientOfUnknown;
					
					spotRate = - Math.log(unknownDF) / tmpTenor[tmpTenor.length - 1];
				}						
			} else {
				//Spot
				if (ytmRates[i].getVertex().isEqual(Vertex.valueOf("D1"))){
					tmpInt = 1;
					spotRate = ytmRates[i].getRate();
					df[i] = Math.exp(-spotRate * tenor[i]);
					tenorArray.add(tenor[i]);
					DFArray.add(Math.exp(-spotRate * tenor[i]));
				}
				//double number = Math.round(1.0 / tenor[i]);
				double adjustedTenor = Math.round(tenor[i] / couponFrequency.getInterval())
						* couponFrequency.getInterval();
				if (adjustedTenor == 0){
					adjustedTenor++;
				}
				int tmpNum = (int)(adjustedTenor/ couponFrequency.getInterval());				
				for (int k = 0; k < tmpNum; k++){
					boolean flag = couponFrequency.getInterval() * (k + 1) < adjustedTenor;
					if (flag){
						//없을때
						spotRate = ytmRates[i].getRate();
						double tmpTenor = couponFrequency.getInterval() * (k + 1);
						df[i] = Math.exp(-spotRate * tmpTenor);
						tenorArray.add(tmpTenor);
						DFArray.add(Math.exp(-spotRate * tmpTenor));
					} else {
						//일반케이스
						spotRate = ytmRates[i].getRate();
						df[i] = Math.exp(-spotRate * tenor[i]);
						tenorArray.add(tenor[i]);
						DFArray.add(Math.exp(-spotRate * tenor[i]));
					}
				}
			}
			//DF 계산뒤 zero rate = -log(DF)/tenor
			//결과값 저장			
			spotRates[i] = new InterestRate(vertex, spotRate, "SPOT");			
		}
		 
		return new InterestRateCurve(asOfDate, spotRates, Frequency.valueOf("C"), dcf);
	
		
	}
	
//	public static InterestRateCurve calculate(Date asOfDate,
//			String countryCode, MarketRateType rateType, InterestRate[] ytmRates, 
//			Vertex ytmStartVertex){
//		
//		InterestRate[] spotRates = new InterestRate[ytmRates.length];		
//		Calendar calendar = CalendarFactory.getInstance(countryCode, 0);
//		
//		//Convention - initial
//		DayCountFraction dcf = DayCountFraction.ACTUAL_360;
//		BusinessDayConvention businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//		int settlementDay = 2;
//		Frequency couponFrequency = Frequency.valueOf("S");
//		
//		if (rateType == MarketRateType.IRS){
//			//IRS
//			if (countryCode.equals("KR")){
//				dcf = DayCountFraction.ACTUAL_365;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("Q");
//			} else if (countryCode.equals("US")){
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("EU")){
//				//TODO				
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("JP")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("CH") || countryCode.equals("CN")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else {
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			}				
//		} else if (rateType == MarketRateType.CRS){
//			//CRS
//			if (countryCode.equals("KR")){
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("EU")){
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("JP")){
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("CH")){
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else {
//				dcf = DayCountFraction.ACTUAL_360;
//				settlementDay = 2;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			}
//		} else if (rateType == MarketRateType.TBOND){
//			//국고채
//			if (countryCode.equals("KR")){
//				dcf = DayCountFraction.ACTUAL_365;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("US")){
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("EU")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("JP")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("CH")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else {
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			}
//		} else if (rateType == MarketRateType.CBOND){
//			//회사채
//			if (countryCode.equals("KR")){
//				dcf = DayCountFraction.ACTUAL_365;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("Q");
//			} else if (countryCode.equals("US")){
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("EU")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;				
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("JP")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;				
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else if (countryCode.equals("CH")){
//				//TODO
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;				
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			} else {
//				dcf = DayCountFraction.ACTUAL_ACTUAL;
//				settlementDay = 1;				
//				businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
//				couponFrequency = Frequency.valueOf("S");
//			}
//		}
//				
//		//settlement Date 계산
//		Date settlementDate = calendar.adjustDate(
//				asOfDate.plusDays(settlementDay), businessDayConvention);
//		
//		//tenor 계산
//		ArrayList<Double> tenorArray = new ArrayList<Double>();
//		
//		double[] tenor = new double[ytmRates.length];
//		for (int i = 0; i < ytmRates.length; i++){
//			Vertex vertex = ytmRates[i].getVertex();
//			Date vertexDate = vertex.getDate(settlementDate);
//			Date adjustedVertexDate = calendar.adjustDate(vertexDate, 
//					businessDayConvention);
//			tenor[i] = dcf.getYearFraction(asOfDate, adjustedVertexDate);
//		}
//		
//		//bootstrapping 계산
//		double[] df = new double[ytmRates.length];
//		ArrayList<Double> DFArray = new ArrayList<Double>();
//		boolean YTMCurveStart = false;
//		int tmpInt = 0;
//		for (int i = 0; i < ytmRates.length; i++){
//			Vertex vertex = ytmRates[i].getVertex();
//			double swapRate = ytmRates[i].getRate();
//			if (ytmStartVertex.getVertex() <= vertex.getVertex()){
//				YTMCurveStart = true;
//			}
////			if (ytmStartVertex.equals(vertex) && !YTMCurveStart){
////				YTMCurveStart = true;
////			}			
//			double spotRate = 0;			
//			
//			if (YTMCurveStart){
//				//YTM
//				int numOfSwapTenor = (int) (vertex.getVertex() *
//						couponFrequency.getFrequencyInYear());	
//				if (numOfSwapTenor == 1){
//					spotRate = ytmRates[i].getRate();
//					df[i] = Math.exp(-spotRate * tenor[i]);
//					DFArray.add(Math.exp(-spotRate * tenor[i]));
//				} else {
//					double[] tmpTenor = new double[numOfSwapTenor];
//					double[] tmpDF = new double[numOfSwapTenor];
//					
//					Vertex initialVertex = Vertex.valueOf("NONE");
//					int tmpIndex = 0;
//					for (int j = 0; j < numOfSwapTenor; j++){
//						double yearFraction = couponFrequency.getInterval() * (j + 1);
//						Vertex tmpVertex = initialVertex.addVertex(yearFraction);
//						Date vertexDate = tmpVertex.getDate(settlementDate);
//						Date adjustedVertexDate = calendar.adjustDate(vertexDate, 
//								businessDayConvention);
//						tmpTenor[j] = dcf.getYearFraction(asOfDate, adjustedVertexDate);
//						if (!tenorArray.contains(tmpTenor[j])){
//							tenorArray.add(tmpTenor[j]);
//						}
//						//tenor 와 tmpTenor를 비교하여 같은값인 tenor의 index에 해당하는 df값을 tmpDF에 입력					
//
//						double targetTenor = tmpTenor[j];
////						double frequencyInYear = couponFrequency.getFrequencyInYear();
////						double tmp = Math.round(targetTenor * frequencyInYear * (j + 1)) / (frequencyInYear* (j + 1));
////						boolean flagForException = tmp < ytmStartVertex.getVertex()
////								 ? true : false;						
//						
//						int index = tenorArray.indexOf(targetTenor);
//						if (DFArray.size() > index){
//							tmpDF[tmpIndex] = DFArray.get(index);
//							tmpIndex++;
//						} else if (i == tmpInt){
//							spotRate = ytmRates[i].getRate();
//							//df[i] = Math.exp(-spotRate * targetTenor);
//							DFArray.add(Math.exp(-spotRate * targetTenor));
//							tmpDF[tmpIndex] = Math.exp(-spotRate * targetTenor);
////							double exceptionalDF = Math.exp(-ytmRates[i].getRate() * targetTenor);
////							DFArray.add(exceptionalDF);
////							tmpDF[tmpIndex] = exceptionalDF;
//							tmpIndex++;
//						}
//						 
//					}	
////					//예외처리
////					if (tmpIndex == 0){
////						for (int m = 0; m < tmpDF.length - 1; m++){
////							tmpDF[m] = Math.exp(- swapRate * tmpTenor[m]);
////						}
////						tmpIndex = tmpDF.length - 1;
////					}
//					double coefficientOfUnknown = 1; 
//					double coefficientOfDF = 1;
//					int swapIndex = tmpDF.length - tmpIndex;
//					
//					for (int l = 0; l < tmpIndex; l++){
//						double couponTenor = (l == 0) ? tmpTenor[l] : (tmpTenor[l] - tmpTenor[l-1]);
//						coefficientOfDF +=  - swapRate * tmpDF[l] * couponTenor;					
//					}
//					
//					double sumOfTenors = (tmpTenor[tmpIndex + swapIndex - 1] - tmpTenor[tmpIndex - 1]);
//					double knownDF = tmpDF[tmpIndex - 1];
//					for (int j = 0; j < swapIndex; j++){
//						double couponTenor = tmpTenor[tmpIndex + j] - tmpTenor[tmpIndex + j - 1];
//						double interpolationTenor = tmpTenor[tmpIndex + j] - tmpTenor[tmpIndex - 1];
//						coefficientOfUnknown += swapRate * couponTenor * interpolationTenor / sumOfTenors;
//						coefficientOfDF += + swapRate * couponTenor * interpolationTenor * knownDF / sumOfTenors 
//								- swapRate * couponTenor * knownDF;
//					}
//					double unknownDF = coefficientOfDF / coefficientOfUnknown;
//					for (int j = 0; j < swapIndex; j++){
//						double interpolationTenor = tmpTenor[tmpIndex + j] - tmpTenor[tmpIndex - 1];
//						tmpDF[tmpIndex + j] =  interpolationTenor * (unknownDF - knownDF) / sumOfTenors + knownDF;
//						DFArray.add(tmpDF[tmpIndex + j]);						
//					}
//					
//					df[i] = coefficientOfDF / coefficientOfUnknown;
//					
//					spotRate = - Math.log(unknownDF) / tmpTenor[tmpTenor.length - 1];
//				}						
//			} else {
//				//Spot
//				if (ytmRates[i].getVertex().isEqual(Vertex.valueOf("D1"))){
//					tmpInt = 1;
//					spotRate = ytmRates[i].getRate();
//					df[i] = Math.exp(-spotRate * tenor[i]);
//					tenorArray.add(tenor[i]);
//					DFArray.add(Math.exp(-spotRate * tenor[i]));
//				}
//				//double number = Math.round(1.0 / tenor[i]);
//				double adjustedTenor = Math.round(tenor[i] / couponFrequency.getInterval())
//						* couponFrequency.getInterval();
//				if (adjustedTenor == 0){
//					adjustedTenor++;
//				}
//				int tmpNum = (int)(adjustedTenor/ couponFrequency.getInterval());				
//				for (int k = 0; k < tmpNum; k++){
//					boolean flag = couponFrequency.getInterval() * (k + 1) < adjustedTenor;
//					if (flag){
//						//없을때
//						spotRate = ytmRates[i].getRate();
//						double tmpTenor = couponFrequency.getInterval() * (k + 1);
//						df[i] = Math.exp(-spotRate * tmpTenor);
//						tenorArray.add(tmpTenor);
//						DFArray.add(Math.exp(-spotRate * tmpTenor));
//					} else {
//						//일반케이스
//						spotRate = ytmRates[i].getRate();
//						df[i] = Math.exp(-spotRate * tenor[i]);
//						tenorArray.add(tenor[i]);
//						DFArray.add(Math.exp(-spotRate * tenor[i]));
//					}
//				}
//			}
//			//DF 계산뒤 zero rate = -log(DF)/tenor
//			//결과값 저장			
//			spotRates[i] = new InterestRate(vertex, spotRate);			
//		}
//		 
//		return new InterestRateCurve(asOfDate, spotRates, Frequency.valueOf("C"), dcf);
//	}
}
