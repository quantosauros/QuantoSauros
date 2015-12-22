package com.quantosauros.common.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <div id="ko">
 * <code>Date</code>는 날짜 정보를 나타내는 클래스이다 
 * </div>
 * <div id="en">
 * <code>Date</code> represents a date information 
 * </div>.
 * <div id="ko">
 * 날짜와 관련된 여러 가지 기능을 제공한다.
 * </div>
 * <div id="en">
 * The class provides several methods related to date.
 * </div>
 * 
 * @author Oh, Sung Hoon
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
 * Implementation Note
 * - Creation: RiskCraft.Market R3, 2008
 * - FIXME:: This class implements Cloneable but does not define or use clone method
 ------------------------------------------------------------------------------*/
public class Date implements Serializable, Cloneable, Comparable {

	private int _year;

	private int _month;

	private int _dayOfMonth;

	private String _canonicalForm;

	// By Yoo Sangwook 2011.09.25
	private static final int[] TOTAL_DAYS_IN_MONTH = new int[] { 31, // 31
			59, // 28,
			90, // 31,
			120, // 30,
			151, // 31,
			181, // 30,
			212, // 31,
			243, // 31,
			273, // 30,
			304, // 31,
			334, // 30,
			365 // 31,
	};

	// By Yoo Sangwook 2011.09.25
	private static final int[] LAST_DAY_IN_MONTH = new int[] {
		31,
		28,
		31,
		30,
		31,
		30,
		31,
		31,
		30,
		31,
		30,
		31
	};

	// Jae-Heon Kim 2010.12.29
	// To apply the end-of-month rule for cash flow dates generation.
	private boolean _isEom;

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 */
	public Date() {
		// this(Calendar.getInstance());
		// this(new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
		// 현재 시스템 날짜를 가져오는 것은 의미가 없어서 그냥 이렇게 처리한다.
		// Yoo Sangwook 2011.09.26
		this("19700101"); // FIXME::
	}

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 * 
	 * @param yyyymmdd
	 * 			  <div id="ko">생성할 날짜의 8자리 문자열 표현</div>
	 * 			  <div id="en">The year, month and day information written as yyyymmdd</div>
	 */
	public Date(String yyyymmdd) {
		if (yyyymmdd == null) {
			throw new NullPointerException("date is null");
		}
		if (yyyymmdd.length() != 8) {
			throw new IllegalArgumentException("YYYYMMDD=" + yyyymmdd);
		}

		_year = Integer.parseInt(yyyymmdd.substring(0, 4));
		_month = Integer.parseInt(yyyymmdd.substring(4, 6));
		_dayOfMonth = Integer.parseInt(yyyymmdd.substring(6));
		checkDateFormat(_year, _month, _dayOfMonth);
		_canonicalForm = yyyymmdd;
	}

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 * 
	 * @param year
	 * 			  <div id="ko">생성할 날짜의 연도 정보</div>
	 * 			  <div id="en">The year</div>
	 * @param month
	 * 			  <div id="ko">생성할 날짜의 월 정보</div>
	 * 			  <div id="en">The month</div>
	 * @param dayOfMonth
	 * 			  <div id="ko">생성할 날짜의 일자 정보</div>
	 * 			  <div id="en">The day</div>
	 */
	public Date(int year, int month, int dayOfMonth) {
		_year = year;
		_month = month;
		_dayOfMonth = dayOfMonth;
		checkDateFormat(year, month, dayOfMonth);
		_canonicalForm = canonicalForm(year, month, dayOfMonth);
	}

	// Commented out by Yoo Sangwook 2011.09.25
	/*
	 * 생성자
	 * 
	 * @param calendar 날짜 정보를 가진 {@code java.util.Calendar} 객체
	 */
//	public Date(Calendar calendar){
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH) + 1; 
//		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); 
//		checkDateFormat(year, month, dayOfMonth); 
//		_canonicalForm = canonicalForm(year, month, dayOfMonth);
//	}

	/**
	 * <div id="ko">
	 * 입력된 날짜의 {@code Date} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a {@code Date} object with a given date
	 * </div>.
	 * <div id="ko">
	 * 해당 날짜가 말일인 경우 무조건 EOM을 true로 설정한다.
	 * </div>
	 * <div id="en">
	 * isEOM is set to "true" whenever a given date is an end of month.
	 * </div>
	 * 
	 * @param yyyymmdd
	 * 			  <div id="ko">입력할 날짜를 8자리 문자열로 표현</div>
	 * 			  <div id="en">The year, month and day information written as yyyymmdd</div>
	 * 
	 * @return <div id="ko">입력된 날짜의 {@code Date} 객체</div>
	 * 		   <div id="en">A {@code Date} object with the given date</div>
	 */
	public static Date valueOf(String yyyymmdd) {
		/*
		 * if (yyyymmdd == null) { return null; } return new Date(yyyymmdd);
		 */
		// Jae-Heon Kim 2010.12.29
		return valueOf(yyyymmdd, true);
	}

	/**
	 * <div id="ko">
	 * 입력된 날짜의 {@code Date} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a {@code Date} object with a given date
	 * </div>.
	 * <div id="ko">
	 * 해당 날짜가 말일인 경우 isEom 변수의 값에 따라 EOM 여부를 설정한다.
	 * </div>
	 * <div id="en">
	 * isEOM is set depending on isEOM parameter when a given date is an end of month.
	 * </div>
	 * 
	 * @param yyyymmdd
	 * 			  <div id="ko">입력할 날짜를 8자리 문자열로 표현</div>
	 * 			  <div id="en">The year, month and day information written as yyyymmdd</div>
	 * @param isEom
	 * 			  <div id="ko">EOM 설정 여부</div>
	 * 			  <div id="en">true if EOM, false otherwise </div>
	 * 
	 * @return <div id="ko">입력된 날짜의 {@code Date} 객체</div>
	 * 		   <div id="en">A {@code Date} object with the given date</div>
	 */
	// Jae-Heon Kim 2010.12.29
	public static Date valueOf(String yyyymmdd, boolean isEom) {
		if (yyyymmdd == null) {
			return null;
		}
		Date ret = new Date(yyyymmdd);
		if (isEom) {
			ret.setEom(ret.isLastDayOfMonth());
		}
		return ret;
	}

	/**
	 * <div id="ko">
	 * 입력된 날짜들의 Date 객체 배열을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns an array of date objectives corresponding to an array of date strings
	 * </div>.
	 * 
	 * @param yyyymmdd
	 * 			  <div id="ko">입력할 날짜들을 8자리 문자열로 표현한 것들의 배열</div>
	 * 			  <div id="en">An array of the year, month and day information written as yyyymmdd</div>
	 * 
	 * @return <div id="ko">입력할 날짜들을 8자리 문자열로 표현한 것들의 배열</div>
	 * 		   <div id="en">An array of date objectivs</div>
	 */
	public static Date[] valuesOf(String[] yyyymmdds) {
		if (yyyymmdds == null) {
			return null;
		}
		Date[] dates = new Date[yyyymmdds.length];
		for (int i = 0; i < dates.length; ++i) {
			dates[i] = valueOf(yyyymmdds[i]);
		}
		return dates;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 연도 정보를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the year of this {@code Date} object.
	 * </div>.
	 * 
	 * @return <div id="ko">연도 정보</div>
	 * 		   <div id="en">The year</div>
	 */
	public int getYear() {
		return _year;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 월 정보를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the month of this {@code Date} object.
	 * </div>.
	 * 
	 * @return <div id="ko">월 정보</div>
	 * 		   <div id="en">The month</div>
	 */
	public int getMonth() {
		return _month;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 일자 정보를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the month of this {@code Date} object.
	 * </div>.
	 * 
	 * @return <div id="ko">일자 정보</div>
	 * 		   <div id="en">The day</div>
	 */
	public int getDayOfMonth() {
		return _dayOfMonth;
	}

	private void checkDateFormat(int year, int month, int day) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Month=" + month);
		}
		if (day < 1 || day > 31) {
			throw new IllegalArgumentException("Day=" + day);
		}

		// Yoo sangwook 2011.09.26
		if (Date.isLeapYear(year) && month == 2
				&& (LAST_DAY_IN_MONTH[1] + 1) < day) {
			throw new IllegalArgumentException(year + "/" + month + "/" + day);
		}
		if (!(Date.isLeapYear(year) && month == 2)
				&& LAST_DAY_IN_MONTH[month - 1] < day) {
			throw new IllegalArgumentException(year + "/" + month + "/" + day);
		}
	}

	private static String canonicalForm(int year, int month, int day) {
		return year + (month < 10 ? "0" : "") + month + (day < 10 ? "0" : "")
				+ day;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 날짜와 입력된 {@code date}의 개월 수 차이를 구한다
	 * </div>
	 * <div id="en">
	 * Returns the months difference from this {@code Date} obeject to a given date
	 * </div>.
	 * 
	 * @param date
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The date to be compared</div>
	 * 
	 * @return <div id="ko">입력된 날짜와의 개월 수 차이</div>
	 * 		   <div id="en">The months difference between two dates</div>
	 */
	public int getMonths(Date date) {
		return (date.getYear() - getYear()) * 12 + date.getMonth() - getMonth();
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 날짜와 입력된 {@code date}의 일자 수 차이를 구한다
	 * </div>
	 * <div id="en">
	 * Returns the days difference from this {@code Date} obeject to a given date
	 * </div>.
	 * 
	 * @param date
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The date to be compared</div>
	 * 
	 * @return <div id="ko">입력된 날짜와의 일자 수 차이</div>
	 * 		   <div id="en">The days difference between two dates</div>
	 */
	public long getDays(Date date) {
		/*
		 * return toDays(date.toCalendar().getTimeInMillis() -
		 * toCalendar().getTimeInMillis());
		 */
		if (_canonicalForm.compareTo(date._canonicalForm) == 0) {
			return 0L;
		}

		int dayCount2 = date.getDayCountOfYear();
		int dayCount = getDayCountOfYear();

		int dayCountYear = 0;
		if (_year <= date.getYear()) {
			for (int i = _year; i < date.getYear(); i++) {
				if (Date.isLeapYear(i)) {
					dayCountYear += 366;
				} else {
					dayCountYear += 365;
				}
			}
			return dayCountYear - dayCount + dayCount2;
		} else {
			return -date.getDays(this);
		}
	}

	// Yoo sangwook 2011.09.25
	public int getDayCountOfYear() {
		if (_month == 1)
			return _dayOfMonth;

		if (isLeapYear() && _month > 2) {
			return _dayOfMonth + TOTAL_DAYS_IN_MONTH[_month - 2] + 1;
		} else {
			return _dayOfMonth + TOTAL_DAYS_IN_MONTH[_month - 2];
		}
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 날짜와 입력된 {@code date}의 일자 수 차이를 
	 * 30/360 방식 기준으로 구한다
	 * </div>
	 * <div id="en">
	 * Returns the days difference from this {@code Date} obeject to a given date
	 * using 30/360 day count convention
	 * </div>.
	 * 
	 * @param date
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The date to be compared</div>
	 * 
	 * @return <div id="ko">입력된 날짜와의 일자 수 차이</div>
	 * 		   <div id="en">The days difference between two dates</div>
	 */
	public long getDays360(Date date) {
		return getDays360(date, false);
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 날짜와 입력된 {@code date}의 일자 수 차이를
	 * E30/360 방식 기준으로 구한다.
	 * </div>
	 * <div id="en">
	 * Returns the days difference from this {@code Date} obeject to a given date
	 * using E30/360 day count convention
	 * </div>.
	 * 
	 * @param date
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The date to be compared</div>
	 * @param isEuropean
	 * 			  <div id="ko">{@code true}이면 30E/360, {@code false}이면 30/360</div>
	 * 			  <div id="en">30E/360 if {@code true}, 30/360 if {@code false}</div>
	 * 
	 * @return <div id="ko">입력된 날짜와의 일자 수 차이</div>
	 * 		   <div id="en">The days difference between two dates</div>

	 */
	public long getDays360(Date date, boolean isEuropean) {
		int d1 = _dayOfMonth;
		int d2 = date._dayOfMonth;
		if (isEuropean) {
			d1 = Math.min(d1, 30);
			d2 = Math.min(d2, 30);
		} else {
			d1 = Math.min(d1, 30);
			d2 = (d2 == 31 && d1 == 30) ? 30 : d2;
		}
		long days = (date._year - _year) * 360 + (date._month - _month) * 30
				+ (d2 - d1);
		return days;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체가 속한 연도의 윤년 여부를 판단한다.
	 * </div>
	 * <div id="en">
	 * Tells whether a {@code Date} object is in a leap year
	 * </div>.
	 * 
	 * @return <div id="ko">이 {@code Date} 객체가 속한 연도의 윤년 여부</div>
	 * 		   <div id="en">True if the {@code Date} object is in a leap year, false otherwise</div>
	 */
	public boolean isLeapYear() {
		// return new GregorianCalendar().isLeapYear(year);
		// Yoo sangwook 2011.09.25
		if ((_year & 3) != 0) {
			return false;
		}

		return (_year % 100 != 0) || (_year % 400 == 0); // Gregorian
	}

	/**
	 * <div id="ko">
	 * 입력된 {@code Date}가 속한 연도의 윤년 여부를 판단한다.
	 * </div>
	 * <div id="en">
	 * Tells whether a given {@code Date} is in a leap year
	 * </div>.
	 * 
	 * @param date
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The date to be checked</div>
	 * 
	 * @return <div id="ko">입력된 {@code Date}가 속한 연도의 윤년 여부</div>
	 * 		   <div id="en">True if the given {@code Date} is in a leap year, false otherwise</div>
	 */
	// Yoo sangwook 2011.09.25
	public static boolean isLeapYear(int year) {
		if ((year & 3) != 0) {
			return false;
		}

		return (year % 100 != 0) || (year % 400 == 0); // Gregorian
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 연도에 입력된 연도 수를 더한 새 
	 * {@code Date} 객체를 생성하여 반환한다.
	 * </div>
	 * <div id="en">
	 * Returns a new {@code Date} object by adding given years to this 
	 * {@code Date} object
	 * </div>.
	 * 
	 * @param years
	 * 			  <div id="ko">연도</div>
	 * 			  <div id="en">The years to be added</div>
	 * 
	 * @return <div id="ko">입력된 연도 수를 더한 새 {@code Date} 객체</div>
	 * 		   <div id="en">A new {@code Date} object</div>
	 */
	public Date plusYears(int years) {
		/*
		 * Calendar calendar = toCalendar(); calendar.add(Calendar.YEAR, years);
		 * return new Date(calendar);
		 */
		// Yoo sangwook 2011.09.25
		if (years == 0) {
			return new Date(_year, _month, _dayOfMonth);
		}

		if (isLeapYear() && _month == 2 && _dayOfMonth == 29) {
			if (isLeapYear(_year + years)) {
				return new Date(_year + years, _month, _dayOfMonth);
			} else {
				return new Date(_year + years, _month, 28);
			}
		} else {
			return new Date(_year + years, _month, _dayOfMonth);
		}
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 월에 입력된 개월 수를 더한 새 
	 * {@code Date} 객체를 생성하여 반환한다.
	 * </div>
	 * <div id="en">
	 * Returns a new {@code Date} object by adding given months to this 
	 * {@code Date} object
	 * </div>.
	 * 
	 * @param months
	 * 			  <div id="ko">월</div>
	 * 			  <div id="en">The months to be added</div>
	 * 
	 * @return <div id="ko">입력된 개월 수를 더한 새 {@code Date} 객체</div>
	 * 		   <div id="en">A new {@code Date} object</div>
	 */
	public Date plusMonths(int months) {
		// Yoo sangwook 2011.09.25
		if (months == 0) {
			return new Date(_year, _month, _dayOfMonth);
		}

		Date ret = null;
		if (months > 0) {
			int yearDelta = 0;
			int newMonth = _month;
			int newDayOfMonth = _dayOfMonth;

			if (_month + months <= 12) {
				newMonth = _month + months;
				newDayOfMonth = Math.min(newDayOfMonth,
						getLastDayOfMonth(_year + yearDelta, newMonth));
				ret = new Date(_year + yearDelta, newMonth, newDayOfMonth);
			} else {
				int cnt = 1;
				while (_month + months - 12 * cnt > 12) {
					cnt++;
				}
				yearDelta = cnt;
				newMonth = _month + months - 12 * cnt;
				newDayOfMonth = Math.min(newDayOfMonth,
						getLastDayOfMonth(_year + yearDelta, newMonth));
				ret = new Date(_year + yearDelta, newMonth, newDayOfMonth);
			}
		} else {
			int yearDelta = 0;
			int newMonth = _month;
			int newDayOfMonth = _dayOfMonth;

			if (_month + months > 0) {
				newMonth = _month + months;
				newDayOfMonth = Math.min(newDayOfMonth,
						getLastDayOfMonth(_year + yearDelta, newMonth));
				ret = new Date(_year + yearDelta, newMonth, newDayOfMonth);
			} else {
				int cnt = 1;
				while (_month + months + 12 * cnt <= 0) {
					cnt++;
				}
				yearDelta = -cnt;
				newMonth = _month + months + 12 * cnt;
				newDayOfMonth = Math.min(newDayOfMonth,
						getLastDayOfMonth(_year + yearDelta, newMonth));
				ret = new Date(_year + yearDelta, newMonth, newDayOfMonth);
			}
		}

		if (_isEom) {
			Date ret2 = new Date(ret._year, ret._month, getLastDayOfMonth(
					ret._year, ret._month));
			ret2.setEom(true);
			return ret2;
		} else {
			return ret;
		}
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 일에 입력된 일자 수를 더한 새
	 * {@code Date} 객체를 생성하여 반환한다.
	 * </div>
	 * <div id="en">
	 * Returns a new {@code Date} object by adding given days 
	 * to this {@code Date} object
	 * </div>.
	 * 
	 * @param days
	 * 			  <div id="ko">일자 수</div>
	 * 			  <div id="en">The months to be added</div>
	 * 
	 * @return <div id="ko">입력된 일자 수를 더한 새 {@code Date} 객체</div>
	 * 		   <div id="en">A new {@code Date} object</div>
	 */
	public Date plusDays(int days) {
		/*
		 * Calendar calendar = toCalendar(); calendar.add(Calendar.DATE, days);
		 * return new Date(calendar);
		 */

		// Yoo sangwook 2011.09.25
		if (days == 0) {
			return new Date(_year, _month, _dayOfMonth);
		}

		if (days > 0) {
			int yearDelta = 0;
			int newMonth = _month;
			int newDayOfMonth = _dayOfMonth;

			int dayDelta = getLastDayOfMonth()._dayOfMonth - _dayOfMonth + 1;
			if ((days - dayDelta) < 0) {
				newDayOfMonth = _dayOfMonth + days;
				return new Date(_year, _month, newDayOfMonth);
			} else {
				newMonth++;
				if (newMonth > 12) {
					newMonth = 1;
					yearDelta++;
				}
				newDayOfMonth = 1;
				days -= dayDelta;
				Date ret = new Date(_year + yearDelta, newMonth, newDayOfMonth);
				return ret.plusDays(days);
			}
		} else {
			int yearDelta = 0;
			int newMonth = _month;
			int newDayOfMonth = _dayOfMonth;

			if ((_dayOfMonth + days) > 0) {
				newDayOfMonth = _dayOfMonth + days;
				return new Date(_year, _month, newDayOfMonth);
			} else {
				newMonth--;
				if (newMonth <= 0) {
					newMonth = 12;
					yearDelta--;
				}
				newDayOfMonth = getLastDayOfMonth(_year + yearDelta, newMonth);
				days += _dayOfMonth;
				Date ret = new Date(_year + yearDelta, newMonth, newDayOfMonth);
				return ret.plusDays(days);
			}
		}

	}

	/*
	 * private Date getFirstDayOfMonth() { return new Date(year, month, 1); }
	 */

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체가 속한 달의 마지막 날을 구한다.
	 * </div>
	 * <div id="en">
	 * Returns the last day of the month of this {@code Date} object 
	 * </div>.
	 * 
	 * @return <div id="ko">이 달의 마지막 날</div>
	 * 		   <div id="en">The last day of the month</div>
	 */
	public Date getLastDayOfMonth() {
		/*
		 * Date date = new Date(canonicalForm); date = date.plusMonths(1); date
		 * = date.getFirstDayOfMonth(); return date.plusDays(-1);
		 */

		// Yoo sangwook 2011.09.25
		if (isLeapYear() && _month == 2)
			return new Date(_year, _month, LAST_DAY_IN_MONTH[_month - 1] + 1);
		else
			return new Date(_year, _month, LAST_DAY_IN_MONTH[_month - 1]);
	}

	// Yoo sangwook 2011.09.25
	public static int getLastDayOfMonth(int year, int month) {
		if (isLeapYear(year) && month == 2)
			return LAST_DAY_IN_MONTH[month - 1] + 1;
		else
			return LAST_DAY_IN_MONTH[month - 1];
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 날짜가 해당 월의 마지막 날인지를 판단한다.
	 * </div>
	 * <div id="en">
	 * Tells whether the day of this {@code Date} object is the last day of the month 
	 * </div>.
	 * 
	 * @return <div id="ko">마지막 날이면 {@code true}, 그렇지 않으면 {@code false}</div>
	 * 		   <div id="en">True if the day is the last dayhe last day, false otherwise of the month</div>
	 */
	// Jae-Heon Kim, 2009.09.08
	public boolean isLastDayOfMonth() {
		Date last = getLastDayOfMonth();
		if (last.compareTo(this) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <div id="ko">
	 * 현재 날짜를 8자리 형태의 문자열로 반환한다(YYYYMMDD)
	 * </div>
	 * <div id="en">
     * Converts this {@code Date} object to a string(YYYYMMDD)
	 * </div>.
	 * 
	 * @return <div id="ko">현재 날짜의 8자리 문자열</div>
	 * 		   <div id="en">A string representation of the date</div>
	 */
	public String toString() {
		return _canonicalForm;
	}

	/**
	 * <div id="ko">
	 * 입력된 날짜가 이 {@code Date} 객체의 날짜와 같은지 판단한다
	 * </div>
	 * <div id="en">
     * Compares two dates for equality
	 * </div>.
	 * 
	 * @param o
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The object to be compared</div>
	 * 
	 * @return <div id="ko">같으면 {@code true}, 그렇지 않으면 {@code false}</div>
	 * 		   <div id="en">True if the objects are the same, flase otherwise</div>
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Date)) {
			return false;
		}

		return _canonicalForm.equals(((Date) o)._canonicalForm);
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 날짜를 나타내는 8자리 문자열에 대한
	 * hash code를 구한다.
	 * </div>
	 * <div id="en">
     * Returns a hash code value for this {@code Date} object
	 * </div>.
	 * 
	 * @return <div id="ko">hash code</div>
	 * 		   <div id="en">hash code</div>
	 */
	public int hashCode() {
		return _canonicalForm.hashCode();
	}

	/**
	 * <div id="ko">
	 * 이 {@code Date} 객체의 날짜와 주어진 날짜를 비교한다
	 * </div>
	 * <div id="en">
     * Compares two dates for ordering
	 * </div>.
	 * <div id="ko">
	 * 주어진 날짜가 이 {@code Date} 객체의 날짜보다 앞이면 1, 뒤면 -1을 반환하며, 
	 * 두 날짜가 같으면 0을 반환한다.
	 * </div>
	 * <div id="en">
     * Returns 1 if a given date is before this {@code Date} object, -1 otherwise.
	 * </div>
	 * 
	 * @param o
	 * 			  <div id="ko">대상일</div>
	 * 			  <div id="en">The object to be compared</div>
	 * 
	 * @return <div id="ko">비교 결과</div>
	 * 		   <div id="en">1 if the given date is before the {@code Date} object, -1 otherwise</div>
	 */
	public int compareTo(Object o) {
		return _canonicalForm.compareTo(((Date) o)._canonicalForm);
	}

	public void setEom(boolean isEom) {
		_isEom = isEom;
	}

	public boolean isEom() {
		return _isEom;
	}
	
	// Jae-Heon Kim for MtM
    public void setDt(String yyyymmdd) {
    	if (yyyymmdd == null) {
        	throw new NullPointerException("date is null");
        }
        if (yyyymmdd.length() != 8) {
        	throw new IllegalArgumentException("YYYYMMDD=" + yyyymmdd);
        }

        _year = Integer.parseInt(yyyymmdd.substring(0, 4));
        _month = Integer.parseInt(yyyymmdd.substring(4, 6));
        _dayOfMonth = Integer.parseInt(yyyymmdd.substring(6));
        checkDateFormat(_year, _month, _dayOfMonth);
        _canonicalForm = yyyymmdd;
    }
    
	// Jae-Heon Kim for MtM
    public String getDt() {
    	return _canonicalForm;
    }

	// Jae-Heon Kim 2011.06.07 For MtM
	// Count dates in inverse direction of getDays().
    public int diff(Date date) {
    	return (int)(-getDays(date));
    }
    
	// Jae-Heon Kim 2011.06.07 For MtM
	public int getDayOfWeek() {
    	Calendar cal = new GregorianCalendar(_year, _month - 1, _dayOfMonth);
    	return cal.get(Calendar.DAY_OF_WEEK);
    }	
}
