package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code SouthKoreaCalendar} 클래스는 대한민국의 달력을 구현한다.
 * 공휴일만 있는 달력과 대한민국 증권거래소(KRX)에서 사용하는 달력,
 * 이렇게 두 가지 달력을 지원한다.
 * 
 * <p>공개된 생성자는 두지 않으며, {@code getCalendar} 함수를 이용하여
 * 구체적인 {@code Calendar} 객체를 생성한다.
 * <p>
 * 1. 공휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>신정 (New Year's Day), 1월 1일</li>
 * <li>삼일절 (Independence Day), 3월 1일</li>
 * <li>어린이날 (Children's Day), 5월 5일</li>
 * <li>현충일 (Memorial Day), 6월 6일</li>
 * <li>광복절 (Liberation Day), 8월 15일</li>
 * <li>개천절 (National Foundation Day), 10월 3일</li>
 * <li>크리스마스 (Christmas), 12월 25일</li>
 * <li>설날 연휴 (Lunar New Year), 음력 12월 31일 ~ 1월 2일</li>
 * <li>석가탄신일 (Buddha's birthday), 음력 4월 8일</li>
 * <li>추석 연휴 (Harvest Moon Day), 음력 8월 14일 ~ 8월 16일</li>
 * </ul>

 * <p>2. 한국 증권거래소(KRX) 휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>신정 (New Year's Day), 1월 1일</li>
 * <li>삼일절 (Independence Day), 3월 1일</li>
 * <li>근로자의 날 (Labor's Day), 5월 1일</li>
 * <li>어린이날 (Children's Day), 5월 5일</li>
 * <li>현충일 (Memorial Day), 6월 6일</li>
 * <li>광복절 (Liberation Day), 8월 15일</li>
 * <li>개천절 (National Foundation Day), 10월 3일</li>
 * <li>크리스마스 (Christmas), 12월 25일</li>
 * <li>설날 연휴 (Lunar New Year), 음력 12월 31일 ~ 1월 2일</li>
 * <li>석가탄신일 (Buddha's birthday), 음력 4월 8일</li>
 * <li>추석 연휴 (Harvest Moon Day), 음력 8월 14일 ~ 8월 16일</li>
 * <li>증시 폐장일 (Closing Day), 연 마지막 거래일
 * (기본적으로 12월 31일이나 휴일일 경우 앞당겨짐)</li>
 * </ul>
 * 참조:한국 증권거래소 (<a href=http://www.krx.co.kr/index.html>http://www.krx.co.kr/index.html</a>)
 *
 * @author Kang Seok-Chan
 * @since 3.2
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/

public class SouthKoreaCalendar extends DelegateCalendar {

	private final static SouthKoreaCalendar SETTLEMENT_CALENDAR
		= new SouthKoreaCalendar(Market.SouthKorea.SETTLEMENT);
	private final static SouthKoreaCalendar KRX_CALENDAR
		= new SouthKoreaCalendar(Market.SouthKorea.KRX);

	private SouthKoreaCalendar(int market) {
		Calendar delegate;
		switch (market) {
			case Market.SouthKorea.SETTLEMENT:
				delegate = new SouthKoreaSettlementCalendar();
				break;
			case Market.SouthKorea.KRX:
				delegate = new SouthKoreaKRXCalendar();
				break;
			default:
				throw new IllegalArgumentException("Unknown Market Code: "
						+ market);
			}
		setDelegate(delegate);
	}

	/**
	 * 입력된 시장 코드에 해당하는 대한민국 달력을 가져온다.
	 * 시장 코드는 다음과 같다:
	 * <ul>
	 * <li>공휴일: {@code Market.SouthKorea.SETTLEMENT}
	 * <li>대한민국 증권거래소: {@code Market.SouthKorea.KRX}
	 * </ul>
	 *
	 * @param market	생성하고자 하는 시장 코드
	 * @return		 	{@code SouthKoreaCalendar} 객체
	 */
	public static SouthKoreaCalendar getCalendar(int market) {
		switch (market) {
			case Market.SouthKorea.SETTLEMENT:
				return SETTLEMENT_CALENDAR;
			case Market.SouthKorea.KRX:
				return KRX_CALENDAR;
			default:
				throw new IllegalArgumentException("Unknown Market Code: "
						+ market);
		}
	}

	private class SouthKoreaSettlementCalendar extends AbstractCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "SouthKorea Settlement Calendar";
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

			LunarCalculator lc = new LunarCalculator();
			String lunar = lc.toLunar(date.toString());
			
			// 2012.02.07 Jae-Heon Kim
			int lm = Integer.parseInt(lunar.substring(4, 6));
			int ld = Integer.parseInt(lunar.substring(6, 8));
//			Date lunarDate = new Date(lunar);
//			int lm = lunarDate.getMonth();
//			int ld = lunarDate.getDayOfMonth();

			if ((isWeekend(date)) // Weekend
					// Added Holiday
					|| (isAddedHoliday(date)) 
					// New Year's Day
					|| ((m == 1) && (d == 1)) 
					// Independence Day
					|| ((m == 3) && (d == 1)) 
					// Arbour Day
					|| (d == 5 && m == 4 && y <= 2005)
		            // Labour Day
					|| (d == 1 && m == 5)
					// Children's Day
					|| (d == 5 && m == 5)
		            // Memorial Day
		            || (d == 6 && m == 6)
		            // Constitution Day
		            || (d == 17 && m == 7 && y <= 2007)
		            // Liberation Day
		            || (d == 15 && m == 8)
		            // National Foundation Day
		            || (d == 3 && m == 10)
		            // Hangul Proclamation of Korea
					|| ((m == 10) && (d == 9) && (y >= 2013))
					// Christmas Day
					|| ((m == 12) && (d == 25)) 
					//Lunar Calendar
					|| ((lm == 12) && (ld == 31)) // Lunar New Year1
					|| ((lm == 1) && (ld == 1)) // Lunar New Year2
					|| ((lm == 1) && (ld == 2)) // Lunar New Year3
					|| ((lm == 4) && (ld == 8)) // Buddha's birthday
					|| ((lm == 8) && (ld == 14)) // Harvest Moon Day1
					|| ((lm == 8) && (ld == 15)) // Harvest Moon Day2
					|| ((lm == 8) && (ld == 16)) // Harvest Moon Day3
					// Election Days
		            || (d == 15 && m == 4 && y == 2004)    // National Assembly
		            || (d == 31 && m == 5 && y == 2006)      // Regional election
		            || (d == 19 && m == 12 && y == 2007) // Presidency
		            || (d ==  9 && m == 4 && y == 2008)    // National Assembly
		            || (d ==  2 && m == 6 && y == 2010)     // Local election
		            || (d == 11 && m == 4 && y == 2012)    // National Assembly
		            || (d == 19 && m == 12 && y == 2012) // Presidency
					|| (d ==  4 && m == 6 && y == 2014)     // Local election
					|| (d == 13 && m == 4 && y == 2016)    // National Assembly
					|| (d == 20 && m == 12 && y == 2017) // Presidency
					//TODO 대체휴일 입력
					|| (d == 26 && m == 9 && y == 2018)
					|| (d == 7 && m == 5 && y == 2018)
					|| (d == 6 && m == 10 && y == 2017)
					|| (d == 30 && m == 1 && y == 2017)
					|| (d == 6 && m == 5 && y == 2016)
					|| (d == 10 && m == 2 && y == 2016) 
					|| (d == 29 && m == 9 && y == 2015)					
					|| (d == 14 && m == 8 && y == 2015)
					|| (d == 30 && m == 1 && y == 2014)
					|| (d == 10 && m == 9 && y == 2014)					
			) {
				return true;
			}

			return false;
		}
	}

	private class SouthKoreaKRXCalendar extends SouthKoreaSettlementCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "SouthKorea KRX Calendar";
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

			if ((super.isHoliday(date)) // SouthKorea SettlementCalendar Holiday
					|| ((m == 5) && (d == 1)) // Labor's Day
					|| ((m == 12) && (d == 31)) // Closing Day
					|| ((m == 12) && (d == 30) && (w == Weekday.FRIDAY)) // Closing Day
					|| ((m == 12) && (d == 29) && (w == Weekday.FRIDAY)) // Closing Day
			) {
				return true;
			}

			return false;
		}
	}
}
