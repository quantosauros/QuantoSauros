package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code UnitedStatesCalendar} 클래스는 미국의 달력을 구현한다.
 * 공휴일만 있는 달력과 뉴욕 증권거래소(NYSE)에서 사용하는 달력,
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
 * <li>Martin Luther King's birthday, 1월 3번째 월요일</li>
 * <li>Presidents' Day, 2월 3번째 월요일</li>
 * <li>Good Friday</li>
 * <li>Memorial Day, 5월 마지막 월요일</li>
 * <li>Independence Day, 7월 4일</li>
 * <li>Labor Day, 9월 1번째 월요일</li>
 * <li>Columbus Day, 10월 2번째 월요일</li>
 * <li>Veterans' Day, 11월 11일</li>
 * <li>Thanksgiving Day, 11월 4번째 목요일</li>
 * <li>Christmas, 12월 25일</li>
 * </ul>
 *
 * <p>2. 뉴욕 증권거래소(NYSE) 휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>New Year's Day, 1월 1일</li>
 * <li>Martin Luther King's birthday, 1월 3번째 월요일</li>
 * <li>Presidents' Day, 2월 3번째 월요일</li>
 * <li>Good Friday</li>
 * <li>Memorial Day, 5월 마지막 월요일</li>
 * <li>Independence Day, 7월 4일</li>
 * <li>Labor Day, 9월 1번째 월요일</li>
 * <li>Thanksgiving Day, 11월 4번째 목요일</li>
 * <li>Christmas, 12월 25일</li>
 * </ul>
 * 참조:미국 뉴욕 증권거래소 (http://www.nyse.com)
 *
 * @author Kang Seok-Chan
 * @since 3.2
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public class UnitedStatesCalendar extends DelegateCalendar {

	private final static UnitedStatesCalendar SETTLEMENT_CALENDAR
		= new UnitedStatesCalendar(Market.UnitedStates.SETTLEMENT);

	private final static UnitedStatesCalendar NYSE_CALENDAR
		= new UnitedStatesCalendar(Market.UnitedStates.NYSE);

	private UnitedStatesCalendar(int market) {
		Calendar delegate;
		switch (market) {
			case Market.UnitedStates.SETTLEMENT:
				delegate = new UnitedStatesSettlementCalendar();
				break;
			case Market.UnitedStates.NYSE:
				delegate = new UnitedStatesNYSECalendar();
				break;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
		setDelegate(delegate);
	}

	/**
	 *  입력된 시장 코드에 해당하는 미국 달력을 가져온다.
	 * 시장 코드는 다음과 같다:
	 * <ul>
	 * <li>공휴일: {@code Market.UnitedStates.SETTLEMENT}
	 * <li>뉴욕 증권거래소: {@code Market.UnitedStates.NYSE}
	 * </ul>
	 * 
	 * @param market	생성하고자 하는 시장 코드
	 * @return		 	{@code UnitedKingdomCalendar} 객체
	 */
	public static UnitedStatesCalendar getCalendar(int market) {
		switch (market) {
			case Market.UnitedStates.SETTLEMENT:
				return SETTLEMENT_CALENDAR;
			case Market.UnitedStates.NYSE:
				return NYSE_CALENDAR;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
	}

	private class UnitedStatesSettlementCalendar extends
			UnitedStatesNYSECalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "UnitedStates Settlement Calendar";
		}

		/**
		 * 입력된 날짜가 휴일인지를 판단한다.
		 *
		 * @param date	날짜
		 * @return 	휴일이면 {@code true}, 그렇지 않으면 {@code false}
		 */
		public boolean isHoliday(final Date date) {
			int m = date.getMonth();
			int d = date.getDayOfMonth();
			Weekday w = getDayOfWeek(date);

			if (super.isHoliday(date) // UnitedStates NYSECalendar Holiday
					// Columbus Day (second Monday in October)
					|| ((d >= 8 && d <= 14) && w == Weekday.MONDAY && m == 10)
					// Veteran's Day (Monday if Sunday or Friday if Saturday)
					|| ((d == 11 || (d == 12 && w == Weekday.MONDAY) || (d == 10 && w == Weekday.FRIDAY)) && m == 11)) {
				return true;
			}

			return false;
		}
	}

	private class UnitedStatesNYSECalendar extends AbstractCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "UnitedStates NYSE Calendar";
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
					// New Year's Day (possibly moved to Monday if on Sunday)
					|| ((d == 1 || (d == 2 && w == Weekday.MONDAY)) && m == 1)
					// (or to Friday if on Saturday)
					|| (d == 31 && w == Weekday.FRIDAY && m == 12)
					// Martin Luther King's birthday (third Monday in January)
					|| ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == 1)
					// Washington's birthday (third Monday in February)
					|| ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == 2)
					// Good Friday
					|| ((dd == em - 3) && (y >= 2008)) 
					// Memorial Day (last Monday in May)
					|| (d >= 25 && w == Weekday.MONDAY && m == 5)
					// Independence Day (Monday if Sunday or Friday if Saturday)
					|| ((d == 4 || (d == 5 && w == Weekday.MONDAY)
							|| (d == 3 && w == Weekday.FRIDAY)) && m == 7)
					// Labor Day (first Monday in September)
					|| (d <= 7 && w == Weekday.MONDAY && m == 9)
					// Thanksgiving Day (fourth Thursday in November)
					|| ((d >= 22 && d <= 28) && w == Weekday.THURSDAY && m == 11)
					// Christmas (Monday if Sunday or Friday if Saturday)
					|| ((d == 25 || (d == 26 && w == Weekday.MONDAY)
							|| (d == 24 && w == Weekday.FRIDAY)) && m == 12)) {
				return true;
			}
			if (y >= 1998) {
	            if (// Martin Luther King's birthday (third Monday in January)
	                ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == 1)
	                // President Reagan's funeral
	                || (y == 2004 && m == 6 && d == 11)
	                // September 11, 2001
	                || (y == 2001 && m == 9 && (11 <= d && d <= 14))
	                // President Ford's funeral
	                || (y == 2007 && m == 1 && d == 2)
	                ) return true;
	        } else if (y <= 1980) {
	            if (// Presidential election days
	                ((y % 4 == 0) && m == 11 && d <= 7 && w == Weekday.TUESDAY)
	                // 1977 Blackout
	                || (y == 1977 && m == 7 && d == 14)
	                // Funeral of former President Lyndon B. Johnson.
	                || (y == 1973 && m == 1 && d == 25)
	                // Funeral of former President Harry S. Truman
	                || (y == 1972 && m == 11 && d == 28)
	                // National Day of Participation for the lunar exploration.
	                || (y == 1969 && m == 7 && d == 21)
	                // Funeral of former President Eisenhower.
	                || (y == 1969 && m == 3 && d == 31)
	                // Closed all day - heavy snow.
	                || (y == 1969 && m == 2 && d == 10)
	                // Day after Independence Day.
	                || (y == 1968 && m == 7 && d == 5)
	                // June 12-Dec. 31, 1968
	                // Four day week (closed on Wednesdays) - Paperwork Crisis
	                || (y == 1968 && dd >= 163 && w == Weekday.WEDNESDAY)
	                ) return true;
	        } else {
	            if (// Nixon's funeral
	                (y == 1994 && m == 4 && d == 27)
	                ) return true;
	        }
			return false;
		}
	}
}
