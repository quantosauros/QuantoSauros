package com.quantosauros.jpl.dto;

import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.date.Date;

public class OptionInfo {

	protected Date[] _exerciseDates;
	protected double[] _exercisePrices;
	protected OptionType _optionType;
	
	public OptionInfo(OptionType optionType, 
			Date[] exerciseDates, double[] exercisePrices) {
		_optionType = optionType;
		_exerciseDates = exerciseDates;
		_exercisePrices = exercisePrices;	
	}
	
	public OptionType getOptionType(){
		return _optionType;
	}
	public void setOptionType(OptionType optionType){
		this._optionType = optionType;
	}
	public Date[] getExerciseDates(){
		return _exerciseDates;
	}
	public void setExerciseDates(Date[] exerciseDates){
		this._exerciseDates = exerciseDates;
	}
	public double[] getExercisePrices(){
		return _exercisePrices;
	}
	public void setExercisePrices(double[] exercisePrices){
		this._exercisePrices = exercisePrices;
	}
	public boolean hasExercise(){
		if (_optionType == null){
			return false;
		} else {
			if (_optionType.equals(OptionType.NONE)){
				return false;
			} else {
				return true;
			}
		}		
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n");
		buf.append("==" + getClass().getName() + "==");
		buf.append("\r\n");
		if (_optionType != null){
			buf.append("optionType=" + _optionType.toString());
			buf.append("\r\n");
		}
		if (_exerciseDates != null){
			for (int i = 0; i < _exerciseDates.length; i++){
				buf.append("exerciseDates[" + i +"]=" + _exerciseDates[i].toString());
				buf.append("\r\n");
			}			
		}
		if (_exercisePrices != null){
			for (int i = 0; i < _exercisePrices.length; i++){
				buf.append("exercisePrices[" + i +"]=" + _exercisePrices[i]);
				buf.append("\r\n");
			}			
		}
		
		return buf.toString();
	}
}
