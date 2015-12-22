package com.quantosauros.common.hullwhite;

import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.volatility.Volatility;

public class HWVolatility extends Volatility {

	protected double _HW2FVol_1;
	protected double _HW2FVol_2;	
	
	public HWVolatility(Vertex vertex, double HW1F, double HW2F_1, double HW2F_2) {
		super(vertex, HW1F);
		_HW2FVol_1 = HW2F_1;
		_HW2FVol_2 = HW2F_2;		
	}
	
	public double getHW1F(){
		return volatility;
	}
	
	public double getHW2F_1(){
		return _HW2FVol_1;
	}
	
	public double getHW2F_2(){
		return _HW2FVol_2;
	}
	
	public double getY2(){
		return getHW2F_1();
	}
	public double getY3(){
		return getHW2F_2();
	}

	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("["+vertex.toString());
		buf.append(", HW1F: "+ volatility + ", HW2F_1: " + 
				_HW2FVol_1 + ", HW2F_2: " + _HW2FVol_2 + "],");
		//buf.append("/dayCountFraction="+dcf.toString()+"]");

		return buf.toString();
	}
}
