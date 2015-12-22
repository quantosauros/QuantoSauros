package com.quantosauros.common.volatility;

import java.io.Serializable;

import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.math.interpolation.Coordinates;

/**
 * <div id="ko">
 * {@code Volatility} 클래스는 변동성을 나타낸다
 * </div>
 * <div id="en">
 * {@code Volatility} represents volatility
 * </div>.
 * <div id="ko">
 * <p>
 * 여기서 변동성이란 미래 주가 변동 등에 대한 불확실성을 측정하는 수단을 뜻한다.
 * Note: This class has a natural ordering that is inconsistent with equals.
 * </div>
 * <div id="en">
 * <p>
 * Volatility refers to a means of measuring the uncertainty about future stock movement
 * Note: This class has a natural ordering that is inconsistent with equals.
 * </div>
 * @author Oh, Sung Hoon
 * @author Jae-Heon Kim
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class Volatility implements Serializable, Coordinates {

	protected Vertex vertex;

	protected double volatility;

	protected DayCountFraction dcf;

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param vertex		<div id="ko">변동성의 Vertex</div><div id="en">The vertex</div>
	 * @param volatility	<div id="ko">변동성</div><div id="en">The corresponding annualized vertex</div>
	 */
	public Volatility(Vertex vertex, double volatility) {
		this.vertex = vertex;
		this.volatility = volatility;
		this.dcf = DayCountFraction.DEFAULT;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 {@code Vertex}를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the {@code Vertex} of this {@code Volatility} object
	 * </div>.
	 * 
	 * @return	<div id="ko">이 객체의 {@code Vertex}</div><div id="en">The {@code Vertex} of the object</div>
	 */
	public Vertex getVertex() {
		return vertex;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 변동성을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the volatility of this {@code Volatility} object
	 * </div>.
	 * 
	 * @return	<div id="ko">이 객체의 변동성</div><div id="en">The volatility of the object</div>
	 */
	public double getRate() {
		return volatility;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 {@code Vertex}를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the year fraction of the {@code Vertex} of this {@code Volatility} object
	 * </div>.
	 * 
	 * @return	<div id="ko">Volatility Vertex의 Year Fraction</div><div id="en">The year fraction of the volatility vertex</div>
	 */
	public double getX() {
		return getVertex().getVertex(dcf);
	}

	/**
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 변동성을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the volatility of this {@code Volatility} object
	 * </div>.
	 * 
	 * @return	<div id="ko">이 객체의 변동성</div><div id="en">The volatility of the object</div>
	 */
	public double getY() {
		return getRate();
	}

	/**
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 Day Count Fraction을 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a day count fraction of an array of given {@code Volatility} objects 
	 * </div>.
	 * 
	 * @param vols	<div id="ko">변동성</div><div id="en">An array of the volatilities</div>
	 * @param dcf	<div id="ko">Day Count Fraction</div><div id="en">The corresponding dat count fraction</div>
	 */
	public static void setDayCountFraction(Volatility [] vols, DayCountFraction dcf) {
		if (vols == null) return;
		for (int i = 0; i < vols.length; ++i) {
			vols[i].dcf = dcf;
		}
	}

	/**
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 기간을 주어진 {@code Coordinate}
	 * 객체의 X 값인 기간과 비교한다
	 * </div>
	 * <div id="en">
     * Compares the vertex of this {@code Volatility} object with the X value of a given 
     * {@code Coordinate} object
	 * </div>.
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 기간이 입력된 좌표의 X 값보다
	 * 크면 1, 작으면 -1, 그리고 두 값이 같으면 0을 반환한다.
	 * </div>
	 * <div id="en">
     * Returns 1 if the vertex of this {@code Volatility} is larger than the X
     * value of a given {@code Coordinate} object, -1 otherwise.
	 * </div>
	 * 
	 * @param o
	 * 			  <div id="ko">비교할 {@code Coordinates} 객체</div>
	 * 			  <div id="en">The {@code Coordinates} object to be compared</div>
	 * 
	 * @return <div id="ko">비교 결과</div>
	 * 		   <div id="en">1 if the vertex of the {@code Volatility} is larger than the given X value, -1 otherwise</div>
	 */
	// Jae-Heon Kim, 2009.04.28
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

	/**
	 * <div id="ko">
	 * 이 {@code Volatility} 객체의 문자열 표현을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the string representation of this {@code Volatility} object
	 * </div>.
	 * 
	 * @return <div id="ko">이 {@code Volatility} 객체의 문자열 표현</div>
	 * 		   <div id="en">The string representation of the {@code Volatility} object</div>
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("["+vertex.toString());
		buf.append(","+volatility + "]");
		//buf.append("/dayCountFraction="+dcf.toString()+"]");

		return buf.toString();
	}

	
}
