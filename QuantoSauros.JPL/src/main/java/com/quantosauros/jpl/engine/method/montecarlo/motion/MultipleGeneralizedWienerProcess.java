package com.quantosauros.jpl.engine.method.montecarlo.motion;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.math.distribution.NormalDistributionRandomGenerator;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;


public class MultipleGeneralizedWienerProcess {

	private NormalDistributionRandomGenerator _randomGen;
	private GeneralizedWienerMotion[] _processes;
	
	public MultipleGeneralizedWienerProcess(
			GeneralizedWienerMotion[] processes,
			NormalDistributionRandomGenerator randomGen) {

		_processes = processes;
		_randomGen = randomGen;
	}
	
	public double[][] pathGenerate() {
		int n = numberTimeSteps();
		//usually numberTimeSteps = # of days
		double[][] paths; //[# of assets][rand for # of timeSteps]
		paths = _randomGen.generate(n);
		return paths;
		//1차원인경우 paths = [0][# of days]
	}
	
	public double[][][] pathGenerate(int[] motionIndices, double[] time, int simIndex,
			MarketInfo[] marketInfos) {
		int n = numberTimeSteps();
		int processNum = _processes.length;
		ModelType[] modelTypes = new ModelType[processNum];
		HullWhiteParameters[] hwParams = new HullWhiteParameters[processNum];
		HullWhiteVolatility[][] hwVols = new HullWhiteVolatility[processNum][];
		for (int processIndex = 0; processIndex < processNum; processIndex++){
			modelTypes[processIndex] = _processes[processIndex].getModelType();
			if (marketInfos[processIndex] instanceof RateMarketInfo){
				RateMarketInfo rateMarketInfo = (RateMarketInfo)marketInfos[processIndex];
				hwParams[processIndex] = rateMarketInfo.getHullWhiteParameters();
				hwVols[processIndex] = rateMarketInfo.getHWVolatilities();
			} else {
				hwParams[processIndex] = null;
				hwVols[processIndex] = null;
			}					
		}
			 
		return _randomGen.generate(processNum, n, modelTypes, motionIndices, 
				time, simIndex, hwParams, hwVols);
		//1차원인경우 paths = [0][# of days]
	}
	
//	int assetNum, int n, HullWhiteType[] hwTypeArray, 
//	HullWhiteParameters[] hwParamsArray, HullWhiteVolatility[][] vols, double[] time, long seed
	
	public int numberTimeSteps() {
		return _processes[0].numberTimeSteps();
	}
}
