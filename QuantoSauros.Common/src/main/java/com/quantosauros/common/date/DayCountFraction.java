package com.quantosauros.common.date;

import java.io.Serializable;

import com.quantosauros.common.date.daycounter.DayCounter;
import com.quantosauros.common.date.daycounter.DayCounterFactory;


/**
 * <div id="ko">
 * 날짜 계산 관습을 나타내는 클래스
 * </div>
 * <div id="en">
 * Class for day count convention
 * </div>.
 * <div id="ko">
 * Day Count Convention, Day Count Fraction 
 * 등으로 부른다. Day Count Fraction is an ISDA term. The equivalent AFB (Association Francaise
 * de Banques) term is Calculation Basis. 
 * </div>
 * <div id="en">
 * Day Count Fraction is an ISDA term. The equivalent AFB (Association Francaise
 * de Banques) term is Calculation Basis. 
 * </div> 
 *
 * @author Oh, Sung Hoon
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class DayCountFraction implements Serializable {

	/**
	 * <div id="ko">
 	 * Actual/365 유형의 {@code DayCountFraction} 객체(code: 1) 
 	 * </div>
 	 * <div id="en">
 	 * A {@code DayCountFraction} object with Actual/365 convention(code: 1) 
 	 * </div>.
	 */
	public static final DayCountFraction ACTUAL_365 = new DayCountFraction("1", "Actual/365");

	/**
	 * <div id="ko">
	 * Actual/360 유형의 {@code DayCountFraction} 객체(code: 2)
 	 * </div>
 	 * <div id="en">
 	 * A {@code DayCountFraction} object with Actual/360 convention(code: 1) 
 	 * </div>.
	 */
	public static final DayCountFraction ACTUAL_360 = new DayCountFraction("2", "Actual/360");

	/**
	 * <div id="ko">
	 * Actual/Actual 유형의 {@code DayCountFraction} 객체(code: 3)
 	 * </div>
 	 * <div id="en">
 	 * A {@code DayCountFraction} object with Actual/Actual convention(code: 3) 
 	 * </div>.
	 */
	public static final DayCountFraction ACTUAL_ACTUAL = new DayCountFraction("3", "Actual/Actual");

	/**
	 * <div id="ko">
	 * 30/360 유형의 {@code DayCountFraction} 객체(code: 4)
 	 * </div>
 	 * <div id="en">
 	 * A {@code DayCountFraction} object with 30/360 convention(code: 4) 
 	 * </div>.
	 */
	public static final DayCountFraction D30_360 = new DayCountFraction("4", "30/360");

	/**
	 * <div id="ko">
	 * 30E/360 유형의 {@code DayCountFraction} 객체(code: 5)
 	 * </div>
 	 * <div id="en">
 	 * A {@code DayCountFraction} object with 30E/360 convention(code: 4) 
 	 * </div>.
	 */
	public static final DayCountFraction D30E_360 = new DayCountFraction("5", "30E/360");

	/**
	 * <div id="ko">
	 * DEFAULT로 사용할 {@code DayCountFraction} 객체를 지정한다(현재는 Actual/365)
 	 * </div>
 	 * <div id="en">
 	 * Set a default {@code DayCountFraction} object(Actual is a default dcf)
 	 * </div>.
	 */
	public static final DayCountFraction DEFAULT = ACTUAL_365;

	// Jae-Heon Kim, 2009.09.01
	private DayCounter _delegate;

	private String _code;

	private String _name;

	private Date _date;
	
	private DayCountFraction(String code, String name) {
		_code = code;
		_name = name;
		_delegate = DayCounterFactory.getInstance(code);
	}

	/**
	 * <div id="ko">
	 * 주어진 코드 값에 해당하는 {@code DayCountFraction} 객체를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns a {@code DayCountFraction} object corresponding to a given code
	 * </div>.
	 * 
	 * @param code
	 * 			  <div id="ko">Day Count Fraction 유형 코드</div>
	 * 			  <div id="en">The day count fraction type code</div>
	 * 
	 * @return <div id="ko">해당 {@code DayCountFraction} 객체</div>
	 * 		   <div id="en">A corresponding {@code DayCountFraction} object</div>
	 */
	// Jae-Heon Kim, 2009.09.08
	public static DayCountFraction valueOf(int code) {
		String s = "" + code;
		return valueOf(s);
	}

	/**
	 * <div id="ko">
	 * 주어진 코드 값에 해당하는 {@code DayCountFraction} 객체를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns a {@code DayCountFraction} object corresponding to a given code
	 * </div>.
	 * 
	 * @param code
	 * 			  <div id="ko">Day Count Fraction 유형 코드</div>
	 * 			  <div id="en">The day count fraction type code</div>
	 * 
	 * @return <div id="ko">해당 {@code DayCountFraction} 객체</div>
	 * 		   <div id="en">A corresponding {@code DayCountFraction} object</div>
	 */

	public static DayCountFraction valueOf(String code) {
		if (ACTUAL_365.getCode().equals(code)) {
			return ACTUAL_365;
		}
		if (ACTUAL_360.getCode().equals(code)) {
			return ACTUAL_360;
		}
		if (ACTUAL_ACTUAL.getCode().equals(code)) {
			return ACTUAL_ACTUAL;
		}
		if (D30_360.getCode().equals(code)) {
			return D30_360;
		}
		if (D30E_360.getCode().equals(code)) {
			return D30E_360;
		}

		return null;
	}

	/**
	 * <div id="ko">
	 * 이 {@code DayCountFraction} 객체의 코드를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the type code of this {@code DayCountFraction} object
	 * </div>.
	 * 
	 * @return <div id="ko">DayCountFraction 유형의 code</div>
	 * 		   <div id="en">The day count fraction type code</div>
	 */
	public String getCode() {
		return _code;
	}

	/**
	 * <div id="ko">
	 * 이 {@code DayCountFraction} 객체의 이름을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the type name of this {@code DayCountFraction} object
	 * </div>.
	 * 
	 * @return <div id="ko">DayCountFraction 유형의 이름</div>
	 * 		   <div id="en">The day count fraction type name</div>
	 */
	public String toString() {
		return _name;
	}

	/**
	 * <div id="ko">
	 * 이 {@code DayCountFraction} 객체의 이름을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the type name of this {@code DayCountFraction} object
	 * </div>.
	 * 
	 * @return <div id="ko">DayCountFraction 유형의 이름</div>
	 * 		   <div id="en">The day count fraction type name</div>
	 */
	// Jae-Heon Kim, 2009.09.07
	public String getName() {
		return _name;
	}

	/**
	 * <div id="ko">
	 * 1년의 날짜 수를 이 {@code DayCountFraction} 객체가 지칭하는 방식으로 계산한다
	 * </div>
	 * <div id="en">
	 * Returns the number of days of the year using the day count fraction 
	 * this object indicates
	 * </div>.
	 * 
	 * @return <div id="ko">1년간의 날짜 수</div>
	 * 		   <div id="en">The number of days of the year</div>
	 */
	public double getDaysOfYear() {
		if (this == DayCountFraction.ACTUAL_ACTUAL && _date == null) {
//			throw new IllegalArgumentException(
//					"Date instance variable is null and "
//					+ name + " needs date object");
			System.err.println(
					"[WARN] DayCountFraction - Date object variable is null and ["
					+ _name + "] needs date object.");
			return getDaysOfYear(new Date());// default Date 를 사용한다.
		}
		
		return getDaysOfYear(_date);
	}
	
	public void setDate(Date date) {
		_date = date;
	}

	/**
	 * <div id="ko">
	 * 대상일이 속한 연도의 1년간 날짜 수를 이 {@code DayCountFraction} 객체가 지칭하는
	 * 방식으로 계산한다
	 * </div>
	 * <div id="en">
	 * Returns the number of days of the year in which the given date lies using
	 * the day count fraction this object indicates
	 * </div>.
	 * 
	 * @param date
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The date to be used</div>
	 * 
	 * @return <div id="ko">1년간의 날짜 수</div>
	 * 		   <div id="en">The number of days of the year</div>
	 */
	public double getDaysOfYear(Date date) {
		double daysOfYear = 0;
		if (this == DayCountFraction.ACTUAL_365) {
			daysOfYear = 365.0;
		}
		else if (this == DayCountFraction.ACTUAL_360) {
			daysOfYear = 360.0;
		}
		else if (this == DayCountFraction.ACTUAL_ACTUAL) {
			daysOfYear = date.isLeapYear() ? 366.0 : 365.0;
		}
		else if (this == DayCountFraction.D30_360) {
			daysOfYear = 360.0;
		}
		else if (this == DayCountFraction.D30E_360) {
			daysOfYear = 360.0;
		}
		return daysOfYear;
	}

	/**
	 * <div id="ko">
	 * 이 {@code DayCountFraction} 객체가 지칭하는 방식에 따라 주어진 두 날짜 사이의 
	 * Year Fraction을 계산한다
	 * </div>
	 * <div id="en">
	 * Returns The year fraction between two dates using the day count 
	 * fraction this object indicates
	 * </div>.
	 * 
	 * @param startDate
	 * 			  <div id="ko">시작일</div>
	 * 			  <div id="en">The start date</div>
	 * @param endDate
	 * 			  <div id="ko">종료일</div>
	 * 			  <div id="en">The end date</div>
	 * 
	 * @return <div id="ko">두 날짜 사이의 Year Fraction</div>
	 * 		   <div id="en">The year fraction between two dates</div>
	 */
	public double getYearFraction(Date startDate, Date endDate) {
		// Jae-Heon Kim, 2009.09.01
		if (this == DayCountFraction.ACTUAL_ACTUAL) {
			return _delegate.getYearFraction(startDate, endDate);
		}

		double daysOfFraction = 0;
		double daysOfYear = getDaysOfYear(startDate);
		if (this == DayCountFraction.ACTUAL_365) {
			daysOfFraction = startDate.getDays(endDate);
		}
		else if (this == DayCountFraction.ACTUAL_360) {
			daysOfFraction = startDate.getDays(endDate);
		}
		/*else if (this == DayCountFraction.ACTUAL_ACTUAL) {
			daysOfFraction = startDate.getDays(endDate);
		}*/
		else if (this == DayCountFraction.D30_360) {
			daysOfFraction = startDate.getDays360(endDate, false);
		}
		else if (this == DayCountFraction.D30E_360) {
			daysOfFraction = startDate.getDays360(endDate, true);
		}
		return daysOfFraction / daysOfYear;
	}

	/**
	 * <div id="ko">
	 * 주어진 두 날짜 사이의 Year Fraction을 입력된 {@code dcf} 객체가 지칭하는 방식에 
	 * 따라 계산한다
	 * </div>
	 * <div id="en">
	 * Returns The year fraction between two dates using a given day count 
	 * fraction
	 * </div>.
	 * 
	 * @param dcf
	 * 			  <div id="ko">DayCountFraction</div>
	 * 			  <div id="en">Day count fraction</div>
	 * @param startDate
	 * 			  <div id="ko">시작일</div>
	 * 			  <div id="en">The start date</div>
	 * @param endDate
	 * 			  <div id="ko">종료일</div>
	 * 			  <div id="en">The end date</div>
	 * 
	 * @return <div id="ko">두 날짜 사이의 Year Fraction</div>
	 * 		   <div id="en">The year fraction between two dates</div>
	 */
	public static double getYearFraction(DayCountFraction dcf, Date startDate, Date endDate) {
		return dcf.getYearFraction(startDate, endDate);
	}

	// This method is called immediately after an object of this class is deserialized.
    // This method returns the singleton instance.
    protected Object readResolve() {
    	DayCountFraction dcf = valueOf(_code);
    	dcf.setDate(_date);
    	return dcf;
    }

}
