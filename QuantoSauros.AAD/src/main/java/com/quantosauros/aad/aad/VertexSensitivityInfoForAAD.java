package com.quantosauros.aad.aad;

import java.util.ArrayList;

import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;

public class VertexSensitivityInfoForAAD{
	//private InterestRateCurve _irCurve;
	//private double[] _irRates;
	private Vertex[] _irVertex;
	private double[] _selectedRateSensi;
	private double[][] _totalRateSensiMtx;
	private int _nums;
	//private DayCountFraction _dcf;	
	private ArrayList<Double> _stepTime;
	//private DayCountFraction _dcf;
	//private int _monitorFreq;
	//private InterestRateCurve _irCurve;
	//private Date[][] _dates;
	
	public VertexSensitivityInfoForAAD(
			ArrayList<Double> stepTime,  
			InterestRateCurve irCurve, 
			DayCountFraction dcf) {

		InterestRate[] spotRates = irCurve.getSpotRates();
		_irVertex = new Vertex[spotRates.length];
		for(int i = 0; i < spotRates.length; i++){
//			_irRates[i] = spotRates[i].getRate();
			_irVertex[i] = spotRates[i].getVertex();
		}
		_stepTime = stepTime;
		_nums = _stepTime.size();		
		_totalRateSensiMtx = new double[_irVertex.length][_nums];
	
	}

	public double[][] getTotalSensiMtx(){
		
		for (int i = 0; i < _irVertex.length ; i++){
			_totalRateSensiMtx[i] = getSensitivityForSpecificVertex(_irVertex[i]);
		}
		
		return _totalRateSensiMtx;
	}	
	
	public double[] getSensitivityForSpecificVertex(Vertex selectedVertex){
		
		int selectedIndex = 0;
		for (int i = 0; i < _irVertex.length; i++){
			if( selectedVertex.isEqual(_irVertex[i])){
				selectedIndex = i;
				break;
			}
		}
		
		double[] periods = new double [_nums - 1];
		_selectedRateSensi = new double [_nums - 1];
		double selectedTime = selectedVertex.getVertex();
		double selectedBefore;
		int selectedBeforeIndex;
		int selectedAfterIndex;
		
		if(selectedIndex != 0){
			selectedBefore = _irVertex[selectedIndex - 1].getVertex();
			selectedBeforeIndex = selectedIndex - 1;
		} else {
			selectedBefore = 0;
			selectedBeforeIndex = 0;
		}
		
		double selectedAfter = 0;
		if(selectedIndex == _irVertex.length - 1){
			selectedAfter = _irVertex[selectedIndex].getVertex();
			selectedAfterIndex = selectedIndex;
		} else {
			selectedAfter = _irVertex[selectedIndex + 1].getVertex();
			selectedAfterIndex = selectedIndex + 1;
		}
		
		int startIndex = 0;
		int endIndex = 0;
		boolean startbool = false;
		boolean endbool = false;
		for (int i = 0 ; i < periods.length   ; i++){
			periods[i] = _stepTime.get(i + 1);
			if(periods[i] >= selectedBefore){
				if(startbool == false){
					startIndex = i;
					startbool = true;
				}
			}
			if(periods[i] - selectedAfter > 0){
				if(endbool == false){
					endIndex = i;
					endbool = true;
				}
			}
		}
//		periods[periods.length - 1] = _periods.get(periods.length - 1);
		
		if (endIndex == 0 && endbool == false){
			endIndex = periods.length;
		}
		
		if (startbool ){
			for (int i = startIndex; i < endIndex ; i++){
				if(periods[i] < selectedTime ){
					_selectedRateSensi[i] = (periods[i] - selectedBefore) / (selectedTime - selectedBefore);
					if (selectedIndex == selectedBeforeIndex){
						_selectedRateSensi[i] = 1;
					}
				} else {
					_selectedRateSensi[i] = 1-(periods[i] - selectedTime) / (selectedAfter - selectedTime);
					if (selectedIndex == selectedAfterIndex){
						_selectedRateSensi[i] = 1;
					}
				}
			}
		} else {
			for (int i = startIndex; i < endIndex ; i++){
				_selectedRateSensi[i] = 0;
			}
		}
		
		return _selectedRateSensi;
	}
	
	public double[] getAllSensitivity(double[] sensi){
		double[][] totalRateSensiMtx = getTotalSensiMtx();
		double[] totalSensi = new double[totalRateSensiMtx.length];
		for(int i = 0 ; i < totalRateSensiMtx.length; i++){
			for(int j = 0; j < totalRateSensiMtx[i].length; j++){
				totalSensi[i] += sensi[j + 1] * totalRateSensiMtx[i][j];
			}
		}	
		//totalSensi[0] += sensi[0] * totalRateSensiMtx[0][0];
		return totalSensi;
	}
	
	public double getSpecificSensitivity(Vertex selectedVertex, double[] sensi){
		
		double result = 0;
		double[] sensiRow = getSensitivityForSpecificVertex(selectedVertex);
		for(int i = 0; i < sensiRow.length; i++){
			result += sensiRow[i] * sensi[i];
		}
		return result;
	}

}
