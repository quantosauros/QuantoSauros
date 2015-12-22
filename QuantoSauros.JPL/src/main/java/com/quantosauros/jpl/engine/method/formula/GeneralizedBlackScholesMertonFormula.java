package com.quantosauros.jpl.engine.method.formula;

import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.math.distribution.NormalDistribution;

public class GeneralizedBlackScholesMertonFormula {
	OptionType _optionType;
	
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
	
	/**
	 * Black-Scholes-Meton Formula에 대한 생성자이다.
	 * 
	 * @param optionType			Option의 Call/Put 구분자로 
	 * 								<code>Option.Type</code>의  <code>CALL</code>
	 * 								또는 <code>PUT</code>에 해당함
	 * @param underlyingPrice		Option의 기초자산
	 * @param strike				행사가
	 * @param maturity				측정일에서 Option 만기까지 연환산한 기간
	 * @param riskFreeRate			무위험 이자율
	 * @param costOfCarry			무위험 이자율에서 연속 배당률을 차감한 값(r-q)`
	 * @param underlyingVolatility	기초자산의 변동성
	 */
	public GeneralizedBlackScholesMertonFormula(
			OptionType optionType, double underlyingPrice, double strike, 
			double maturity, double riskFreeRate, double costOfCarry,
			double underlyingVolatility) {
		_optionType = optionType;
		S = underlyingPrice;
		X = strike;
		T = maturity;
		r = riskFreeRate;
		b = costOfCarry;
		v = underlyingVolatility;
		
		//Assert.ensure(maturity > 0.0, ErrorCode.NON_POSITIVE_MATURITY);
		// 2009.11.13 Yang Jaechul 0인경우 Payoff를 반환하도록 수정함. 
		//ErrorCode의 내용도 0을 고려하도록 되있음
		//Assert.ensure(maturity >= 0.0, ErrorCode.NON_POSITIVE_MATURITY);
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
	
	/**
	 * Black-Scholes-Merton 방정식을 이용한 Option Type 별 이론가를 계산한다.
	 * 
	 * @return Call/Put 에 대한 European Plain Vanilla Option의 가격을 산출한다.
	 */
	public double getValue() {
		//2009.11.13 Yang Jaechul 만기가 0인 경우 페이오프를 리턴함
		if (T <= 0) {
			//Payoff로 변경하도록 김종혁 박사님이 요청함
			if (_optionType == OptionType.CALL) {
				return Math.max(S - X, 0); 
			} else if (_optionType == OptionType.PUT) {
				return Math.max(X - S, 0);
			}
			//return 0.0;
		}
		if (_optionType == OptionType.CALL) {
			return S * _dividendFactor * nd1 - X * _discountFactor * nd2; 
		} else if (_optionType == OptionType.PUT) {
			return X * _discountFactor * n_d2 - S * _dividendFactor * n_d1;
		}
		return 0;
	}
	
	/**
	 * 
	 * European Vanilla Option의 Delta를 계산한다.
	 * 
	 * @return Delta를 산출한다.
	 */
	public double getDelta() {
		if (T <= 0) {
			return 0.0;
		}
		if (_optionType == OptionType.CALL) {
			return _dividendFactor * nd1;
		} else if (_optionType == OptionType.PUT) {
			return _dividendFactor * nd1 - 1;
		}
		return 0;
	}
	
	/**
	 * European Vanilla Option의 Gamma를 계산한다.
	 * 
	 * @return Gamma를 산출한다.
	 */
	public double getGamma() {
		if (T <= 0) {
			return 0.0;
		}
		double n = Math.exp(-0.5 * d1 * d1) / Math.sqrt((2.0 * Math.PI));
		return n * _dividendFactor / (S * _stdDev);
	}
	
	/**
	 * European Vanilla Option의 Vega를 계산한다.
	 * 
	 * @return Vega를 산출한다.
	 */
	public double getVega() {
		if (T <= 0) {
			return 0.0;
		}
		double n = Math.exp(-0.5 * d1 * d1) / Math.sqrt((2.0 * Math.PI));
		return S * _dividendFactor * n * Math.sqrt(T);
	}
	
	/**
	 * European Vanilla Option의 Theta를 계산한다.
	 * 
	 * @return Theta를 산출한다.
	 */
	public double getTheta() {
		if (T <= 0) {
			return 0.0;
		}
		double n = Math.exp(-0.5 * d1 * d1) / Math.sqrt((2.0 * Math.PI));
		if (_optionType == OptionType.CALL) {
			return -S * _dividendFactor * n * v / (2 * Math.sqrt(T))
				- (b - r) * S * _dividendFactor * nd1
				- r * X * _discountFactor * nd2;
		} else if (_optionType == OptionType.PUT) {
			return -S *_dividendFactor * n * v / (2 * Math.sqrt(T))
				+ (b - r) * S * _dividendFactor * n_d1
				+ r * X * _discountFactor * n_d2;
		}
		return 0;
	}
	
	/**
	 * European Vanilla Option의 Rho를 계산한다.
	 * 
	 * @return Rho를 산출한다.
	 */
	public double getRho() {
		if (T <= 0) {
			return 0.0;
		}
		if (_optionType == OptionType.CALL) {
			return T * X * Math.exp(-b * T) * nd2;
		} else if (_optionType == OptionType.PUT) {
			return -T * X * Math.exp(-b * T) * n_d2;
		}
		return 0;
	}
	
//	/**
//	 * European Vanilla Option의 내재변동성을 계산한다.
//	 * 
//	 * @param calcMethod	Solver Method 유형(Bisetion, Newton-Raphson Method)
//	 * @param maxIteration	iteration 횟수
//	 * @param min			Solver 함수의 Maximum Condition
//	 * @param max			Solver 함수의 Minimum Condition
//	 * @param price			Option의 시장가
//	 * 
//	 * @return				Option의 내재변동성을 산출한다.			
//	 */
//	public double getImpliedVolatility(int maxIteration, 
//			double min, double max, double maxLimit,
//			double price) {
//		if (T <= 0) {
//			return 0.0;
//		}
//		
//		Solvable function = new FindingVolatilityFunction(price);
//		
//		BisectionSolver bisection = new BisectionSolver();
//		bisection.setMaximalIterationCount(maxIteration);
//		bisection.setMaxLimit(maxLimit);
//		return bisection.calculate(min, max);
//		
//	}
//	
//	private void setV(double v) {
//		this.v = v;
//		init();
//	}
//	
//	private class FindingVolatilityFunction extends DifferentiableSolvable {
//
//		private double _optionPrice;
//		
//		private FindingVolatilityFunction(double optionPrice) {
//			_optionPrice = optionPrice;
//		}
//		
//		public double getY(double v) {
//			setV(v);
//			return getValue() - _optionPrice;
//		}
//
//		public Solvable getDerivative() {
//			return new VegaFunction();
//		}			
//	}		
//	
//	private class VegaFunction extends Solvable {
//		
//		private VegaFunction() {
//		}
//		
//		public double getY(double v) {
//			setV(v);
//			return getVega();
//		}		
//	}
}
