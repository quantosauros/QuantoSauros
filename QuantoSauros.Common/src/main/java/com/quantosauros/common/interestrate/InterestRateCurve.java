package com.quantosauros.common.interestrate;

import java.io.Serializable;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.LinearInterpolation;
import com.quantosauros.common.math.interpolation.PiecewiseConstantInterpolation;
import com.quantosauros.common.volatility.Curve;

/**
 * <div id="ko">
 * {@code InterestRateCurve} 클래스는 이자율(금리) 곡선을 나타낸다
 * </div>
 * <div id="en">
 * {@code InterestRateCurve} represents an interest rate curve
 * </div>.
 * <div id="ko">
 * <p>
 * 예를 들면 채권의 경우, 여러 가지 위험과 기타 모든 조건이 동일할 때, 만기에 따라 수익률이 
 * 어떻게 변하는지 나타내 주는 곡선을 의미한다.
 * </div>
 * <div id="en">
 * <p>
 * The curve shows interest rates across different terms. 
 * </div>
 * 
 * 
 * @author Oh, Sung Hoon
 * @author Jae-Heon Kim
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
* - 2010-01-08 : Spread 추가, Spot Rate 계산시 Spread 가 더해진 값을 반환
------------------------------------------------------------------------------*/
public class InterestRateCurve implements Serializable {

	private Date _date;

	private Curve curve;

	private Frequency compoundFreq;

	private DayCountFraction dcf;

	private InterestRate[] spotRates;

	private Interpolation interpolation;

	/**
	 * <div id="ko">
	 * 스프레드
	 * </div>
	 * <div id="en">
	 * The spread
	 * </div>
	 */
	private double spread;
	
	// 2010.03.23 Jae-Heon Kim
	private boolean _isCms = false;
	
	/**
	 * <div id="ko">
	 * 기준일과 실제 만기일 vertex로 이자율커브 배열을 생성한다
	 * </div>
	 * <div id="en">
	 * Returns a interest rate curve generated from a given valuation date,
	 * maturity vertex dates and spot rates
	 * </div>. 
	 * 
	 * @param date 
	 * 		<div id="ko">기준일</div>
	 * 		<div id="en">The valuation date</div>
	 * @param vertexDates 
	 * 		<div id="ko">만기일</div>
	 * 		<div id="en">An array of the maturity dates</div>
	 * @param spotRates 
	 * 		<div id="ko">만기일의 현물이자율</div>
	 * 		<div id="en">An array of the spot rates</div>
	 * @param compoundFreq 
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">The compounding frequency</div>
	 * @param dcf 
	 * 		<div id="ko">Day Count Fraction</div>
	 * 		<div id="en">The day count fraction</div>
	 * 
	 * @return
	 * 		<div id="ko">이자율 커브 객체</div>
	 * 		<div id="en">A {@code InterestRateCurve} object</div>
	 * 
	 * @since 3.3
	 * @author 류상욱 on 2012.11.12
	 */
	public static InterestRateCurve getInterestRateCurve(Date date, Date[] vertexDates, double[] spotRates, Frequency compoundFreq, DayCountFraction dcf) {

		if (vertexDates.length != spotRates.length)
			throw new IllegalArgumentException("Array lengths of vertex-dates and spot-rates do not match.");
		
		InterestRate[] irRates = new InterestRate[spotRates.length];
		for (int i=0 ; i < vertexDates.length ; i++) {
			Vertex v = Vertex.calculate(date, vertexDates[i]);
			irRates[i] = new InterestRate(v, spotRates[i]);
		}
		
		return new InterestRateCurve(date, irRates, LinearInterpolation.getInstance(), compoundFreq, dcf);
	}
	
	/**
	 * <div id="ko">
	 * 기준일과 실제 만기일 vertex로 이자율커브 배열을 생성한다
	 * </div>
	 * <div id="en">
	 * Returns a interest rate curve generated from a given valuation date,
	 * maturity vertex dates and spot rates
	 * </div>. 
	 * 
	 * @param date 
	 * 		<div id="ko">기준일</div>
	 * 		<div id="en">The valuation date</div>
	 * @param vertexDates 
	 * 		<div id="ko">만기일</div>
	 * 		<div id="en">An array of the maturity dates</div>
	 * @param spotRates 
	 * 		<div id="ko">만기일의 현물이자율</div>
	 * 		<div id="en">An array of the spot rates</div>
 	 * @param interpolation	
 	 * 		<div id="ko">보간법</div>
 	 * 		<div id="en">The interpolation method</div>
	 * @param compoundFreq 
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">The compounding frequency</div>
	 * @param dcf 
	 * 		<div id="ko">Day Count Fraction</div>
	 * 		<div id="en">The day count fraction</div>
	 * 
	 * @return
	 * 		<div id="ko">이자율 커브 객체</div>
	 * 		<div id="en">A {@code InterestRateCurve} object</div>
	 * 
	 */
	// 2013.02.13. Youngseok Lee
	public static InterestRateCurve getInterestRateCurve(Date date, Date[] vertexDates, double[] spotRates, Interpolation interpolation, Frequency compoundFreq, DayCountFraction dcf) {

		if (vertexDates.length != spotRates.length)
			throw new IllegalArgumentException("Array lengths of vertex-dates and spot-rates do not match.");
		
		InterestRate[] irRates = new InterestRate[spotRates.length];
		for (int i=0 ; i < vertexDates.length ; i++) {
			Vertex v = Vertex.calculate(date, vertexDates[i]);
			irRates[i] = new InterestRate(v, spotRates[i]);
		}
		// 2013.03.15. Youngseok Lee
		if (interpolation == PiecewiseConstantInterpolation.getInstance()) {
			return new InterestRateCurve(date, irRates, interpolation, compoundFreq, DayCountFraction.D30_360);
		}
		return new InterestRateCurve(date, irRates, interpolation, compoundFreq, dcf);
	}


	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param date				<div id="ko">기준일(이자율 곡선의 측정일)</div><div id="en">The valuation date</div>
	 * @param spotRates			<div id="ko">현물 이자율</div><div id="en">The spot rates</div>
	 * @param compoundFreq		<div id="ko">이자 계산 주기</div><div id="en">The compounding frequency</div>
	 * @param dcf				<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	public InterestRateCurve(Date date, InterestRate[] spotRates,
			Frequency compoundFreq, DayCountFraction dcf) {

		this(date, spotRates, LinearInterpolation.getInstance(), compoundFreq,
				dcf);
	}

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param date				<div id="ko">기준일(이자율 곡선의 측정일)</div><div id="en">The valuation date</div>
	 * @param spotRates			<div id="ko">현물 이자율</div><div id="en">The spot rates</div>
 	 * @param interpolation		<div id="ko">보간법</div>	<div id="en">The interpolation method</div>
	 * @param compoundFreq		<div id="ko">이자 계산 주기</div><div id="en">The compounding frequency</div>
	 * @param dcf				<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	public InterestRateCurve(Date date, InterestRate[] spotRates,
			Interpolation interpolation, Frequency compoundFreq,
			DayCountFraction dcf) {

		this._date = date;
		this.curve = new Curve(spotRates, interpolation);
		this.compoundFreq = compoundFreq;
		this.dcf = dcf;
		InterestRate.setDayCountFraction(spotRates, dcf);
		this.spotRates = spotRates;
		this.interpolation = interpolation;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 기준일(이자율 커브의 측정일)을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the valuation date of this {@code InterestRateCurve} object
	 * </div>.
	 *
	 * @return <div id="ko">금리 생성자 기준일(이자율 커브의 측정일)</div><div id="en">The valuation date</div>
	 */
	public Date getDate() {
		return _date;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 이자 계산 주기를 가져온다.
	 * </div>
	 * <div id="en">
	 * Returns the compounding frequency of this {@code InterestRateCurve} object
	 * </div>.
	 *
	 * @return <div id="ko">금리 커브의 이자 계산 주기</div><div id="en">The compounding frequency</div>
	 */
	public Frequency getCompoundingFrequency() {
		return compoundFreq;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 compounding frequency를 이용하여
	 * 입력된 날짜의 현물 이자율을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the spot rate at a given date using the compounding frequency
	 * of this {@code InterestRateCurve} object
	 * </div>.
	 *
	 * @param date	<div id="ko">대상일</div><div id="en">The given date</div>
	 * 
	 * @return		<div id="ko">대상일의 현물 이자율</div><div id="en">The spot rate at the given date</div>
	 */
	public double getSpotRate(Date date) {
		return getSpotRate(date, compoundFreq);
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 compounding frequency를 이용하여
	 * 주어진 연 단위 환산 기간 후의 현물 이자율을 반환한다
	 * </div>
	 * <div id="en">
	 * Calculates the spot rate at the date from the valuation date by a given 
	 * term using the compounding frequency of this {@code ParSwapRateCurve} object
	 * </div>.
	 *
	 * @param t		<div id="ko">이자율 계산 기간</div><div id="en">The given term</div>
	 * 
	 * @return		<div id="ko">현물 이자율</div><div id="en">The spot rate</div>
	 */
	// Jae-Heon Kim, 2009.10.15
	public double getSpotRate(double t) {
		return getSpotRate(t, compoundFreq);
	}
	
	/**
	 * <div id="ko">
	 * 주어진 compounding frequency를 이용하여 입력된 날짜의 현물 이자율을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the spot rate at a given date using a given compounding frequency
	 * </div>.
	 *
	 * @param date				<div id="ko">대상일</div><div id="en">The given date</div>
	 * @param targetCmpdFreq	<div id="ko">이자 계산 주기</div><div id="en">The given compounding frequency</div>
	 * 
	 * @return					<div id="ko">대상일의 현물 이자율</div><div id="en"> The spot rate at the given date</div>
	 */
	// Jae-Heon Kim 2011.01.10
	// Changed from public to protected
	protected double getSpotRate(Date date, Frequency targetCmpdFreq) {
		double t = dcf.getYearFraction(_date, date);
		return getSpotRate(t, targetCmpdFreq);
	}

	/**
	 * <div id="ko">
	 * 기준일(이자율 커브의 측정일)에서 {@code t} 기간 후의 현물 이자율을 주어진
	 * compounding frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the spot rate at the date from the valuation date by a given 
	 * term using a given compounding frequency
	 * </div>.
	 * <div id="ko">
	 * 이자율 커브의 Compounding Frequency와 target Compounding Frequency가
	 * 다를 때 Compounding에 맞도록 이자율을 변환한다
	 * </div>
	 * <div id="en">
	 * If the compounding frequency of this interest curve and a given 
	 * compounding are different, the spot rate is adjusted in accordance with
	 * the given currency
	 * </div>.
	 *
	 * @param t					<div id="ko">이자율 계산 기간</div><div id="en">The given term</div>
	 * @param targetCmpdFreq	<div id="ko">이자 계산 주기</div><div id="en">The given compounding frequency</div>
	 * 
	 * @return					<div id="ko">현물 이자율</div><div id="en">The spot rate</div>
	 */
	public double getSpotRate(double t, Frequency targetCmpdFreq) {
		double r = curve.get(t);
		if (compoundFreq == targetCmpdFreq) {
			return r + spread;
		}

		if (targetCmpdFreq == Frequency.NONE) {
			if (compoundFreq == Frequency.CONTINUOUS) {
				r = (Math.exp(r * t) - 1.0) / t;
			} else { // Discrete Compounding
				double m = compoundFreq.getFrequencyInYear();
				r = (Math.pow(1.0 + r / m, m * t) - 1.0) / t;
			}
		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
			if (compoundFreq == Frequency.NONE) {
				r = Math.log(1.0 + r * t) / t;
			} else { // Discrete Compounding
				double m = compoundFreq.getFrequencyInYear();
				r = m * Math.log(1.0 + r / m);
			}
		} else { // Discrete Compounding
			if (compoundFreq == Frequency.NONE) {
				double mt = targetCmpdFreq.getFrequencyInYear();
				r = mt * (Math.pow(1 + r * t, 1.0 / (mt * t)) - 1.0);
			} else if (compoundFreq == Frequency.CONTINUOUS) {
				double mt = targetCmpdFreq.getFrequencyInYear();
				r = mt * (Math.exp(r / mt) - 1);
			} else { // Discrete Compounding
				double m = compoundFreq.getFrequencyInYear();
				double mt = targetCmpdFreq.getFrequencyInYear();
				r = mt * (Math.pow(1.0 + r / m, m / mt) - 1.0);
			}
		}
		return r + spread;
	}

	/*
	 * {@code date1}과 {@code date2} 사이 기간의 선도 금리를 계산한다.
	 *
	 * @param date1				시작일
	 * @param date2				종료일
	 * @param targetCmpdFreq	이자 계산 주기
	 * @return					계산된 선도 금리
	 */
	// Commented out by Jae-Heon Kim 2010.12.30
	/*public double getForwardRate(Date date1, Date date2,
			Frequency targetCmpdFreq) {

		double t1 = dcf.getYearFraction(_date, date1);
		double t2 = dcf.getYearFraction(_date, date2);
		return getForwardRate(t1, t2, targetCmpdFreq);
	}*/

	/**
	 * <div id="ko">
	 * {@code date1}과 {@code date2} 사이 기간의 선도 금리를 계산한다
	 * </div>
	 * <div id="en">
	 * Calculate the forward rate between a {@code date1} and a {@code date2}
	 * </div>.
	 *
	 * @param date1	
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The start date</div>
	 * @param date2	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The end date</div>
	 * 
	 * @return		
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The forward rate</div>
	 */
	public double getForwardRate(Date date1, Date date2) {
//		double t1 = dcf.getYearFraction(_date, date1);
//		double t2 = dcf.getYearFraction(_date, date2);
//		return getForwardRate(t1, t2, compoundFreq);
		return getForwardRate(date1, date2, compoundFreq);
	}
	
	/**
	 * <div id="ko">
	 * 입력된 {@code date}로부터 {@code tenor}만큼의 기간 사이의 선도 금리를 단리로 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the simple forward rate between a given {@code date} and the 
	 * date from a given {@code date} by a given {@code tenor}
	 * </div>.
	 *
	 * @param date				
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The given date</div>
	 * @param tenor				
	 * 		<div id="ko">tenor 기간 </div>
	 * 		<div id="en">The given term</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The simple forward rate</div>
	 */
	// Renamed from getForwardRate(Date date, double tenor) to avoid ambiguity.
	// Jae-Heon Kim 2011.01.10
	public double getSimpleForwardRate(Date date, double tenor) {
		return getForwardRate(date, tenor, Frequency.NONE);
	}

	/**
	 * <div id="ko">
	 * {@code t1}과 {@code t2}의 차이만큼의 기간 사이의 선도 금리를 단리로 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the simple forward rate between a {@code date1} and a {@code date2}
	 * </div>.
	 *
	 * @param date1	
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The starting term</div>
	 * @param date2	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The ending term</div>
	 * 
	 * @return		
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The simple forward rate</div>
	 */
	// Renamed from getForwardRate(double t1, double t2) to avoid ambiguity.
	// Jae-Heon Kim 2011.01.10
	public double getSimpleForwardRate(double date1, double date2) {
		return getForwardRate(date1, date2, Frequency.NONE);
	}
	
	/**
	 * <div id="ko">
	 * {@code t1}과 {@code t2}의 차이만큼의 기간 사이의 선도 금리를 단리로 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the simple forward rate between a {@code t1} and a {@code t2}
	 * </div>.
	 *
	 * @param t1	
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The start date</div>
	 * @param t2	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The end date</div>
	 * 
	 * @return		
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The simple forward rate</div>
	 */
	//2011.08.19., Youngseok Lee
	public double getSimpleForwardRate(Date t1, Date t2) {
		return getForwardRate(t1, t2, Frequency.NONE);
	}

	/**
	 * <div id="ko">
	 * 입력된 {@code date1}과 {@code date2}의 차이만큼의 기간 사이의 선도 금리를 
	 * 주어진 tenor를 이용하여 단리로 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the simple forward rate between a {@code date1} and a {@code date2}
	 * using a given tenor
	 * </div>.
	 *
	 * @param date1	
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The start date</div>
	 * @param date2	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The end date</div>
	 * @param tenor				
	 * 		<div id="ko">tenor 기간</div>
	 * 		<div id="en">The tenor</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The simple forward rate</div>
	 */
	//2011.08.19., Youngseok Lee
	public double getSimpleForwardRate(Date date1, Date date2, double tenor) {
		return getForwardRate(date1, date2, tenor, Frequency.NONE);
	}

	
	/**
	 * <div id="ko">
	 * 입력된 {@code date}로부터 {@code tenor}만큼의 기간 사이의 선도 금리를 이 커브의 
	 * compounding frequency를 이용해서 계산한다
	 * </div>
	 * <div id="en">
	 * Calculate the forward rate between a given {@code date} and the date from
	 * a given {@code date} by a given {@code tenor} using the compounding 
	 * frequency of this curve
	 * </div>.
	 *
	 * @param date				
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The given date</div>
	 * @param tenor				
	 * 		<div id="ko">tenor 기간 </div>
	 * 		<div id="en">The given term</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The forward rate</div>
	 */
	// Jae-Heon Kim 2011.01.10
	// This replaces getSimpleForwardRate(double t1, double t2)
	public double getForwardRate(Date date, double tenor) {
		return getForwardRate(date, tenor, compoundFreq);
	}

	/**
	 * <div id="ko">
	 * {@code t1}과 {@code t2}의 차이만큼의 기간 사이의 선도 금리를 이 커브의 compounding
	 * frequency를 이용해서 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the forward rate between a {@code date1} and a{@code date2}
	 * using the compounding frequency of this curve
	 * </div>.
	 *
	 * @param date1	
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The starting term</div>
	 * @param date2	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The ending term</div>
	 * @param tenor				
	 * 		<div id="ko">tenor 기간</div>
	 * 		<div id="en">The tenor</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The forward rate</div>
	 */
	// Jae-Heon Kim 2011.01.10
	// getSimpleForwardRate(double t1, double t2)
	public double getForwardRate(double t1, double t2) {
		return getForwardRate(t1, t2, compoundFreq);
	}
	
	/**
	 * <div id="ko">
	 * 입력된 {@code date1}과 {@code date2}의 차이만큼의 기간 사이의 선도 금리를 
	 * 주어진 compounding frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the forward rate between a {@code date1} and a {@code date2}
	 * using a given compounding frequency
	 * </div>.
	 *
	 * @param date1	
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The start date</div>
	 * @param date2	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The end date</div>
	 * @param targetCmpdFreq	
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">The given compounding frequency</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The forward rate</div>
	 */
	// 2011.08.19. Youngseok Lee,
	// 2011.12.20. Youngseok Lee,
	// Changed from public to protected
	protected double getForwardRate(Date date1, Date date2, 
			Frequency targetCmpdFreq) {
		double t1 = dcf.getYearFraction(_date, date1);
		double t2 = dcf.getYearFraction(_date, date2);
		
		return getForwardRate(t1, t2, targetCmpdFreq);
	}
	
	 
	/**
	 * <div id="ko">
	 * 입력된 {@code date1}과 {@code date2}의 차이만큼의 기간 사이의 선도 금리를 
	 * 주어진 tenor와 compounding frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the forward rate between a {@code date1} and a {@code date2}
	 * using a given tenor and compounding frequency
	 * </div>.
	 *
	 * @param date1	
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The start date</div>
	 * @param date2	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The end date</div>
	 * @param tenor				
	 * 		<div id="ko">tenor 기간</div>
	 * 		<div id="en">The tenor</div>
	 * @param targetCmpdFreq	
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">The given compounding frequency</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The forward rate</div>
	 */
	// 2011.08.19. Youngseok Lee,
	// 2011.12.20. Youngseok Lee,
	// Changed from public to protected
	protected double getForwardRate(Date date1, Date date2, 
			double tenor, Frequency targetCmpdFreq) {
		double t1 = dcf.getYearFraction(_date, date1);
		double t2 = dcf.getYearFraction(_date, date2);
		return getForwardRate(t1, t2, tenor, targetCmpdFreq);
	}

	/**
	 * <div id="ko">
	 * 입력된 {@code date}로부터 {@code tenor}만큼의 기간 사이의 선도 금리를
	 * 주어진 compounding frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en"
	 * Calculate the forward rate between a given {@code date} and the date from
	 * a given {@code date} by a given {@code tenor} using a given compounding 
	 * frequency
	 * </div>.
	 *
	 * @param date				
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The given date</div>
	 * @param tenor				
	 * 		<div id="ko">tenor 기간 </div>
	 * 		<div id="en">The given term</div>
	 * @param targetCmpdFreq	
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">The given compounding frequency</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The forward rate</div>
	 */
	// Jae-Heon Kim 2011.01.10
	// Changed from public to protected
	protected double getForwardRate(Date date, double tenor,
			Frequency targetCmpdFreq) {

		double t1 = dcf.getYearFraction(_date, date);
		double t2 = t1 + tenor; //use floatCurveTenor here instead of schedule difference
		return getForwardRate(t1, t2, targetCmpdFreq);
	}

	/**
	 * <div id="ko">
	 * {@code t1}과 {@code t2}의 차이만큼의 기간 사이의 선도 금리를
	 * 주어진 compounding frequency를 이용하여 계산한다
	 * </div>
	 * Calculates the forward rate between a {@code 51} and a {@code 52}
	 * using a given compounding frequency
	 *  </div>.
	 *
	 * @param t1				
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The starting term</div>
	 * @param 52	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The ending term</div>
	 * @param targetCmpdFreq	
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">The given compounding frequency</div>
	 * 
	 * @return					
	 * 		<div id="ko">계산된 선도 금리</div>
	 * 		<div id="en">The forward rate</div>
	 */
	// Jae-Heon Kim 2010.12.30
	// Changed from public to protected
	protected double getForwardRate(double t1, double t2,
			Frequency targetCmpdFreq) {
		double tenor = t2 - t1;
		return getForwardRate(t1, t2, tenor, targetCmpdFreq);
//		if (t1 == t2) {
//			return getSpotRate(t1, targetCmpdFreq);
//		}
//
//		double df1 = getDiscountFactor(t1);
//		double df2 = getDiscountFactor(t2);
//		double tenor = t2 - t1;
//
//		if (targetCmpdFreq == Frequency.NONE) { // Simple
//			return (df1 / df2 - 1.0) / tenor;
//		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
//			return Math.log(df1 / df2) / tenor;
//		}
//
//		double m = targetCmpdFreq.getFrequencyInYear();
//		return m * (Math.pow(df1 / df2, 1.0 / (m * tenor)) - 1.0);
	}

	/**
	 * <div id="ko">
	 * {@code t1}과 {@code t2}의 차이만큼의 기간 사이의 선도 금리를
	 * 주어진 tenor와 compounding frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the forward rate between a {@code 51} and a {@code 52}
	 * using a given tenor and compounding frequency
	 *  </div>.
	 *
	 * @param t1				
	 * 		<div id="ko">시작일</div>
	 * 		<div id="en">The starting term</div>
	 * @param 52	
	 * 		<div id="ko">종료일</div>
	 * 		<div id="en">The ending term</div>
	 * @param tenor				
	 * 		<div id="ko">tenor 기간</div>
	 * 		<div id="en">The tenor</div>
	 * @param targetCmpdFreq	
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">The given compounding frequency</div>
	 * 
	 * @return					
	 * 		<div id="ko"> 계산된 선도 금리 </div>
	 * 		<div id="en"> The forward rate from {@code t1} to {@code t2} </div>
	 */
	//2011.08.19., Youngseok Lee
	protected double getForwardRate(double t1, double t2,
			double tenor, Frequency targetCmpdFreq) {

		//20140304 Jihoon Lee, for instantaneous forward rate
		if (t1 == t2){
			t2 += 0.0001;
			tenor = 0.0001;
		}
//		if (t1 == t2) {
//			return getSpotRate(t1, targetCmpdFreq);
//		}

		double df1 = getDiscountFactor(t1);
		double df2 = getDiscountFactor(t2);

		if (targetCmpdFreq == Frequency.NONE) { // Simple
			return (df1 / df2 - 1.0) / tenor;
		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
			return Math.log(df1 / df2) / tenor;
		}

		double m = targetCmpdFreq.getFrequencyInYear();
		return m * (Math.pow(df1 / df2, 1.0 / (m * tenor)) - 1.0);
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 측정일부터 주어진 {@code date}까지의
	 * 기간 동안 적용할 할인 계수를 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the discount factor from the valuation date of this 
	 * {@code InterestRateCurve} object	to a given {@code date}
	 * </div>.
	 *
	 * @param date	
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * 
	 * @return		
	 * 		<div id="ko">할인 계수</div>
	 * 		<div id="en">The discount factor</div>
	 */
	// Discount factor 는 targetCmpdFreq 가 의미 없음
	public double getDiscountFactor(Date date) {
		double t = dcf.getYearFraction(_date, date);
		return getDiscountFactor(t);
	}

	/**
	 * <div id="ko">
	 * 대상일의 프리미엄 팩터를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the premium factor
	 * </div>.
	 *
	 * @param date	
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * @return		
	 * 		<div id="ko">프리미엄 팩터</div>
	 * 		<div id="en">The premium factor</div>
	 */
	public double getPremiumFactor(Date date) {
		return 1.0 / getDiscountFactor(date);
	}

	public double getDiscountFactor(double t1) {
		double r1 = getSpotRate(t1, compoundFreq);
		return getDiscountFactor(r1, t1, compoundFreq);
	}

	private double getDiscountFactor(double r, double t,
			Frequency targetCmpdFreq) {

		if (targetCmpdFreq == Frequency.NONE) { // Simple
			return 1.0 / (1.0 + r * t);
		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
			return Math.exp(-r * t);
		}
		// Discrete Compounding
		double m = targetCmpdFreq.getFrequencyInYear();
		return 1.0 / Math.pow(1.0 + r / m, m * t);
	}
	
	// 2010.09.14 Jaeon Woo for MtM
	public double getDiscountFactor(Date date1, Date date2) {
		double r = getForwardRate(date1, date2);
		double dt = dcf.getYearFraction(date1, date2);
		return getDiscountFactor(r, dt, compoundFreq);
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 측정일부터 주어진 {@code date}까지의
	 * 기간 동안 FRA에 대해서 적용할 할인 계수를 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the discount factor that is applied to FRA from the valuation 
	 * date of this {@code InterestRateCurve} object to a given {@code date} 
	 * </div>.
	 * 
	 * @param date	
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * 
	 * @return		
	 * 		<div id="ko">할인 계수</div>
	 * 		<div id="en">The discount factor</div>
	 */
	public double getFraDiscountFactor(Date date) {
		double t = dcf.getYearFraction(_date, date);
		double r = getSpotRate(t, compoundFreq);
		return getDiscountFactor(r, t, Frequency.NONE);
	}

	/**
	 * <div id="ko">
	 * 대상일의 선도 스왑 금리를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the forward swap rate issued at a given date and matured after a given time to maturity
	 * </div>.
	 *
	 * @param date			
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * @param swapMaturity	
	 * 		<div id="ko">스왑 만기</div>
	 * 		<div id="en">The swap maturity date</div>
	 * 
	 * @return				
	 * 		<div id="ko">대상일의 선도 스왑 금리</div>
	 * 		<div id="en">The forward swap rate</div>
	 */
	public double getForwardSwapRate(Date date, double swapMaturity) {
		return getForwardSwapRate(date, swapMaturity, compoundFreq);
	}

	// Swap rate(t) * annuity(t) = floating leg price(t)
	// -> Swap rate(t) = floating leg price(t) / annuity(t)

	/**
	 * <div id="ko">
	 * 대상일의 선도 스왑 금리를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the forward swap rate issued at a given date and matured after a given time to maturity
	 * </div>.
	 *
	 * @param date			
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * @param swapTenor	
	 * 		<div id="ko">스왑 테너</div>
	 * 		<div id="en">The swap maturity date</div>
	 * @param swapPaymentFreq	
	 * 		<div id="ko">스왑 지급 주기</div>
	 * 		<div id="en">The swap payment frequency</div>
	 * 
	 * @return					
	 * 		<div id="ko">대상일의 선도 스왑 금리</div>
	 * 		<div id="en">The forward swap rate</div>
	 */
	public double getForwardSwapRate(Date date, double swapTenor,
			Frequency swapPaymentFreq) {

		double t1 = dcf.getYearFraction(_date, date);
//		double t1 = date.diff(_date)/364.0;
		double swapEffectiveT = t1;
		double swapTerminationT = swapEffectiveT + swapTenor;
		int swapPaymentCnt = (int) (swapTenor * swapPaymentFreq
				.getFrequencyInYear());

		double annuityFactor = getAnnuityFactor(swapEffectiveT,
				swapPaymentFreq, swapPaymentCnt);
		double floatingLegPrice = getDiscountFactor(swapEffectiveT)
				- getDiscountFactor(swapTerminationT);
		double swapRate = floatingLegPrice / annuityFactor;
		return swapRate;
	}
	
	/**
	 * <div id="ko">
	 * 대상일의 선도 스왑 금리를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the forward swap rate issued at a given date and matured after a given time to maturity
	 * </div>.
	 *
	 * @param date						
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * @param swapMaturity	
	 * 		<div id="ko">스왑 만기</div>
	 * 		<div id="en">The swap maturity date</div>
	 * @param swapPaymentFreq	
	 * 		<div id="ko">스왑 지급 주기</div>
	 * 		<div id="en">The swap payment frequency</div>
	 * @param fixedNotionalPrincipal	
	 * 		<div id="ko">고정금리부 명목원금</div>
	 * 		<div id="en">The fixed notional principal</div>
	 * @param floatingNotionalPrincipal	
	 * 		<div id="ko">변동금리부 명목원금 </div>
	 * 		<div id="en">The floating notional principal</div>
	 * 
	 * @return 
	 * 		<div id="ko">대상일의 선도 스왑 금리</div>
	 * 		<div id="en">The forward swap rate</div>
	 */
	// 2012.02.06. Youngseok Lee,
	public double getForwardSwapRate(Date date, double swapMaturity,
			Frequency swapPaymentFreq, Money fixedNotionalPrincipal, 
			Money floatingNotionalPrincipal) {

		double t1 = dcf.getYearFraction(_date, date);

		double swapEffectiveT = t1;
		double swapTerminationT = swapEffectiveT + swapMaturity;
		int swapPaymentCnt = (int) (swapMaturity * swapPaymentFreq
				.getFrequencyInYear());

		double annuityFactor = getAnnuityFactor(swapEffectiveT,
				swapPaymentFreq, swapPaymentCnt)
				* fixedNotionalPrincipal.getAmount();
		double floatingLegPrice = (getDiscountFactor(swapEffectiveT) - getDiscountFactor(swapTerminationT))
				* floatingNotionalPrincipal.getAmount();
		double swapRate = floatingLegPrice / annuityFactor;
		return swapRate;
	}


	private double getAnnuityFactor(double effectiveT, Frequency paymentFreq,
			int paymentCnt) {
		
		double tenor = 1.0 / paymentFreq.getFrequencyInYear();
		double paymentT = effectiveT;
		double annuityFactor = 0;
		for (int i = 0; i < paymentCnt; ++i) {
			paymentT += tenor;
			annuityFactor += tenor * getDiscountFactor(paymentT);
		}
		return annuityFactor;
	}

	/**
	 * <div id="ko">
	 * 대상일의 할인 계수의 합을 반환한다
	 * </div>
	 * <div id="en">
	 * Calculates the sum of discount factors from a given date to a given time to maturity
	 * </div>.
	 *
	 * @param date						
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * @param swapMaturity	
	 * 		<div id="ko">스왑 만기</div>
	 * 		<div id="en">The swap maturity date</div>
	 * 
	 * @return				
	 * 		<div id="ko">대상일의 할인 계수의 합</div>
	 * 		<div id="en">The sum of discount factors</div>
	 */
	public double getSumSwapDiscountFactorWithCoupon(Date date,
			double swapMaturity) {
		return getSumSwapDiscountFactorWithCoupon(date, swapMaturity,
				compoundFreq);
	}

	/**
	 * <div id="ko">
	 * 대상일의 할인 계수의 합을 반환한다
	 * </div>
	 * <div id="en">
	 * Calculates the sum of discount factors from a given date to a given time to maturity
	 * </div>.
	 *
	 * @param date						
	 * 		<div id="ko">대상일</div>
	 * 		<div id="en">The given date</div>
	 * @param swapMaturity	
	 * 		<div id="ko">스왑 만기</div>
	 * 		<div id="en">The swap maturity date</div>
	 * @param swapPaymentFreq	
	 * 		<div id="ko">스왑 지급 주기</div>
	 * 		<div id="en">The swap Payment frequency</div>
	 * 
	 * @return					
	 * 		<div id="ko">대상일의 할인 계수의 합</div>
	 * 		<div id="en">The sum of discount factors</div>
	 */
	public double getSumSwapDiscountFactorWithCoupon(Date date,
			double swapMaturity, Frequency swapPaymentFreq) {
		int m = (int) swapPaymentFreq.getFrequencyInYear();
		double n = swapMaturity;
		int index = (int) (m * n);
		double sum = 0;
		for (int i = 0; i < index; ++i) {
			date = date.plusMonths(swapPaymentFreq.toMonthUnit());
			sum += getDiscountFactor(date);
		}
		return sum / (double) m;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 현물 이자율을 {@code parallelShift}만큼 
	 * 증가시킨 이자율을 가지는 새 {@code InterestRateCurve} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code InterestRateCurve} object by adding a given {@code parallelShift} 
	 * to the spot rate of this {@code InterestRateCurve} object
	 * </div>.
	 *
	 * @param parallelShift	
	 * 		<div id="ko">현물 이자율의 증가분</div>
	 * 		<div id="en">The parallel shift</div>
	 * 
	 * @return				
	 * 		<div id="ko">현물 이자율을 증가시킨 새 {@code InterestRateCurve} 객체</div>
	 * 		<div id="en">A New {@code InterestRateCurve} object</div>
	 */
	public InterestRateCurve copy(double parallelShift) {
		InterestRate[] shiftedSpotRates = new InterestRate[spotRates.length];
		for (int i = 0; i < spotRates.length; ++i) {
			shiftedSpotRates[i] = new InterestRate(spotRates[i].getVertex(),
					spotRates[i].getRate() + parallelShift, spotRates[i]
							.getFactorCode());
		}
		InterestRateCurve irc = new InterestRateCurve(_date, shiftedSpotRates,
				interpolation, compoundFreq, dcf);
		irc.setSpread(this.spread);
		return irc;
	}
	
	public InterestRateCurve copy(int index, double perturbation) {
		InterestRate[] shiftedSpotRates = new InterestRate[spotRates.length];
		
		for (int i = 0; i < spotRates.length; ++i) {
			if (index == i){
				shiftedSpotRates[i] = new InterestRate(spotRates[i].getVertex(),
						spotRates[i].getRate() + perturbation, spotRates[i]
								.getFactorCode());
			} else {
				shiftedSpotRates[i] = new InterestRate(spotRates[i].getVertex(),
						spotRates[i].getRate(), spotRates[i]
								.getFactorCode());
			}			
		}
		
		
		InterestRateCurve irc = new InterestRateCurve(_date, shiftedSpotRates,
				interpolation, compoundFreq, dcf);
		irc.setSpread(this.spread);
		return irc;
	}
	
	/**
	 * <div id="ko">이 {@code InterestRateCurve} 객체의 현물 이자율 배열을 반환한다</div>
	 * <div id="en">Returns an array of the spot rates of this {@code InterestRateCurve} object </div>.
	 *
	 * @return	
	 * 		<div id="ko">현물 이자율 배열</div>
	 * 		<div id="en">An array of the spot rates</div>
	 */
	// Jae-Heon Kim on 2009.01.30
	public InterestRate[] getSpotRates() {
		return spotRates;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 문자열 표현을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the string representation of an array of the spot rates of this
	 * {@code InterestRateCurve} object
	 * </div>.
	 * 
	 * @return	
	 * 		<div id="ko">현물 이자율 배열의 문자열 표현 </div>
	 * 		<div id="en">The string representation of an array the spot rates</div>
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < spotRates.length; ++i) {
			buf.append(spotRates[i]);
			buf.append("\r\n");
		}
		if (spread != 0) {
			buf.append("[s=" + spread + "]\r\n");
		}
		return buf.toString();
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 Day Count Fraction을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the day count fraction of this {@code InterestRateCurve} object
	 * </div>.
	 *
	 * @return	
	 * 		<div id="ko">Day Count Fraction</div>
	 * 		<div id="en">The day count fraction</div>
	 */
	// Jae-Heon Kim on 2009.04.24
	public DayCountFraction getDayCountFraction() {
		return dcf;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 이자 계산 주기를 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a compounding frequency of this {@code InterestRateCurve} object
	 * </div>.
	 * 
	 * @param compoundFreq	
	 * 		<div id="ko">이자 계산 주기</div>
	 * 		<div id="en">A compounding frequency</div>
	 */
	public void setCompoundingFrequency(Frequency compoundFreq) {
		this.compoundFreq = compoundFreq;

	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 스프레드를 주어진 값으로 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a spread of this {@code InterestRateCurve} object
	 * </div>.
	 * 
	 * @param spread 
	 * 		<div id="ko">스프레드</div>
	 * 		<div id="en">A spread</div>
	 */
	public void setSpread(double spread) {
		this.spread = spread;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRateCurve} 객체의 스프레드를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the spread of this {@code InterestRateCurve} object
	 * </div>.
	 * 
	 * @return 
	 * 		<div id="ko">스프레드</div>
	 * 		<div id="en">The spread</div>
	 */
	public double getSpread() {
		return spread;
	}

	// 2010.03.22 Jae-Heon Kim
	public void setCms(boolean value) {
		_isCms = value;
	}

	public boolean isCms() {
		return _isCms;
	}
	
	// 2012.01.20. Youngseok Lee,
	static public InterestRateCurve[] getInterestRateCurves(Date[] dates,
			Vertex[] vertices, double[][] spotRates, Frequency compoundFreq,
			DayCountFraction dcf) {
		int length = dates.length;
		int size = vertices.length;
		InterestRateCurve[] interestRateCurves = new InterestRateCurve[length];
		for (int i = 0; i < length; i++) {
			InterestRate[] interestRate = new InterestRate[size];
			for (int j = 0; j < size; j++) {
				interestRate[j] = new InterestRate(vertices[j], spotRates[i][j]);
			}
			interestRateCurves[i] = new InterestRateCurve(dates[i],
					interestRate, compoundFreq, dcf);
		}
		return interestRateCurves;
	}
	
	/**
	 * 입력받은 커브를 interpolation을 이용해 주어진 객체의 vertex별 rate에 더해준다.
	 * 
	 * @param curve
	 * 
	 * @return InterestRateCurve
	 */
	// 2013.04.30. Youngseok Lee,
	public InterestRateCurve add(InterestRateCurve curve) {
		InterestRate[] _spotRates = new InterestRate[spotRates.length];
		for (int i = 0; i < spotRates.length; i++) {
			Vertex vertex = spotRates[i].getVertex();
			double rate = spotRates[i].getRate() + curve.getSpotRate(vertex.getVertex());
			_spotRates[i] = new InterestRate(vertex, rate);
		}
		return new InterestRateCurve(_date, spotRates, compoundFreq, dcf);
	}
	
	
	/**
	 * 입력받은 커브를 interpolation을 이용해 주어진 객체의 vertex별 rate에 빼준다.
	 * 
	 * @param curve
	 * 
	 * @return InterestRateCurve
	 */
	// 2013.04.30. Youngseok Lee,
	public InterestRateCurve subtract(InterestRateCurve curve) {
		InterestRate[] _spotRates = new InterestRate[spotRates.length];
		for (int i = 0; i < spotRates.length; i++) {
			Vertex vertex = spotRates[i].getVertex();
			double rate = spotRates[i].getRate() - curve.getSpotRate(vertex.getVertex());
			_spotRates[i] = new InterestRate(vertex, rate);
		}
		return new InterestRateCurve(_date, _spotRates, compoundFreq, dcf);
	}
	
	/**
	 * 입력받은 커브와 객체를 비교하여 각각의 spot rate 별로 vertex와 rate이 같으면 true를
	 * 그렇지 않으면 false를 반환한다.
	 * 
	 * @param curve
	 * 
	 * @return boolean
	 */
	// 2013.11.22. Youngseok Lee,
	public boolean isEqual(InterestRateCurve curve) {
		InterestRate[] curveSpotRates = curve.getSpotRates();
		if(spotRates.length != curveSpotRates.length) {
			return false;
		}
		for (int i = 0; i < spotRates.length; i++) {
			if(spotRates[i].compareTo(curveSpotRates[i]) != 0) {
				return false;
			}
			if(spotRates[i].getRate() != curveSpotRates[i].getRate()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean IsSameCurveArray(InterestRateCurve[] irCurve1, InterestRateCurve[] irCurve2){
		for (int i = 0; i < irCurve1.length; i++) {
			if (!irCurve1[i].isEqual(irCurve2[i])) {
				//different case
				return false;
			}
		}
		return true;
	}
	
	

}
