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
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.common.math.distribution.NormalDistribution;
import com.quantosauros.common.math.solver.Solvable;
import com.quantosauros.common.volatility.VolatilitySurface;

/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2014
* - Creation Date : 2014-06-20
* - Creator : Jihoon Lee
------------------------------------------------------------------------------*/

public class HullWhite2FCalibration implements Serializable {
	
	private static final double M_PI = 3.14159265358979323846;
	private PointVectorValuePair _optimum;
	
	/**
	 * Hull White 2-factor 모델의 파라미터를 Calibration 하는 클래스
	 * 캘리브레이션 대상이 되는 파라미터는 a, b, sigma1, sigma2, rho이다. 
	 *  
	 * @param asOfDate					측정일
	 * @param countryCode				국가코드
	 * @param surface					Swaption 변동성 곡면
	 * @param spotCurve					이자율 커브
	 */
	public HullWhite2FCalibration(Date asOfDate,
			String countryCode,
			VolatilitySurface surface,
			InterestRateCurve spotCurve){
		
		//초기값
		double a = 0.3;
		double b = 0.03;
		double sigma1 = 0.02;
		double sigma2 = 0.01;
		double rho = -0.75;
		
		
		
//		Tenor Vertex
//		int tenorStartIndex = 0;
//		int tenorEndIndex = 0;
		int tenorLen = surface.Length();
		ArrayList<Vertex> selectTenorVertex = new ArrayList<Vertex>();
		Vertex[] tenorVertex = new Vertex[tenorLen];
		for (int i = 0; i < tenorLen; i++){
			tenorVertex[i] = surface.getVolatiltyCurve(i).getSwapMaturity();
//			if (tenorVertex[i].getVertex() <= 3 || i % 2 == 1){
			if (tenorVertex[i].getVertex() < 5 || i % 2 == 1){
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
		
		//대상 스왑션 고정
		//[Maturity] [Tenor]
//		Vertex[][] swaptionVertex = new Vertex[][] {
////				{Vertex.valueOf("Y3"), Vertex.valueOf("Y2")},
////				{Vertex.valueOf("Y3"), Vertex.valueOf("Y10")},
////				{Vertex.valueOf("Y5"), Vertex.valueOf("Y1")},
////				{Vertex.valueOf("Y5"), Vertex.valueOf("Y5")},
////				{Vertex.valueOf("Y5"), Vertex.valueOf("Y10")},
////				{Vertex.valueOf("Y7"), Vertex.valueOf("Y1")},
////				{Vertex.valueOf("Y7"), Vertex.valueOf("Y7")},
////				{Vertex.valueOf("Y7"), Vertex.valueOf("Y15")},
////				{Vertex.valueOf("Y8"), Vertex.valueOf("Y2")},
////				{Vertex.valueOf("Y8"), Vertex.valueOf("Y12")},
////				{Vertex.valueOf("Y15"), Vertex.valueOf("Y2")},
////				{Vertex.valueOf("Y15"), Vertex.valueOf("Y10")},
////				{Vertex.valueOf("Y15"), Vertex.valueOf("Y15")},		
//				{Vertex.valueOf("Y3"), Vertex.valueOf("Y2")},
//				{Vertex.valueOf("Y3"), Vertex.valueOf("Y10")},
//				{Vertex.valueOf("Y5"), Vertex.valueOf("Y1")},
//				{Vertex.valueOf("Y5"), Vertex.valueOf("Y5")},
//				{Vertex.valueOf("Y5"), Vertex.valueOf("Y10")},
//				{Vertex.valueOf("Y7"), Vertex.valueOf("Y1")},
//				{Vertex.valueOf("Y7"), Vertex.valueOf("Y7")},
//				{Vertex.valueOf("Y7"), Vertex.valueOf("Y15")},
//				{Vertex.valueOf("Y8"), Vertex.valueOf("Y12")},
//		};
		
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
			couponFrequency = Frequency.valueOf("S");
			settlementDay = 1;
			businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
		} else if (countryCode.equals("US")){
			dcf = DayCountFraction.ACTUAL_360;
			couponFrequency = Frequency.valueOf("Q");
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
			
			//Swaption의 ATM Strike 도출
			atmStrike[i] = spotCurve.getForwardSwapRate(adjustedStartDate,
					swaptionVertex[i][1].getYears(), couponFrequency);
			ESwaption eswaption = new ESwaption(Money.valueOf("KRW", 1),
					adjustedStartDate, swaptionVertex[i][1].getYears(), 
					atmStrike[i], 2, dcf, couponFrequency);
			swaptionValues[i] = eswaption.getPrice(asOfDate, spotCurve,
					swaptionVols[i], spotCurve).getAmount();
			//System.out.println("BK: " + swaptionValues[i]);
		}	
		final double[] weights = new double[swaptionN];
		for (int i = 0; i < swaptionN; i++){
			weights[i] = 1.0 ;/// swaptionValues[i];
		}
		 
		//function
		Problem f = new Problem(a, b, sigma1, sigma2, rho, spotCurve, couponFrequency);
		
		for (int i = 0; i < swaptionN; i++){
			f.addPoint(
					swaptionVertex[i][0].getYears(), 
					swaptionVertex[i][1].getYears(),
					atmStrike[i], swaptionValues[i]);
		}
		ModelFunction function = new ModelFunction(f);
		
		//optimizer
		double costRelativeTolerance = 1.0e-8;
		double parRelativeTolerance = 1.0e-8;
		double orthoTolerance = 1.0e-8;
		final LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(
				costRelativeTolerance, parRelativeTolerance, orthoTolerance);

		_optimum = optimizer.optimize(new MaxEval(1000),
				function, f.getModelFunctionJacobian(), 
				new Target(f.getTarget()),
				new Weight(weights),
				new InitialGuess(new double[] {a, b, sigma1, sigma2, rho}));//, rho})); //}));
		
		/*
		double[] solution = optimum.getPoint();
		double[] value = optimum.getValue();
		for (int i = 0; i < solution.length; i++){
			System.out.println("solution[" + i +"] : " + solution[i]);			
		}
		double sum = 0;
		for (int i = 0; i < value.length; i++){
			System.out.println("value[" + i +"] : " + value[i] + "; " + swaptionValues[i]);
			sum =+ (value[i] - swaptionValues[i]) * (value[i] - swaptionValues[i]);
		}
		System.out.println(sum);
		System.out.println(optimizer.getIterations());
		*/		
	}
	public HullWhiteParameters calibration(){
		double[] optimizationValue = _optimum.getPoint();
		
		HullWhiteParameters params = new HullWhiteParameters(
				Math.abs(optimizationValue[0]), Math.abs(optimizationValue[1]), Math.abs(optimizationValue[2]),
				Math.abs(optimizationValue[3]), optimizationValue[4]);
		
		return params;
	}
	
	private static class Problem implements MultivariateVectorFunction {
		
		private List<Double> maturity;
	    private List<Double> tenor;
	    private List<Double> strike;
	    private List<Double> swaptionValue;
	    
		private Frequency _couponFrequency;
		private InterestRateCurve _spotCurve;
	    
        private double _range = 6.0;
        private int _intervals = 16;
        private int _optionType = -1;
		
        private double _rho;
        
		public Problem(double a, double b, double sigma1, double sigma2,
				double rho, InterestRateCurve spotCurve,
				Frequency couponFrequency) {
			maturity = new ArrayList<Double>();
			tenor = new ArrayList<Double>();
			strike = new ArrayList<Double>();
			swaptionValue = new ArrayList<Double>();
			_rho = rho;
			this._couponFrequency = couponFrequency;
			this._spotCurve = spotCurve;
		}
		public void addPoint(double maturity, double tenor, double strike, 
				double swaptionValue){
			this.maturity.add(maturity);
			this.tenor.add(tenor);
			this.strike.add(strike);
			this.swaptionValue.add(swaptionValue);
		}
		public double[] value(double[] arg0) throws IllegalArgumentException {
			double[] values = new double[maturity.size()];
	        for (int i = 0; i < values.length; ++i) {            	
	        	//this._model = new HullWhite(variables[0], variables[1], this._spotCurve);
	            values[i] = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
	            		strike.get(i), _range, _intervals, 
	            		arg0[0], arg0[1], arg0[2], arg0[3], _rho);//arg0[4]);//_rho);
	        }
	        return values;
		}
		public ModelFunctionJacobian getModelFunctionJacobian() {
            return new ModelFunctionJacobian(new MultivariateMatrixFunction() {
            	public double[][] value(double[] variables) {                    
            		double[][] jacobian = new double[maturity.size()][variables.length];
            		double temp = 1.0e-8;
            		double eps = Math.sqrt(temp);            		
                    for (int i = 0; i < jacobian.length; ++i) {
                    	for (int j = 0; j < variables.length; j++){
                    		double upValue = 0;
        	            	double downValue = 0;
        	            	temp = variables[j];
        	            	double interval = Math.abs(variables[j]) * eps;
        	        		double upVariable = variables[j] * (1 + interval);
        	            	double downVariable = variables[j] * (1 - interval);
        	        		if (j == 0){//i == 0 : for a	        			
        	            		// up
        	            		upValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
        	                    		strike.get(i), _range, _intervals, 
        	                    		upVariable, variables[1], variables[2], variables[3], _rho); 
        	            		//down            		
        	            		downValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
        	                    		strike.get(i), _range, _intervals,
        	                    		downVariable, variables[1], variables[2], variables[3], _rho);
        	            		
        	            	} else if (j == 1){ //i == 1 : for b
        	            		upValue = 0;
//        	            		upValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
//        	                    		strike.get(i), _range, _intervals,
//        	                    		variables[0], upVariable, variables[2], variables[3], _rho);
        	            		//down      
        	            		downValue = 0;
//        	            		downValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
//        	                    		strike.get(i), _range, _intervals,
//        	                    		variables[0], downVariable, variables[2], variables[3], _rho);
        	            	} else if (j == 2){ //i == 2 : for sigma1
        	            		upValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
        	                    		strike.get(i), _range, _intervals,
        	                    		variables[0], variables[1], upVariable, variables[3], _rho);
        	            		//down            		
        	            		downValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
        	                    		strike.get(i), _range, _intervals,
        	                    		variables[0], variables[1], downVariable, variables[3], _rho);
        	            	} else if (j == 3){ //i == 3 : for sigma2
        	            		upValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
        	                    		strike.get(i), _range, _intervals,
        	                    		variables[0], variables[1], variables[2], upVariable, _rho);
        	            		//down            		
        	            		downValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
        	                    		strike.get(i), _range, _intervals,
        	                    		variables[0], variables[1], variables[2], downVariable, _rho);
        	            	} 
//        	            		else if (j == 4){ //i == 3 : for correlation
//        	            		upValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
//        	                    		strike.get(i), _range, _intervals,
//        	                    		variables[0], variables[1], variables[2], variables[3], upVariable);
//        	            		//down            		
//        	            		downValue = HWSwaptionPrice(_optionType, maturity.get(i), tenor.get(i), 
//        	                    		strike.get(i), _range, _intervals,
//        	                    		variables[0], variables[1], variables[2], variables[3], downVariable);
//        	            	}	
        	        		jacobian[i][j] = (upValue - downValue) / (2 * variables[j] * interval);			            
        	        	}
                    }
                    return jacobian;
                }
            });
        }
		public double[] getTarget(){
			double[] target = new double[swaptionValue.size()];
	        for (int i = 0; i < swaptionValue.size(); i++) {
	            target[i] = swaptionValue.get(i).doubleValue();
	        }
	        return target;
		}
		
		
		private double HWSwaptionPrice(int optionType, 
				double maturity, double tenor,
				double strikeRate ,double range, int intervals,
				double a, double b, double sigma1, double sigma2, double rho){

			
	        int swapLength = (int)(tenor / _couponFrequency.getInterval());
	        double[] payTimes = new double[swapLength];
	        for (int i = 0; i < swapLength; ++i){
	        	payTimes[i] = maturity + _couponFrequency.getInterval() * (i + 1);
	        }			
	        
			SwaptionPricingFunction swaption = new SwaptionPricingFunction(
					optionType, 0, maturity, payTimes, strikeRate, 
					a, sigma1, b, sigma2, rho, _spotCurve);
			
			//integration
			double upper = swaption.getMuX() + range * swaption.getSigmaX();
			double lower = swaption.getMuX() - range * swaption.getSigmaX();
			
			double dx = (upper - lower) / intervals;
			double sum = 0.5 * (swaption.operator(lower) + swaption.operator(upper));
			double end = upper - 0.5 * dx;
			for (double x = lower + dx; x < end; x+= dx){
				sum += swaption.operator(x);
			}		
			double discount = _spotCurve.getDiscountFactor(maturity);
			return optionType * discount * sum * dx;
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
		
		//raw Data
		private InterestRateCurve _spotCurve;
		private double _a;
		private double _b;
		private double _sigma1;
		private double _sigma2;
		private double _rho;
		
		private int _size;
		
		//type -> 1: call, -1 : put
		public SwaptionPricingFunction(int type,
				double currentTime, double maturity, double[] payTimes,
				double strikeRate,
				double a, double sigma, double b, double eta, double rho,
				InterestRateCurve spotCurve) {
			_w = type;
			_payTimes = payTimes;
			_size = payTimes.length;
			_maturity = maturity;
			_fixedRate = strikeRate;
			
			//raw data 
			_spotCurve = spotCurve;
			_a = a;
			_b = b;
			_sigma1 = sigma;
			_sigma2 = eta;
			_rho = rho;
			
			_sigmaX = sigma * Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * a * maturity)) / a);
	        _sigmaY = eta * Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * b * maturity)) / b);
	        _rhoXY = rho * eta * sigma * (1.0 - Math.exp(-(a + b) * maturity))/
	            ((a + b) * _sigmaX * _sigmaY);
	        
	        double temp = sigma * sigma / (a * a);
	        _muX = -((temp + rho * sigma * eta /(a * b))*(1.0 - Math.exp(-a * maturity)) -
	                 0.5 * temp * (1.0 - Math.exp(-2.0 * a * maturity)) -
	                 rho * sigma * eta / (b * (a + b))*
	                 (1.0- Math.exp(-(b + a) * maturity)));

	        temp = eta * eta /(b * b);
	        _muY = -((temp + rho * sigma * eta /(a * b))*(1.0 - Math.exp(-b * maturity)) -
	                 0.5 * temp * (1.0 - Math.exp(-2.0 * b * maturity)) -
	                 rho * sigma * eta / (a *(a + b))*
	                 (1.0 - Math.exp(-(b + a) * maturity)));
	        
	        _A = new double[_size];
	        _Ba = new double[_size];	
	        _Bb = new double[_size];
	        	        
	        for (int i = 0; i < _size; i++) {
	        	_A[i] = getA(maturity, payTimes[i]);
	        	_Ba[i] = getB(maturity, payTimes[i], a);
	        	_Bb[i] = getB(maturity, payTimes[i], b);            
	        }
		}
		public double operator(double x){
			double temp = (x - _muX) / _sigmaX;
			double txy = Math.sqrt(1.0 - _rhoXY * _rhoXY);
	        double[] lambda = new double[_size];
	        
	        for (int i = 0; i < _size; i++) {
	            double tau = (i == 0 ? _payTimes[0] - _maturity : _payTimes[i] - _payTimes[i-1]);
	            double c = (i == _size - 1 ? (1.0 + _fixedRate * tau) : _fixedRate * tau);
	            lambda[i] = c * _A[i] * Math.exp(-_Ba[i] * x);
	        }
	        
	        int maxEval = 10000;
	        double absoluteAccuracy = 1.0E-10;
	        Solvable function = new RStarSolvingFunction(lambda, _Bb);
	        BisectionSolver bs = new BisectionSolver(absoluteAccuracy);	        
	        
	        double yb = bs.solve(maxEval, function, -1.0, 1.0);
	        double h1 = (yb - _muY)/(_sigmaY * txy) - _rhoXY * (x - _muX)/(_sigmaX * txy);
	        double value = NormalDistribution.getCumulativeProbability(-_w * h1);
	        
	        for (int i = 0; i < _size; i++) {
	            double h2 = h1 + _Bb[i] * _sigmaY * Math.sqrt(1.0 - _rhoXY * _rhoXY);
	            double kappa = - _Bb[i] * (_muY - 0.5 * txy * txy * _sigmaY * _sigmaY *
	            		_Bb[i] + _rhoXY * _sigmaY * (x - _muX) / _sigmaX);
	            value -= lambda[i] * Math.exp(kappa) * 
	            		NormalDistribution.getCumulativeProbability(-_w * h2);
	        }
	        
	        return Math.exp(-0.5 * temp * temp) * value / 
	        		(_sigmaX * Math.sqrt(2.0 * M_PI));
		}
		public double getMuX(){
			return _muX;
		}
		public double getMuY(){
			return _muY;
		}
		public double getSigmaX(){
			return _sigmaX;
		}
		public double getA(double from, double to){
			double discount1 = _spotCurve.getDiscountFactor(from);
			double discount2 = _spotCurve.getDiscountFactor(to);
			
			 return  discount2 / discount1 *
					 Math.exp(0.5 * (getV(from, to) - getV(0, to) + getV(0, from)));		 
		}
		public double getB(double from, double to, double x){
			
			double dt = to - from;		
			double exponentialB = (1 - Math.exp(-x * dt)) / x;		
			return exponentialB; 		
		}
		private double getV(double from, double to){
			double t = to - from;
			double expat = Math.exp(-_a * t);
		    double expbt = Math.exp(-_b * t);
		    double cx = _sigma1 / _a;
		    double cy = _sigma2 / _b;
	        double valuex = cx * cx * (t + (2.0 * expat - 0.5 * expat * expat - 1.5) /
	        		_a);
	        double valuey = cy * cy * (t + (2.0 * expbt - 0.5 * expbt * expbt - 1.5) /
	        		_b);
	        double value = 2.0* _rho * cx * cy * (t + (expat - 1.0) / _a
	        		+ (expbt - 1.0) / _b
	        		- (expat*expbt-1.0) / (_a + _b));
	        
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
		public double getY(double rate){
			double value = 1.0;
			for (int i = 0; i < _lambda.length; i++) {
	            value -= _lambda[i] * Math.exp(-_Bb[i] * rate);
	        }
        return value;			
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
}

