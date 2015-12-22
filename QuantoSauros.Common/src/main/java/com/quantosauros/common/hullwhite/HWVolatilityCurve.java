package com.quantosauros.common.hullwhite;

import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.volatility.VolatilityCurve;

public class HWVolatilityCurve extends VolatilityCurve {

	//protected HWVolatility[] _vols;
	
	public HWVolatilityCurve(Date date, HWVolatility[] vols, DayCountFraction dcf) {
		super(date, vols, dcf);
		

	}
	public HWVolatilityCurve(Vertex swapMaturity, HWVolatility[] vols, Date date,
			DayCountFraction dcf) {
		this(date, vols, dcf);
		_swapMaturity = swapMaturity;
	}
	
//	public Volatility[] getVolatilities(){
//		return _vols;
//	}
}
