package com.quantosauros.common.hullwhite;

public class HullWhiteVolatility {
	protected double[] _tenors;
	protected double[] _volatilities;
		
	public HullWhiteVolatility(double[] tenors, double[] volatilities) {
		this._tenors = tenors;
		this._volatilities = volatilities;
	}	
	public double getVolatility(double time){
				
		if (_tenors.length == 1){
			return _volatilities[0];
		} else {
			int index = findNearestIndex(_tenors, time);			
			return _volatilities[index];			
		}		
	}
	//index : 해당시점
	//tenor : 3M
	public double getVolatility(double time, double tenor){
		int inN = 0;
		double vol = 0;
		int startIndex = findNextIndex(_tenors, time);
		for (int i = startIndex; i < _tenors.length; i++){			
			if (time + tenor < _tenors[i]){
				break;
			} else {
				inN++;
			}
		}
		
		if (inN == 0){	 
			vol = _volatilities[startIndex];				
		} else {
			double startVol = _volatilities[startIndex];
			vol =  startVol * startVol * (_tenors[startIndex] - time);
			for(int i = 1; i < inN; i++) {
				double startVoli = _volatilities[startIndex + i];
				vol += startVoli * startVoli * 
						(_tenors[startIndex + i] - _tenors[startIndex + i - 1]);
			}
			double endVol = _volatilities[startIndex + inN - 1];
			vol += endVol * endVol * (time + tenor - _tenors[startIndex + inN - 1]);
			vol = Math.sqrt(vol/tenor);
		}
		
		return vol;
	}
	public int Length(){
		if (_tenors != null){
			return _tenors.length;
		} else {
			return 0;
		}
	}
	public double[] getTenors(){
		if (_tenors != null){
			return this._tenors;
		} else {
			return null;
		}
		
	}
	public double[] getVolatility(){
		if (_volatilities != null){
			return this._volatilities;
		} else {
			return null;
		}
	}
	public boolean isEqual(HullWhiteVolatility volContainer){
		
		if (_tenors.length != volContainer.Length()){
			return false;
		}		
		double[] tenors = volContainer.getTenors();
		double[] volatility = volContainer.getVolatility();		
		for (int i = 0; i < volContainer.Length(); i++){
			if (tenors[i] != this._tenors[i]){
				return false;
			}
			if (volatility[i] != this._volatilities[i]){
				return false;
			}
		}		
		return true;
	}
	public static int findNearestIndex(double[] arr, double value) {
	    int mid;
	    int low = 0;
	    int high = arr.length-1;

	    while (low <= high) {
	        mid = (low + high) / 2;

	        if (value < arr[mid])
	            high = mid - 1;
	        else if (value > arr[mid])
	            low = mid + 1;
	        else
	            return mid;
	    }
	    if (high == -1){
	    	return 0;
	    }	    
	    if (low == arr.length){
	    	return low - 1;
	    }
	    double diffLow = Math.abs(value - arr[low]);
	    double diffHigh = Math.abs(value - arr[high]);

	    if (diffLow < diffHigh)
	        return low;
	    // Else
	    return high;
	}
	public static int findNextIndex(double[] arr, double value){
		int mid;
	    int low = 0;
	    int high = arr.length-1;
	    while (low <= high) {
	        mid = (low + high) / 2;

	        if (value < arr[mid])
	            high = mid - 1;
	        else if (value > arr[mid])
	            low = mid + 1;
	        else
	            return mid;
	    }
	    if (high == -1){
	    	return 0;
	    }	    
	    if (low == arr.length){
	    	return low - 1;
	    }
	   return low;	    
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < _tenors.length; i++){
			buf.append("[tenor: " + _tenors[i] + ", volatility: " + _volatilities[i] + "]");
			buf.append("\r\n");
		}
		
		return buf.toString();
	}
}
