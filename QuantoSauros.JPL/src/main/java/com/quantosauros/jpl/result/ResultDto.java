package com.quantosauros.jpl.result;

import java.util.ArrayList;

public class ResultDto {

	//[period]
	private ArrayList<Double> _avg;
	private ArrayList<Double> _std;
	
	public ResultDto(int periodNum) {
		_avg = new ArrayList<Double>(periodNum);
		_std = new ArrayList<Double>(periodNum);
	}	
	
	public void setAvgValue(int periodIndex, double avg){
		_avg.add(periodIndex, avg);		
	}
	public double getAvgValue(int periodIndex){
		if (_avg.size() > periodIndex){
			return _avg.get(periodIndex);
		} else {
			return 0;
		}		
	}
	public void setStdValue(int periodIndex, double std){
		_std.add(periodIndex, std);
	}	
	public double getStdValue(int periodIndex){
		if (_std.size() > periodIndex){
			return _std.get(periodIndex);
		} else {
			return 0;
		}
		
	}
	public int length(){
		return _avg.size();
		
	}
}
