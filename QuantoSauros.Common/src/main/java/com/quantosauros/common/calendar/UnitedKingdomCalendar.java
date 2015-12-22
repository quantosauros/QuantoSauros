package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code UnitedKingdomCalendar} 클래스는 영국의 달력을 구현한다.
 * 공휴일만 있는 달력과 런던 증권거래소(LSE)에서 사용하는 달력,
 * 이렇게 두 가지 달력을 지원한다.
 * 
 * <p>공개된 생성자는 두지 않으며, {@code getCalendar} 함수를 이용하여
 * 구체적인 {@code Calendar} 객체를 생성한다.
 * <P>
 * 1. 공휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>New Year's Day, 1월 1일</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Early May Bank Holiday, 5월 1번째 월요일</li>
 * <li>Spring Bank Holiday, 5월 마지막 월요일</li>
 * <li>Summer Bank Holiday, 8월 마지막 월요일</li>
 * <li>Christmas, 12월 25일</li>
 * <li>Boxing Day, 12월 26일</li>
 * </ul>
 *
 * <p>2. 런던 증권거래소(LSE) 휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>New Year's Day, 1월 1일</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>Early May Bank Holiday, 5월 1번째 월요일</li>
 * <li>Spring Bank Holiday, 5월 마지막 월요일</li>
 * <li>Summer Bank Holiday, 8월 마지막 월요일</li>
 * <li>Christmas, 12월 25일</li>
 * <li>Boxing Day, 12월 26일</li>
 * </ul>
 * 참조:영국 런던 증권거래소 (<a href=http://www.londonstockexchange.com>http://www.londonstockexchange.com</a>)
 *
 * @author Kang Seok-Chan
 * @since 3.2
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/

public class UnitedKingdomCalendar extends DelegateCalendar {
	private final static UnitedKingdomCalendar SETTLEMENT_CALENDAR
		= new UnitedKingdomCalendar(Market.UnitedKingdom.SETTLEMENT);

	private final static UnitedKingdomCalendar EXCHANGE_CALENDAR
		= new UnitedKingdomCalendar(Market.UnitedKingdom.LSE);

	private UnitedKingdomCalendar(int market) {
		Calendar delegate;
		switch (market) {
			case Market.UnitedKingdom.SETTLEMENT:
				delegate = new UnitedKingdomSettlementCalendar();
				break;
			case Market.UnitedKingdom.LSE:
				delegate = new UnitedKingdomLSECalendar();
				break;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
		setDelegate(delegate);
	}

	/**
	 * 입력된 시장 코드에 해당하는 영국 달력을 가져온다.
	 * 시장 코드는 다음과 같다:
	 * <ul>
	 * <li>공휴일: {@code Market.UnitedKingdom.SETTLEMENT}
	 * <li>영국 런던 증권거래소: {@code Market.UnitedKingdom.LSE}
	 * </ul>
	 *
	 * @param market	생성하고자 하는 시장 코드
	 * @return		 	{@code UnitedKingdomCalendar} 객체
	 */
	public static UnitedKingdomCalendar getCalendar(int market) {
		switch (market) {
			case Market.UnitedKingdom.SETTLEMENT:
				return SETTLEMENT_CALENDAR;
			case Market.UnitedKingdom.LSE:
				return EXCHANGE_CALENDAR;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
	}

	private class UnitedKingdomSettlementCalendar extends AbstractCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "UnitedKingdom Settlement Calendar";
		}

		/**
		 * 입력된 날짜가 휴일인지를 판단한다.
		 *
		 * @param date	날짜
		 * @return 	휴일이면 {@code true}, 그렇지 않으면 {@code false}
		 */
		public boolean isHoliday(final Date date) {
			int y = date.getYear();
			int m = date.getMonth();
			int d = date.getDayOfMonth();
			Weekday w = getDayOfWeek(date);

			Date date2 = new Date(y, 1, 1);
			int dd = (int) date2.getDays(date);
			int em = easterMonday(y);

			if ((isWeekend(date)) // Weekend
					|| (isAddedHoliday(date)) // Added Holiday
					// New Year's Day
					|| (m == 1 &&
							(d == 1 || ((d == 2 || d == 3) && w == Weekday.MONDAY)))
					|| (dd == em - 3) // Good Friday
					|| (dd == em) // Easter Monday
					// first Monday of May (Early May Bank Holiday)
					|| (d <= 7 && w == Weekday.MONDAY && m == 5)
					// last Monday of May (Spring Bank Holiday)
					|| (d >= 25 && w == Weekday.MONDAY && m == 5 && y != 2002)
					// last Monday of August (Summer Bank Holiday)
					|| (d >= 25 && w == Weekday.MONDAY && m == 8)
					// Christmas (possibly moved to Monday or Tuesday)
					|| (m == 12 && (d == 25 || (d == 27
							&& (w == Weekday.MONDAY || w == Weekday.TUESDAY))))
					// Boxing Day (possibly moved to Monday or Tuesday)
					|| (m == 12 && (d == 26 || (d == 28
							&& (w == Weekday.MONDAY || w == Weekday.TUESDAY))))
					// June 3rd, 2002 only (Golden Jubilee Bank Holiday,
					// June 4rd, 2002 only (special Spring Bank Holiday)
					|| ((d == 3 || d == 4) && m == 6 && y == 2002)
					// December 31st, 1999 only
					|| (d == 31 && m == 12 && y == 1999)
			) {
				return true;
			}

			return false;
		}
	}

	private class UnitedKingdomLSECalendar extends
			UnitedKingdomSettlementCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "UnitedKingdom LSE Calendar";
		}

		/**
		 * 입력된 날짜가 휴일인지를 판단한다.
		 *
		 * @param date	날짜
		 * @return 	휴일이면 {@code true}, 그렇지 않으면 {@code false}
		 */
		public boolean isHoliday(final Date date) {
			// UnitedKingdom SettlementCalendar Holiday
			if (super.isHoliday(date)) {
				return true;
			}

			return false;
		}
	}
}
