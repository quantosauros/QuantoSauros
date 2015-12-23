package com.quantosauros.batch.instrument.marketDataCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.dao.MarketDataDao;
import com.quantosauros.batch.dao.MySqlMarketDataDao;
import com.quantosauros.batch.model.IrCurveModel;
import com.quantosauros.batch.model.ProductInfoModel;
import com.quantosauros.batch.model.ProductLegModel;
import com.quantosauros.batch.model.VolSurfModel;
import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.batch.types.RiskFactorType.RiskFactor;
import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.currency.FxRate;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.common.volatility.Volatility;
import com.quantosauros.common.volatility.VolatilityCurve;
import com.quantosauros.common.volatility.VolatilitySurface;

public class AbstractMarketDataCreator {
	
	public static FxRate getStlLegFxRates(String instrumentCd, String payRcvCd){
		
		MarketDataDao marketDataDao = new MySqlMarketDataDao();		
		HashMap paramMap = new HashMap();        		
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);
		Map<String, Object> stlLegFxMap = marketDataDao.selectSTLLegFxRate(paramMap);
		
		//StlLeg FXRATE
		FxRate fxRate = FxRate.valueOf(
    			(String) stlLegFxMap.get("BASE_CCY"),
    			(String) stlLegFxMap.get("REFERENCE_CCY"),
    			((Double) stlLegFxMap.get("VALUE")).doubleValue());
		
		return fxRate;
	}
	
	public static HullWhiteVolatility[] getHullWhiteVolatility1(
			String ccyCd){
		
		MarketDataDao marketDataDao = new MySqlMarketDataDao();		
		HashMap paramMap = new HashMap();        		
		paramMap.put("ccyCd", ccyCd);
		Map hwMap = marketDataDao.selectHWVolatility(paramMap);

		String vertexStr = (String) hwMap.get("MRTY_CD");
		double vtx = Vertex.valueOf(vertexStr).getVertex();
		double hwVol = ((Double) hwMap.get("VOL")).doubleValue();
		
		HullWhiteVolatility[] HWVol = new HullWhiteVolatility[]{
				new HullWhiteVolatility(new double[]{vtx},new double[]{hwVol}),
		};
		
		return HWVol;		
	}	
	
	public static HullWhiteParameters getHWParameters1(){
		double meanReversion1_1F = 0.01;
		double hullWhiteVolatility_1F = 0.004;
		
		double meanReversion1_2F = 0.02;
		double meanReversion2_2F = 0.001;
		double hullWhiteVolatility1_2F = 0.01;
		double hullWhiteVolatility2_2F = 0.004;
		double correlation = -0.7;		
		
		HullWhiteParameters params = new HullWhiteParameters(
				meanReversion1_1F, hullWhiteVolatility_1F, 
				meanReversion1_2F, meanReversion2_2F, 
				hullWhiteVolatility1_2F, hullWhiteVolatility2_2F, 
				correlation);
		
		return params;
	}
	
	public static HullWhiteParameters getHWParameters2(){
		double meanReversion1_1F = 0.01;
		double hullWhiteVolatility_1F = 0.01;
		
		double meanReversion1_2F = 0.02;
		double meanReversion2_2F = 0.001;
		double hullWhiteVolatility1_2F = 0.01;
		double hullWhiteVolatility2_2F = 0.004;
		double correlation = -0.7;		
		
		HullWhiteParameters params = new HullWhiteParameters(
				meanReversion1_1F, hullWhiteVolatility_1F, 
				meanReversion1_2F, meanReversion2_2F, 
				hullWhiteVolatility1_2F, hullWhiteVolatility2_2F, 
				correlation);
		
		return params;
	}
	
	public static double[][] getCorrelation(
			String instrumentCd, RiskFactor[][] corrRFArray){		
				
		MarketDataDao marketDataDao = new MySqlMarketDataDao();
		HashMap paramMap = new HashMap();        		
		paramMap.put("instrumentCd", instrumentCd);
		Map hwMap = marketDataDao.selectIRCCodesofProduct(paramMap);
		
		int legNum = corrRFArray.length;
		int[] undNum = new int[legNum];
		int totalNum = 0;
		for (int legIndex = 0; legIndex < legNum; legIndex++){			
			undNum[legIndex] = corrRFArray[legIndex].length;
			totalNum += undNum[legIndex];
		}
			
		String[] ircCd = new String[totalNum + 1];
		int index = 0;
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			for (int undIndex = 0; undIndex < undNum[legIndex]; undIndex++){				
				RiskFactor rf = corrRFArray[legIndex][undIndex];
				if (rf.equals(RiskFactor.P_COUPON_IRC_CD1)){
					ircCd[index] = (String) hwMap.get("P_COUPON_IRC_CD1");
				} else if (rf.equals(RiskFactor.P_COUPON_IRC_CD2)){
					ircCd[index] = (String) hwMap.get("P_COUPON_IRC_CD2");
				} else if (rf.equals(RiskFactor.P_COUPON_IRC_CD3)){
					ircCd[index] = (String) hwMap.get("P_COUPON_IRC_CD3");
				} else if (rf.equals(RiskFactor.R_COUPON_IRC_CD1)){
					ircCd[index] = (String) hwMap.get("R_COUPON_IRC_CD1");
				} else if (rf.equals(RiskFactor.R_COUPON_IRC_CD2)){
					ircCd[index] = (String) hwMap.get("R_COUPON_IRC_CD2");
				} else if (rf.equals(RiskFactor.R_COUPON_IRC_CD3)){
					ircCd[index] = (String) hwMap.get("R_COUPON_IRC_CD3");
				}
				index++;
			}
		}
		ircCd[totalNum] = (String) hwMap.get("DISC_IRC_CD");
		
		double[][] corr = new double[totalNum + 1][totalNum + 1];
		
		for (int i = 0; i < totalNum + 1; i++){
			for (int j = 0; j < totalNum + 1; j++){
				String cd1 = ircCd[i];
				String cd2 = ircCd[j];
				if (cd1.equals(cd2)){
					corr[i][j] = 1.0;
				} else {
					corr[i][j] = 0.8;
				}										
			}
		}			
		return corr;
	}

	public static double[] getFxAssetCorrelation(){
		
		return new double[]{1.0, 0.0};
	}
	
	public static double[] getFXVolatility(){
		
		return new double[]{0.0, 0.1};
	}
	
	public static InterestRateCurve getIrCurve(Date asOfDate,
			List irCurveModelList){
		
		InterestRate[] spotRates = new InterestRate[irCurveModelList.size()];
		DayCountFraction dcf = null;
		Frequency compoundFreq = null;

		for (int i = 0; i < irCurveModelList.size(); i++){
			IrCurveModel irCurveModel = (IrCurveModel) irCurveModelList.get(i);
			dcf = DayCountFraction.valueOf(irCurveModel.getDcf());
			compoundFreq = Frequency.valueOf(irCurveModel.getCompoundFrequency());
			String type = irCurveModel.getType();
						
			spotRates[i] = new InterestRate(
					Vertex.valueOf(irCurveModel.getMrtyCd()),
					Double.parseDouble(irCurveModel.getIrValue()), type);
		}
				
		InterestRateCurve irCurve = 
				new InterestRateCurve(asOfDate, spotRates, compoundFreq, dcf);
		
		return irCurve;
	}
	
	public static VolatilitySurface getVolatilitySurface(Date asOfDate,
			List volSurfModelList){
		
		int totalNumber = volSurfModelList.size();		
		int mrtyNumber = Integer.parseInt(
				((VolSurfModel) volSurfModelList.get(0)).getCntNumber());
		int tenorNumber = totalNumber / mrtyNumber;
		
		DayCountFraction dcf = DayCountFraction.valueOf(
				((VolSurfModel) volSurfModelList.get(0)).getDcf());
		
		//generate Swaption Surface		
		VolatilityCurve[] volCurves = new VolatilityCurve[tenorNumber];
		int index = 0;
		for (int i = 0; i < tenorNumber; i++){
			String tenor = null;
			Volatility[] vols = new Volatility[mrtyNumber];
			for (int j = 0; j < mrtyNumber; j++){
				index = i * mrtyNumber + j;
				VolSurfModel volSurfModel = (VolSurfModel) volSurfModelList.get(index);
				
				tenor = volSurfModel.getSwaptionTenor();
				String mrty = volSurfModel.getSwaptionMrty();
				double volatility = Double.parseDouble(volSurfModel.getVol());
				
				vols[j] = new Volatility(Vertex.valueOf(mrty), volatility);
				
			}			
			
			volCurves[i] = new VolatilityCurve(
					Vertex.valueOf(tenor), vols, asOfDate, dcf);			
		}
		
		return new VolatilitySurface(volCurves, asOfDate, dcf);
	}
	public static ModelType getModelType(){
		return ModelType.HW1F;
	}
	
	public static double[][][] getQuantoMarketData(ProductInfoModel productInfoModel,
			ProductLegModel[] productLegModels){
		String productCcy = productInfoModel.getCcyCd();
		//[0:corr, 1:vol][][]
		double[][][] quantoMarketData = new double[2][productLegModels.length][];
		
		for (int legIndex = 0; legIndex < productLegModels.length; legIndex++){
			int undNum = 0;	
			ProductLegModel productLegModel = productLegModels[legIndex];
			String[] ccy = new String[] {
					productLegModel.getCouponIrcCcy1(),
					productLegModel.getCouponIrcCcy2(),
					productLegModel.getCouponIrcCcy3(),
			};
			if (ccy[2] == null){
				undNum = 2;
				if (ccy[1] == null){
					undNum = 1;
					if (ccy[0] == null){
						undNum = 0;
					}
				}				
			}
			quantoMarketData[0][legIndex] = new double[undNum];
			quantoMarketData[1][legIndex] = new double[undNum];
						
			//TODO quanto correlation, fx vol
			for (int undIndex = 0; undIndex < undNum; undIndex++){
				if (productCcy.equals(ccy[undIndex])){
					quantoMarketData[0][legIndex][undIndex] = 0;
					quantoMarketData[1][legIndex][undIndex] = 0;
				} else {
					quantoMarketData[0][legIndex][undIndex] = 0;
					quantoMarketData[1][legIndex][undIndex] = 0.1;
				}
			}		
		}
		return quantoMarketData;
		
	}
}
