package com.quantosauros.common.interestrate;

import java.io.Serializable;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.math.interpolation.Coordinates;

/**
 * <div id="ko">
 * {@code InterestRate}는 이자율(금리)를 나타내는 클래스로서 {@code Coordinates} 
 * 클래스를 상속받는다
 * </div>
 * <div id="en">
 * {@code InterestRate} implementing {@code Coordinates} represents an interest rate
 * </div>.
 * <div id="ko">
 * <P>
 * 좌표의 X축은 {@code Vertex}, 즉 해당 이자율이 적용되는 시점을 표시하며,
 * 좌표의 Y축은 이자율을 나타낸다.
 *<p>
 * Note: This class has a natural ordering that is inconsistent with equals.
 * </div> 
 * <div id="en">
 * <P>
 * X-axis represents a {@code Vertex} objective at which a corresponding interest rate is applied,
 * Y-axis represents an interest rate.
 *<p>
 * </div>
 *
 * @author Oh, Sung Hoon
 * @author Jae-Heon Kim
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/

public class InterestRate implements Serializable, Coordinates {

	private Vertex vertex;

	private double rate;

	private DayCountFraction dcf;

	// Jae-Heon Kim on 2009.01.30
	private String factorCode;

	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param vertex	<div id="ko">이자율 {@code Vertex}, 즉 이자율이 적용되는 시점</div><div id="en">The vertex at which a corresponding interest rate is applied</div>
	 * @param rate		<div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 */
	public InterestRate(Vertex vertex, double rate) {
		this.vertex = vertex;
		this.rate = rate;
		this.dcf = DayCountFraction.DEFAULT;
	}
	
	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param vertex	<div id="ko">vertex code</div><div id="en">The vertex code at which a corresponding interest rate is applied</div>
	 * @param rate		<div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 */

	// Jae-Heon Kim on 2010.02.11
	public InterestRate(String vertexCode, double rate) {
		this.vertex = Vertex.valueOf(vertexCode);
		this.rate = rate;
		this.dcf = DayCountFraction.DEFAULT;
	}

	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param vertex		<div id="ko">이자율 {@code Vertex}, 즉 이자율이 적용되는 시점</div><div id="en">The vertex at which a corresponding interest rate is applied</div>
	 * @param rate			<div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 * @param factorCode	<div id="ko">팩터 코드</div><div id="en">The corresponding factor code</div>
	 */
	// Jae-Heon Kim on 2009.01.30
	public InterestRate(Vertex vertex, double rate, String factorCode) {
		this(vertex, rate);
		this.factorCode = factorCode;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 {@code Vertex}를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the {@code Vertex} of this {@code InterestRate} object
	 * </div>.
	 *
	 * @return <div id="ko">Vertex</div><div id="en">The vetex</div>
	 */
	public Vertex getVertex() {
		return vertex;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 이자율을 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the corresponding interest rate of this {@code InterestRate} object
	 * </div>.
	 *
	 * @return <div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 {@code Vertex}가 나타내는 기간을
	 * 연 단위로 환산한다
	 * </div>
	 * <div id="en">
	 * Returns the annualized term this {@code Vertex} indicates
	 * </div>.
	 *
	 * @return <div id="ko">이 이자율이 적용되는 연 단위 기간</div><div id="en">The annualized term the {@code Vertex} indicates</div>
	 */
	public double getX() {
		return getVertex().getVertex(dcf);
	}

	/**
	 * <div id="ko">
	 * 이자율을 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the corresponding interest rate
	 * </div>.
	 *
	 * @return <div id="ko">이자율</div><div id="en">The corresponding interest rate</div>
	 */
	public double getY() {
		return getRate();
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
	public static void setDayCountFraction(InterestRate [] spotRates,
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
	 * 이 {@code InterestRate} 객체의 문자열 표현을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the string representation of this {@code InterestRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">'[기간, 이자율]' 형태의 문자열</div>
	 * 		   <div id="en">The string representation of the {@code InterestRate} object:'[vertex, rate]'</div>
	 */
	public String toString() {
		return "[" + vertex + "," + rate + ", " + factorCode + "] ";
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 팩터 코드를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the factor code of this {@code InterestRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">팩터 코드</div>
	 * 		   <div id="en">The factor code</div>
	 */
	// Jae-Heon Kim on 2009.01.30
	public String getFactorCode() {
		return factorCode;
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
	// Jae-Heon Kim on 2009.01.30
	public void setRate(double r) {
		rate = r;
	}

	/**
	 * <div id="ko">
	 * 이 {@code InterestRate} 객체의 적용 기간을 주어진 {@code Coordinate}
	 * 객체의 X 값인 기간과 비교한다
	 * </div>
	 * <div id="en">
     * Compares the X value of a given {@code Coordinate} with the X value of this {@code InterestRate} object
	 * </div>.
	 * <div id="ko">
	 * <p>
	 * 이 {@code InterestRate} 객체의 기간이 입력된 좌표의 X 값보다
	 * 크면 1, 작으면 -1, 그리고 두 값이 같으면 0을 반환한다.
	 * </div>
	 * <div id="en">
     * Returns 1 if the X value of this {@code InterestRate} object is larger 
     * than the X value of a given coordinate, -1 otherwise.
	 * </div>
	 * 
	 * @param o
	 * 			  <div id="ko">비교할 {@code Coordinates} 객체</div>
	 * 			  <div id="en">The {@code Coordinates} object to be compared</div>
	 * 
	 * @return <div id="ko">비교 결과</div>
	 * 		   <div id="en">The result</div>
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
	 * 이 {@code InterestRate} 객체의 Day Count Fraction을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the day count fraction of this {@code InterestRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">Day Count Fraction</div>
	 * 		   <div id="en">The day count fraction</div>
	 */
	// 2009.09.18 Yang Jaechul.
	public DayCountFraction getDayCountFraction() {
		return dcf;
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
	// 2009.09.18 Yang Jaechul. DiscountFactor Function
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
	// 2009.09.18 Yang Jaechul. DiscountFactor Function
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
    public InterestRate plusRate(double rate) {
    	InterestRate ir = new InterestRate(this.getVertex(), this.getRate() + rate, this.factorCode);
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


}
