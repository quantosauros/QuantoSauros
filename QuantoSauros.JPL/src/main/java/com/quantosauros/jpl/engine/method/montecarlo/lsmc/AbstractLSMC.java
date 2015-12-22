package com.quantosauros.jpl.engine.method.montecarlo.lsmc;

import java.util.Arrays;

public class AbstractLSMC {
	private int _regressionDepth = 1;
	private double _percentile = 0.95;
	private int _legNum;
	private int _simNum;
	private int _rfNum;
	
	//[legIndex][rfIndex][simIndex]
	private double[][][] _inputX;
	//[legIndex][simIndex]
	private double[][] _inputY;
	
	private double _exercisePrice;
	
	private double[][] _continuationAtRate;
	private double[][] _lsmcParams;
	
	private boolean[] _flags;	
	private int _numOfCandidates;
	private int _swapLegIdx;
	private int _structuredLegIdx;
	private boolean _stickLSMCParams;
	
	public AbstractLSMC(double[][][] X, double[][] Y, 
			double exercisePrice, int structuredLegIndex){
		_inputX = X;
		_inputY = Y;
		
		_legNum = Y.length;
		_simNum = Y[0].length;
		_rfNum = _inputX[0].length;		
		_exercisePrice = exercisePrice;
		_structuredLegIdx = structuredLegIndex;
		if (structuredLegIndex == 0){
			_swapLegIdx = 1;
		}
		_flags = new boolean[_simNum];
		for (int i = 0; i < _simNum; i++){
			_flags[i] = true;
		}
		_continuationAtRate = new double[_legNum][];
		_lsmcParams = new double[_legNum][];

	}
	
	public void generate(){		
		if (_inputY.length >= 2)
			calculateSwap();
		calculate();		
	}
	
	protected void calculateSwap(){		
		LongstaffSchwartzMethod LS = null;
		_continuationAtRate[_swapLegIdx] = new double[_simNum];
		//_beta[_swapLegIdx] = new double[_simNum];
		
		if(_inputX[_swapLegIdx].length == 1){
			LS = new LongstaffSchwartzMethod(_inputY[_swapLegIdx], 
					_inputX[_swapLegIdx][0]);
		} else {
//			LS = new LongstaffSchwartzMethod(_inputY[_swapLegIdx], 
//					_inputX[_swapLegIdx][0]);
			LS = new LongstaffSchwartzMethod(_inputY[_swapLegIdx], 
					_inputX[_swapLegIdx][0], _inputX[_swapLegIdx][1]);
		}		
		if (_stickLSMCParams){
			LS.setBetaHat(_lsmcParams[_swapLegIdx]);
			_continuationAtRate[_swapLegIdx] = LS.getContinuations();
		} else {
			_continuationAtRate[_swapLegIdx] = LS.getContinuations();
			_lsmcParams[_swapLegIdx] = LS.getBetaHat();
		}		
	}
	
	protected void calculate(){
		_numOfCandidates = _simNum;
		//int legIndex = 1;
		
		double[][] continuationValues = new double[_regressionDepth][_simNum];
		double[][] absoluteValues = new double[_regressionDepth][_simNum];
		double[] limitValues = new double[_regressionDepth];
		boolean[] typeFlag = new boolean[_simNum];
		if (_stickLSMCParams){
			LongstaffSchwartzMethod LS = null;
			if (_rfNum == 1){
				LS = new LongstaffSchwartzMethod(_inputY[_structuredLegIdx], 
						_inputX[_structuredLegIdx][0]);
			} else if (_rfNum == 2){
				LS = new LongstaffSchwartzMethod(_inputY[_structuredLegIdx], 
						_inputX[_structuredLegIdx][0], _inputX[_structuredLegIdx][1]);
			}
			LS.setBetaHat(_lsmcParams[_structuredLegIdx]);	
			_continuationAtRate[_structuredLegIdx] = LS.getContinuations();
			
			
		} else {
			for (int regDepth = 0; regDepth < _regressionDepth; regDepth++) {
				
				double[][] X = new double[_rfNum][_numOfCandidates];
				double[] Y = new double[_numOfCandidates];
							
				for (int riskIndex =  0; riskIndex < _rfNum; riskIndex++){
					int canIndex = 0;
					for (int simIndex = 0; simIndex < _simNum; simIndex++){	
						if (_flags[simIndex] == true){ 
							X[riskIndex][canIndex] = _inputX[_structuredLegIdx][riskIndex][simIndex];
							Y[canIndex] = _inputY[_structuredLegIdx][simIndex];
									
							canIndex++;								
						}
					}
				}			
				LongstaffSchwartzMethod LS = null;
				if (_rfNum == 1){
					LS = new LongstaffSchwartzMethod(Y, X[0]);
				} else if (_rfNum == 2){
					LS = new LongstaffSchwartzMethod(Y, X[0], X[1]);
				}		
				
				double[] continuation = LS.getContinuations();
				if (regDepth == _regressionDepth - 1){
					_lsmcParams[_structuredLegIdx] = LS.getBetaHat();
				}
				int continuationIndex = 0;
				// save continuation values for each nodes
				for (int simIndex = 0; simIndex < _simNum; simIndex++) {
					if (_flags[simIndex] == true) {
						continuationValues[regDepth][simIndex] = 
								continuation[continuationIndex];
						if (_continuationAtRate.length == 2){
							absoluteValues[regDepth][simIndex] = Math.abs(
									continuationValues[regDepth][simIndex] - _continuationAtRate[_swapLegIdx][simIndex]);
						} else {
							absoluteValues[regDepth][simIndex] = Math.abs(
									continuationValues[regDepth][simIndex] - _exercisePrice);
						}	
						continuationIndex++;
						typeFlag[simIndex] = true;
					} else {
						continuationValues[regDepth][simIndex] = continuationValues[regDepth - 1][simIndex] ;
						absoluteValues[regDepth][simIndex] = absoluteValues[regDepth - 1][simIndex];
//								absoluteValues[regDepth][simIndex] = 0;
						typeFlag[simIndex] = false;
					}				
				}
				
				// get limit values from the absolute value which has been derived earlier.
				double[] tempValue = new double[continuationIndex];
				int simNumber = 0;
				for (int simIndex = 0; simIndex < _simNum; simIndex++){
					
					if (typeFlag[simIndex] == true){
						tempValue[simNumber] = absoluteValues[regDepth][simIndex];
						simNumber += 1;
					}
				}
				limitValues[regDepth] = continuationPercentile(tempValue, _percentile);
				
				//save remained continuation values
				_numOfCandidates = 0;
				for (int simIndex = 0; simIndex < _simNum; simIndex++) {
					if (absoluteValues[regDepth][simIndex] > limitValues[regDepth]) {
						_flags[simIndex] = false;
						//_exercise
					} else { 
						_flags[simIndex] = true;
						_numOfCandidates += 1;
					}
				}			
			}
			
			// derive continuation values
			_continuationAtRate[_structuredLegIdx] = new double[_simNum];
			
			for (int simIndex = 0; simIndex < _simNum; simIndex++){

				double tempContinuationValue = 0;
				_continuationAtRate[_structuredLegIdx][simIndex] = 0;
				for (int degree = 0; degree < _regressionDepth - 1; degree++){
					tempContinuationValue = continuationValues[degree][simIndex] 
							* kroneckerDeltaUpper(absoluteValues[degree][simIndex], limitValues[degree]);
					for (int step = 0; step < degree; step++){
						tempContinuationValue *= kroneckerDeltaLower(absoluteValues[step][simIndex], limitValues[step]);
					}
					_continuationAtRate[_structuredLegIdx][simIndex] += tempContinuationValue;
				}
				double tempContinuation = 1;
				for (int degree = 0; degree < _regressionDepth - 1; degree++){
					 tempContinuation *= kroneckerDeltaLower(absoluteValues[degree][simIndex], limitValues[degree]);
				}
				_continuationAtRate[_structuredLegIdx][simIndex] += 
						continuationValues[_regressionDepth - 1][simIndex] * tempContinuation; 
			}
		}					
	}
	
	public double[] getContinuationValues(int legIndex){
		if (_continuationAtRate[legIndex] != null){
			return _continuationAtRate[legIndex];
		} else {
			return new double[]{1};
		}		
	}
	public double[] getLSMCParams(int legIndex){
		if (_lsmcParams[legIndex] != null){
			return _lsmcParams[legIndex];
		} else {
			return null;
		}
	}
	
	public void setLSMCParams(int legIndex, double[] lsmcParams){
		_lsmcParams[legIndex] = lsmcParams;
		_stickLSMCParams = true;
	}
	
	// apply Joshi's LSMC method, added by taeheumcho 2015.04.07 
	protected double kroneckerDeltaUpper(double absoluteValues, double limit) {
		if (absoluteValues >= limit) {
			return 1;
		} else {
			return 0;
		}
	}

	protected double kroneckerDeltaLower(double absoluteValues, double limit) {
		if (absoluteValues < limit) {
			return 1;
		} else {
			return 0;
		}
	}

	protected double continuationPercentile(double[] contiValues, double percentile) {
		Arrays.sort(contiValues);
		double index = percentile * (contiValues.length - 1);
		int lower = (int) Math.floor(index);
		if (lower < 0) {
			return contiValues[0];
		}
		if (lower >= contiValues.length - 1) {
			return contiValues[contiValues.length - 1];
		}
		double fraction = index - lower;
		double result = contiValues[lower] + fraction
				* (contiValues[lower + 1] - contiValues[lower]);
		return result;
	}

}
