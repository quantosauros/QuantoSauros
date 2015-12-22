package com.quantosauros.common.volatility;

import java.io.Serializable;

import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.LinearInterpolation;

/**
 * <div id="ko">
 * {@code VolatilityCurve} 클래스는 변동성 곡선, 즉, 
 * 시간에 따른 옵션 변동성의 변화를 나타내 주는 곡선이다
 * </div>
 * <div id="en">
 * {@code VolatilityCurve} represents a volatility curve, indicating volatility 
 * change over time
 * </div>.
 * 
 * @author Oh, Sung Hoon
 * @author Jae-Heon Kim
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
* - Made to extend Comparable by Jae-Heon Kim on 2009.04.28
------------------------------------------------------------------------------*/
public class VolatilityCurve implements Serializable {

	protected Date _date;

	protected Curve curve;

	protected DayCountFraction dcf;

	// Jae-Heon Kim, 2009.04.28
	protected Vertex _swapMaturity;
	protected Volatility[] _vols;

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param date	<div id="ko">기준일(변동성 커브의 측정일)</div><div id="en">The valuation date</div>
	 * @param vols	<div id="ko">기간별 변동성 배열</div><div id="en">An array of the volatilities over time</div>
	 * @param dcf	<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	public VolatilityCurve(Date date, Volatility[] vols, DayCountFraction dcf) {

		this(date, vols, LinearInterpolation.getInstance(), dcf);
	}

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param date			<div id="ko">기준일(변동성 커브의 측정일)</div><div id="en">The valuation date</div>
	 * @param vols			<div id="ko">기간별 변동성 배열</div><div id="en">An array of the volatilities over time</div>
	 * @param interpolation	<div id="ko">보간법</div><div id="en">The interpolation method</div>
	 * @param dcf			<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	public VolatilityCurve(Date date, Volatility [] vols, Interpolation interpolation,
			DayCountFraction dcf) {

		this._date = date;
		this.curve = new Curve(vols, interpolation);
		this.dcf = dcf;
		_vols = vols; // Jae-Heon Kim, 2009.04.28
		Volatility.setDayCountFraction(vols, dcf);
	}

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param swapMaturity	<div id="ko">스왑 만기 Vertex</div><div id="en">The swap maturity vertex</div>
	 * @param vols			<div id="ko">기간별 변동성 배열</div><div id="en">An array of the volatilities over time</div>
	 * @param date			<div id="ko">기준일(변동성 커브의 측정일)</div><div id="en">The valuation date</div>
	 * @param dcf			<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	// Jae-Heon Kim, 2009.04.28
	public VolatilityCurve(Vertex swapMaturity, Volatility [] vols, Date date,
			DayCountFraction dcf) {
		this(date, vols, dcf);
		_swapMaturity = swapMaturity;
	}

	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 측정일을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the valuation date of this {@code VolatilityCurve} object
	 * </div>.
	 *
	 * @return	<div id="ko">변동성 커브의 기준일</div><div id="en">The valuation date of the volatility curve</div>
	 */
	public Date getDate() {
		return _date;
	}

	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 측정일부터 {@code date} 후의
	 * 변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized volatility from the valuation date to a given {@code date}
	 * </div>.
	 *
	 * @param date	<div id="ko">대상일</div><div id="en">The given date</div>
	 * 
	 * @return		<div id="ko">대상일의 변동성</div><div id="en">The annualized volatility at the given date</div>
	 */
	public double getVol(Date date) {
		double t = dcf.getYearFraction(_date, date);
		return getVol(t);
	}

	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 측정일로부터 {@code tenor}만큼의 기간 
	 * 후의 변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized volatility during a given {@code tenor}
	 * </div>.
	 *
	 * @param tenor	<div id="ko">기간</div><div id="en">The given tenor</div>
	 * 
	 * @return		<div id="ko">대상일의 변동성</div><div id="en">The annualized volatility</div>
	 */
	// Jae-Heon Kim, 2009.04.28
	public double getVol(double tenor) {
		return curve.get(tenor);
	}
	
	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 측정일로부터 {@code date1}부터 {@code date2}까지의 
	 * 선도 연변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized forward volatility from {@code date1} to {@code date2} starting at the given valuation date
	 * </div>.
	 *
	 * @param date	<div id="ko">대상일1</div><div id="en">The given date1</div>
	 * @param date	<div id="ko">대상일2</div><div id="en">The given date2</div>
	 * 
	 * @return		<div id="ko">대상일의 선도 변동성</div><div id="en">The annualized forward volatility</div>
	 */
	// 2013.08.23. Youngseok Lee,
	public double getImpliedVol(Date date1, Date date2) {

		double t1 = dcf.getYearFraction(_date, date1);
		double t2 = dcf.getYearFraction(_date, date2);
		return getImpliedVol(t1, t2);
	}

	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 측정일로부터 {@code tenor1}부터 {@code tenor2}까지의 
	 * 선도 연변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized forward volatility from {@code tenor1} to {@code tenor2} starting at the given valuation date
	 * </div>.
	 *
	 * @param tenor1	<div id="ko">기간1</div><div id="en">The given tenor1</div>
	 * @param tenor2	<div id="ko">기간2</div><div id="en">The given tenor2</div>
	 * 
	 * @return		<div id="ko">대상일의 선도 변동성</div><div id="en">The annualized forward volatility</div>
	 */
	// 2013.08.23. Youngseok Lee,
	public double getImpliedVol(double tenor1, double tenor2) {
		double vol1 = curve.get(tenor1);
		double vol2 = curve.get(tenor2);
		return (vol2 * tenor2 - vol1 * tenor1) / (tenor2 - tenor1);
	}
	
	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 측정일로부터 {@code date1}부터 {@code date2}까지의 
	 * 평균 제곱근 선도 연변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized root mean squared forward volatility from {@code date1} to {@code date2} starting at the given valuation date
	 * </div>.
	 *
	 * @param date	<div id="ko">대상일1</div><div id="en">The given date1</div>
	 * @param date	<div id="ko">대상일2</div><div id="en">The given date2</div>
	 * 
	 * @return		<div id="ko">대상일의 평균 제곱근 선도 변동성</div><div id="en">The annualized root mean squared forward volatility</div>
	 */
	// 2013.08.23. Youngseok Lee,
	public double getRootMeanSquaredImpliedVol(Date date1, Date date2) {

		double t1 = dcf.getYearFraction(_date, date1);
		double t2 = dcf.getYearFraction(_date, date2);
		return getRootMeanSquaredImpliedVol(t1, t2);
	}

	
	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 측정일로부터 {@code tenor1}부터 {@code tenor2}까지의 
	 * 평균 제곱근 선도 연변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized root mean squared forward volatility from {@code tenor1} to {@code tenor2}  starting at the given valuation date
	 * </div>.
	 *
	 * @param tenor1	<div id="ko">기간1</div><div id="en">The given tenor1</div>
	 * @param tenor2	<div id="ko">기간2</div><div id="en">The given tenor2</div>
	 * 
	 * @return		<div id="ko">대상일의 평균 제곱근 선도 변동성</div><div id="en">The annualized root mean squared forward volatility</div>
	 */
	// 2013.08.23. Youngseok Lee,
	public double getRootMeanSquaredImpliedVol(double tenor1, double tenor2) {
		double vol1 = curve.get(tenor1);
		double vol2 = curve.get(tenor2);
		return Math.sqrt((vol2 * vol2 * tenor2 - vol1 * vol1 * tenor1) / (tenor2 - tenor1));
	}


	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 길이를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the length of this {@code VolatilityCurve} object
	 * </div>.
	 * <div id="ko">
	 * <p>
	 * 여기서 변동성 곡선의 길이는 해당 변동성 곡선을 생성할 때 지정한 변동성의
	 * 개수를 의미한다.
	 * </div>
	 * <div id="en">
	 * <p>
	 * The length of this volatility curve is equal to the length of the array of
	 * the annualized volatilities.
	 * </div>
	 *
	 * @return	<div id="ko">변동성 곡선의 길이</div><div id="en">The length of the volatility curve</div>
	 */
	// Jae-Heon Kim, 2009.04.28
	public int length() {
		return curve.length();
	}

	/**
	 * <div id="ko">
	 * 스와프 만기 Vertex를 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a swap maturity vertex
	 * </div>.
	 * 
	 * @param v		<div id="ko">스와프 만기 Vertex</div><div id="en">The swap maturity vertex</div>
	 */
	// Jae-Heon Kim, 2009.04.28
	public void setSwapMaturity(Vertex v) {
		_swapMaturity = v;
	}

	/**
	 * <div id="ko">
	 * 스와프 만기 Vertex를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the swap maturity vertex
	 * </div>.
	 * 
	 * @return	<div id="ko">스와프 만기 Vertex</div><div id="en">The swap maturity vertex</div>
	 */
	// Jae-Heon Kim, 2009.04.28
	public Vertex getSwapMaturity() {
		return _swapMaturity;
	}

	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 변동성 배열을 반환한다
	 * </div>
	 * <div id="en">
	 * Returns an array of the volatility of this {@code VolatilityCurve} object
	 * </div>.
	 * 
	 * @return	<div id="ko">변동성 배열</div><div id="en">An array of the volatilities</div>
	 */
	// Jae-Heon Kim, 2009.04.28
	public Volatility[] getVolatilities() {
		return _vols;
	}

	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체에 설정되어 있는 Day Count Fraction을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the day count fraction of {@code VolatilityCurve} object
	 * </div>.
	 * 
	 * @return	<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	// Jae-Heon Kim, 2009.04.28
	public DayCountFraction getDayCountFraction() {
		return dcf;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (_swapMaturity != null) {
			buf.append("[Swap Tenor =" + _swapMaturity + "]\r\n");
		}
		
		for (int i = 0; i < _vols.length; ++i) {
			buf.append(_vols[i].toString());
			buf.append(" ");	
		}
		
		return buf.toString();
	}
	
	/**
	 * <div id="ko">
	 * 이 {@code VolatilityCurve} 객체의 변동성에 {@code parallelShift}만큼 증가시킨 
	 * 변동성을 가지는 새 {@code VolatilityCurve} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code VolatilityCurve} object by adding a given {@code parallelShift} 
	 * to the annualized volatility of this {@code VolatilityCurve} object
	 * </div>.
	 *
	 * @param parallelShift	<div id="ko">변동성의 증가분</div><div id="en">The parallel shift</div>
	 * @return 				<div id="ko">변동성을 증가시킨 새 {@code VolatilityCurve} 객체</div><div id="en">A new {@code VolatilityCurve} object</div>
	 */
	public VolatilityCurve copy(double parallelShift) {
		Volatility[] shiftVols = new Volatility[_vols.length];
		for (int i = 0; i < _vols.length; ++i) {
			shiftVols[i] = new Volatility(_vols[i].getVertex(),
					                      _vols[i].getRate() + parallelShift);
		}
		VolatilityCurve irvc = new VolatilityCurve(_date, shiftVols, dcf);
		if (this._swapMaturity != null)
			irvc.setSwapMaturity(_swapMaturity);
		return irvc;
	}
	
	/**
	 * 입력받은 Curve와 객체를 비교하여 각각의 만기별로 변동성이 같으면 true를
	 * 그렇지 않으면 false를 반환한다.
	 * 
	 * @param curve
	 * 
	 * @return boolean
	 */
	// 20140317 Jihoon Lee
	public boolean isEqual(VolatilityCurve curve) {
		
		if(_vols.length != curve.length()){
			return false;
		}
		Volatility[] vols = curve.getVolatilities();
		for (int i = 0; i < _vols.length; i++){
			if (!_vols[i].getVertex().isEqual(vols[i].getVertex())){
				return false;
			}
			if (_vols[i].getRate() != vols[i].getRate()){
				return false;
			}
		}
		return true;
	}
}
