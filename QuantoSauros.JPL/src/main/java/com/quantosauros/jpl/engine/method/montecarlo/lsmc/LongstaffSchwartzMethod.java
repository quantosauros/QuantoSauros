package com.quantosauros.jpl.engine.method.montecarlo.lsmc;

import com.quantosauros.common.math.matrix.ConjugateGradientMethod;

public class LongstaffSchwartzMethod {
	private double[] _payoffs;
	private double[] _prices;
	private double[] _prices2;
	private double[] _prices3;
	private double[][] pricePolyMatrix;
	private int _degree;
	
	private double _lowerPrice;
	private double _upperPrice;
	private int _numOfAreas;
	
	private boolean _isDivideAreaMethod;
	
	private int _maxIndex;
	private double _maxPrice;
	
	private double[] _betaHat;
	//1 asset case
	public LongstaffSchwartzMethod(double[] payoffs, double[] prices) {
		
		_payoffs = payoffs;
		_prices = prices;
		_isDivideAreaMethod = false;
	}
	//added by jihoon lee, 20140801, divide a price area
	public LongstaffSchwartzMethod(double[] payoffs, double[] prices, 
			double lowerPrice, double upperPrice) {
		
		_payoffs = payoffs;
		_prices = prices;
		_lowerPrice = lowerPrice;
		_upperPrice = upperPrice;
		_numOfAreas = 3;
		_isDivideAreaMethod = true;
	}
	//20140312 Jihoon Lee, For 2 Asset Case
	public LongstaffSchwartzMethod(double[] payoffs, double[] prices1, double[] prices2) {
		
		_payoffs = payoffs;
		_prices = prices1;
		_prices2 = prices2;	
		_isDivideAreaMethod = false;
	}
	
	//20140312 Taeheum Cho, For 3 Asset Case
	public LongstaffSchwartzMethod(double[] payoffs, double[] prices1, 
			double[] prices2, double[] prices3) {
		
		_payoffs = payoffs;
		_prices = prices1;
		_prices2 = prices2;
		_prices3 = prices3;
		_isDivideAreaMethod = false;
	}
		
		
	public double[] getContinuations(){
		
		// MODIFIED BY JAEOH, JUNE 16
		// instead of polynomial regression class here 
		// we use conjugate-gradient method to solve linear system
		/***********************************************************************
		double[] continuations = new double[_payoffs.length];
		
		PolynomialRegression polynomialRegression = new PolynomialRegression(_prices, _payoffs, 2);
		Polynomial conditionalExpectation = polynomialRegression.getBestFit();
		
		for(int i = 0; i < continuations.length; i++){
			continuations[i] = conditionalExpectation.getY(_prices[i]);
		}
		
		return continuations;
		***********************************************************************/
		double[] continuations = new double [_payoffs.length];
		//double[] betahat = null;
		// with ConjugateGradient, degree > 5 is not recommended!
		if (_isDivideAreaMethod == false){
			//non-divide method
			if (_prices2 == null){
				_degree = 2;
//				_degree = 1;
				calculatePolyRegressionMatrix();				
			} else {
				if(_prices3 == null){
				_degree = 5;
//				_degree = 4;
//				_degree = 2;
				calculatePolyRegressionMatrixFor2Asset();
				} else {
					_degree = 6;
					calculatePolyRegressionMatrixFor3Asset();
				}
			}
			if (_betaHat == null){
				_betaHat = ConjugateGradientMethod.solve(pricePolyMatrix, _payoffs);
			}
			
			for (int i = 0; i < _payoffs.length; i++) {
				continuations[i] = 0;
				for (int j = 0; j < (_degree + 1); j++) {
					continuations[i] += pricePolyMatrix[i][j] * _betaHat[j];
				}
			}
		} else {
			//divide method
			_degree = 1;
			int[] order = new int[_payoffs.length];			
			double[][] tmpPrice = new double[_numOfAreas][_payoffs.length];
			double[][] tmpPayoff = new double[_numOfAreas][_payoffs.length];
			
			double lengthOfArea = (_upperPrice - _lowerPrice) / _numOfAreas;
			int index1 = 0;
			int index2 = 0;
			int index3 = 0;
			
			for (int i = 0; i < _payoffs.length; i++){
				int quotient = (int)(_prices[i] / lengthOfArea);
				order[i] = quotient;
				
					if (quotient == 0){
						tmpPrice[quotient][index1] = _prices[i];
						tmpPayoff[quotient][index1] = _payoffs[i];
						index1++;
					} else if (quotient == 1){
						tmpPrice[quotient][index2] = _prices[i];
						tmpPayoff[quotient][index2] = _payoffs[i];
						index2++;
					} else if (quotient == 2){						
						tmpPrice[quotient][index3] = _prices[i];
						tmpPayoff[quotient][index3] = _payoffs[i];
						index3++;
					} else {
						
					}
			}		
			double[] newPrices1 = new double[index1];
			double[] newPrices2 = new double[index2];
			double[] newPrices3 = new double[index3];
			double[] newPayoff1 = new double[index1];
			double[] newPayoff2 = new double[index2];
			double[] newPayoff3 = new double[index3];
			
			System.arraycopy(tmpPrice[0], 0, newPrices1, 0, index1);
			System.arraycopy(tmpPrice[1], 0, newPrices2, 0, index2);
			System.arraycopy(tmpPrice[2], 0, newPrices3, 0, index3);
			System.arraycopy(tmpPayoff[0], 0, newPayoff1, 0, index1);
			System.arraycopy(tmpPayoff[1], 0, newPayoff2, 0, index2);
			System.arraycopy(tmpPayoff[2], 0, newPayoff3, 0, index3);
			
			double[][] polyMatrix1 = calculateDividedRegionRegressionMatrix(newPrices1);
			double[][] polyMatrix2 = calculateDividedRegionRegressionMatrix(newPrices2);
			double[][] polyMatrix3 = calculateDividedRegionRegressionMatrix(newPrices3);
			double[] betahat1 = null;
			double[] betahat2 = null;
			double[] betahat3 = null;
			
			if (index1 != 0){
				betahat1 = ConjugateGradientMethod.solve(polyMatrix1, newPayoff1);
			}
			if (index2 != 0){
				betahat2 = ConjugateGradientMethod.solve(polyMatrix2, newPayoff2);
			}
			if (index3 != 0){
				betahat3 = ConjugateGradientMethod.solve(polyMatrix3, newPayoff3);
			}
			
			double[] continuations1 = new double[index1];
			double[] continuations2 = new double[index2];
			double[] continuations3 = new double[index3];
			for (int i = 0; i < newPayoff1.length; i++) {
				continuations1[i] = 0;
				for (int j = 0; j < (_degree + 1); j++) {
					continuations1[i] += polyMatrix1[i][j] * betahat1[j];
				}
			}
			for (int i = 0; i < newPayoff2.length; i++) {
				continuations2[i] = 0;
				for (int j = 0; j < (_degree + 1); j++) {
					continuations2[i] += polyMatrix2[i][j] * betahat2[j];
				}
			}
			for (int i = 0; i < newPayoff3.length; i++) {
				continuations3[i] = 0;
				for (int j = 0; j < (_degree + 1); j++) {
					continuations3[i] += polyMatrix3[i][j] * betahat3[j];
				}
			}
			int ind1 = 0;
			int ind2 = 0;
			int ind3 = 0;
			for (int i = 0; i < _payoffs.length; i++){
				if (order[i] == 0){
					continuations[i] = continuations1[ind1];
					ind1++;
				} else if (order[i] == 1){
					continuations[i] = continuations2[ind2];
					ind2++;
				} else if (order[i] == 2){
					continuations[i] = continuations3[ind3];
					ind3++;
				}
			}
		}
		
		return continuations;
	}
	
	private double[][] calculateDividedRegionRegressionMatrix(double[] prices){
		double[][] polyMatrix  = new double [prices.length][_degree + 1];
		
		for (int i = 0; i < prices.length; i++) {
			polyMatrix[i][0] = 1;
			for (int j = 1; j < (_degree + 1); j++) {
				polyMatrix[i][j] = polyMatrix[i][j - 1] * prices[i];
			}
		}
		return polyMatrix;
	}
	
	// ADDED BY JAEOH, June 17, 2010
	private void calculatePolyRegressionMatrix() {
		
		pricePolyMatrix  = new double [_prices.length][_degree + 1];
		//1 + x + x^2
		for (int i = 0; i < _prices.length; i++) {
			pricePolyMatrix[i][0] = 1;
			for (int j = 1; j < (_degree + 1); j++) {
				pricePolyMatrix[i][j] = pricePolyMatrix[i][j - 1] * _prices[i];
//				pricePolyMatrix[i][j] = laguerrePolynomial(j, _prices[i]);
			}
		}
	}
	
	private void calculatePolyRegressionMatrixFor2Asset(){
		pricePolyMatrix  = new double [_prices.length][_degree + 1];
		// 1+ x1 + x2 + x1^2 + x2^2 + x1x2
		for (int i = 0; i < _prices.length; i++) {
			pricePolyMatrix[i][0] = 1;
			for (int j = 1; j < (_degree + 1); j++) {
				if (j == 1){
					pricePolyMatrix[i][j] = _prices[i];
				} else if (j == 2){
					pricePolyMatrix[i][j] = _prices2[i];
				} else if (j == 3){
					pricePolyMatrix[i][j] = _prices[i] * _prices[i];
				} else if (j == 4){
					pricePolyMatrix[i][j] = _prices2[i] * _prices2[i];
				} 
				else if (j == 5){
					pricePolyMatrix[i][j] = _prices[i] * _prices2[i];
				}
			}
		}
	}
		
	private void calculatePolyRegressionMatrixFor3Asset(){
		pricePolyMatrix  = new double [_prices.length][_degree + 1];
		// 1+ x1 + x2 + x1^2 + x2^2 + x1x2
		for (int i = 0; i < _prices.length; i++) {
			pricePolyMatrix[i][0] = 1;
			for (int j = 1; j < (_degree + 1); j++) {
				if (j == 1){
					pricePolyMatrix[i][j] = _prices[i];
				} else if (j == 2){
					pricePolyMatrix[i][j] = _prices2[i];
				} else if (j == 3){
					pricePolyMatrix[i][j] = _prices3[i];
				} else if (j == 4){
					pricePolyMatrix[i][j] = _prices[i] * _prices[i];
				} else if (j == 5){
					pricePolyMatrix[i][j] = _prices2[i] * _prices2[i];
				} else if (j == 6){
					pricePolyMatrix[i][j] = _prices3[i] * _prices3[i];
				} else if (j == 7){
					pricePolyMatrix[i][j] = _prices[i] * _prices2[i];
				} else if (j == 8){
					pricePolyMatrix[i][j] = _prices[i] * _prices3[i];
				} else if (j == 9){
					pricePolyMatrix[i][j] = _prices2[i] * _prices3[i];
				} 
			}
		}
	}
	// ADDED BY JAEOH, June 17, 2010
	// may use LaguerrePolynomial, but get same results..
	// reference:: http://en.wikipedia.org/wiki/Laguerre_polynomials
	private double laguerrePolynomial(int degree, double value) {
		
		double result = 1;
		if (degree < 1) {
			return result;
		}
		if (degree == 1) {
			return (-value + 1.0);
		}
		if (degree == 2) {
			return (0.5 * value * value - 2.0 * value + 1.0);
		}
		if (degree == 3) {
			return (-value * value * value + 9.0 * value * value - 18.0 * value
					+ 6.0) / 6.0;
		}
		if (degree == 4) {
			return (value * value * value * value 
					- 16.0 * value * value * value + 72.0 * value * value 
					- 96.0 * value + 24.0) / 24.0;
		}
		return result;
	}
	
	public double[] getBetaHat(){
		return _betaHat;
	}
	public void setBetaHat(double[] betaHat){
		this._betaHat = betaHat;
	}
}
