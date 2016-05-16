package com.quantosauros.common.calibration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunction;
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunctionJacobian;
import org.apache.commons.math3.optim.nonlinear.vector.Target;
import org.apache.commons.math3.optim.nonlinear.vector.Weight;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.common.math.distribution.NormalDistribution;
import com.quantosauros.common.math.solver.Solvable;
import com.quantosauros.common.volatility.VolatilitySurface;
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2014
* - Creation Date : 2014-04-18
* - Creator : Jihoon Lee
------------------------------------------------------------------------------*/

public class HullWhiteCalibration implements Serializable {
	PointVectorValuePair _optimum;
	
	/**
	 * Hull White 1-factor 모델의 파라미터를 Calibration 하는 클래스
	 * 캘리브레이션의 대상이 되는 Swaption은 1x1, 1x10, 2x3, 5x5, 5x10, 7x3 이다.
	 * 
	 * @param asOfDate						측정일
	 * @param countryCode					국가코드
	 * @param surface						Swaption 변동성 곡면
	 * @param spotCurve						이자율 커브
	 */
	public HullWhiteCalibration(Date asOfDate,
			String countryCode,
			VolatilitySurface surface,
			ZeroRateCurve spotCurve) {
		
		//초기값
		double initialMeanReversion = 0.05;
		double initialVol = 0.02;

//		Using input surface vertex not specific predetermined vertex(taeheum.cho 2015.07.28) 		
//		Tenor Vertex
//		int tenorStartIndex = 0;
//		int tenorEndIndex = 0;
		int tenorLen = surface.Length();
		ArrayList<Vertex> selectTenorVertex = new ArrayList<Vertex>();
		Vertex[] tenorVertex = new Vertex[tenorLen];
		for (int i = 0; i < tenorLen; i++){
			tenorVertex[i] = surface.getVolatiltyCurve(i).getSwapMaturity();
			if (tenorVertex[i].getVertex() <= 3 || i % 2 == 1){
//			if (tenorVertex[i].getVertex() < 5 || i % 2 == 1){
//				tenorStartIndex += 1;
			} else {
				selectTenorVertex.add(tenorVertex[i]);
			}

		}
//		Maturity Vertex
//		int matStartIndex = 0;
//		int matEndIndex = 0;
		int matLen = surface.getVolatiltyCurve(0).length();
		Vertex[] matVertex = new Vertex[matLen];
		ArrayList<Vertex> selectMrtVertex = new ArrayList<Vertex>();
		for (int i = 0; i < matLen; i++){
			matVertex[i] = surface.getVolatiltyCurve(0).getVolatilities()[i].getVertex();
			if (matVertex[i].getVertex() <= 3 || i % 2 == 1){
//				matStartIndex += 1;
			} else {
				selectMrtVertex.add(matVertex[i]);
			}

		}

//		만기 및 테너 모두 실제 surface의 vertex에서 3년 이상의 vertex를 사용하도록 수정
		Vertex[][] swaptionVertex = new Vertex[selectTenorVertex.size()*selectMrtVertex.size()][2];
		
		for (int i = 0; i < selectMrtVertex.size(); i++){
			for (int j = 0; j < selectTenorVertex.size(); j++){
				swaptionVertex[(i) * (selectTenorVertex.size()) + j ][0] = selectMrtVertex.get(i);
				swaptionVertex[(i) * (selectTenorVertex.size()) + j ][1] = selectTenorVertex.get(j);
			}
		}
		
//		//[Maturity] [Tenor]
//		Vertex[][] swaptionVertex = new Vertex[][] {
////				{Vertex.valueOf("Y1"), Vertex.valueOf("Y1")},
////				{Vertex.valueOf("Y1"), Vertex.valueOf("Y3")},
////				{Vertex.valueOf("Y1"), Vertex.valueOf("Y5")},
////				{Vertex.valueOf("Y2"), Vertex.valueOf("Y5")},
////				{Vertex.valueOf("Y3"), Vertex.valueOf("Y5")},
////				{Vertex.valueOf("Y5"), Vertex.valueOf("Y1")},
////				{Vertex.valueOf("Y5"), Vertex.valueOf("Y3")},
////				{Vertex.valueOf("Y5"), Vertex.valueOf("Y5")},
//				{Vertex.valueOf("Y3"), Vertex.valueOf("Y2")},
//				{Vertex.valueOf("Y3"), Vertex.valueOf("Y10")},
//				{Vertex.valueOf("Y5"), Vertex.valueOf("Y1")},
//				{Vertex.valueOf("Y5"), Vertex.valueOf("Y5")},
//				{Vertex.valueOf("Y5"), Vertex.valueOf("Y10")},
//				{Vertex.valueOf("Y7"), Vertex.valueOf("Y1")},
//				{Vertex.valueOf("Y7"), Vertex.valueOf("Y7")},
//				{Vertex.valueOf("Y7"), Vertex.valueOf("Y15")},
//				{Vertex.valueOf("Y8"), Vertex.valueOf("Y12")},
//				
//				};
		
		int swaptionN = swaptionVertex.length;
		
		double[] swaptionVols = new double[swaptionN];
		for (int i = 0; i < swaptionN; i++){		
			swaptionVols[i] = surface.getVol(
					swaptionVertex[i][1].getYears(), swaptionVertex[i][0].getYears());
		}		
		
		double[] atmStrike = new double[swaptionN];
		double[] swaptionValues = new double[swaptionN];
		
		//국가코드에 따른 dcf, couponFrequency 결정
		DayCountFraction dcf = null;
		Frequency couponFrequency = null;
		Calendar calendar = CalendarFactory.getInstance(countryCode, 0);
		BusinessDayConvention businessDayConvention = null;
		int settlementDay = 0;
		
		if (countryCode.equals("KR")){
			dcf = DayCountFraction.ACTUAL_365;
			couponFrequency = Frequency.valueOf("Q");
			settlementDay = 1;
			businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
		} else if (countryCode.equals("US")){
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("S");
			settlementDay = 2;
			businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
		} else {
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("Q");
			settlementDay = 2;
			businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
		}
		Date adjustedAsOfDate = calendar.adjustDate(asOfDate, businessDayConvention);
		Date settlementDate = calendar.adjustDate(
				adjustedAsOfDate.plusDays(settlementDay), businessDayConvention);	
		for (int i = 0; i < swaptionN; i++){			
			
			Vertex vertex = swaptionVertex[i][0];
			Date startDate = vertex.getDate(settlementDate);
			Date adjustedStartDate = calendar.adjustDate(
					startDate, businessDayConvention);
			
			//Date startDate = asOfDate.plusDays((int)(swaptionInfo[i][0] * dcf.getDaysOfYear()));
			//Swaption의 ATM Strike 도출
			atmStrike[i] = spotCurve.getForwardSwapRate(adjustedStartDate,
					swaptionVertex[i][1].getYears(), couponFrequency);
			ESwaption eswaption = new ESwaption(Money.valueOf("KRW", 1),
					adjustedStartDate, swaptionVertex[i][1].getYears(), 
					atmStrike[i], 2, dcf, couponFrequency);
			swaptionValues[i] = eswaption.getPrice(adjustedAsOfDate, spotCurve,
					swaptionVols[i], spotCurve).getAmount();
			//System.out.println("BK: " + swaptionValues[i]);
		}
		
		hullWhiteProblem problem = new hullWhiteProblem(
				initialMeanReversion, initialVol, spotCurve, couponFrequency);
		 
		 for (int i = 0; i < swaptionN; i++){
			 problem.addPoint(
					 swaptionVertex[i][0].getYears(), 
					 swaptionVertex[i][1].getYears(), 
					 atmStrike[i], swaptionValues[i]);
		 }			
		 
		 final double[] weights = new double[swaptionN];
		 for (int i = 0; i < swaptionN; i++){
				weights[i] = 1.0 ;/// swaptionValues[i];
			}
//		 final double[] weights = {1 / swaptionValues[0],
//				 1 / swaptionValues[1], 1 / swaptionValues[2], 1 / swaptionValues[3],
//				 1 / swaptionValues[4], 1 / swaptionValues[5]};

		 final double[] initialSolution = {initialMeanReversion, initialVol};
			int MaxIterations = 800;
			int maxStationaryStateIterations = 400;
			double rootEpsilon = 1.0e-6;
			double functionEpsilon = 1.0e-6;
			double gradientNormEpsilon = 1.0e-6;
		 
        LevenbergMarquardtOptimizer optimizer
        = new LevenbergMarquardtOptimizer(MaxIterations, 
       		 rootEpsilon, functionEpsilon, gradientNormEpsilon, 0);
        
        ModelFunction function = new ModelFunction(problem);
        
        _optimum = optimizer.optimize(new MaxEval(1000), 
        		function,
        		problem.getModelFunctionJacobian(),
        		new Target(problem.getTarget()),
                new Weight(weights),
                new InitialGuess(initialSolution));	     
                
	}
	public HullWhiteParameters calibration(){
		
		double[] optimizationValue = _optimum.getPoint();
		
		HullWhiteParameters params = new HullWhiteParameters(
				Math.abs(optimizationValue[0]), Math.abs(optimizationValue[1]));
		
		return params;
	}
	
	
	private static class hullWhiteProblem implements MultivariateVectorFunction, Serializable {
		
		private Frequency _couponFrequency;
		private ZeroRateCurve _spotCurve;
		private HullWhite _model;
		
	    private List<Double> maturity;
	    private List<Double> tenor;
	    private List<Double> strike;
	    private List<Double> swaptionValue;
		
	    public hullWhiteProblem(double meanReversion, double initialVol, ZeroRateCurve spotCurve,
				Frequency couponFrequency) {
			maturity = new ArrayList<Double>();
			tenor = new ArrayList<Double>();
			strike = new ArrayList<Double>();
			swaptionValue = new ArrayList<Double>();
			this._model = new HullWhite(meanReversion, initialVol, spotCurve);
			this._couponFrequency = couponFrequency;
			this._spotCurve = spotCurve;
		}
		public void addPoint(double maturity, double tenor, double strike, double swaptionValue) {
			this.maturity.add(maturity);
			this.tenor.add(tenor);
			this.strike.add(strike);
			this.swaptionValue.add(swaptionValue);
	    }		
	    public double[] getTarget() {
	        double[] target = new double[swaptionValue.size()];
	        for (int i = 0; i < swaptionValue.size(); i++) {
	            target[i] = swaptionValue.get(i).doubleValue();
	        }
	        return target;
	    }
		// 0 : a , 1 : vol
		public double[] value(double[] variables){
	        double[] values = new double[maturity.size()];
	        for (int i = 0; i < values.length; ++i) {            	
	        	this._model = new HullWhite(variables[0], variables[1], this._spotCurve);
	            values[i] = HWSwaptionPrice(0, maturity.get(i), tenor.get(i), 
	            		strike.get(i), 2);
	        }
	        return values;
		}	   
		public ModelFunctionJacobian getModelFunctionJacobian() {
			return new ModelFunctionJacobian(new MultivariateMatrixFunction() {
				public double[][] value(double[] arg0){
					return jacobian(arg0);
				}				
			});
		}
		//[관측값][변수]
		private double[][] jacobian(double[] variables) {
	        double[][] jacobian = new double[maturity.size()][variables.length];
	        for (int i = 0; i < jacobian.length; ++i) {            	
	        	for (int j = 0; j < variables.length; j++){
	        		double upValue = 0;
	            	double downValue = 0;
	        		double upVariable = variables[j] * 1.01;
	            	double downVariable = variables[j] * 0.99;
	        		if (j == 0){//i == 0 : for a
	        			if (variables[0] <= 0.01){

	        			} else {
		            		// up            		
		            		this._model = new HullWhite(upVariable, variables[1], this._spotCurve);
		            		upValue = HWSwaptionPrice(0, maturity.get(i), tenor.get(i), 
		                    		strike.get(i), 2); 
		            		//down            		
		            		this._model = new HullWhite(downVariable, variables[1], this._spotCurve);
		            		downValue = HWSwaptionPrice(0, maturity.get(i), tenor.get(i), 
		                    		strike.get(i), 2);

	        			}
//	            		
	            	} else { //i == 1 : for vol
	            		this._model = new HullWhite(variables[0], upVariable, this._spotCurve);
	            		upValue = HWSwaptionPrice(0, maturity.get(i), tenor.get(i), 
	                    		strike.get(i), 2); 
	            		//down            		
	            		this._model = new HullWhite(variables[0], downVariable, this._spotCurve);
	            		downValue = HWSwaptionPrice(0, maturity.get(i), tenor.get(i), 
	                    		strike.get(i), 2);
	            	}	        		 
	        		jacobian[i][j] = (upValue - downValue) / (2 * variables[j] * 0.01);			            
	        	}		            
	        }
	        return jacobian;
	    }
		public void setMeanReversion(double meanReversion){
			this._model = new HullWhite(meanReversion, _model.getVol(), _model.getTermStructure());
		}
		public void setVol(double vol){
			this._model = new HullWhite(_model.getMeanReversion(), vol, _model.getTermStructure());
		}
		public double HWSwaptionPrice(double valueTime, double maturity, double tenor, double strike,
				int type){
			
			double value = 0;
			int swapLength = (int)(tenor / _couponFrequency.getInterval());
			double[] tenorDt = new double[swapLength];
			double[] strikes = new double[swapLength];
			double[] dboValue = new double[swapLength];
			double[] coupon = new double[swapLength];
			
			double rStar = getRStar(maturity, maturity, tenor, strike, 
					1, 1.0e-008, -10.0, 10.0);
			for (int i = 0; i < swapLength; i++){
				tenorDt[i] =  maturity + _couponFrequency.getInterval() * (i + 1);
				strikes[i] = _model.discountBond(maturity, tenorDt[i], rStar) /
						_model.discountBond(maturity, maturity, rStar);
				coupon[i] = strike * _couponFrequency.getInterval();
				if (i == swapLength - 1)
					coupon[i] += 1; 
				dboValue[i] = _model.discountBondOption(type, strikes[i], maturity,
						tenorDt[i]);
				value += dboValue[i] * coupon[i];
			}
			
			return value;
		}
		private double getRStar(double valueTime, double maturity, double tenor,
				double strike, double targetValue, 
				double accuracy, double xMin, double xMax){
			
			int maxIteration = 10000;
			Solvable function = new FindingRStarFunction(valueTime, maturity, tenor, targetValue, strike);
			
			double absoluteAccuracy = 1.0E-10;
			BisectionSolver bs = new BisectionSolver(absoluteAccuracy);
			
			return bs.solve(maxIteration, function, xMin, xMax);
		}
		
		private class HullWhite {
			
			private double _meanReversion;
			private double _sigma;
			private ZeroRateCurve _termStructure;
//				private HullWhiteVolatility _vols;
				
			public HullWhite(double meanReversion, double sigma, ZeroRateCurve termStructure) {
				this._meanReversion = meanReversion;
				this._sigma = sigma;
				this._termStructure = termStructure;
			}
//				public HullWhite(double meanReversion, HullWhiteVolatility vols, InterestRateCurve termStructure) {
//					this._meanReversion = meanReversion;
//					this._vols = vols;
//					this._termStructure = termStructure;
//				}

			public double discountBondOption(int type, double strike, double maturity, 
					double bondMaturity){

		        double v = _sigma * getB(maturity, bondMaturity) *
		                Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * _meanReversion *maturity)) / _meanReversion);
//					System.out.println(v);		
		        double f = _termStructure.getDiscountFactor(bondMaturity);
		        double k = _termStructure.getDiscountFactor(maturity) * strike;
		        
		        //v에 이미 tenor가 들어가있음, 그래서 만기가 1
		        GeneralizedBlackScholesMertonFormula gB = new GeneralizedBlackScholesMertonFormula(
		        		type, f, k, 1, 
		        		0, 0, v);
		        
		        return gB.getValue();        
			}
			public double discountBondOption(int type, double strike, double maturity, 
					double bondStart, double bondMaturity){
				
				 double v = _sigma / (_meanReversion * Math.sqrt(2.0 * _meanReversion)) * 
				 Math.sqrt(Math.exp(-2.0 * _meanReversion * (bondStart - maturity)) -
						 Math.exp(-2.0 * _meanReversion * bondStart) - 2.0 * (Math.exp(- _meanReversion *
								 (bondStart + bondMaturity -2.0 * maturity)) - 
								 Math.exp(-_meanReversion * (bondStart + bondMaturity))) +
								 Math.exp(-2.0 * _meanReversion * (bondMaturity - maturity)) -
								 Math.exp(-2.0 * _meanReversion * bondMaturity));
				 
			        double f = _termStructure.getDiscountFactor(bondMaturity);
			        double k = _termStructure.getDiscountFactor(maturity) * strike;
			        
			        //v에 이미 tenor가 들어가있음, 그래서 만기가 1
			        GeneralizedBlackScholesMertonFormula gB = new GeneralizedBlackScholesMertonFormula(
			        		type, f, k, 1, 
			        		0, 0, v);
			        
			        return gB.getValue(); 
				
			}
			public double discountBond(double now, double maturity, double rate){
				
				return getA(now, maturity) * Math.exp(- getB(now, maturity) * rate);
			}
			public double getA(double from, double to){
				double discount1 = _termStructure.getDiscountFactor(from);
				double discount2 = _termStructure.getDiscountFactor(to);
				
				double dt = 1.0/365.0;
//					double forward = _termStructure.getForwardRate(from, from);
				double forward = ((_termStructure.getSpotRate(from + dt) - 
						_termStructure.getSpotRate(from)) * (from / dt)) +
						_termStructure.getSpotRate(from);
				
				double temp = _sigma * getB(from, to);
				
				double value = getB(from, to) * forward - 0.25 * temp * temp * getB(0, 2.0 * from);
				
				return Math.exp(value) * discount2 / discount1;
				
			}
			public double getB(double from, double to){
				
				double dt = to - from;
				
				double exponentialB = (1 - Math.exp(-_meanReversion * dt)) / _meanReversion;
				
				return exponentialB; 
			}
			public double getMeanReversion(){
				return _meanReversion;
			}
			public double getVol(){
				return _sigma;
			}
			public ZeroRateCurve getTermStructure(){
				return _termStructure;
			}
		}
		private class FindingRStarFunction extends Solvable {
			
			private double _targetValue;
			private double _valueTime;
			private double _maturity;
			private double _tenor;
			private double _strike;
			
			private FindingRStarFunction(double valueTime, double maturity, double tenor, 
					double targetValue, double strike) {			
				_targetValue = targetValue;
				_valueTime = valueTime;
				_maturity = maturity;
				_tenor = tenor;
				_strike = strike;
			}
			
			public double getY(double rate) {
				double value = 0;
				int swapLength = (int)(_tenor / _couponFrequency.getInterval());
				double[] tenorDt = new double[swapLength];
				double[] coupon = new double[swapLength];
				
				for (int i =0; i < swapLength; i++){
					coupon[i] = _strike * _couponFrequency.getInterval();
					if (i == swapLength - 1)
						coupon[i] += 1; 
					tenorDt[i] =  _maturity + _couponFrequency.getInterval() * (i + 1);
					
					double dbValue = coupon[i] * _model.discountBond(_maturity, tenorDt[i], rate) /
							_model.discountBond(_maturity, _valueTime, rate);
					value += dbValue;
				}			
				
				return  value - _targetValue;
			}
		}
	
		private class GeneralizedBlackScholesMertonFormula {
			private double S;
			private double X;
			private double T;
			private double r;
			private double b;
			private double v;
			
			private double d1;
			private double d2;
			private double _stdDev;
			private double nd1;
			private double n_d1;
			private double nd2;
			private double n_d2;
			private double _dividendFactor;
			private double _discountFactor;
			private int _optionType;
			
			//optionType[1:Call, 2:Put]
			public GeneralizedBlackScholesMertonFormula(
					int optionType, double underlyingPrice, double strike, 
					double maturity, double riskFreeRate, double costOfCarry,
					double underlyingVolatility) {
				_optionType = optionType;
				S = underlyingPrice;
				X = strike;
				T = maturity;
				r = riskFreeRate;
				b = costOfCarry;
				v = underlyingVolatility;

				init();
			}
			private void init() {
				//2009.11.13 Yang Jaechul 만기가 0인 경우 계산시 에러가 발생하기 때문에 아무 계산도 수행하지 않음
				if (T > 0) {
					_stdDev = v * Math.sqrt(T);
					d1 = (Math.log(S / X) + (b + 0.5 * v * v) * T) / _stdDev;
					d2 = d1 - _stdDev;
					nd1 = NormalDistribution.getCumulativeProbability(d1);
					n_d1 = NormalDistribution.getCumulativeProbability(-d1);
					nd2 = NormalDistribution.getCumulativeProbability(d2);
					n_d2 = NormalDistribution.getCumulativeProbability(-d2);
					_dividendFactor = Math.exp((b - r) * T);
					_discountFactor = Math.exp(-r * T);	
				}
			}
			public double getValue() {
				//2009.11.13 Yang Jaechul 만기가 0인 경우 페이오프를 리턴함
				if (T <= 0) {
					//Payoff로 변경하도록 김종혁 박사님이 요청함
					if (_optionType == 1) {
						return Math.max(S - X, 0); 
					} else if (_optionType == 2) {
						return Math.max(X - S, 0);
					}
					//return 0.0;
				}
				if (_optionType == 1) {
					return S * _dividendFactor * nd1 - X * _discountFactor * nd2; 
				} else if (_optionType == 2) {
					return X * _discountFactor * n_d2 - S * _dividendFactor * n_d1;
				}
				return 0;
			}
		}
		
	}
	private class ESwaption{
		private Money _fixedNotionalPrincipal;
		private Money _floatingNotionalPrincipal;
		private Date _terminationDate;
		private double _swapMaturity;
		private double _strikeRate;
		private int _swaptionType;
		private DayCountFraction _dcf;
		private Frequency _paymentFrequency;
		
		//swaptionType [1: Call, 2: Put]
		public ESwaption(Money notionalPrincipal, Date terminationDate,
				double swapMaturity, double strikeRate, int swaptionType,
				DayCountFraction dcf, Frequency paymentFrequency) {
			_fixedNotionalPrincipal = new Money(notionalPrincipal.getAppCurrency(), 
					notionalPrincipal.getAmount());
			_floatingNotionalPrincipal = new Money(notionalPrincipal.getAppCurrency(), 
					notionalPrincipal.getAmount());
			
			_terminationDate = terminationDate;
			_swapMaturity = swapMaturity;
			_strikeRate = strikeRate;
			_swaptionType = swaptionType;
			_dcf = dcf;
			_paymentFrequency = paymentFrequency;
		}
		
		public Money getPrice(Date date, ZeroRateCurve swapCurve, double swaptionVol,
				ZeroRateCurve discountCurve){
			double forwardSwapRate =
					swapCurve.getForwardSwapRate(_terminationDate, _swapMaturity,
							_paymentFrequency, _fixedNotionalPrincipal, 
							_floatingNotionalPrincipal);
			double t = _dcf.getYearFraction(date, _terminationDate);
			
			double price = getGeneralizedBlackScholesMerton(
					_swaptionType, forwardSwapRate, _strikeRate,
					t, 0, 0, swaptionVol);
			double a = discountCurve.getSumSwapDiscountFactorWithCoupon(
					_terminationDate, _swapMaturity, _paymentFrequency);
			price *= a;
			return _fixedNotionalPrincipal.multiplyAmount(price);		
				
		}
		public double getGeneralizedBlackScholesMerton(int optionType,
				double S, double X,	double T, double r,	double b, double v) {
			if (T <= 0) {
				return 0;
			}
			double d1;
			if (S / X < 0){
				d1 = 100;
			} else{
				d1 = (Math.log(S / X) + (b + 0.5 * v * v) * T) / (v * Math.sqrt(T));
			}
			if (Double.isNaN(d1)){
				d1 = 100;
			}
			double d2 = d1 - v * Math.sqrt(T);
			
			//Call
			if (optionType == 1) {
				return S * Math.exp((b - r) * T) * 
						NormalDistribution.getCumulativeProbability(d1) - 
						X * Math.exp(-r * T) * 
						NormalDistribution.getCumulativeProbability(d2);
			} 
			else { // PUT
				return X * Math.exp(-r * T) * 
						NormalDistribution.getCumulativeProbability(-d2) - 
						S * Math.exp((b - r) * T) * 
						NormalDistribution.getCumulativeProbability(-d1);
			}
		}
	}
	public double[][] testCalibration(Date asOfDate,
			String countryCode,
			VolatilitySurface surface,
			ZeroRateCurve spotCurve){
		
		int totalNumVol = 10;
		int totalNumMeanReversion = 10;
		int targetIndex = 0;
		double volIncremental = 0.001;
		double meanReversionIncremental = 0.01;
		
		double[][] values = new double[totalNumVol][totalNumMeanReversion];
		
		double initialMeanReversion = 0.01;
		double initialVol = 0.01;
		//대상 스왑션 고정
		double[][] swaptionInfo = new double[][] 
				{{1, 1}, {1, 10}, {2, 3}, {5, 5}, {5, 10}, {7, 3}};
		double[] swaptionVols = new double[6];
		for (int i = 0; i < swaptionInfo.length; i++){
			swaptionVols[i] = surface.getVol(swaptionInfo[i][1], swaptionInfo[i][0]);
		}
		DayCountFraction dcf = null;
		Frequency couponFrequency = null;
		if (countryCode.equals("KR")){
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("S");
		} else if (countryCode.equals("US")){
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("Q");
		} else {
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("Q");					
		}
		
		//black scholes price		
		Date startDate = asOfDate.plusDays((int)(swaptionInfo[targetIndex][0] * dcf.getDaysOfYear()));
		double atmStrike = spotCurve.getForwardSwapRate(startDate,
				swaptionInfo[targetIndex][1], couponFrequency);
		ESwaption eswaption = new ESwaption(Money.valueOf("KRW", 1),
				startDate, swaptionInfo[targetIndex][1], atmStrike, 2,
				dcf, couponFrequency);
		double bkPrice = eswaption.getPrice(asOfDate, spotCurve,
				swaptionVols[targetIndex], spotCurve).getAmount();
		System.out.println("BlackScholes : " + bkPrice);
		
		//hull white price
		double meanReversion = initialMeanReversion;
		double sigma = initialVol;
		hullWhiteProblem problem = new hullWhiteProblem(meanReversion, initialVol,
				spotCurve, couponFrequency);		
		for (int j = 0; j < totalNumVol; j++){
			for (int k = 0; k < totalNumMeanReversion; k++){
				values[j][k] = problem.HWSwaptionPrice(0, 
		        		swaptionInfo[targetIndex][0], swaptionInfo[targetIndex][0], 
		        		atmStrike, 2);
				
				System.out.println("HullWhite : " + meanReversion + " : " + sigma + " : " + values[j][k]);
				
				meanReversion += meanReversionIncremental;
				problem.setMeanReversion(meanReversion);
				
			}			
	        sigma += volIncremental;
	        meanReversion = initialMeanReversion;
	        problem.setVol(sigma);
	        problem.setMeanReversion(meanReversion);
	        System.out.println("=============================================================");
		}
		
		return values;		
	}
	public double[][] testCalibration2(Date asOfDate,
			String countryCode,
			VolatilitySurface surface,
			ZeroRateCurve spotCurve){
		int totalNumVol = 50;
		int totalNumMeanReversion = 50;
		double volIncremental = 0.0001;
		double meanReversionIncremental = 0.001;
		
		double[][] values = new double[totalNumVol][totalNumMeanReversion];
		
		double initialMeanReversion = 0.001;
		double initialVol = 0.001;
		//대상 스왑션 고정
		double[][] swaptionInfo = new double[][] 
				{{1, 1}, {1, 10}, {2, 3}, {5, 5}, {5, 10}, {7, 3}};
		double[] swaptionVols = new double[6];
		for (int i = 0; i < swaptionInfo.length; i++){
			swaptionVols[i] = surface.getVol(swaptionInfo[i][1], swaptionInfo[i][0]);
		}
		DayCountFraction dcf = null;
		Frequency couponFrequency = null;
		if (countryCode.equals("KR")){
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("S");
		} else if (countryCode.equals("US")){
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("Q");
		} else {
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("Q");					
		}
		int swaptionN = swaptionInfo.length;
		
		double meanReversion = initialMeanReversion;
		double sigma = initialVol;
		hullWhiteProblem problem = new hullWhiteProblem(meanReversion, initialVol,
				spotCurve, couponFrequency);
		
		for (int j = 0; j < totalNumVol; j++){
			for (int k = 0; k < totalNumMeanReversion; k++){				
				for (int i = 0; i < swaptionN; i++){
					//black scholes price		
					Date startDate = asOfDate.plusDays((int)(swaptionInfo[i][0] * dcf.getDaysOfYear()));
					double atmStrike = spotCurve.getForwardSwapRate(startDate,
							swaptionInfo[i][1], couponFrequency);
					ESwaption eswaption = new ESwaption(Money.valueOf("KRW", 1),
							startDate, swaptionInfo[i][1], atmStrike, 2,
							dcf, couponFrequency);
					double bkPrice = eswaption.getPrice(asOfDate, spotCurve,
							swaptionVols[i], spotCurve).getAmount();
//					System.out.println("BlackScholes : " + bkPrice);
					
					//hull white price					
					double hwPrice = problem.HWSwaptionPrice(0, 
			        		swaptionInfo[i][0], swaptionInfo[i][0], 
			        		atmStrike, 2); 
					values[j][k] += (bkPrice - hwPrice) * (bkPrice - hwPrice); 
				}
				meanReversion += meanReversionIncremental;
				problem.setMeanReversion(meanReversion);
			}
			 sigma += volIncremental;
		        meanReversion = initialMeanReversion;
		        problem.setVol(sigma);
		        problem.setMeanReversion(meanReversion);
		}
//		System.out.println("HullWhite : " + meanReversion + " : " + sigma + " : " + values[j][k]);
//		System.out.println("=============================================================");
		
		return values;
	}
}


