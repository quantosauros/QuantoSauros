package com.quantosauros.jpl.engine.method.montecarlo.motion;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.distribution.NormalDistributionRandomGenerator;

public class GeometricBrownianMotion extends GeneralizedWienerMotion {
	
	private double _riskFreeRate;
	private double _vol;
	
	public GeometricBrownianMotion(double tenor, double dt,
			double startTenor, double exerciseTenor,
			DayCountFraction dcf,
			ModelType modelType,
			double riskFreeRate, double vol, 
			NormalDistributionRandomGenerator randomGen) {
		super(tenor, dt, modelType, randomGen);
		
		_startTenor = startTenor;
		_exerciseTenor = exerciseTenor;
		_dcf = dcf;
		_tenors = new ArrayList<Double>();
		
		_riskFreeRate = riskFreeRate;
		_vol = vol;
		
		generateTenors();
	}
	
	@Override
	public double[][] generate(double[][] random, double[] initial) {
		double[][] path = new double[1][_numTimeSteps];
		double drift = - 0.5 * _vol * _vol;
		double dt = _tenors.get(0);
		path[0][0] = drift * dt 
                + _vol * Math.sqrt(dt) * random[0][0] 
                + initial[0];
		
		for (int i = 1; i < _numTimeSteps - 1; i++) {
			dt = _tenors.get(i);
			double diffusionTerm = _vol * Math.sqrt(dt);
			
			path[0][i] = drift * dt + diffusionTerm * random[0][i] + path[0][i - 1];						
			//lnS2 = lnS1 + (drift)dt + sigma * root(dt) * random
		}			
		
		if (_numTimeSteps > 1) {
			// i = n - 1 (irregular dt)
			path[0][_numTimeSteps - 1]
			     = drift * _timeSteps[1] // _timeSteps[1] == irregular dt
				+ _vol * Math.sqrt(_timeSteps[1]) * random[0][_numTimeSteps - 1] 
                + path[0][_numTimeSteps - 2];
			//마지막에 조금 남은 일수 만큼만 path를 생성함
		}
		
		return path;
	}

	@Override
	public double getInitialValue(double previousNode, double randomValue, Object flag) {
		double dt = _tenors.get(_numTimeSteps - 1);
		double drift = ((double)flag - 0.5 * _vol * _vol) * dt;		
		double diffusionTerm = _vol * Math.sqrt(dt);		
		double returnValue = previousNode + drift  + diffusionTerm * randomValue;  
		
		return returnValue;
	}

	@Override
	public double[] addAlpha(double[][] path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] addAlpha(double[][] path, double[] drifts) {
		int num = path[0].length;
		double[] newPath = new double[num];
		double drift = 0;						
		for (int j = 0; j < num; j++){
			drift += drifts[j] * _tenors.get(j);
			newPath[j] = path[0][j] + drift;
		}
		
		return newPath;
	}
	
}
