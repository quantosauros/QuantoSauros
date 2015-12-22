package com.quantosauros.jpl.engine.method.montecarlo.motion;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.common.math.distribution.NormalDistributionRandomGenerator;

public class OrnsteinUhlenbeckMotion extends GeneralizedWienerMotion {

	private HullWhiteParameters _hwParams;
	private InterestRateCurve _irCurve;
	private HullWhiteVolatility _hwVols;
	
	public OrnsteinUhlenbeckMotion(double tenor, double dt,
			double startTenor, double exerciseTenor,
			DayCountFraction dcf, 
			ModelType modelType,
			HullWhiteParameters hwParams,
			HullWhiteVolatility hwVols,
			InterestRateCurve irCurve,
			NormalDistributionRandomGenerator randomGen) {
		super(tenor, dt, modelType, randomGen);
		
		_startTenor = startTenor;
		_exerciseTenor = exerciseTenor;
		_dcf = dcf;
		_hwParams = hwParams;
		_hwVols = hwVols;
		_irCurve = irCurve;
		_tenors = new ArrayList<Double>();	
		
		generateTenors();
	}
			
	public double[][] generate(double[][] random, double[] initial){
		double[][] path = new double[1][_numTimeSteps];
		double meanReversion = _hwParams.getMeanReversion1F();
		double dt = _tenors.get(0);
		double drift = Math.exp(-meanReversion * dt);
		double vol = Math.sqrt((1 - Math.exp(-2 * meanReversion * dt))/(2 * meanReversion));
//		path[0][0] = drift * initial[0] + getDiffusion(0) * vol * random[0][0];
		path[0][0] = initial[0];
		
		
		for (int i = 1; i < _numTimeSteps; i++){
//				dt = _tenors.get(i);
//				drift = Math.exp(-_meanReversion * dt);
//				vol = Math.sqrt((1 - Math.exp(-2 * _meanReversion * dt))/(2 * _meanReversion));
//				
//				path[0][i] = drift * path[0][i - 1] + getDiffusion(i) * vol * random[0][i];
			
			dt = _tenors.get(i - 1);
			drift = Math.exp(-meanReversion * dt);
			vol = Math.sqrt((1 - Math.exp(-2 * meanReversion * dt))/(2 * meanReversion));
			
			path[0][i] = drift * path[0][i - 1] + getDiffusion(i - 1) * vol * random[0][i - 1];
		}
		
		return path;
	}
	
	public double getInitialValue(double nodeValue, double randomValue, Object flag){
		
		double meanReversion = _hwParams.getMeanReversion1F();
		//double dt =_tenors.get(_numTimeSteps - 1) + 1.0 / _dcf.getDaysOfYear();
		double dt =_tenors.get(_numTimeSteps - 1);
		double drift = Math.exp(-meanReversion * dt);
		double vol = Math.sqrt((1 - Math.exp(-2 * meanReversion * dt))/(2 * meanReversion));
		
		return drift * nodeValue + getDiffusion(_numTimeSteps - 1) * vol * randomValue;
	}
	
	public double[] addAlpha(double[][] path){
		double[] newPath = new double[_numTimeSteps];
				
		for (int i = 0; i < _numTimeSteps; i++){
			newPath[i] = path[0][i] + getAlpha(i, _irCurve);
		}
		return newPath;
	}
	@Override
	public double[] addAlpha(double[][] path, double[] drifts) {
		// TODO Auto-generated method stub
		return null;
	}
	private double getAlpha(int index, InterestRateCurve irCurve){	
		double tenorSum = 0;
		//TODO index를 포함하는지 안하는지 체크
		for (int i = 0; i < index; i++){
			tenorSum += _tenors.get(i);
		}
		double meanReversion = _hwParams.getMeanReversion1F();
		double t = tenorSum + _startTenor;		
		double deltaT = 1.0 / 365.0;
		//t=t+deltaT;
		double spotRatet = irCurve.getSpotRate(t);
		double spotRatedt = irCurve.getSpotRate(t + deltaT);
		double forwardRate = ((spotRatedt - spotRatet) * (t / deltaT)) + spotRatedt;
		double diffusion = getDiffusion(index);
		double result = forwardRate + (diffusion * diffusion * 
				Math.pow((1- Math.exp(- meanReversion * t)), 2)) / 
				(2 * meanReversion * meanReversion);
		
		return result; 
		
	}
	public double getDiffusion(int index){		
		double tenorSum = 0;
		for (int i = 0; i < index; i++){
			tenorSum += _tenors.get(i);
		}
		
		return _hwVols.getVolatility(_startTenor + tenorSum);
		
	}
	
}
