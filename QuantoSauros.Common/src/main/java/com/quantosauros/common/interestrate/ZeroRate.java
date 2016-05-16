package com.quantosauros.common.interestrate;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.YTMRateType;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;

public class ZeroRate extends AbstractRate {

	
	
	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param vertex	<div id="ko">이자율 {@code Vertex}, 즉 이자율이 적용되는 시점</div><div id="en">The vertex at which a corresponding interest rate is applied</div>
	 * @param rate		<div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 */
	public ZeroRate(Vertex vertex, double rate) {
		super(vertex, rate, YTMRateType.ZERO);		
	}

	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param vertex		<div id="ko">이자율 {@code Vertex}, 즉 이자율이 적용되는 시점</div><div id="en">The vertex at which a corresponding interest rate is applied</div>
	 * @param rate			<div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 * @param factorCode	<div id="ko">팩터 코드</div><div id="en">The corresponding factor code</div>
	 */
	public ZeroRate(Vertex vertex, double rate, YTMRateType rateType) {
		super(vertex, rate, rateType);		
	}

	public ZeroRate(double tenor, double rate) {
		super(Vertex.valueOf("NONE"), rate, YTMRateType.ZERO);		
		this.tenor = tenor;
	}

	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param vertex		<div id="ko">이자율 {@code Vertex}, 즉 이자율이 적용되는 시점</div><div id="en">The vertex at which a corresponding interest rate is applied</div>
	 * @param rate			<div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 * @param factorCode	<div id="ko">팩터 코드</div><div id="en">The corresponding factor code</div>
	 */
	public ZeroRate(double tenor, double rate, YTMRateType rateType) {
		super(Vertex.valueOf("NONE"), rate, rateType);	
		this.tenor = tenor;
	}
	
	/**
	 *
	 * 
	 * 
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 이자율이 적용되는 기간을 연 단위로
	 * 환산할 때 사용할 Day Count Fraction을 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a day count fraction 
	 * </div>.
	 * <div id="ko">
	 * </div>
	 * <div id="en">
	 * It is used when a term or a period is conveted to an annualized term or period. 
	 * </div>
	 *
	 * @return 
	 * @param spotRates	<div id="ko">현물 이자율</div><div id="en">The spot rate</div>
	 * @param dcf		<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	public static void setDayCountFraction(ZeroRate [] spotRates,
			DayCountFraction dcf) {
		if (spotRates == null) {
			return;
		}
		for (int i = 0; i < spotRates.length; ++i) {
			spotRates[i].dcf = dcf;
		}
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 이자율을 설정한다
	 * </div>
	 * <div id="en">
	 * Sets an interest rate of this {@code InterestRate} object.
	 * </div>.
	 *
	 * @param r	<div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 */
	public void setRate(double r) {
		rate = r;
	}

	/**
	 * <div id="ko">
	 * 입력된 기간({@code from}부터 {@code to}까지)에 대해 적용할 할인 계수를
	 * 이 {@code InterestRate} 객체의 Day Count Fraction과 주어진 compounding
	 * frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the discount factor from ({@code from} to {@code to}) using 
	 * the day count fraction of this {@code InterestRate} object and a given 
	 * compounding frequency
	 * </div>.
	 *
	 * @param from				<div id="ko">시작일</div><div id="en">The start date</div>
	 * @param to				<div id="ko">종료일</div><div id="en">The end date</div>
	 * @param targetCmpdFreq	<div id="ko">주기</div><div id="en">The compounding frequency</div>
	 * @return 					<div id="ko">할인 계수</div><div id="en">The discount factor</div>
	 */
	public double getDiscountFactor(Date from, Date to, Frequency targetCmpdFreq) {
		double t = dcf.getYearFraction(from, to);
		return getDiscountFactor(t, targetCmpdFreq);
	}

	/**
	 * <div id="ko">
	 * 입력된 연 단위 기간{@code t}에 대해 적용할 할인 계수를
	 * 이 {@code InterestRate} 객체의 Day Count Fraction과 주어진 compounding
	 * frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the discount factor during a annualized term {@code t}using 
	 * the day count fraction of this {@code InterestRate} object and a given 
	 * compounding frequency
	 * </div>.
	 *
	 * @param t					<div id="ko">기간</div><div id="en">The given term</div>
	 * @param targetCmpdFreq	<div id="ko">주기</div><div id="en">The compounding frequency</div>
	 * @return 					<div id="ko">할인 계수</div><div id="en">The discount factor</div>
	 */
    public double getDiscountFactor(double t, Frequency targetCmpdFreq) {

        if (targetCmpdFreq == Frequency.NONE) { // Simple
            return 1.0 / (1.0 + rate * t);
        }
        else if (targetCmpdFreq == Frequency.CONTINUOUS) {
            return Math.exp(-rate * t);
        }
        // Discrete Compounding
        double m = targetCmpdFreq.getFrequencyInYear();
        return 1.0 / Math.pow(1.0 + rate / m, m * t);
    }

	/**
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 rate에 입력된 금리를 더한
	 * 새 {@code InterestRate} 객체를 생성하여 반환한다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code InterestRate} object by adding a spread to the
	 * rate of this {@code InterestRate} object
	 * </div>.
	 *
	 * @param rate	<div id="ko">가산할 금리</div><div id="en">The spread</div>
	 * @return 		<div id="ko">입력된 금리를 더한 새 {@code InterestRate} 객체</div><div id="en">A new {@code InterestRate} object</div>
	 */
    public ZeroRate plusRate(double rate) {
    	ZeroRate ir = new ZeroRate(this.getVertex(), this.getRate() + rate, this.rateType);
    	return ir;
    }

	/**
	 * <div id="ko">
	 * 입력된 기간({@code from}부터 {@code to}까지)에 대해 적용할 프리미엄 팩터를
	 * 이 {@code InterestRate} 객체의 Day Count Fraction과 주어진 compounding
	 * frequency를 이용하여 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the premium factor from ({@code from} to {@code to}) using 
	 * the day count fraction of this {@code InterestRate} object and a given 
	 * compounding frequency
	 * </div>.
	 *
	 * @param from				<div id="ko">시작일</div><div id="en">The start date</div>
	 * @param to				<div id="ko">종료일</div><div id="en">The end date</div>
	 * @param targetCmpdFreq	<div id="ko">주기</div><div id="en">The compounding frequency</div>
	 * @return 					<div id="ko">프리미엄 팩터</div><div id="en">The premium factor</div>
	 */
	//2011.08.17. Younsgeok Lee,
	public double getPremiumFactor(Date from, Date to, Frequency ytmFreq) {
		return 1.0 / getDiscountFactor(from, to, ytmFreq);
	}
	
	public double getX() {
		if (vertex.getCode() == "NONE"){
			return getTenor();
		} else {
			return getVertex().getVertex(dcf);
		}		
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (vertex.getCode() == "NONE"){
			buf.append("[" + tenor + "," + rate + ", " + rateType + "] ");
		} else {
			buf.append("[" + vertex + "," + rate + ", " + rateType + "] ");
		}
		
		
		return buf.toString();
	}
	
}
