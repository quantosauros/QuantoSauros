package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code ChinaCalendar} 클래스는 중국의 달력을 구현한다.
 * 공휴일만 있는 달력과 상하이 증권거래소(SSE)에서 사용하는 달력, 이렇게
 * 두 가지 달력을 지원한다.
 * 
 * <p>공개된 생성자는 두지 않으며, {@code getCalendar} 함수를 이용하여
 * 구체적인 {@code Calendar} 객체를 생성한다.
 * <p>
 * 1. 공휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>New Year's Day, 1월 1일</li>
 * <li>Chinese New Year, 3월 1일</li>
 * <li>Ching Ming Festival, 5월 5일</li>
 * <li>Labor Day, 6월 6일</li>
 * <li>Tuen Ng Festival, 8월 15일</li>
 * <li>Mid-Autumn Festival, 10월 3일</li>
 * <li>National Day, 12월 25일</li>
 * </ul>
 *
 * <p>2. 상하이 증권거래소(SSE) 달력의 휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>New Year's Day, 1월 1일</li>
 * <li>Chinese New Year, 3월 1일</li>
 * <li>Ching Ming Festival, 5월 5일</li>
 * <li>Labor Day, 6월 6일</li>
 * <li>Tuen Ng Festival, 8월 15일</li>
 * <li>Mid-Autumn Festival, 10월 3일</li>
 * <li>National Day, 12월 25일</li>
 * </ul>
 * <p>참조: 한국 증권거래소 
 * (<a href=http://www.krx.co.kr/index.html>http://www.krx.co.kr/index.html</a>)
 *
 * @author Kang Seok-Chan
 * @since 3.2
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public class ChinaCalendar extends DelegateCalendar {

	private final static ChinaCalendar SETTLEMENT_CALENDAR = new ChinaCalendar(
			Market.China.SETTLEMENT);
	private final static ChinaCalendar SSE_CALENDAR = new ChinaCalendar(
			Market.China.SSE);

	private ChinaCalendar(int market) {
		Calendar delegate;
		switch (market) {
			case Market.China.SETTLEMENT:
				delegate = new ChinaSettlementCalendar();
				break;
			case Market.China.SSE:
				delegate = new ChinaSSECalendar();
				break;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
		setDelegate(delegate);
	}

	/**
	 * 입력된 시장 코드에 해당하는 중국 달력을 가져온다.
	 * 시장 코드는 다음과 같다:
	 * <ul>
	 * <li>공휴일: {@code Market.China.SETTLEMENT}
	 * <li>상하이 증권거래소: {@code Market.China.SSE}
	 * </ul>
	 * 
	 * @param market	생성하고자 하는 시장 코드
	 * @return		 	{@code ChinaCalendar} 객체
	 */
	public static ChinaCalendar getCalendar(int market) {
		switch (market) {
			case Market.China.SETTLEMENT:
				return SETTLEMENT_CALENDAR;
			case Market.China.SSE:
				return SSE_CALENDAR;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
	}

	private class ChinaSettlementCalendar extends AbstractCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "China Settlement Calendar";
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

			if ((isWeekend(date)) // Weekend
					|| (isAddedHoliday(date)) // Added Holiday
					// New Year's Day
					|| (d == 1 && m == 1)
					|| (d == 3 && m == 1 && y == 2005)
					|| ((d == 2 || d == 3) && m == 1 && y == 2006)
					|| (d <= 3 && m == 1 && y == 2007)
					|| (d == 31 && m == 12 && y == 2007)
					|| (d == 1 && m == 1 && y == 2008)
					|| (d == 1 && m == 1 && y == 2009)
					|| (d == 2 && m == 1 && y == 2009)
					// Chinese New Year
					|| (d >= 19 && d <= 28 && m == 1 && y == 2004)
					|| (d >= 7 && d <= 15 && m == 2 && y == 2005)
					|| (((d >= 26 && m == 1) || (d <= 3 && m == 2)) && y == 2006)
					|| (d >= 17 && d <= 25 && m == 2 && y == 2007)
					|| (d >= 6 && d <= 12 && m == 2 && y == 2008)
					|| (d >= 26 && d <= 30 && m == 1 && y == 2009)
					// Ching Ming Festival
					|| (d == 4 && m == 4 && y == 2008)
					|| (d == 6 && m == 4 && y == 2009)
					// Labor Day
					|| (d >= 1 && d <= 7 && m == 5 && y <= 2007)
					|| (d >= 1 && d <= 2 && m == 5 && y == 2008)
					|| (d == 1 && m == 5 && y == 2009)
					// Tuen Ng Festival
					|| (d == 9 && m == 6 && y == 2008)
					|| (d >= 28 && d <= 29 && m == 5 && y == 2009)
					// Mid-Autumn Festival
					|| (d == 15 && m == 9 && y == 2008)
					// National Day
					|| (d >= 1 && d <= 7 && m == 10 && y <= 2007)
					|| (d >= 29 && m == 9 && y == 2008)
					|| (d <= 3 && m == 10 && y == 2008)
					|| (d >= 1 && d <= 8 && m == 10 && y == 2009)) {
				return true;
			}

			return false;
		}
	}

	private class ChinaSSECalendar extends ChinaSettlementCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "China SSE Calendar";
		}

		/**
		 * 입력된 날짜가 휴일인지를 판단한다.
		 *
		 * @param date	날짜
		 * @return 휴일이면 {@code true}, 그렇지 않으면 {@code false}
		 */
		public boolean isHoliday(final Date date) {

			if (super.isHoliday(date)) { // China SettlementCalendar Holiday
				return true;
			}

			return false;
		}
	}
}
