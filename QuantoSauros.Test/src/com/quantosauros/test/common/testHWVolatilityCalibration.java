package com.quantosauros.test.common;
import com.quantosauros.common.Frequency;
import com.quantosauros.common.calibration.HWVolatilityCalibration;
import com.quantosauros.common.calibration.HullWhite2FCalibration;
import com.quantosauros.common.calibration.HullWhiteCalibration;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.common.volatility.Volatility;
import com.quantosauros.common.volatility.VolatilityCurve;
import com.quantosauros.common.volatility.VolatilitySurface;

public class testHWVolatilityCalibration {
	
	public static void main(String args[]) {
		TestHullWhiteVolatilityCalibration_Common.test();
	}
	
	public static class TestHullWhiteVolatilityCalibration_Common {
		public static void test() {
			
			Date asOfDate = new Date("20140630");
			
			DayCountFraction dcf = DayCountFraction.ACTUAL_365;
			
//			String countryCode = "KR";
			String countryCode = "US";
//			String countryCode = "EU";
			
			double[] spotRateValues = new double[] {
					//KRW
//					0.025,	0.0265,	0.0260119813,	0.0259386911,	0.0258640046,	0.0260402742,	0.0262180627,	0.026727407,	0.0273453368,	0.0279168184,	0.0283609037,	0.0288110225,	0.0293781157,	0.0297831583,	0.0300223785,	0.0304863321,	0.0309263316,	0.0324298719,
//					USD
//					0.000966,	0.00123,	0.001515,	0.0019645,	0.002346,	0.002395,	0.002535,	0.0028796598,	0.0041499661,	0.0058836765,	0.0099026116,	0.0138043812,	0.0170191846,	0.0198995278,	0.0219722753,	0.0239834566,	0.0255160749,	0.0268143316,	0.0291519576,	0.0313090373,	0.0337143349,
					//EUR
//					0.0008214,	0.0017429,	0.0027214,	0.0029279722,	0.0029979119,	0.0031680231,	0.0039296214,	0.0051261852,	0.0066527669,	0.008333379,	0.0100606461,	0.0117545055,	0.0133205651,	0.0147231792,	0.0171320788,	0.0197175354,	0.0218809376,
					
					
//					NEwData Base date = 20150630
//					KRW
//					0.0149,		0.016466,	0.016192,	0.016117,	0.016142,	0.016457,	0.016771,	0.017315,	0.01786,	0.01888,	0.019831,	0.020462,	0.0211,		0.021615,	0.022136,	0.022664,	0.023916,	0.024975,				
//					USD
					0.00125,	0.002817,	0.003437,	0.004146,	0.004976,	0.006912,	0.008872,	0.010699,	0.012366,	0.015259,	0.017708,	0.019756,	0.02147,	0.02284,	0.023909,	0.024805,	0.027658,	0.028953,
//					EUR
//					-0.00123,	-0.00015,	0.0005,		0.001,		0.000829,	0.00088,	0.00128,	0.001756,	0.002272,	0.003489,	0.005024,	0.006549,	0.007984,	0.009326,	0.010524,	0.011628,	0.015152,	0.01649,
			};
			Vertex[] spotRateVertex = new Vertex[] {
//					KRW
//					Vertex.valueOf("D1"),	Vertex.valueOf("M3"),	Vertex.valueOf("M6"),	Vertex.valueOf("M9"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y12"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),
					//USD
//					Vertex.valueOf("D1"),	Vertex.valueOf("W1"),	Vertex.valueOf("M1"),	Vertex.valueOf("M2"),	Vertex.valueOf("M3"),	Vertex.valueOf("M6"),	Vertex.valueOf("M9"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y12"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),
					//EUR
//					Vertex.valueOf("M1"),	Vertex.valueOf("M3"),	Vertex.valueOf("M6"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y12"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),
					
//					NEwData Base date = 20150630
//					Common
					Vertex.valueOf("D1"),	Vertex.valueOf("M3"),	Vertex.valueOf("M6"),	Vertex.valueOf("M9"),	Vertex.valueOf("Y1"),	Vertex.valueOf("Y1H"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y2H"),	Vertex.valueOf("Y3"),	Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),

			};
			
			double[][] swaptionVolValues = new double[][]{
					//KRW
//					{0.091,	0.100068,	0.103893,	0.122457,	0.128336,	0.139774,	0.146133,	0.157,	0.169679,	0.1535,	0.1535,},	
//					{0.107,	0.106898,	0.111463,	0.128307,	0.132537,	0.139449,	0.145781,	0.154,	0.161268,	0.1455,	0.1435,},	
//					{0.112,	0.117836,	0.118941,	0.133986,	0.136796,	0.14235,	0.147373,	0.1525,	0.15685,	0.148,	0.146,},	
//					{0.1195,	0.126,	0.13,	0.142,	0.145693,	0.1495,	0.149,	0.151,	0.1545,	0.1465,	0.143905,},	
//					{0.1305,	0.136375,	0.137982,	0.148923,	0.150953,	0.152967,	0.150137,	0.15,	0.15119,	0.146,	0.146,},	
//					{0.1295,	0.132672,	0.133898,	0.145668,	0.146593,	0.1479,	0.146165,	0.146,	0.145708,	0.139,	0.132,},	
//					{0.1295,	0.135306,	0.136493,	0.141289,	0.144474,	0.147625,	0.147117,	0.147,	0.145688,	0.137,	0.129,},
					//USD
//					{0.560955,	0.852909,	0.578176,	0.595173,	0.493528,	0.479106,	0.401807,	0.342927,	0.309731,	0.244845,	0.21781,},	
//					{0.587677,	0.504523,	0.486008,	0.496131,	0.425875,	0.405448,	0.361054,	0.316225,	0.280685,	0.243316,	0.209496,},	
//					{0.515986,	0.470225,	0.450068,	0.427336,	0.382333,	0.362448,	0.328089,	0.297354,	0.273811,	0.23385,	0.211429,},	
//					{0.395187,	0.423735,	0.384863,	0.37383,	0.346511,	0.332757,	0.299801,	0.278146,	0.262216,	0.228787,	0.195451,},	
//					{0.340353,	0.344666,	0.343547,	0.334556,	0.316242,	0.30644,	0.280826,	0.263305,	0.248429,	0.22457,	0.201648,},	
//					{0.280282,	0.279784,	0.296392,	0.282088,	0.276936,	0.275099,	0.255103,	0.245404,	0.235572,	0.218727,	0.186307,},	
//					{0.214765,	0.231938,	0.23918,	0.244587,	0.243958,	0.244827,	0.236744,	0.228399,	0.222123,	0.208183,	0.191804,},
					//EUR
//					{0.6611,	0.7281,	0.7619,	0.8159,	0.8207,	0.844,	0.7685,	0.5901,	0.4707,	0.3417,	0.2699,},	
//					{0.4983,	0.5214,	0.5272,	0.5575,	0.5949,	0.6214,	0.569,	0.473,	0.3999,	0.3092,	0.2599,},	
//					{0.535,	0.5425,	0.5367,	0.5626,	0.5668,	0.5599,	0.4963,	0.4262,	0.3693,	0.2961,	0.2549,},	
//					{0.5348,	0.5338,	0.5127,	0.5353,	0.515,	0.494,	0.4454,	0.39,	0.345,	0.2875,	0.2533,},	
//					{0.4968,	0.4931,	0.4851,	0.4954,	0.4762,	0.4561,	0.4135,	0.3672,	0.33,	0.2813,	0.2529,},	
//					{0.3849,	0.3876,	0.3842,	0.392,	0.3838,	0.3756,	0.3523,	0.3257,	0.3033,	0.2687,	0.2495,},	
//					{0.3082,	0.3155,	0.3158,	0.3164,	0.315,	0.3142,	0.3018,	0.2902,	0.2811,	0.2591,	0.2476,},
					
					
//					NEwData Base date = 20150630
//					KRW
//					{	0.2155,	0.223,	0.2125,	0.2015,	0.2025,	0.192,	0.179,	},
//					{	0.2135,	0.214,	0.2045,	0.1985,	0.1985,	0.1865,	0.1755,	},
//					{	0.214,	0.203,	0.1985,	0.1955,	0.195,	0.178,	0.174,	},
//					{	0.216,	0.2035,	0.196,	0.194,	0.1925,	0.1775,	0.1755,	},
//					{	0.221,	0.201,	0.1945,	0.191,	0.187,	0.176,	0.176,	},
//					{	0.2155,	0.198,	0.19,	0.185,	0.181,	0.174,	0.172,	},
//					{	0.2105,	0.195,	0.189,	0.183,	0.179,	0.174,	0.169,	},
//					{	0.204,	0.192,	0.186,	0.18,	0.176,	0.17,	0.163,	},
//					{	0.1975,	0.186,	0.181,	0.175,	0.17,	0.164,	0.156,	},

//					USD
					{	0.544,	0.479,	0.3991,	0.3568,	0.3289,	0.3052,	0.29,	0.2652,	0.2587,	0.2642,	0.2324,	0.2066,	},
					{	0.5016,	0.4298,	0.3705,	0.332,	0.3093,	0.2931,	0.2809,	0.2576,	0.2493,	0.257,	0.2226,	0.2004,	},
					{	0.4502,	0.3925,	0.3485,	0.324,	0.3002,	0.2863,	0.2789,	0.2546,	0.2457,	0.253,	0.221,	0.2106,	},
					{	0.4265,	0.3717,	0.3252,	0.3067,	0.2941,	0.2826,	0.2743,	0.251,	0.2435,	0.2501,	0.2185,	0.2096,	},
					{	0.3856,	0.3485,	0.3138,	0.2989,	0.2892,	0.2786,	0.2704,	0.2479,	0.241,	0.2475,	0.2168,	0.2087,	},
					{	0.3813,	0.3429,	0.3056,	0.2932,	0.2844,	0.2738,	0.2662,	0.2452,	0.2381,	0.2449,	0.215,	0.2092,	},
					{	0.3706,	0.323,	0.2992,	0.2877,	0.2782,	0.2697,	0.2615,	0.243,	0.2351,	0.2417,	0.2136,	0.2096,	},
					{	0.3526,	0.3219,	0.3012,	0.2828,	0.2812,	0.2657,	0.2629,	0.2394,	0.2333,	0.2437,	0.2126,	0.2099,	},
					{	0.3408,	0.3113,	0.2942,	0.2783,	0.2713,	0.2627,	0.2596,	0.2373,	0.2311,	0.241,	0.2229,	0.2102,	},
					{	0.3251,	0.303,	0.2835,	0.2747,	0.2681,	0.2603,	0.2563,	0.2348,	0.2286,	0.2347,	0.2234,	0.2104,	},
					{	0.2926,	0.2761,	0.264,	0.2628,	0.2488,	0.2437,	0.2377,	0.2184,	0.2123,	0.2219,	0.2116,	0.203,	},
					{	0.2783,	0.2643,	0.2548,	0.2495,	0.2388,	0.2317,	0.2399,	0.2089,	0.203,	0.2101,	0.2009,	0.1937,	},
					{	0.2723,	0.2597,	0.2508,	0.2468,	0.235,	0.2226,	0.2318,	0.2063,	0.2005,	0.205,	0.1953,	0.1902,	},
					{	0.2673,	0.2543,	0.2465,	0.2391,	0.2316,	0.225,	0.2199,	0.2039,	0.1982,	0.2042,	0.1844,	0.1853,	},

//					EUR
//					{	0.6869,	0.5509,	0.4686,	0.4264,	0.3939,	0.3754,	0.3595,	},
//					{	0.5334,	0.4617,	0.4184,	0.3884,	0.3702,	0.3572,	0.3461,	},
//					{	0.4856,	0.4344,	0.399,	0.3738,	0.3616,	0.3504,	0.3434,	},
//					{	0.4596,	0.4188,	0.3874,	0.366,	0.3525,	0.344,	0.3401,	},
//					{	0.442,	0.4092,	0.3788,	0.3595,	0.3464,	0.3399,	0.3372,	},
//					{	0.4279,	0.3999,	0.3726,	0.3554,	0.3449,	0.3397,	0.338,	},
//					{	0.4186,	0.393,	0.3692,	0.3534,	0.3443,	0.3403,	0.3394,	},
//					{	0.4105,	0.3876,	0.3668,	0.3528,	0.3448,	0.3413,	0.3406,	},
//					{	0.4059,	0.3849,	0.3651,	0.353,	0.3459,	0.3431,	0.342,	},
//					{	0.4043,	0.3837,	0.3658,	0.354,	0.348,	0.3453,	0.344,	},
//					{	0.381,	0.3616,	0.3497,	0.3393,	0.3368,	0.336,	0.3365,	},
//					{	0.3866,	0.3692,	0.3572,	0.3442,	0.3387,	0.334,	0.3313,	},
//					{	0.3924,	0.375,	0.3607,	0.3482,	0.3407,	0.3355,	0.332,	},
//					{	0.3944,	0.3771,	0.3615,	0.3499,	0.3409,	0.3362,	0.3329,	},			

			};
			Vertex[] swaptionTenorVertex = new Vertex[] {
//					Vertex.valueOf("Y1"), Vertex.valueOf("Y2"), Vertex.valueOf("Y3"),
//					Vertex.valueOf("Y4"), 
//					Vertex.valueOf("Y5"), 
//					Vertex.valueOf("Y7"),
//					Vertex.valueOf("Y10"),
					
//					NEwData Base date = 20150630
//					KRW
//					Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	
//					Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y7"),
//					Vertex.valueOf("Y10"),	Vertex.valueOf("Y12"),	Vertex.valueOf("Y15"),

//					USD
					Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	
					Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	
					Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	
					Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),
					Vertex.valueOf("Y25"),	Vertex.valueOf("Y30"),

//					EUR
//					Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	
//					Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),
//					Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	
//					Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),	
//					Vertex.valueOf("Y25"),	Vertex.valueOf("Y30"),

					
			};
			Vertex[] swaptionMRTYVertex = new Vertex[] {
//					Vertex.valueOf("M1"), Vertex.valueOf("M3"), Vertex.valueOf("M6"),
//					Vertex.valueOf("Y1"), Vertex.valueOf("Y1H"), Vertex.valueOf("Y2"),
//					Vertex.valueOf("Y3"), Vertex.valueOf("Y4"), Vertex.valueOf("Y5"),
//					Vertex.valueOf("Y7"), Vertex.valueOf("Y10"),
					
					
//					NEwData Base date = 20150630
//					KRW
//					Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	
//					Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y7"),
//					Vertex.valueOf("Y10"),

//					USD
					Vertex.valueOf("Y1"),	Vertex.valueOf("Y2"),	Vertex.valueOf("Y3"),	
					Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	
					Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	
					Vertex.valueOf("Y10"),	Vertex.valueOf("Y15"),	Vertex.valueOf("Y20"),

//					EUR
//					Vertex.valueOf("Y4"),	Vertex.valueOf("Y5"),	Vertex.valueOf("Y6"),	
//					Vertex.valueOf("Y7"),	Vertex.valueOf("Y8"),	Vertex.valueOf("Y9"),	
//					Vertex.valueOf("Y10"),

					
					
			};
			
			
			InterestRate [] spotRates = new InterestRate[spotRateValues.length];
			for (int i = 0; i < spotRateValues.length; i++){
				spotRates[i] = new InterestRate(spotRateVertex[i], spotRateValues[i]);
			}
			
			InterestRateCurve spotCurve = new InterestRateCurve(asOfDate, spotRates,
					Frequency.valueOf("C"), dcf);
					
			Frequency couponFrequency = Frequency.valueOf("S");	
			
			VolatilityCurve[] volatilityCurves = new VolatilityCurve[swaptionVolValues.length];
			for (int i = 0; i < swaptionVolValues.length; i++){
				Volatility[] vol = new Volatility[swaptionVolValues[i].length];
				for (int j = 0; j < swaptionVolValues[i].length; j++){
					vol[j] = new Volatility(swaptionMRTYVertex[j] ,swaptionVolValues[i][j]);				
				}
				volatilityCurves[i] = new VolatilityCurve(swaptionTenorVertex[i], vol, asOfDate, dcf);
				
			}
			
			VolatilitySurface swaptionSurface = new VolatilitySurface(volatilityCurves,
					asOfDate, dcf);
			
			
			HullWhiteCalibration cali1 = new HullWhiteCalibration(asOfDate,"KR", swaptionSurface, spotCurve);
			HullWhite2FCalibration cali2 = new HullWhite2FCalibration(asOfDate,"KR", swaptionSurface, spotCurve);
			HullWhiteParameters HW1F =  cali1.calibration();
			HullWhiteParameters HW2F =  cali2.calibration();
			
			double meanReversion1_1F =  (HW1F.getMeanReversion1F());
			double hullWhiteVolatility_1F = HW1F.getHullWhiteVolatility1F();
			double meanReversion1_2F = HW2F.getMeanReversion1_2F();
			double meanReversion2_2F = HW2F.getMeanReversion2_2F();
			double hullWhiteVolatility1_2F = (HW2F.getHullWhiteVolatility1_2F());
			double hullWhiteVolatility2_2F = (HW2F.getHullWhiteVolatility2_2F());
			double correlation = -0.75;
			
//			double meanReversion1_1F = 0.018679721066364126;
//			double hullWhiteVolatility_1F = 0.009624896295240975;
//			meanReversion1_2F = 0.3;/*0.11772753312890531;*/
//			double meanReversion2_2F = 0.03;
//			hullWhiteVolatility1_2F = Math.min(0.109452751883725857, hullWhiteVolatility1_2F);
//			double hullWhiteVolatility2_2F = 1.370321330010119E-7;
//			double correlation = -0.75;
			
			HullWhiteParameters HWParams= new HullWhiteParameters(
					meanReversion1_1F, hullWhiteVolatility_1F, 
					meanReversion1_2F, meanReversion2_2F, 
					hullWhiteVolatility1_2F, hullWhiteVolatility2_2F, 
					correlation);
			
			
			HWVolatilityCalibration cali = new HWVolatilityCalibration(
					countryCode, asOfDate, spotCurve, 
					swaptionSurface, HWParams);
			
			VolatilitySurface hwSurf = cali.calculate();
			
			double[] tenor = new double[]{
				0.25, 0.5, 0.75, 1.0, 
				1.25, 1.5, 1.75, 2.0, 
				2.25, 2.5, 2.75, 3.0,
				3.25, 3.5, 3.75, 4.0,
				4.25, 4.5, 4.75, 5.0,
				5.25, 5.5, 5.75, 6.0,
				6.25, 6.5, 6.75, 7.0,
				7.25, 7.5, 7.75, 8.0,
				8.25, 8.5, 8.75, 9.0,
				9.25, 9.5, 9.75, 10.0,
			};
			double[] maturity = new double[]{
				1.0/365.0, 2.0/365.0, 3.0/365.0,
				7.0/365.0, 
				0.08333, 0.16666, 0.25, 0.5, 0.75, 1, 
				1.25, 1.5, 1.75, 2.0, 
				2.25, 2.5, 2.75, 3.0,
				3.25, 3.5, 3.75, 4.0,
				4.25, 4.5, 4.75, 5.0,
				5.25, 5.5, 5.75, 6.0,
				6.25, 6.5, 6.75, 7.0,
				7.25, 7.5, 7.75, 8.0,
				8.25, 8.5, 8.75, 9.0,
				9.25, 9.5, 9.75, 10.0,			
			};
			
//			String fileName = "D:\\test01.txt";
//			
//			File file = new File(fileName);
//			
//			try {
//				FileWriter fw = new FileWriter(file);
//				for (int i = 0; i < tenor.length; i++){
//					for (int j = 0; j < maturity.length; j++){
//						
//						double NewVol1 = ((HullWhiteVolatilitySurface)hwSurf).getVol(tenor[i], maturity[j], 1);
//						double NewVol2 = ((HullWhiteVolatilitySurface)hwSurf).getVol(tenor[i], maturity[j], 2);
//						double NewVol3 = ((HullWhiteVolatilitySurface)hwSurf).getVol(tenor[i], maturity[j], 3);
//						
//						double[][] swaptionInfo = new double[][]{
//								{maturity[j], tenor[i]},};
//						
//						HullWhiteVolatilityCalibration volCali = new HullWhiteVolatilityCalibration(
//								asOfDate, swaptionInfo, spotCurve, swaptionSurface, HWParams, dcf);
//						
//						HullWhiteVolatility[] tmpResult = volCali.calibration();
//						
//						HullWhite2FVolatilityCalibration volCali2 = new HullWhite2FVolatilityCalibration(
//								asOfDate, swaptionInfo, spotCurve, swaptionSurface, HWParams, dcf);
//						
//						HullWhiteVolatility[] tmpResult2 = volCali2.calibration();
	//
//						double OldVol1 = tmpResult[0].getVolatility(0);
//						double OldVol2 = tmpResult2[0].getVolatility(0);
//						double OldVol3 = tmpResult2[1].getVolatility(0);
//						
//						String contents = "1F Tenor: " + tenor[i] + ", Maturity: " + maturity[j] 
//								+ ", NewVol: " + NewVol1 + ", OldVol: " + OldVol1 + 
//								", DIFF: " + (NewVol1- OldVol1) + "\n";
//						
////						String contents2 = "2F Tenor: " + tenor[i] + ", Maturity: " + maturity[j] + 
////								", NewVol1: " + NewVol2 + ", OldVol1: " + OldVol2 + 
////								", DIFF1: " + (NewVol2- OldVol2) +
////								", NewVol2: " + NewVol3 + ", OldVol2: " + OldVol3 + 
////								", DIFF2: " + (NewVol3- OldVol3) + "\n";
//						
//						fw.write(contents);
////						fw.write(contents2);
//						fw.flush();
//						log(contents);
////						log(contents2);
//						
//					}
//				}
//				fw.close(); 			
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
			
			
			
			
//			
//			//tenor, maturity
//			double vol1 = hwSurf.getVol(0.5, 1);		
//			log(vol1);
//			double vol2 = hwSurf.getVol(0.5, 0.75);		
//			log(vol2);
//			double vol3 = hwSurf.getVol(1.5, 0.75);		
//			log(vol3);
//			
//			double[][] swaptionInfo = new double[][]{
//					{1, 0.5},
////					{0.75, 0.5},
////					{0.75, 1.5},
//					};
//			
//			HullWhiteVolatilityCalibration volCali = new HullWhiteVolatilityCalibration(
//					asOfDate, swaptionInfo, spotCurve, swaptionSurface, HWParams, dcf);
//			
//			HullWhiteVolatility[] volResult = volCali.calibration();
//			
//			log(volResult[0].getVolatility()[0]);
//			
//			
//			
		}
	}
}
