package com.quantosauros.jpl.engine.method.model.hullwhite;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.hullwhite.HWVolatilitySurface;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;

public class HullWhiteStripping {
	//[swaptionN][0: swaption Maturity, 1: swaption Tenor]
	private double[][] _swaptionInfo;
	private Date _asOfDate;
	private HWVolatilitySurface _hwSurface;
	
	public HullWhiteStripping(Date asOfDate, Date maturityDate,
			Date[] exerciseDates, HWVolatilitySurface hwSurface) {
		
		_asOfDate = asOfDate;
		_hwSurface = hwSurface;
		
		String countryCode = "KR";
		Calendar calendar = CalendarFactory.getInstance(countryCode, 0);
		BusinessDayConvention convention = BusinessDayConvention.FOLLOWING;
		DayCountFraction dcf = DayCountFraction.ACTUAL_365;
		int settlementDay = 2;
		int numOfMonth = 3;
		
		Date adjustedAsOfDate = calendar.adjustDate(asOfDate, convention);
		Date adjustedSettlementDate = calendar.adjustDate(
				adjustedAsOfDate.plusDays(settlementDay), convention); 
		
		if (exerciseDates == null){
			//마지막 행사일이 1일 남았거나 지난경우
//				_swaptionInfo = new double[1][2];			
//				_swaptionInfo[0][0] = dcf.getYearFraction(adjustedAsOfDate, 
//						maturityDate);
//				_swaptionInfo[0][1] = 0.25;
			
			_swaptionInfo = new double[1][2];			
			_swaptionInfo[0][0] = dcf.getYearFraction(adjustedAsOfDate, 
					adjustedSettlementDate);
			_swaptionInfo[0][1] = Math.max((Math.round(
					(maturityDate.diff(adjustedSettlementDate) / 365.25 * 12.0)/numOfMonth) 
					* numOfMonth) / 12.0, 0.25);
		} else {
			int exerciseLength = exerciseDates.length;
			int tmpidx = 0;
			while (asOfDate.compareTo(exerciseDates[tmpidx]) >=0 ){
				if (tmpidx == exerciseLength - 1) {
					break;
				}
				tmpidx++;
			}			
			if(exerciseDates[tmpidx].diff(asOfDate) <= 1){
				exerciseLength--;
			}
			
			if (exerciseLength - tmpidx == 0){
				//마지막 행사일이 1일 남았거나 지난경우
				_swaptionInfo = new double[1][2];			
				_swaptionInfo[0][0] = dcf.getYearFraction(adjustedAsOfDate, 
						adjustedSettlementDate);
				_swaptionInfo[0][1] = Math.max((Math.round(
						(maturityDate.diff(adjustedSettlementDate) / 365.25 * 12.0)/numOfMonth) 
						* numOfMonth) / 12.0, 0.25);
				
			} else {			
				//일반적인 경우
				_swaptionInfo = new double[exerciseLength - tmpidx][2];			
				
				int idx = 0;
				for(int i = tmpidx; i < exerciseDates.length; i++){
					Date adjustedExerciseDate = calendar.adjustDate(exerciseDates[i], convention);
					int diff = adjustedExerciseDate.diff(adjustedAsOfDate);
					if (diff != 1){
						_swaptionInfo[idx][0] = diff / dcf.getDaysOfYear();
						_swaptionInfo[idx][1] = Math.max((Math.round(
								(maturityDate.diff(exerciseDates[i]) / 365.25 * 12.0) / numOfMonth) 
								* numOfMonth) / 12.0, 0.25);
						idx++;
					}
				}
			}
		}		
	}
	
	public HullWhiteVolatility[] getHWVolatility(ModelType hullWhiteType){
		if (hullWhiteType.equals(ModelType.HW1F)){
			return getHW1FVolatility();
		} else if (hullWhiteType.equals(ModelType.HW2F)){
			return getHW2FVolatility();
		} else {
			return null;
		}
	}
	
	private HullWhiteVolatility[] getHW1FVolatility(){

		HullWhiteVolatility[] hwVols = new HullWhiteVolatility[1];
		
		int swaptionN = _swaptionInfo.length;
		double[] spotVol1F = new double[swaptionN];		
		double[] resultVol = new double[swaptionN];
		double[] maturityDt = new double[swaptionN];
				
		for (int i = 0; i < swaptionN; i++){
			
			spotVol1F[i] = _hwSurface.getVol(_swaptionInfo[i][1], _swaptionInfo[i][0], 1);			
			maturityDt[i] = _swaptionInfo[i][0];
		}
		
		resultVol[0] = spotVol1F[0];
		
		for(int si = 1; si < swaptionN; si++) {
			double tmp = (spotVol1F[si] * spotVol1F[si] * _swaptionInfo[si][0] -
					spotVol1F[si-1] * spotVol1F[si-1] *  _swaptionInfo[si - 1][0] )/
					( _swaptionInfo[si][0] -  _swaptionInfo[si - 1][0]);
			
			resultVol[si] = tmp > 0 ? Math.sqrt(tmp) : 0.0001;			
		}
		
		hwVols[0] = new HullWhiteVolatility(maturityDt, resultVol);
		
		return hwVols;		
	}
	
	private HullWhiteVolatility[] getHW2FVolatility(){
		HullWhiteVolatility[] hwVols = new HullWhiteVolatility[2];
		int swaptionN = _swaptionInfo.length;
		double[] spotVol2F_1 = new double[swaptionN];		
		double[] spotVol2F_2 = new double[swaptionN];
		double[] resultVol1 = new double[swaptionN];
		double[] resultVol2 = new double[swaptionN];
		double[] maturityDt = new double[swaptionN];
		
		for (int i = 0; i < swaptionN; i++){
			
			spotVol2F_1[i] = _hwSurface.getVol(_swaptionInfo[i][1], _swaptionInfo[i][0], 2);			
			spotVol2F_2[i] = _hwSurface.getVol(_swaptionInfo[i][1], _swaptionInfo[i][0], 3);
			maturityDt[i] = _swaptionInfo[i][0];
		}
		
		resultVol1[0] = spotVol2F_1[0];
		resultVol2[0] = spotVol2F_2[0];
		
		for(int si = 1; si < swaptionN; si++) {
			double tmp1 = (spotVol2F_1[si] * spotVol2F_1[si] * _swaptionInfo[si][0] -
					spotVol2F_1[si-1] * spotVol2F_1[si-1] *  _swaptionInfo[si - 1][0] )/
					( _swaptionInfo[si][0] -  _swaptionInfo[si - 1][0]);
			double tmp2 = (spotVol2F_2[si] * spotVol2F_2[si] * _swaptionInfo[si][0] -
					spotVol2F_2[si-1] * spotVol2F_2[si-1] *  _swaptionInfo[si - 1][0] )/
					( _swaptionInfo[si][0] -  _swaptionInfo[si - 1][0]);
			
			resultVol1[si] = tmp1 > 0 ? Math.sqrt(tmp1) : 0.0001;
			resultVol2[si] = tmp2 > 0 ? Math.sqrt(tmp2) : 0.0001;
			
		}
		
		hwVols[0] = new HullWhiteVolatility(maturityDt, resultVol1);
		hwVols[1] = new HullWhiteVolatility(maturityDt, resultVol2);
		
		return hwVols;	
	}
}
