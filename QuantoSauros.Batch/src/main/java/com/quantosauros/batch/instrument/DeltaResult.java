package com.quantosauros.batch.instrument;

import com.quantosauros.common.date.Vertex;

public class DeltaResult {

	private String _riskFactorCd;
	private Vertex[] _vertex;
	private String _type;
	private double[] _greeks;
	
	public DeltaResult(String riskFactorCd, Vertex[] vertex, 
			String type, double[] greeks){
		_riskFactorCd = riskFactorCd;
		_vertex = vertex;
		_type = type;		
		_greeks = greeks;
	}
	
	public Vertex[] getVertex(){
		return _vertex;
	}	
	public String getRiskFactorCode(){
		return _riskFactorCd;				
	}
	public double[] getGreeks(){
		return _greeks;
	}
}
