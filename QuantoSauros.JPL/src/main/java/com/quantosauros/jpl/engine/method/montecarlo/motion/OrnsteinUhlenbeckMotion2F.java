package com.quantosauros.jpl.engine.method.montecarlo.motion;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.common.math.distribution.NormalDistributionRandomGenerator;

public class OrnsteinUhlenbeckMotion2F extends GeneralizedWienerMotion {

	private HullWhiteParameters _hwParams;
	private ZeroRateCurve _irCurve;
	private HullWhiteVolatility[] _hwVols;
	
	public OrnsteinUhlenbeckMotion2F(double tenor, double dt,
			double startTenor, double exerciseTenor,
			DayCountFraction dcf, 
			ModelType modelType,
			HullWhiteParameters hwParams,
			HullWhiteVolatility[] hwVols,
			ZeroRateCurve irCurve,
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
		double[][] path = new double[2][_numTimeSteps];
		double mr1 = _hwParams.getMeanReversion1_2F();
		double mr2 = _hwParams.getMeanReversion2_2F();		
		double dt = _tenors.get(0);
		
		double drift1 = Math.exp(-mr1 * dt);
		double drift2 = Math.exp(-mr2 * dt);
		
		double vol1 = Math.sqrt((1 - Math.exp(-2 * mr1 * dt)) / (2 * mr1));
		double vol2 = Math.sqrt((1 - Math.exp(-2 * mr2 * dt)) / (2 * mr2));
				
		path[0][0] = initial[0];
		path[1][0] = initial[1];
		
		for (int i = 1; i < _numTimeSteps; i++){			 
			dt = _tenors.get(i - 1);
			
			drift1 = Math.exp(-mr1* dt);
			drift2 = Math.exp(-mr2* dt);
			vol1 = Math.sqrt((1 - Math.exp(-2 * mr1 * dt)) / (2 * mr1));
			vol2 = Math.sqrt((1 - Math.exp(-2 * mr2 * dt)) / (2 * mr2));
			
			path[0][i] = drift1 * path[0][i - 1] + getDiffusion1(i - 1) * vol1 * random[0][i - 1];
			path[1][i] = drift2 * path[1][i - 1] + getDiffusion2(i - 1) * vol2 * random[1][i - 1];
		}
		
		return path;
	}
	
	public double getInitialValue(double nodeValue, double randomValue, Object flag){
		//flag - 0 : 1F or 2F_X, 1 : 2F_Y
		double meanReversion = 0;
		double diffusion = 0;
		if (flag.equals(0)){
			meanReversion = _hwParams.getMeanReversion1_2F();
			diffusion = getDiffusion1(_numTimeSteps - 1);
			
		} else if (flag.equals(1)){
			meanReversion = _hwParams.getMeanReversion2_2F();
			diffusion = getDiffusion2(_numTimeSteps - 1);
		}
//				double dt = _tenors.get(_numTimeSteps - 1) + 1.0 /_dcf.getDaysOfYear();
		double dt = _tenors.get(_numTimeSteps - 1);
		double drift = Math.exp(-meanReversion * dt);
		double vol = Math.sqrt((1 - Math.exp(-2 * meanReversion * dt))/(2 * meanReversion));
		
		return drift * nodeValue + diffusion * vol * randomValue;
	}
	
	public double[] addAlpha(double[][] path){
		double[] newPath = new double[_numTimeSteps];
		
		for (int i = 0; i < _numTimeSteps; i++){
			newPath[i] = path[0][i] + path[1][i] + getAlpha(i, _irCurve);
		}
		return newPath;
	}
	@Override
	public double[] addAlpha(double[][] path, double[] drifts) {
		// TODO Auto-generated method stub
		return null;
	}
	private double getAlpha(int index, ZeroRateCurve irCurve){	
		double tenorSum = 0;
		//TODO index를 포함하는지 안하는지 체크
		for (int i = 0; i < index; i++){
			tenorSum += _tenors.get(i);
		}
		double mr1 = _hwParams.getMeanReversion1_2F();
		double mr2 = _hwParams.getMeanReversion2_2F();
		double t = tenorSum + _startTenor;		
		double deltaT = 1.0 / 365.0;
		//t=t+deltaT;
		double spotRatet = irCurve.getSpotRate(t);
		double spotRatedt = irCurve.getSpotRate(t + deltaT);
		double forwardRate = ((spotRatedt - spotRatet) * (t / deltaT)) + spotRatedt;
		
		double sigma1 = getDiffusion1(index);
		double sigma2 = getDiffusion2(index);
		
		double tmp1 = Math.exp(mr1 * t);
		double tmp2 = Math.exp(mr2 * t);
		double correlation = _hwParams.getCorrelation();
		
		double t1 = sigma1 * sigma1 * (1 - tmp1) * (1 - tmp1) /
				(2 * mr1 * mr1);
		
		double t2 = sigma2 * sigma2 * (1 - tmp2) * (1 - tmp2) /
				(2 * mr2 * mr2);
		
		double t3 = correlation * sigma1 * sigma2 * (1 - tmp1) * (1 - tmp2) /
				(mr1 * mr2);
						
		return forwardRate + t1 + t2 + t3;
		
	}
	public double getDiffusion1(int index){		
		double tenorSum = 0;
		for (int i = 0; i < index; i++){
			tenorSum += _tenors.get(i);
		}
		
		return _hwVols[0].getVolatility(_startTenor + tenorSum);
		
	}
	public double getDiffusion2(int index){
		double tenorSum = 0;
		for (int i = 0; i < index; i++){
			tenorSum += _tenors.get(i);
		}
		
		return _hwVols[1].getVolatility(_startTenor + tenorSum);
	}
}
