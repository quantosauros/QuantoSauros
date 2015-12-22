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
import com.quantosauros.common.hullwhite.HWVolatility;
import com.quantosauros.common.hullwhite.HWVolatilityCurve;
import com.quantosauros.common.hullwhite.HWVolatilitySurface;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.common.math.distribution.NormalDistribution;
import com.quantosauros.common.math.solver.Solvable;
import com.quantosauros.common.volatility.VolatilitySurface;


public class HWVolatilityCalibration implements Serializable {
	
	private static final double M_PI = 3.14159265358979323846;
	private InterestRateCurve _spotCurve;
	private VolatilitySurface _swaptionSurface;
	private HullWhiteParameters _hwParams;
	private Calendar _calendar;		
	private DayCountFraction _dcf;
	private int _settlementDay;
	private BusinessDayConvention _businessDayConvention; 
	private Frequency _couponFrequency;
	private Date _asOfDate;
	
	private int _tenorMax;
	private int _mrtyMax;
	
	private double[][][] _hwVols;	
	
	private HWVolatilitySurface _hwSurface;
	
	public HWVolatilityCalibration(String countryCode, Date asOfDate,
			InterestRateCurve spotCurve, VolatilitySurface swaptionSurface,
			HullWhiteParameters HWParams){
		
		_asOfDate = asOfDate;
		
		_calendar = CalendarFactory.getInstance(countryCode, 0);		
		_dcf = null;
		_settlementDay = 0;
		_businessDayConvention = null;
		_couponFrequency = null;
		
		if (countryCode.equals("KR")){
			_dcf = DayCountFraction.ACTUAL_365;
			_settlementDay = 1;
			_businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
			_couponFrequency = Frequency.valueOf("Q");
		} else if (countryCode.equals("US")){
			_dcf = DayCountFraction.ACTUAL_360;
			_settlementDay = 2;
			_businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
			_couponFrequency = Frequency.valueOf("S");
		} else if (countryCode.equals("EU")){
			//TODO				
			_dcf = DayCountFraction.ACTUAL_360;
			_settlementDay = 2;
			_businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
			_couponFrequency = Frequency.valueOf("S");
		} else if (countryCode.equals("JP")){
			//TODO
			_dcf = DayCountFraction.ACTUAL_360;
			_settlementDay = 2;
			_businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
			_couponFrequency = Frequency.valueOf("S");
		} else if (countryCode.equals("CH") || countryCode.equals("CN")){
			//TODO
			_dcf = DayCountFraction.ACTUAL_360;
			_settlementDay = 2;
			_businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
			_couponFrequency = Frequency.valueOf("S");
		} else {
			_dcf = DayCountFraction.ACTUAL_360;
			_settlementDay = 2;
			_businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
			_couponFrequency = Frequency.valueOf("S");
		}
		
		_swaptionSurface = swaptionSurface;
		_spotCurve = spotCurve;
		_hwParams = HWParams;
		
//		_tenorMax = _swaptionSurface.Length();
//		_mrtyMax = _swaptionSurface.getVolatiltyCurve(0).length();
		
		_tenorMax = _swaptionSurface.getSurface().getXs().length;
		_mrtyMax = _swaptionSurface.getSurface().getYs().length;
		
		_hwVols = new double[_tenorMax][_mrtyMax][3];
	}
	
	public HWVolatilitySurface calculate(){

		calcHW1FVolatility();
		calcHW2FVolatility();
		
		HWVolatilityCurve[] hwVolCurves = new HWVolatilityCurve[_tenorMax];
		for (int tenorIndex = 0; tenorIndex < _tenorMax; tenorIndex++){
			HWVolatility[] hwVols = new HWVolatility[_mrtyMax];
			Vertex tenorVertex = _swaptionSurface.getSwaptionTenorVertex(tenorIndex);
			for (int mrtyIndex = 0; mrtyIndex < _mrtyMax; mrtyIndex++){
				Vertex mrtyVertex = _swaptionSurface.getSwaptionMaturityVertex(tenorIndex, mrtyIndex);
				hwVols[mrtyIndex] = new HWVolatility(mrtyVertex, 
						_hwVols[tenorIndex][mrtyIndex][0],
						_hwVols[tenorIndex][mrtyIndex][1],
						_hwVols[tenorIndex][mrtyIndex][2]);
			}
			
			hwVolCurves[tenorIndex] = new HWVolatilityCurve(tenorVertex, hwVols, _asOfDate, _dcf);
		}
		_hwSurface = new HWVolatilitySurface(hwVolCurves, _asOfDate, _dcf);
//		System.out.println(_hwVols);
//		System.out.println("****************************");
//		System.out.println(_hwSurface);
		
		return _hwSurface;
	}

	private void calcHW1FVolatility(){
		Date adjustedAsOfDate = _calendar.adjustDate(_asOfDate, _businessDayConvention);
				
		for (int tenorIndex = 0; tenorIndex < _tenorMax; tenorIndex++){
			Vertex tenorVertex = _swaptionSurface.getSwaptionTenorVertex(tenorIndex);
						
			for (int mrtyIndex = 0; mrtyIndex < _mrtyMax; mrtyIndex++){				
				Vertex mrtyVertex = _swaptionSurface.getSwaptionMaturityVertex(tenorIndex, mrtyIndex);
				double swaptionVol = _swaptionSurface.getVolByIndex(tenorIndex, mrtyIndex);
				Date startDate = _calendar.adjustDate(
						mrtyVertex.getDate(adjustedAsOfDate), _businessDayConvention);				
//				System.out.println(startDate);
				double swaptionMrty = _dcf.getYearFraction(adjustedAsOfDate, startDate);
				Date swapEndDate = tenorVertex.getDate(startDate);
				double swapTenor = Math.round(_dcf.getYearFraction(startDate, swapEndDate));
//				System.out.println(swapTenor);
				
				double atmStrike = _spotCurve.getForwardSwapRate(startDate, 
						swapTenor, _couponFrequency);
//				System.out.println(atmStrike);
				
				ESwaption eSwaption = new ESwaption(Money.valueOf("KRW", 1), 
						startDate, swapTenor, 
						atmStrike, 2, _dcf, _couponFrequency);
				
				double bkPrice = eSwaption.getPrice(adjustedAsOfDate, _spotCurve, 
						swaptionVol, _spotCurve).getAmount();
//				System.out.println(bkPrice);
				
				HW1FProblem f = new HW1FProblem(_hwParams.getMeanReversion1F(),
						_hwParams.getHullWhiteVolatility1F(),
						_spotCurve, _couponFrequency);
				
				f.addPoint(swaptionMrty, swapTenor, atmStrike,
						bkPrice);
				
				double[] weights = new double[] { 1 };
				ModelFunction function = new ModelFunction(f);

				// optimizer
				double costRelativeTolerance = 1.0e-8;
				double parRelativeTolerance = 1.0e-8;
				double orthoTolerance = 1.0e-8;
				final LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(
						costRelativeTolerance, parRelativeTolerance, orthoTolerance);

				PointVectorValuePair optimum = optimizer.optimize(
						new MaxEval(1000), function, f.getModelFunctionJacobian(),
						new Target(f.getTarget()), new Weight(weights),
						new InitialGuess(new double[] {_hwParams.getHullWhiteVolatility1F()}));

				double[] value = optimum.getValue();
				double[] tmp = optimum.getPoint();
				
				_hwVols[tenorIndex][mrtyIndex][0] = tmp[0];
				
//				System.out.println("TENOR: " + tenorIndex + ", MRTY: " + mrtyIndex +
//						", BKPRICE: " + bkPrice +
//						", HWPRICE: " + value[0] +
//						", DIFF: " + (bkPrice - value[0]) +
//						", VOL: " + _hwVols[tenorIndex][mrtyIndex][0]);								
			}			
		}		
	}
	
	private void calcHW2FVolatility(){
		Date adjustedAsOfDate = _calendar.adjustDate(_asOfDate, _businessDayConvention);
		double _a1F;
		double _sigma1F;
		double _a;
		double _b;
		double _sigma1;
		double _sigma2;
		double _rho;
		
		for (int tenorIndex = 0; tenorIndex < _tenorMax; tenorIndex++){
			Vertex tenorVertex = _swaptionSurface.getSwaptionTenorVertex(tenorIndex);
						
			for (int mrtyIndex = 0; mrtyIndex < _mrtyMax; mrtyIndex++){
				Vertex mrtyVertex = _swaptionSurface.getSwaptionMaturityVertex(tenorIndex, mrtyIndex);
				double swaptionVol = _swaptionSurface.getVolByIndex(tenorIndex, mrtyIndex);
				Date startDate = _calendar.adjustDate(
						mrtyVertex.getDate(adjustedAsOfDate), _businessDayConvention);				
//				System.out.println(startDate);
				double swaptionMrty = _dcf.getYearFraction(adjustedAsOfDate, startDate);
				Date swapEndDate = tenorVertex.getDate(startDate);
				double swapTenor = Math.round(_dcf.getYearFraction(startDate, swapEndDate));
//				System.out.println(swapTenor);
				
				double atmStrike = _spotCurve.getForwardSwapRate(startDate, 
						swapTenor, _couponFrequency);
//				System.out.println(atmStrike);
				
				ESwaption eSwaption = new ESwaption(Money.valueOf("KRW", 1), 
						startDate, swapTenor, 
						atmStrike, 2, _dcf, _couponFrequency);
				
				double bkPrice = eSwaption.getPrice(adjustedAsOfDate, _spotCurve, 
						swaptionVol, _spotCurve).getAmount();
				_a = Math.abs(_hwParams.getMeanReversion1_2F());
				_b = _hwParams.getMeanReversion2_2F();
				_sigma1 = Math.max(Math.min(Math.abs(Math.max(_hwParams.getHullWhiteVolatility1_2F(),_hwParams.getHullWhiteVolatility1_2F())), 0.1), 0.02);
				_sigma2 =  Math.max(Math.min(Math.abs(_hwParams.getHullWhiteVolatility2_2F()), 0.1), 0.01);
				
				_rho = _hwParams.getCorrelation();
				
				_a1F = Math.abs(_hwParams.getMeanReversion1F());
				_sigma1F = _hwVols[tenorIndex][mrtyIndex][0];
				// optimization
				HW2FProblem f = new HW2FProblem(
						_a,	_b, _sigma1,_rho,
						_spotCurve, _couponFrequency, 
						_a1F, _sigma1F);
				
//				HW2FProblem f = new HW2FProblem(
//						_a,	_b, _sigma1,_sigma2, _rho,
//						_spotCurve, _couponFrequency 
//						);
												
				f.addPoint(swaptionMrty, swapTenor, atmStrike,
						bkPrice);

				double[] weights = new double[] { 1 };
				ModelFunction function = new ModelFunction(f);

				// optimizer
				double costRelativeTolerance = 1.0e-8;
				double parRelativeTolerance = 1.0e-8;
				double orthoTolerance = 1.0e-8;
				final LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(
						costRelativeTolerance, parRelativeTolerance, orthoTolerance);
				
				PointVectorValuePair optimum = optimizer.optimize(
						new MaxEval(50000), function, f.getModelFunctionJacobian(),
						new Target(f.getTarget()), new Weight(weights),
						new InitialGuess(new double[] {_sigma1}));

				double[] value = optimum.getValue();
				double[] tmp = optimum.getPoint();
				
				_hwVols[tenorIndex][mrtyIndex][1] = tmp[0];
				_hwVols[tenorIndex][mrtyIndex][2] = f.sigma2Calculator(tmp[0], swaptionMrty);

				
			}			
			
		}
	}
	
	private class ESwaption {
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
		
		public Money getPrice(Date date, InterestRateCurve swapCurve, double swaptionVol,
				InterestRateCurve discountCurve){
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

	//For 1F Calibration
	private static class HW1FProblem implements MultivariateVectorFunction {

		private List<Double> _maturity;
		private List<Double> _tenor;
		private List<Double> _strike;
		private List<Double> _swaptionValue;

		private Frequency _couponFrequency;
		private InterestRateCurve _spotCurve;

//		private double _range = 6.0;
//		private int _intervals = 16;
		private int _optionType = -1;
		private double _a;
		
		public HW1FProblem(double a, double sigma,
				InterestRateCurve spotCurve,
				Frequency couponFrequency) {
			_maturity = new ArrayList<Double>();
			_tenor = new ArrayList<Double>();
			_strike = new ArrayList<Double>();
			_swaptionValue = new ArrayList<Double>();
			_a = a;
			//_b = b;
			//_rho = rho;
			this._couponFrequency = couponFrequency;
			this._spotCurve = spotCurve;
		}

		public void addPoint(double maturity, double tenor, double strike,
				double swaptionValue) {
			this._maturity.add(maturity);
			this._tenor.add(tenor);
			this._strike.add(strike);
			this._swaptionValue.add(swaptionValue);
		}

		public double[] value(double[] arg0) throws IllegalArgumentException {
			double[] values = new double[_maturity.size()];
			for (int i = 0; i < values.length; ++i) {
				values[i] = HWSwaptionPrice(_optionType, _maturity.get(i),
						_tenor.get(i), _strike.get(i), _a, arg0[0]);
			}
			return values;
		}

		public ModelFunctionJacobian getModelFunctionJacobian() {
			return new ModelFunctionJacobian(new MultivariateMatrixFunction() {
				public double[][] value(double[] variables) {
					double[][] jacobian = new double[_maturity.size()][variables.length];
					double temp = 1.0e-8;
					double eps = Math.sqrt(temp);
					for (int i = 0; i < jacobian.length; ++i) {
						for (int j = 0; j < variables.length; j++) {
							double upValue = 0;
							double downValue = 0;
							temp = variables[j];
							double interval = Math.abs(variables[j]) * eps;
							double upVariable = variables[j] * (1 + interval);
							double downVariable = variables[j] * (1 - interval);

							// up
							upValue = HWSwaptionPrice(_optionType,
									_maturity.get(i), _tenor.get(i),
									_strike.get(i), _a, upVariable);
							// down
							downValue = HWSwaptionPrice(_optionType,
									_maturity.get(i), _tenor.get(i),
									_strike.get(i), _a, downVariable);

							jacobian[i][j] = (upValue - downValue)
									/ (2 * variables[j] * interval);
						}
					}
					return jacobian;
				}
			});
		}

		public double[] getTarget() {
			double[] target = new double[_swaptionValue.size()];
			for (int i = 0; i < _swaptionValue.size(); i++) {
				target[i] = _swaptionValue.get(i).doubleValue();
			}
			return target;
		}

		private double HWSwaptionPrice(int optionType, 
				double maturity, double tenor, double strike, 
				double a, double sigma) {

			double value = 0;
			int swapLength = (int)(tenor / _couponFrequency.getInterval());
			double[] tenorDt = new double[swapLength];
			double[] strikes = new double[swapLength];
			double[] dboValue = new double[swapLength];
			double[] coupon = new double[swapLength];
			
			double rStar = getRStar(maturity, maturity, tenor, strike, 
					1, 1.0e-06, -1.0, 1.0,
					_couponFrequency, a, sigma, _spotCurve);
			HW1FSwaptionFunction swaption = new HW1FSwaptionFunction(
					a, sigma, _spotCurve);
			
			for (int i = 0; i < swapLength; i++){
				tenorDt[i] =  maturity + _couponFrequency.getInterval() * (i + 1);
				strikes[i] = swaption.discountBond(maturity, tenorDt[i], rStar) /
						swaption.discountBond(maturity, maturity, rStar);						
				coupon[i] = strike * _couponFrequency.getInterval();
				if (i == swapLength - 1)
					coupon[i] += 1; 
				dboValue[i] =swaption.discountBondOption(optionType, strikes[i], maturity,
						tenorDt[i]); 
						
				value += dboValue[i] * coupon[i];
			}
			
			return value;
		}
	}
	
	private static class HW1FSwaptionFunction {
		
		private double _meanReversion;
		private double _sigma;
		private InterestRateCurve _termStructure;
		
		public HW1FSwaptionFunction(double meanReversion, double sigma, 
				InterestRateCurve termStructure) {
			_meanReversion = meanReversion;
			_sigma = sigma;
			_termStructure = termStructure;
		}
		
		public double discountBond(double now, double maturity, double rate){
			
			return getA(now, maturity) * Math.exp(- getB(now, maturity, _meanReversion) * rate);
		}
		public double discountBondOption(int type, double strike, double maturity, 
				double bondMaturity){

	        double v = _sigma * getB(maturity, bondMaturity, _meanReversion) *
	                Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * _meanReversion *maturity)) / _meanReversion);
//			System.out.println(v);		
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
		protected double getA(double from, double to){
			double discount1 = _termStructure.getDiscountFactor(from);
			double discount2 = _termStructure.getDiscountFactor(to);
			
			double dt = 1.0/365.0;
//			double forward = _termStructure.getForwardRate(from, from);
			double forward = ((_termStructure.getSpotRate(from + dt) - 
					_termStructure.getSpotRate(from)) * (from / dt)) +
					_termStructure.getSpotRate(from + dt);
			
			double temp = _sigma * getB(from, to, _meanReversion);
			
			double value = getB(from, to, _meanReversion) * forward - 
					0.25 * temp * temp * getB(0, 2.0 * from, _meanReversion);
			
			return Math.exp(value) * discount2 / discount1;
			
		}
		protected double getB(double from, double to, double x){
			
			double dt = to - from;
			
			double exponentialB = (1 - Math.exp(- x * dt)) / x;
			
			return exponentialB; 
		}
	}

	private static double getRStar(double valueTime, double maturity, double tenor,
			double strike, double targetValue, 
			double accuracy, double xMin, double xMax,
			Frequency couponFrequency,
			double meanReversion, double sigma, InterestRateCurve termStructure){
		
		int maxIteration = 1000;
		double absoluteAccuracy = 1.0E-10;
		Solvable f = new FindingRStarFunction(valueTime, maturity, tenor, targetValue, strike,
				couponFrequency, meanReversion, sigma, termStructure);
		BisectionSolver bs = new BisectionSolver(absoluteAccuracy);
		
		return bs.solve(maxIteration, f, xMin, xMax);		
	}
	
	private static class FindingRStarFunction extends Solvable {
		
		private double _targetValue;
		private double _valueTime;
		private double _maturity;
		private double _tenor;
		private double _strike;
		private Frequency _couponFrequency;
		private double _meanReversion;
		private double _sigma;
		private InterestRateCurve _termStructure;
		
		private FindingRStarFunction(double valueTime, double maturity, double tenor, 
				double targetValue, double strike, Frequency couponFrequency,
				double meanReversion, double sigma, InterestRateCurve termStructure) {			
			_targetValue = targetValue;
			_valueTime = valueTime;
			_maturity = maturity;
			_tenor = tenor;
			_strike = strike;
			_couponFrequency = couponFrequency;
			_meanReversion = meanReversion;
			_sigma = sigma;
			_termStructure = termStructure;
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
				HW1FSwaptionFunction swaption = new HW1FSwaptionFunction(
						_meanReversion, _sigma, _termStructure);
				
				double dbValue = coupon[i] * swaption.discountBond(_maturity, tenorDt[i], rate) /
						swaption.discountBond(_maturity, _valueTime, rate);
				value += dbValue;
			}			
			
			return  value - _targetValue;
		}

	}		

	private static class GeneralizedBlackScholesMertonFormula{
		
		//call : 1, put :-1
		private int _optionType;
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
		
		public GeneralizedBlackScholesMertonFormula(int optionType,
				double underlyingPrice, double strike, 
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
				} else if (_optionType == -1) {
					return Math.max(X - S, 0);
				}
				//return 0.0;
			}
			if (_optionType == 1) {
				return S * _dividendFactor * nd1 - X * _discountFactor * nd2; 
			} else if (_optionType == -1) {
				return X * _discountFactor * n_d2 - S * _dividendFactor * n_d1;
			}
			return 0;
		}
	}

	//For 2F Calibration
	private static class HW2FProblem implements MultivariateVectorFunction {

		private List<Double> maturity;
		private List<Double> tenor;
		private List<Double> strike;
		private List<Double> swaptionValue;

		private Frequency _couponFrequency;
		private InterestRateCurve _spotCurve;

		private double _range = 6.0;
		private int _intervals = 16;
		private int _optionType = -1;

		private double _a;
		private double _b;
		private double _rho;
		private double _F1Vol;
		private double _F1a;
		
		public HW2FProblem(double a, double b, double sigma2, double rho, 
				InterestRateCurve spotCurve, Frequency couponFrequency, 
				double F1a, double F1Vol) {
			maturity = new ArrayList<Double>();
			tenor = new ArrayList<Double>();
			strike = new ArrayList<Double>();
			swaptionValue = new ArrayList<Double>();
			_a = a;
			_b = b;
			_rho = rho;
			this._couponFrequency = couponFrequency;
			this._spotCurve = spotCurve;
			this._F1Vol = F1Vol;
			this._F1a = F1a;
		}

		public void addPoint(double maturity, double tenor, double strike,
				double swaptionValue) {
			this.maturity.add(maturity);
			this.tenor.add(tenor);
			this.strike.add(strike);
			this.swaptionValue.add(swaptionValue);
		}

		public double[] value(double[] arg0) throws IllegalArgumentException {
			double[] values = new double[maturity.size()];
			for (int i = 0; i < values.length; ++i) {
				double sigma2 = sigma2Calculator(arg0[0], maturity.get(i));
				double tmpSwap = HWSwaptionPrice(_optionType, maturity.get(i),
						tenor.get(i), strike.get(i), _range, _intervals, _a,
						_b, arg0[0], sigma2, _rho);
				
//				double sigma2 = sigma2Calculator(arg0[0], maturity.get(i));
//				double tmpSwap = HWSwaptionPrice(_optionType, maturity.get(i),
//						tenor.get(i), strike.get(i), _range, _intervals, _a,
//						_b, sigma2, arg0[0], _rho);
				
				values[i] = tmpSwap; 
			}
			return values;
		}

		public ModelFunctionJacobian getModelFunctionJacobian() {
			return new ModelFunctionJacobian(new MultivariateMatrixFunction() {
				public double[][] value(double[] variables) {
					double[][] jacobian = new double[maturity.size()][variables.length];
					double temp = 1.0e-10;
					double eps = Math.sqrt(temp);
					for (int i = 0; i < jacobian.length; ++i) {
						for (int j = 0; j < variables.length; j++) {
							double upValue = 0;
							double downValue = 0;
							temp = variables[j];
							double interval = Math.abs(variables[j]) * eps;
							double upVariable = variables[j] * (1 + interval);
							double downVariable = variables[j] * (1 - interval);
							if (j == 0) { // for sigma1
								double tmpvol2 = sigma2Calculator(upVariable, maturity.get(i));
								
								upValue = HWSwaptionPrice(_optionType,	maturity.get(i), tenor.get(i),
										strike.get(i), _range, _intervals, _a, _b, upVariable, tmpvol2, _rho);
								
								tmpvol2 = sigma2Calculator(downVariable, maturity.get(i));
								downValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
										strike.get(i), _range, _intervals, _a, _b, downVariable, tmpvol2, _rho);
								
//								double tmpvol2 = sigma2Calculator(upVariable, maturity.get(i));
//								
//								upValue = HWSwaptionPrice(_optionType,	maturity.get(i), tenor.get(i),
//										strike.get(i), _range, _intervals, _a, _b, tmpvol2, upVariable, _rho);
//								
//								tmpvol2 = sigma2Calculator(downVariable, maturity.get(i));
//								downValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
//										strike.get(i), _range, _intervals, _a, _b, tmpvol2, downVariable, _rho);
								
							} else if (j == 1) { 

							}

							jacobian[i][j] = (upValue - downValue)
									/ (2 * variables[j] * interval);
						}
					}
					return jacobian;
				}
			});
		}

		public double[] getTarget() {
			double[] target = new double[swaptionValue.size()];
			for (int i = 0; i < swaptionValue.size(); i++) {
				target[i] = swaptionValue.get(i).doubleValue();
			}
			return target;
		}

		private double HWSwaptionPrice(int optionType, double maturity,
				double tenor, double strikeRate, double range, int intervals,
				double a, double b, double sigma1, double sigma2, double rho) {

			int swapLength = (int) (tenor / _couponFrequency.getInterval());
			double[] payTimes = new double[swapLength];
			for (int i = 0; i < swapLength; ++i) {
				payTimes[i] = maturity + _couponFrequency.getInterval()
						* (i + 1);
			}

			SwaptionPricingFunction swaption = new SwaptionPricingFunction(
					optionType, 0, maturity, payTimes, strikeRate, a, sigma1,
					b, sigma2, rho, _spotCurve);

			// integration
			double upper = swaption.getMuX() + range * swaption.getSigmaX();
			double lower = swaption.getMuX() - range * swaption.getSigmaX();

			double dx = (upper - lower) / intervals;
			double sum = 0.5 * (swaption.operator(lower) + swaption
					.operator(upper));
			double end = upper - 0.5 * dx;
			for (double x = lower + dx; x < end; x += dx) {
				sum += swaption.operator(x);
			}
			double temppp=swaption.operator(0);
			double temppp2=swaption.operator(-0.024);
			double discount = _spotCurve.getDiscountFactor(maturity);
			return optionType * discount * sum * dx;
		}
		
		private double sigma2Calculator(double sigma1, double maturity){
			double A = (1 - Math.exp(-2 * _b * maturity)) / (2 * _b);
			double B = 2 * _rho * sigma1 * (1 - Math.exp(-(_a + _b) * maturity)) / (_a + _b);
			double C = sigma1 * sigma1 * (1 - Math.exp(-2 * _a * maturity)) / (2 * _a) - 
					_F1Vol * _F1Vol * (1 - Math.exp(-2 * _F1a * maturity)) / (2 * _F1a);
			double det = B * B - 4 * A * C;
			if (det < 0 ){
				return (- B)/(2 * A);
			} else {
				return Math.max((-B + Math.pow(det, 0.5)) / (2 * A),(-B - Math.pow(det, 0.5)) / (2 * A));
			}
			
//			double A = (1 - Math.exp(-2 * _a * maturity)) / (2 * _a);
//			double B = 2 * _rho * sigma1 * (1 - Math.exp(-(_a + _b) * maturity)) / (_a + _b);
//			double C = sigma1 * sigma1 * (1 - Math.exp(-2 * _b * maturity)) / (2 * _b) - 
//					_F1Vol * _F1Vol * (1 - Math.exp(-2 * _F1a * maturity)) / (2 * _F1a);
//			double det = B * B - 4 * A * C;
//			if (det < 0 ){
//				return (- B)/(2 * A);
//			} else {
//				return (-B + Math.pow(det, 0.5)) / (2 * A);
//			}
			
		}
	}
	
	private static class SwaptionPricingFunction {

		private int _w;
		private double _sigmaX;
		private double _sigmaY;
		private double _rhoXY;
		private double _muX;
		private double _muY;
		private double[] _A;
		private double[] _Ba;
		private double[] _Bb;
		private double[] _payTimes;
		private double _maturity;
		private double _fixedRate;

		// raw Data
		private InterestRateCurve _spotCurve;
		private double _a;
		private double _b;
		private double _sigma1;
		private double _sigma2;
		private double _rho;

		private int _size;

		// type -> 1: call, -1 : put
		public SwaptionPricingFunction(int type, double currentTime,
				double maturity, double[] payTimes, double strikeRate,
				double a, double sigma, double b, double eta, double rho,
				InterestRateCurve spotCurve) {
			_w = type;
			_payTimes = payTimes;
			_size = payTimes.length;
			_maturity = maturity;
			_fixedRate = strikeRate;

			// raw data
			_spotCurve = spotCurve;
			_a = a;
			_b = b;
			_sigma1 = sigma;
			_sigma2 = eta;
			_rho = rho;

			_sigmaX = sigma
					* Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * a * maturity)) / a);
			_sigmaY = eta
					* Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * b * maturity)) / b);
			_rhoXY = rho * eta * sigma * (1.0 - Math.exp(-(a + b) * maturity))
					/ ((a + b) * _sigmaX * _sigmaY);

			double temp = sigma * sigma / (a * a);
			_muX = -((temp + rho * sigma * eta / (a * b))
					* (1.0 - Math.exp(-a * maturity)) - 0.5 * temp
					* (1.0 - Math.exp(-2.0 * a * maturity)) - rho * sigma * eta
					/ (b * (a + b)) * (1.0 - Math.exp(-(b + a) * maturity)));

			temp = eta * eta / (b * b);
			_muY = -((temp + rho * sigma * eta / (a * b))
					* (1.0 - Math.exp(-b * maturity)) - 0.5 * temp
					* (1.0 - Math.exp(-2.0 * b * maturity)) - rho * sigma * eta
					/ (a * (a + b)) * (1.0 - Math.exp(-(b + a) * maturity)));

			_A = new double[_size];
			_Ba = new double[_size];
			_Bb = new double[_size];

			for (int i = 0; i < _size; i++) {
				_A[i] = getA(maturity, payTimes[i]);
				_Ba[i] = getB(maturity, payTimes[i], a);
				_Bb[i] = getB(maturity, payTimes[i], b);
			}
		}

		public double operator(double x) {
			double temp = (x - _muX) / _sigmaX;
			double txy = Math.sqrt(1.0 - _rhoXY * _rhoXY);
			double[] lambda = new double[_size];

			for (int i = 0; i < _size; i++) {
				double tau = (i == 0 ? _payTimes[0] - _maturity : _payTimes[i]
						- _payTimes[i - 1]);
				double c = (i == _size - 1 ? (1.0 + _fixedRate * tau)
						: _fixedRate * tau);
				lambda[i] = c * _A[i] * Math.exp(-_Ba[i] * x);
			}

			int maxIteration = 10000;
			double absoluteAccuracy = 1.0E-10;
			Solvable f = new RStarSolvingFunction(lambda, _Bb);
			BisectionSolver bs = new BisectionSolver(absoluteAccuracy);
			
			double yb = bs.solve(maxIteration, f, -1.0, 1.0);
			double h1 = (yb - _muY) / (_sigmaY * txy) - _rhoXY * (x - _muX)
					/ (_sigmaX * txy);
			double value = NormalDistribution
					.getCumulativeProbability(-_w * h1);

			for (int i = 0; i < _size; i++) {
				double h2 = h1 + _Bb[i] * _sigmaY
						* Math.sqrt(1.0 - _rhoXY * _rhoXY);
				double kappa = -_Bb[i]
						* (_muY - 0.5 * txy * txy * _sigmaY * _sigmaY * _Bb[i] + _rhoXY
								* _sigmaY * (x - _muX) / _sigmaX);
				value -= lambda[i] * Math.exp(kappa)
						* NormalDistribution.getCumulativeProbability(-_w * h2);
			}

			return Math.exp(-0.5 * temp * temp) * value
					/ (_sigmaX * Math.sqrt(2.0 * M_PI));
		}

		public double getMuX() {
			return _muX;
		}

		public double getMuY() {
			return _muY;
		}

		public double getSigmaX() {
			return _sigmaX;
		}

		public double getA(double from, double to) {
			double discount1 = _spotCurve.getDiscountFactor(from);
			double discount2 = _spotCurve.getDiscountFactor(to);

			return discount2
					/ discount1
					* Math.exp(0.5 * (getV(from, to) - getV(0, to) + getV(0,
							from)));
		}

		public double getB(double from, double to, double x) {

			double dt = to - from;
			double exponentialB = (1 - Math.exp(-x * dt)) / x;
			return exponentialB;
		}

		private double getV(double from, double to) {
			double t = to - from;
			double expat = Math.exp(-_a * t);
			double expbt = Math.exp(-_b * t);
			double cx = _sigma1 / _a;
			double cy = _sigma2 / _b;
			double valuex = cx * cx
					* (t + (2.0 * expat - 0.5 * expat * expat - 1.5) / _a);
			double valuey = cy * cy
					* (t + (2.0 * expbt - 0.5 * expbt * expbt - 1.5) / _b);
			double value = 2.0
					* _rho
					* cx
					* cy
					* (t + (expat - 1.0) / _a + (expbt - 1.0) / _b - (expat
							* expbt - 1.0)
							/ (_a + _b));

			return valuex + valuey + value;
		}
	}
	
	private static class RStarSolvingFunction extends Solvable {

		private double[] _lambda;
		private double[] _Bb;

		public RStarSolvingFunction(double[] lambda, double[] Bb) {
			this._lambda = lambda;
			this._Bb = Bb;
		}

		public double getY(double rate) {
			double value = 1.0;
			for (int i = 0; i < _lambda.length; i++) {
				value -= _lambda[i] * Math.exp(-_Bb[i] * rate);
			}
			return value;
		}
	}
	
}
