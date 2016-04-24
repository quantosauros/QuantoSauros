package com.quantosauros.jpl.result;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.quantosauros.common.TypeDef.ResultType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.jpl.instrument.AbstractProduct;

public class AbstractResult {

	private HashMap<ResultType, Object> map;
	
	public AbstractResult() {
		map = new HashMap<ResultType, Object>();
	}

	public HashMap<ResultType, Object> getResults(
			AbstractProduct abstractProduct){
		
		getProductInfoResult(abstractProduct);
		getLegPrice(abstractProduct);
		getCouponResult(abstractProduct);
		getTenorResult(abstractProduct);
		getExerciseIndexResult(abstractProduct);
		getReferenceRateResult(abstractProduct);
		getDiscountRateResult(abstractProduct);
		
		return map;
	}
	
	public void getProductInfoResult(AbstractProduct abstractProduct){
		log("==PRODUCT INFORMATION==");
	}
		
	public void getLegPrice(AbstractProduct abstractProduct){
		log("==LEG PRICE RESULT==");
		List<Object> list = new ArrayList<Object>();
		
		for (int legIndex = 0; legIndex < abstractProduct.getLegNum(); legIndex++){
			Money price = abstractProduct.getLegPrice(legIndex);
			list.add(price);
			log("Leg(" + legIndex + "): " + price);
		}
		
		map.put(ResultType.LEGPRICE_RESULT, list);
	}
	
	public void getCouponResult(AbstractProduct abstractProduct){		
		log("==COUPON RESULT==");		
		ResultDto[] resultDto = new ResultDto[abstractProduct.getLegNum()];
		for (int legIndex = 0; legIndex < abstractProduct.getLegNum(); legIndex++){
			//[simIndex][periodIndex]
			log("=LEG" + legIndex + "=");
			resultDto[legIndex] = new ResultDto(abstractProduct.getPeriodNum());			
			double[][] payoffs = abstractProduct.getCoupon(legIndex);
			for (int periodIndex = 0; periodIndex < abstractProduct.getPeriodNum(); periodIndex++){
				
				DescriptiveStatistics stats = new DescriptiveStatistics();
				for (int simIndex = 0; simIndex < abstractProduct.getSimNum(); simIndex++){									
					stats.addValue(payoffs[simIndex][periodIndex]);
				}
				double avg = stats.getMean();
				double std = stats.getStandardDeviation();
				
				resultDto[legIndex].setAvgValue(periodIndex, avg);
				resultDto[legIndex].setStdValue(periodIndex, std);
				
				log("Period(" + periodIndex + "): " + avg + ", " + std);		
			}			
		}		
		map.put(ResultType.COUPON_RESULT, resultDto);
	}
	
	public void getTenorResult(AbstractProduct abstractProduct){
		log("==TENOR RESULT==");
		ResultDto[] resultDto = new ResultDto[abstractProduct.getLegNum()];
		for (int legIndex = 0; legIndex < abstractProduct.getLegNum(); legIndex++){
			log("=LEG" + legIndex + "=");
			//[simIndex][periodIndex]
			resultDto[legIndex] = new ResultDto(abstractProduct.getPeriodNum());
			double[][] tenors = abstractProduct.getTenor(legIndex);
			for (int periodIndex = 0; periodIndex < abstractProduct.getPeriodNum(); periodIndex++){
				
				DescriptiveStatistics stats = new DescriptiveStatistics();
				for (int simIndex = 0; simIndex < abstractProduct.getSimNum(); simIndex++){									
					stats.addValue(tenors[simIndex][periodIndex]);
				}
				double avg = stats.getMean();
				double std = stats.getStandardDeviation();
				
				resultDto[legIndex].setAvgValue(periodIndex, avg);
				resultDto[legIndex].setStdValue(periodIndex, std);
				
				log("Period(" + periodIndex + "): " + avg + ", " + std);		
			}			
		}	
		map.put(ResultType.TENOR_RESULT, resultDto);
	}
	
	public void getExerciseIndexResult(AbstractProduct abstractProduct){
		log("==EXERCISE TIME RESULT==");
		int[] exerciseIndex = abstractProduct.getExerciseIndex();
		if (exerciseIndex == null){
			log("doesn't have exercise");
		} else {
			ResultDto resultDto = new ResultDto(abstractProduct.getPeriodNum() + 1);
			
			DescriptiveStatistics stats = new DescriptiveStatistics();		
			for (int simIndex = 0; simIndex < exerciseIndex.length; simIndex++){
				stats.addValue(exerciseIndex[simIndex]);
			}
			double avg = stats.getMean();
			double std = stats.getStandardDeviation();
			
			log("Average Exercise Index: " + avg + ", " + std);		
			//double[] sortedValue = stats.getSortedValues();		
									
			int numofExceedIndex= 0;
			int[] exIndex = new int[abstractProduct.getPeriodNum()+1];
			for (int simIndex = 0; simIndex < exerciseIndex.length; simIndex++){
				if (exerciseIndex[simIndex] >= avg){
					numofExceedIndex++;
				}
				exIndex[exerciseIndex[simIndex]]++;
			}
			log ("Num of the Exceed Exercise Index: " + numofExceedIndex);	
			for (int periodIndex = 0; periodIndex < abstractProduct.getPeriodNum()+1; periodIndex++){
				resultDto.setAvgValue(periodIndex, (double)exIndex[periodIndex] / abstractProduct.getSimNum());			
				log ("Period(" + periodIndex+"): " + (double)exIndex[periodIndex] / abstractProduct.getSimNum());
			}
			map.put(ResultType.EXERCISEINDEX_RESULT, resultDto);	
		}		
	}
	
	public void getReferenceRateResult(AbstractProduct abstractProduct){
		log("==REFERENCE RATE RESULT==");
		ResultDto[][] resultDto = new ResultDto[abstractProduct.getLegNum()][];
		int[] undNum = abstractProduct.getUnderlyingNum();
		for (int legIndex = 0; legIndex < undNum.length; legIndex++){
			log("=LEG" + legIndex + "=");
			resultDto[legIndex] = new ResultDto[undNum[legIndex]];
			for (int undIndex = 0; undIndex < undNum[legIndex]; undIndex++){
				log("=UNDERLYING" + undIndex + "=");
				double[][][] ref = abstractProduct.getRefRates(legIndex, undIndex);
				resultDto[legIndex][undIndex] = new ResultDto(ref[0].length);
				for (int periodIndex = 0; periodIndex < ref[0].length; periodIndex++){
					int firstIndex = 0;
					int lastIndex = ref[0][periodIndex].length - 1;
					DescriptiveStatistics stats1 = new DescriptiveStatistics();		
					DescriptiveStatistics stats2 = new DescriptiveStatistics();
					
					for (int simIndex = 0; simIndex < ref.length; simIndex++){
						stats1.addValue(ref[simIndex][periodIndex][firstIndex]);
						stats2.addValue(ref[simIndex][periodIndex][lastIndex]);						
					}					
					double avgFirst = stats1.getMean();
					double stdFirst = stats1.getStandardDeviation();
					double avgLast = stats2.getMean();
					double stdLast = stats2.getStandardDeviation();
					
					resultDto[legIndex][undIndex].setAvgValue(periodIndex, avgFirst);
					resultDto[legIndex][undIndex].setStdValue(periodIndex, stdFirst);
					
					log("Period(" + periodIndex + "): " +
							avgFirst + ", " + stdFirst + ", " +
							avgLast + ", " + stdLast);
				}
			}			
		}
		map.put(ResultType.REFERENCERATE_RESULT, resultDto);
	}
	
	public void getDiscountRateResult(AbstractProduct abstractProduct){
		double[][] discount = abstractProduct.getDiscounts();
		log("==DISCOUNT FACTOR RESULT==");
		ResultDto resultDto = new ResultDto(discount[0].length);
		for (int periodIndex = 0; periodIndex < discount[0].length; periodIndex++){			
			DescriptiveStatistics stats = new DescriptiveStatistics();			
			for (int j = 0; j < discount.length; j++){
				stats.addValue(discount[j][periodIndex]);
			}
			double avg = stats.getMean();
			double std = stats.getStandardDeviation();
			
			resultDto.setAvgValue(periodIndex, avg);
			resultDto.setStdValue(periodIndex, std);			
			
			log("Period(" + periodIndex + "): " + avg + ", " + std);
		}
		map.put(ResultType.DISCOUNTRATE_RESULT, resultDto);
	}	
	
	private static void log(String str){		
		StringBuffer sb = new StringBuffer();
		DateFormat ddf = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG);
		
		sb.append(ddf.format(new Date()));
		sb.append(": ");
		sb.append(str);
//		System.out.println(sb);
	}
}
