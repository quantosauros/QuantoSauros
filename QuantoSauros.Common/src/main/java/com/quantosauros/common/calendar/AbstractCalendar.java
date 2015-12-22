package com.quantosauros.common.calendar;

import java.util.ArrayList;
import java.util.Iterator;

import com.quantosauros.common.date.Date;

/**
 * {@code AbstractCalendar}는 {@code Calendar} 인터페이스를 구현하는 추상
 * 클래스로서 특정 국가, market에서 사용하는 달력을 구현할 때 이 클래스를
 * 상속받아야 한다.
 * <p>하위 클래스에서 공통적으로 사용할 수 있도록 휴일 추가, 요일 계산,
 * Business Day Convention에 따른 결제일의 이동 등의 기능이 포함되어 있다.
 *
 * @author Kang Seok-Chan
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public abstract class AbstractCalendar implements Calendar {

	private ArrayList _holidays;

	protected AbstractCalendar() {
		_holidays = new ArrayList();
	}

	/**
	 * 이 {@code Calendar}에 휴일을 추가한다.
	 *
	 * @param date	추가할 휴일의 날짜
	 */
	public void addHoliday(final Date date) {
		if (!isAddedHoliday(date)) {
			_holidays.add(date);
		}
	}

	/**
	 * {@code addHoliday} 함수를 사용해 추가된 휴일을 제거한다.
	 * <p>{@code Calendar} 객체 생성 시 포함되어 있던 기본 휴일은 제거할
	 * 수 없다.
	 *
	 * @param date	삭제할 휴일의 날짜
	 */
	public void removeHoliday(final Date date) {
		Iterator it = _holidays.iterator();
		while (it.hasNext()) {
			Date cur = (Date)it.next();
			if (cur.equals(date)) {
				_holidays.remove(cur);
				break;
			}
		}
	}

	/**
	 * 입력된 날짜가 {@code addHoliday} 함수를 사용해서 이 {@code Calendar}
	 * 객체에 추가된 휴일인지를 판단한다.
	 * <p>추가된 휴일일 경우 {@code true}를, 객체 생성 시 포함되어 있던
	 * 기본 휴일인 경우 {@code false}를 반환한다.
	 *
	 * @param date	날짜
	 * @return 	휴일 여부
	 */
	public boolean isAddedHoliday(final Date date) {
		Iterator it = _holidays.iterator();
		while (it.hasNext()) {
			Date cur = (Date) it.next();
			if (cur.equals(date)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 입력된 날짜가 주말(토요일 또는 일요일)인지를 판단한다.
	 * 주말인 경우 {@code true}를 반환한다.
	 *
	 * @param date	날짜
	 * @return 	주말 여부
	 */
	public boolean isWeekend(final Date date) {
		Weekday w = getDayOfWeek(date);
		if ((w == Weekday.SATURDAY) || (w == Weekday.SUNDAY)) {
			return true;
		}
		return false;
	}

	/**
	 * 입력된 날짜의 요일을 {@code Weekday}로 반환한다.
	 *
	 * @param date	날짜
	 * @return 입력한 날짜의 요일
	 */
	public Weekday getDayOfWeek(final Date date) {
		int day = getDayOfWeekByZellersCongruence(date);
		return Weekday.valueOf(day);
	}

	private int getDayOfWeekByZellersCongruence(final Date date) {
		// Zeller's Congruence
		int y, m, d;
		y = date.getYear();
		m = date.getMonth();
		d = date.getDayOfMonth();
		if ((m == 1) || (m == 2)) {
			m = m + 12;
			y--;
		}

		int x = (y + y / 4 - y / 100 + y / 400 + (int) (2.6 * m + 2.6) + d) % 7;
		return x;
	}

	/**
	 * 입력된 날짜가 business day인지를 판단한다. 이 {@code Calendar}에서
	 * business day인 경우 {@code true}를, 그렇지 않은 경우에는 {@code false}를
	 * 반환한다.
	 *
	 * @param 	date	날짜
	 * @return 	Business Day 여부
	 */
	public boolean isBusinessDay(final Date date) {
		return !isHoliday(date);
	}

	/**
	 * 입력된 날짜가 business day인지를 판단하여, business day가 아닌 경우에는
	 * 주어진 {@code BusinessDayConvention}에 따라 날짜를 조정한다.
	 *
	 * @param date	날짜
	 * @return 	주어진 business day convention에 따라 조정된 날짜
	 */
	public Date adjustDate(final Date date, final BusinessDayConvention convention) {
		Date d1 = date;
		if (convention == BusinessDayConvention.FOLLOWING
				|| convention == BusinessDayConvention.MODIFIED_FOLLOWING) {
			while (isHoliday(d1)) {
				d1 = d1.plusDays(1);
			}
			if (convention == BusinessDayConvention.MODIFIED_FOLLOWING) {
				if (d1.getMonth() != date.getMonth()) {
					return adjustDate(date, BusinessDayConvention.PRECEDING);
				}
			}
		} else if (convention == BusinessDayConvention.PRECEDING
				|| convention == BusinessDayConvention.MODIFIED_PRECEDING) {
			while (isHoliday(d1)) {
				d1 = d1.plusDays(-1);
			}
			if (convention == BusinessDayConvention.MODIFIED_PRECEDING) {
				if (d1.getMonth() != date.getMonth()) {
					return adjustDate(date, BusinessDayConvention.FOLLOWING);
				}
			}
		}

		if (convention == BusinessDayConvention.NEAREST) {
			Date d2 = adjustDate(date, BusinessDayConvention.PRECEDING);
			Date d3 = adjustDate(date, BusinessDayConvention.FOLLOWING);
			if (d2.getDays(d1) < d1.getDays(d3)) {
				return d2;
			}
			else {
				return d3;
			}
		}
		return d1;
	}

	/**
	 * 입력된 날짜가 해당 월의 마지막 날인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return 	말일인 경우 {@code true}, 그렇지 않으면 {@code false}
	 */
	public boolean isEndOfMonth(final Date date) {
		return (date.equals(date.getLastDayOfMonth()));
	}

	/**
	 * 입력된 날짜가 해당 월의 마지막 business day인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return 	마지막 business day인 경우 {@code true}, 그렇지 않으면 {@code false}
	 */
	public boolean isLastBusinessDayOfMonth(final Date date) {
		return (date.equals(getLastBusinessDayOfMonth(date)));
		/*
		 * return (date.equals(adjustDate(date.getLastDayOfMonth(),
		 * BusinessDayConvention.PRECEDING)));
		 */
	}

	/**
	 * 입력된 날짜가 속한 달의 마지막 날짜를 가져온다.
	 *
	 * @param date	날짜
	 * @return 	입력된 날짜가 속한 달의 마지막 날
	 */
	public Date getEndOfMonth(final Date date) {
		return date.getLastDayOfMonth();
	}

	/**
	 * 입력된 날짜가 속한 달의 마지막 business day를 가져온다.
	 *
	 * @param date	날짜
	 * @return 	입력된 날짜가 속한 달의 마지막 business day
	 */
	public Date getLastBusinessDayOfMonth(final Date date) {
		return adjustDate(date.getLastDayOfMonth(),
				BusinessDayConvention.PRECEDING);
	}

	/**
	 * @return the offset of the Easter Monday relative to the first day of the
	 *         year
	 */
	// Note: byte range is -128..+127
	private static final byte WESTERN_EASTER_MONDAY[] = { 107, 98, 90, 103, 95,
			114, 106, 91, 111, 102, // 1900-1909
			87, 107, 99, 83, 103, 95, 115, 99, 91, 111, // 1910-1919
			96, 87, 107, 92, 112, 103, 95, 108, 100, 91, // 1920-1929
			111, 96, 88, 107, 92, 112, 104, 88, 108, 100, // 1930-1939
			85, 104, 96, 116, 101, 92, 112, 97, 89, 108, // 1940-1949
			100, 85, 105, 96, 109, 101, 93, 112, 97, 89, // 1950-1959
			109, 93, 113, 105, 90, 109, 101, 86, 106, 97, // 1960-1969
			89, 102, 94, 113, 105, 90, 110, 101, 86, 106, // 1970-1979
			98, 110, 102, 94, 114, 98, 90, 110, 95, 86, // 1980-1989
			106, 91, 111, 102, 94, 107, 99, 90, 103, 95, // 1990-1999
			115, 106, 91, 111, 103, 87, 107, 99, 84, 103, // 2000-2009
			95, 115, 100, 91, 111, 96, 88, 107, 92, 112, // 2010-2019
			104, 95, 108, 100, 92, 111, 96, 88, 108, 92, // 2020-2029
			112, 104, 89, 108, 100, 85, 105, 96, 116, 101, // 2030-2039
			93, 112, 97, 89, 109, 100, 85, 105, 97, 109, // 2040-2049
			101, 93, 113, 97, 89, 109, 94, 113, 105, 90, // 2050-2059
			110, 101, 86, 106, 98, 89, 102, 94, 114, 105, // 2060-2069
			90, 110, 102, 86, 106, 98, 111, 102, 94, 107, // 2070-2079
			99, 90, 110, 95, 87, 106, 91, 111, 103, 94, // 2080-2089
			107, 99, 91, 103, 95, 115, 107, 91, 111, 103 // 2090-2099
	};

	protected final int easterMonday(final int year) {
		return WESTERN_EASTER_MONDAY[year - 1900];
	}

	/**
	 * 이 {@code Calendar} 객체의 이름을 가져온다.
	 *
	 * @return 	{@code Calendar}의 이름
	 */
	// Jae-Heon Kim, 2009.11.17
	public String toString() {
		return getName();
	}
}
