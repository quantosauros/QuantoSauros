package com.quantosauros.test.jpl;

import java.util.Map;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.RateType;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.calibration.HWVolatilityCalibration;
import com.quantosauros.common.currency.Currency;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.hullwhite.HWVolatilitySurface;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.common.volatility.Volatility;
import com.quantosauros.common.volatility.VolatilityCurve;
import com.quantosauros.common.volatility.VolatilitySurface;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.LegStructuredCouponInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.dto.underlying.RateUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;
import com.quantosauros.jpl.instrument.StructuredProduct;
import com.quantosauros.test.util.TestBase;

/**
 * USD LIBOR 3M RA Note, Range : 0 ~ 6%, Coupon : 4%, 15NC1
 * 
 * @author Jay
 *
 */

public class testRANote01 extends TestBase {

	Date _asOfDate;
	Date _issueDate;
	Date _maturityDate;
	Date _nextCouponDate;
	Currency _currency;
	Money _principal1;	
	
	Date[] _startDates1;
	Date[] _endDates1;
		
	Date[] _exerciseDates;
	double[] _exercisePrices;
	OptionType _optionType;
	
	DayCountFraction _dcf1;	
	
	InterestRateCurve _structuredLegCurve1;	
	InterestRateCurve _discountCurve;
	
	HullWhiteParameters _structuredLegHWParam1;	
	HullWhiteParameters _discountHWParam;
	
	HullWhiteVolatility _structuredLegHWVol1;	
	HullWhiteVolatility _discountHWVol;
	
	HWVolatilitySurface _structuredHWVolSurface1;
	HWVolatilitySurface _discountHWVolSurface;
	
	ModelType _discountHWType;
		
	public void marketData(){
		double[] spotRateValue1 = new double[] {
				0.00283699,	0.00338713,	0.00410436,	0.00492583,	0.00675428,	0.00858092,	0.01028353,	0.0118022,	0.01431868,	0.01644246,	0.01959603,	0.02252876,	0.02634999,
		};
		Vertex[] spotRateVertex1 = new Vertex[] {
				Vertex.valueOf("M3"),	Vertex.valueOf("M6"),	Vertex.valueOf("M9"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y2H"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y20"),
		};		
		double[] discountRateValue = new double[] {
				0.00283699,	0.00338713,	0.00410436,	0.00492583,	0.00675428,	0.00858092,	0.01028353,	0.0118022,	0.01431868,	0.01644246,	0.01959603,	0.02252876,	0.02634999,
		}; 
		Vertex[] discountRateVertex = new Vertex[] {
				Vertex.valueOf("M3"),	Vertex.valueOf("M6"),	Vertex.valueOf("M9"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y2H"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y20"),		};
		
		InterestRate[] spotRates1 = new InterestRate[spotRateValue1.length];
		InterestRate[] discountRates = new InterestRate[discountRateValue.length];
		for (int i = 0; i < spotRateValue1.length; i++){
			spotRates1[i] = new InterestRate(spotRateVertex1[i], spotRateValue1[i]);			
		}
		for (int i = 0; i < discountRateValue.length; i++){
			discountRates[i] = new InterestRate(discountRateVertex[i], discountRateValue[i]);			
		}
		_structuredLegCurve1 = new InterestRateCurve(_asOfDate, spotRates1,
				Frequency.valueOf("C"), DayCountFraction.ACTUAL_365);
		_discountCurve = new InterestRateCurve(_asOfDate, discountRates,
				Frequency.valueOf("C"), DayCountFraction.ACTUAL_365);
		
		double meanReversion1_1F = 0.024;
		double hullWhiteVolatility_1F = 0.005;
		double meanReversion1_2F = 0.55;
		double meanReversion2_2F = 0.03; 
		double hullWhiteVolatility1_2F = 0.004;
		double hullWhiteVolatility2_2F = 0.005; 
		double correlation = -0.75;
		
		_structuredLegHWParam1 = new HullWhiteParameters(
				meanReversion1_1F, hullWhiteVolatility_1F, 
				meanReversion1_2F, meanReversion2_2F, 
				hullWhiteVolatility1_2F, hullWhiteVolatility2_2F, 
				correlation);
		
		_discountHWParam = new HullWhiteParameters(
				meanReversion1_1F, hullWhiteVolatility_1F, 
				meanReversion1_2F, meanReversion2_2F, 
				hullWhiteVolatility1_2F, hullWhiteVolatility2_2F, 
				correlation);
		
		double[] tenors = new double[] {1,	2,	3,	4,	5,	6,	7,	8,	9,	10,	15,	20,};
		double[] volatilities = new double[] {0.0084,	0.0083,	0.008,	0.0076,	0.0074,	0.0051,	0.0072,	0.0042,	0.0049,	0.0066,	0.0044,	0.0038,};
		
		_structuredLegHWVol1 = new HullWhiteVolatility(tenors, volatilities);
		_discountHWVol = new HullWhiteVolatility(tenors, volatilities);
		
		double[][] swaptionVolValue1 = new double[][] {
				{	0.5748,	0.5044,	0.445,	0.4045,	0.3737,	0.3508,	0.33,	0.316,	0.3047,	0.2943,	0.2295,	0.2196,	0.5267,	0.4571,	},
				{	0.4087,	0.374,	0.3535,	0.3336,	0.3207,	0.3064,	0.2942,	0.2838,	0.2474,	0.2145,	0.4852,	0.4241,	0.3843,	0.3604,	},
				{	0.3428,	0.3272,	0.3147,	0.3032,	0.2899,	0.28,	0.24335,	0.2127,	0.4529,	0.3965,	0.3675,	0.349,	0.3338,	0.3203,	},
				{	0.3093,	0.2967,	0.2859,	0.2768,	0.2291,	0.2119,	0.4164,	0.3774,	0.3535,	0.3394,	0.3258,	0.3139,	0.3044,	0.2925,	},
				{	0.2824,	0.2739,	0.2331,	0.2124,	0.3948,	0.3635,	0.3436,	0.3312,	0.3195,	0.3083,	0.2993,	0.2882,	0.279,	0.2679,	},
				{	0.2345,	0.2106,	0.3786,	0.3525,	0.3353,	0.3239,	0.3138,	0.3031,	0.2945,	0.2844,	0.273,	0.2622,	0.2344,	0.2094,	},
				{	0.3667,	0.3438,	0.3284,	0.3178,	0.3092,	0.2989,	0.2906,	0.2786,	0.2674,	0.2595,	0.2336,	0.2085,	0.3573,	0.3364,	},
				{	0.3224,	0.3121,	0.305,	0.295,	0.2847,	0.2729,	0.2644,	0.2585,	0.2326,	0.2077,	0.3495,	0.3302,	0.3171,	0.3072,	},
				{	0.3011,	0.2894,	0.2789,	0.2696,	0.2629,	0.2575,	0.231,	0.2064,	0.3207,	0.3054,	0.2914,	0.2838,	0.2763,	0.2692,	},
				{	0.2628,	0.2549,	0.2476,	0.241,	0.216,	0.1909,	0.3071,	0.2941,	0.2824,	0.2732,	0.2664,	0.2595,	0.2537,	0.2462,	},
				{	0.2395,	0.2334,	0.2098,	0.1913,	0.3016,	0.2897,	0.2788,	0.2704,	0.2641,	0.2568,	0.2504,	0.243,	0.2363,	0.2301,	},
				{	0.2062,	0.1885,	0.2987,	0.2875,	0.2767,	0.2685,	0.2622,	0.2542,	0.2471,	0.2397,	0.233,	0.2269,	0.2029,	0.1862,	},
		};
		
		Vertex[] swaptionVolTenor1 = new Vertex[] {
				Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),
		};
		Vertex[] swaptionVolMaturity1 = new Vertex[] {
				Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),	Vertex.valueOf("Y25"),	Vertex.valueOf("Y30"),
		};
		double[][] discSwaptionVolValue = new double[][] {
				{	0.5748,	0.5044,	0.445,	0.4045,	0.3737,	0.3508,	0.33,	0.316,	0.3047,	0.2943,	0.2295,	0.2196,	0.5267,	0.4571,	},
				{	0.4087,	0.374,	0.3535,	0.3336,	0.3207,	0.3064,	0.2942,	0.2838,	0.2474,	0.2145,	0.4852,	0.4241,	0.3843,	0.3604,	},
				{	0.3428,	0.3272,	0.3147,	0.3032,	0.2899,	0.28,	0.24335,	0.2127,	0.4529,	0.3965,	0.3675,	0.349,	0.3338,	0.3203,	},
				{	0.3093,	0.2967,	0.2859,	0.2768,	0.2291,	0.2119,	0.4164,	0.3774,	0.3535,	0.3394,	0.3258,	0.3139,	0.3044,	0.2925,	},
				{	0.2824,	0.2739,	0.2331,	0.2124,	0.3948,	0.3635,	0.3436,	0.3312,	0.3195,	0.3083,	0.2993,	0.2882,	0.279,	0.2679,	},
				{	0.2345,	0.2106,	0.3786,	0.3525,	0.3353,	0.3239,	0.3138,	0.3031,	0.2945,	0.2844,	0.273,	0.2622,	0.2344,	0.2094,	},
				{	0.3667,	0.3438,	0.3284,	0.3178,	0.3092,	0.2989,	0.2906,	0.2786,	0.2674,	0.2595,	0.2336,	0.2085,	0.3573,	0.3364,	},
				{	0.3224,	0.3121,	0.305,	0.295,	0.2847,	0.2729,	0.2644,	0.2585,	0.2326,	0.2077,	0.3495,	0.3302,	0.3171,	0.3072,	},
				{	0.3011,	0.2894,	0.2789,	0.2696,	0.2629,	0.2575,	0.231,	0.2064,	0.3207,	0.3054,	0.2914,	0.2838,	0.2763,	0.2692,	},
				{	0.2628,	0.2549,	0.2476,	0.241,	0.216,	0.1909,	0.3071,	0.2941,	0.2824,	0.2732,	0.2664,	0.2595,	0.2537,	0.2462,	},
				{	0.2395,	0.2334,	0.2098,	0.1913,	0.3016,	0.2897,	0.2788,	0.2704,	0.2641,	0.2568,	0.2504,	0.243,	0.2363,	0.2301,	},
				{	0.2062,	0.1885,	0.2987,	0.2875,	0.2767,	0.2685,	0.2622,	0.2542,	0.2471,	0.2397,	0.233,	0.2269,	0.2029,	0.1862,	},
		};
		
		Vertex[] discSwaptionVolTenor = new Vertex[] {
				Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),
		};
		Vertex[] discSwaptionVolMaturity = new Vertex[] {
				Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),	Vertex.valueOf("Y25"),	Vertex.valueOf("Y30"),
		};
		VolatilityCurve[] volatilityCurves = new VolatilityCurve[swaptionVolValue1.length];
		for (int i = 0; i < swaptionVolValue1.length; i++){
			Volatility[] vol = new Volatility[swaptionVolValue1[i].length];
			for (int j = 0; j < swaptionVolValue1[i].length; j++){
				vol[j] = new Volatility(swaptionVolMaturity1[j] ,swaptionVolValue1[i][j]);				
			}
			volatilityCurves[i] = new VolatilityCurve(swaptionVolTenor1[i], vol, _asOfDate, DayCountFraction.ACTUAL_365);
			
		}
		VolatilityCurve[] discVolatilityCurves = new VolatilityCurve[discSwaptionVolValue.length];
		for (int i = 0; i < discSwaptionVolValue.length; i++){
			Volatility[] vol = new Volatility[discSwaptionVolValue[i].length];
			for (int j = 0; j < discSwaptionVolValue[i].length; j++){
				vol[j] = new Volatility(discSwaptionVolMaturity[j] ,discSwaptionVolValue[i][j]);				
			}
			discVolatilityCurves[i] = new VolatilityCurve(discSwaptionVolTenor[i], vol, _asOfDate, DayCountFraction.ACTUAL_365);
			
		}
		VolatilitySurface volatilitySurface1 = new VolatilitySurface(volatilityCurves,
				_asOfDate, DayCountFraction.ACTUAL_365);
		VolatilitySurface discVolatilitySurface = new VolatilitySurface(discVolatilityCurves,
				_asOfDate, DayCountFraction.ACTUAL_365);
		
		HWVolatilityCalibration volCali1 = new HWVolatilityCalibration(
				"KR", _asOfDate, _structuredLegCurve1, volatilitySurface1, 
				_structuredLegHWParam1);
		_structuredHWVolSurface1 = volCali1.calculate();
		
		HWVolatilityCalibration discVolCali = new HWVolatilityCalibration(
				"KR", _asOfDate, _discountCurve, discVolatilitySurface, 
				_discountHWParam);
		
		_discountHWVolSurface = discVolCali.calculate();
		_discountHWType = ModelType.HW1F;
	}
	
	public void instrumentData(){
		
		_asOfDate = Date.valueOf("20150601");
		
		_issueDate = Date.valueOf("20150601");
		_maturityDate = Date.valueOf("20300603");
		_nextCouponDate = Date.valueOf("20160601");
		
		_currency = Currency.getInstance("KR");
		_principal1 = Money.valueOf("KRW", 1.0E10);
		
		_startDates1 = new Date[] {
				Date.valueOf("20150601"),	Date.valueOf("20160603"),	Date.valueOf("20170605"),	Date.valueOf("20180604"),	Date.valueOf("20190603"),	Date.valueOf("20200603"),	Date.valueOf("20210603"),	Date.valueOf("20220603"),	Date.valueOf("20230605"),	Date.valueOf("20240603"),	Date.valueOf("20250603"),	Date.valueOf("20260603"),	Date.valueOf("20270603"),	Date.valueOf("20280605"),	Date.valueOf("20290604"),
		};
		_endDates1 = new Date[]{
				Date.valueOf("20160603"),	Date.valueOf("20170605"),	Date.valueOf("20180604"),	Date.valueOf("20190603"),	Date.valueOf("20200603"),	Date.valueOf("20210603"),	Date.valueOf("20220603"),	Date.valueOf("20230605"),	Date.valueOf("20240603"),	Date.valueOf("20250603"),	Date.valueOf("20260603"),	Date.valueOf("20270603"),	Date.valueOf("20280605"),	Date.valueOf("20290604"),	Date.valueOf("20300603"),
		};
		
		_exerciseDates = new Date[]{
				Date.valueOf("20160527"),	Date.valueOf("20170529"),	Date.valueOf("20180528"),	Date.valueOf("20190527"),	Date.valueOf("20200527"),	Date.valueOf("20210527"),	Date.valueOf("20220527"),	Date.valueOf("20230529"),	Date.valueOf("20240527"),	Date.valueOf("20250527"),	Date.valueOf("20260527"),	Date.valueOf("20270527"),	Date.valueOf("20280529"),	Date.valueOf("20290528"),
		};
		_exercisePrices = new double[_exerciseDates.length];
		for (int i = 0; i < _exerciseDates.length; i++){
			_exercisePrices[i] = 1;
		}
		_optionType = OptionType.CALL;
		
		_dcf1 = DayCountFraction.ACTUAL_365;		
	}
	
	public void test() {
		
		instrumentData();
		marketData();
				
		//PRODUCT INFO
		ProductInfo productInfo = new ProductInfo(
				_issueDate, _maturityDate, _currency);
				
		//LEG SCHEDULE INFO
		LegScheduleInfo[] legScheduleInfos = new LegScheduleInfo[1];
		
		PaymentPeriod[] periods1 = new PaymentPeriod[_startDates1.length];
		for (int i = 0; i < _startDates1.length; i++){
			periods1[i] = new PaymentPeriod(
					_startDates1[i], _startDates1[i], _endDates1[i], _endDates1[i]);
		}
		
		legScheduleInfos[0] = new LegScheduleInfo(periods1, _dcf1);
				
		//LEG DATA INFO
		LegDataInfo[] legDataInfos = new LegDataInfo[1];
		legDataInfos[0] = new LegDataInfo(_nextCouponDate);
		legDataInfos[0].setCumulatedAccrualDays(1);		
		
		//LEG AMORTIZATION INFO
		LegAmortizationInfo[] legAmortizationInfos = new LegAmortizationInfo[1];
		legAmortizationInfos[0] = new LegAmortizationInfo(_principal1);		
		
		//LEG COUPON INFO
		LegCouponInfo[] legCouponInfos = new LegCouponInfo[1];
		
		CouponType[] couponType1 = new CouponType[_startDates1.length];
		UnderlyingType underlyingType1 = UnderlyingType.R1;
		ConditionType conditionType1 = ConditionType.R1;
		UnderlyingInfo[] rcvUndInfos = new UnderlyingInfo[]{
			new RateUnderlyingInfo(
					ModelType.HW1F, 0.25, Frequency.valueOf("Q"), RateType.SPOT),
			
		};
	
		int conditionNum = 1;
		int referenceNum = 1;
		double[][] upperLimits = new double[conditionNum][_startDates1.length];
		double[][] lowerLimits = new double[conditionNum][_startDates1.length];
		double[] inCouponRates = new double[_startDates1.length];
		double[] outCouponRates = new double[_startDates1.length];
		boolean hasCap = false;
		double[] cap = null;
		boolean hasFloor = false;
		double[] floor = null;
		double[][] leverages1 = new double[referenceNum][_startDates1.length];
		double[] spreads1 = new double[_startDates1.length];
		PayRcv payRcv1 = PayRcv.PAY;
		
		for (int i = 0; i < _startDates1.length; i++){
			couponType1[i] = CouponType.ACCRUAL;
			upperLimits[0][i] = 0.06;
			lowerLimits[0][i] = 0;
			inCouponRates[i] = 0.04;
			outCouponRates[i] = 0;
						
			leverages1[0][i] = 1;			
			spreads1[i] = 0;
		}
				
		legCouponInfos[0] = new LegStructuredCouponInfo(payRcv1,
				couponType1, underlyingType1, conditionType1,
				rcvUndInfos,
				upperLimits, lowerLimits, 
				inCouponRates, outCouponRates, 
				hasCap, cap, hasFloor, floor, 
				leverages1, spreads1);
		
		//OPTION INFO
		OptionInfo optionInfo = new OptionInfo(
//				null, null, null);
				_optionType, _exerciseDates, _exercisePrices);
				
		//QUANTO INFO
		double[][] fxAssetCorrelations = new double[legCouponInfos.length][];
		double[][] fxVolatilities = new double[legCouponInfos.length][];
		Currency productCCY = productInfo.getCurrency();
		for (int legIndex = 0; legIndex < legCouponInfos.length; legIndex++){
			int undNum = legCouponInfos[legIndex].getUnderlyingNumber();
			fxAssetCorrelations[legIndex] = new double[undNum];
			fxVolatilities[legIndex] = new double[undNum];
			for (int undIndex = 0; undIndex < undNum; undIndex++){
				fxAssetCorrelations[legIndex][undIndex] = 1;
				fxVolatilities[legIndex][undIndex] = 0;
			}
		}
				
		int monitorFrequency = 1;
		long seed = 12345L;
		int simNum = 1000;
		
		StructuredProduct product = new StructuredProduct(
				_asOfDate,
				productInfo, 
				legScheduleInfos, 
				legDataInfos, 
				legAmortizationInfos, 
				legCouponInfos, 
				optionInfo,
				_discountHWType,
				monitorFrequency,
				seed, simNum);
		
//		UnderlyingInfo[][] legUnderlyingInfos = new UnderlyingInfo[1][1];
//		legUnderlyingInfos[0][0] = new RateUnderlyingInfo(
//				_structuredLegCurve1, _structuredLegHWParam1, 
//				new HullWhiteVolatility[]{_structuredLegHWVol1,}
//		);
		
		MarketInfo[][] legUnderlyingInfos = new MarketInfo[1][1];
		legUnderlyingInfos[0][0] = new RateMarketInfo(
				_structuredLegCurve1, _structuredLegHWParam1, 
				_structuredHWVolSurface1
		);
				
		double[][] correlations = new double[][]{
			{1,1,},{1,1,},
		};
		
//		RateUnderlyingInfo discountUnderlyingInfo = new RateUnderlyingInfo(
//				_discountCurve, _discountHWParam,
//				new HullWhiteVolatility[]{_structuredLegHWVol1,}
//		);
		
		RateMarketInfo discountUnderlyingInfo = new RateMarketInfo(
				_discountCurve, _discountHWParam, _discountHWVolSurface);
		
		Money price = product.getPrice(
				legUnderlyingInfos, discountUnderlyingInfo, correlations);
		
		Money leg1Price = product.getLegPrice(0);		
		
		log(price + ": " + leg1Price);	
		
		Map result = product.getResults();
		
		log(result);
		
//		AbstractResult abstractResult = new AbstractResult(product);
//		abstractResult.getResults();
		
	}
}
