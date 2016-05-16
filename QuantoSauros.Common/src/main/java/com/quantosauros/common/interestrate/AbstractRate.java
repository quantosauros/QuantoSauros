package com.quantosauros.common.interestrate;

import com.quantosauros.common.TypeDef.YTMRateType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.math.interpolation.Coordinates;

public abstract class AbstractRate implements Coordinates {

	protected double rate;
	protected DayCountFraction dcf;
	protected YTMRateType rateType;
	protected Vertex vertex;
	protected double tenor;
	
	public AbstractRate(Vertex vertex, double rate, 
			DayCountFraction dcf, YTMRateType rateType) {
		this.vertex = vertex;
		this.rate = rate;
		this.dcf = dcf;
		this.rateType = rateType;		
		this.tenor = vertex.getVertex(dcf);
	}
	
	public AbstractRate(Vertex vertex, double rate, 
			YTMRateType rateType) {
		this(vertex, rate, DayCountFraction.DEFAULT, rateType);		
	}
	
	public Vertex getVertex(){
		return vertex;
	}
	
	public double getRate(){
		return rate;
	}
	
	public DayCountFraction getDayCountFraction(){
		return dcf;
	}
	
	public YTMRateType getRateType(){
		return rateType;
	}

	public double getX() {
		return getVertex().getVertex(dcf);
	}

	public double getY() {
		return getRate();
	}
	
	public double getTenor(){
		return tenor;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
	
		buf.append("[" + vertex + ", " + rate + ", " + rateType + "] ");
		
		return buf.toString();
	}
	
	public int compareTo(Object o) {
		Coordinates c = (Coordinates)o;
		double thisX = this.getX();
		double thatX = c.getX();
		if (thisX > thatX) {
			return 1;
		} else if (thisX == thatX) {
			return 0;
		} else if (thisX < thatX) {
			return -1;
		}
		return 0;
	}
}
