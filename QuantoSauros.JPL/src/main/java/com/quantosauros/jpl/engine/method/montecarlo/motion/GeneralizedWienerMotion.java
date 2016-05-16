package com.quantosauros.jpl.engine.method.montecarlo.motion;

import java.util.ArrayList;
import java.util.Collections;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.distribution.NormalDistributionRandomGenerator;

public abstract class GeneralizedWienerMotion {

	protected NormalDistributionRandomGenerator _randomGen;
	protected int _numTimeSteps;
	protected double[] _timeSteps;	
	protected ArrayList<Double> _tenors;
	protected double _startTenor;
	protected double _exerciseTenor;
	protected int _exerciseIndex;
	protected DayCountFraction _dcf;
	protected ModelType _modelType;
	
	public GeneralizedWienerMotion(double tenor, double dt, 
			ModelType modelType,
			NormalDistributionRandomGenerator randomGen) {
		
		_randomGen = randomGen;
		_modelType = modelType;
		double remainder = tenor % dt;
		_numTimeSteps = (int) Math.round(((tenor - remainder)/ dt));
		double irregularDt = dt; // Last time period		
		
		if (remainder > 0) {
			// 나머지가 dt 의 50% 미만인 경우 전 time step 에 합산하고,
			// 50% 이상인 경우, 새로운 time step 을 생성한다.
			if (remainder / dt < 0.5) {
				irregularDt += remainder;
			}
			else {
				_numTimeSteps++;
				irregularDt = remainder;
			}
		}

		_timeSteps = new double[2];
		_timeSteps[0] = dt;
		_timeSteps[1] = irregularDt;

		_randomGen = randomGen;
	}
	public abstract double[][] generate(double[][] random, double[] initial);
	public abstract double getInitialValue(double nodeValue, double randomValue, Object flag);
	public abstract double[] addAlpha(double[][] path);
	public abstract double[] addAlpha(double[][] path, double[] drifts);	

	//20140529 added by jihoon lee 
	protected void generateTenors(){
		double tmpTenor = 0;		
		ArrayList<Double> tmpTenors = new ArrayList<Double>();		
		for (int i = 0; i < _numTimeSteps; i++){
			double timeStep = 0;
			if (i == _numTimeSteps - 1){
				timeStep = _timeSteps[1];
			} else {
				timeStep = _timeSteps[0];
			}
			tmpTenor += timeStep;
			
			tmpTenors.add(tmpTenor);
		}
		tmpTenors.add(_exerciseTenor);
//			tmpTenors.add(_autoCallStartTenor);
//			tmpTenors.add(_autoCallEndTenor);
		Collections.sort(tmpTenors);
				
		int tmpIndex = 0;
		for (int i = 0; i < tmpTenors.size(); i++){			
			double tenor = 0;			
			if (i != 0){
				double tenorDiff = tmpTenors.get(i) - tmpTenors.get(i - 1);
				if (Math.round(tenorDiff * 100000000) == 0){
					tenor = 0; 
				} else {
					tenor = tenorDiff;
				}				
			} else {
				tenor = tmpTenors.get(i);
			}			
			if (tenor != 0){
				_tenors.add(tenor);
				tmpIndex++;
			}
			if (tmpTenors.get(i) == _exerciseTenor){
				this._exerciseIndex = tmpIndex - 1;
			}
//				if (tmpTenors.get(i) == _autoCallStartTenor){
//					this._autoCallStartIndex = tmpIndex - 1;
//				}
//				if (tmpTenors.get(i) == _autoCallEndTenor){
//					this._autoCallEndIndex = tmpIndex - 1;
//				}
		}		
		
		int index = 0;
		for (int i = index - 1; i >= 0; i--){
			_tenors.remove(i);
		}
		this._numTimeSteps = _tenors.size();
		
	}
	
	public int numberTimeSteps() {
		return _numTimeSteps;
	}
	public double[] getTimeSteps() {
		return this._timeSteps;
	}
	public ArrayList getTenors(){
		return this._tenors;
	}
	public double getStartTenor(){
		return _startTenor;
	}
	public ModelType getModelType(){
		return _modelType;
	}
}
