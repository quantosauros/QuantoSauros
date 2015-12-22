package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code JapanCalendar} 클래스는 일본의 달력을 구현한다.
 * 공휴일만 있는 달력과 도쿄 증권거래소(TSE)에서 사용하는 달력, 이렇게 
 * 두 가지 달력을 지원한다.
 * <p>공개된 생성자는 두지 않으며, {@code getCalendar} 함수를 이용하여
 * 구체적인 {@code Calendar} 객체를 생성한다.
 * <p>
 * 1. 공휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>New Year's Day 1, 1월 1일</li>
 * <li>Coming of Age Day, 1월 2번째 월요일</li>
 * <li>National Foundation Day, 2월 11일</li>
 * <li>Vernal Equinox, 음력 춘분</li>
 * <li>Showa Day, 4월 29일</li>
 * <li>Constitution Memorial Day, 5월 3일</li>
 * <li>Greenery Day, 5월 4일</li>
 * <li>Children's Day, 5월 5일</li>
 * <li>Marine Day, 7월 3번째 월요일</li>
 * <li>Respect for the Aged Day, 9월 3번째 월요일</li>
 * <li>Autumnal equinox, 음력 추분</li>
 * <li>Health and Sports Day, 10월 2번째 월요일</li>
 * <li>Culture Day, 11월 3일</li>
 * <li>Labor Thanksgiving Day, 11월 23일</li>
 * <li>Emperor's Birthday, 12월 23일</li>
 * </ul>
 *
 * <P>2. 도쿄 증권거래소(TSE) 휴일:
 * <ul>
 * <li>토요일</li>
 * <li>일요일</li>
 * <li>New Year's Day 1, 1월 1일</li>
 * <li>New Year's Day 2, 1월 2일</li>
 * <li>New Year's Day 3, 1월 3일</li>
 * <li>Coming of Age Day, 1월 2번째 월요일</li>
 * <li>National Foundation Day, 2월 11일</li>
 * <li>Vernal Equinox, 음력 춘분</li>
 * <li>Showa Day, 4월 29일</li>
 * <li>Constitution Memorial Day, 5월 3일</li>
 * <li>Greenery Day, 5월 4일</li>
 * <li>Children's Day, 5월 5일</li>
 * <li>Marine Day, 7월 3번째 월요일</li>
 * <li>Respect for the Aged Day, 9월 3번째 월요일</li>
 * <li>Autumnal equinox, 음력 추분</li>
 * <li>Health and Sports Day, 10월 2번째 월요일</li>
 * <li>Culture Day, 11월 3일</li>
 * <li>Labor Thanksgiving Day, 11월 23일</li>
 * <li>Emperor's Birthday, 12월 23일</li>
 * <li>Closing Day, 연 마지막 거래일
 * (기본적으로 12월 31일이나 휴일일 경우 앞당겨짐)</li>
 * </ul>
 * 참조:일본 도쿄 증권거래소 (<a href=http://www.tse.or.jp>http://www.tse.or.jp</a>)
 *
 * @author Kang Seok-Chan
 * @since 3.2
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public class JapanCalendar extends DelegateCalendar {

	private final static JapanCalendar SETTLEMENT_CALENDAR = new JapanCalendar(
			Market.Japan.SETTLEMENT);
	private final static JapanCalendar TSE_CALENDAR = new JapanCalendar(
			Market.Japan.TSE);

	private JapanCalendar(int market) {
		Calendar delegate;
		switch (market) {
			case Market.Japan.SETTLEMENT:
				delegate = new JapanSettlementCalendar();
				break;
			case Market.Japan.TSE:
				delegate = new JapanTSECalendar();
				break;
			default:
				throw new IllegalArgumentException("Unknown Market Code: "
						+ market);
		}
		setDelegate(delegate);
	}

	/**
	 * 입력된 시장 코드에 해당하는 일본 달력을 가져온다.
	 * 시장 코드는 다음과 같다:
	 * <ul>
	 * <li>공휴일: {@code Market.Japan.SETTLEMENT}
	 * <li>도쿄 증권거래소: {@code Market.Japan.TSE}
	 * </ul>
	 *
	 * @param market	생성하고자 하는 시장 코드
	 * @return		 	{@code JapanCalendar} 객체
	 */
	public static JapanCalendar getCalendar(int market) {
		switch (market) {
			case Market.Japan.SETTLEMENT:
				return SETTLEMENT_CALENDAR;
			case Market.Japan.TSE:
				return TSE_CALENDAR;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
	}

	private class JapanSettlementCalendar extends AbstractCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "Japan Settlement Calendar";
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

			double exactVernalEquinoxTime = 20.69115;
			double exactAutumnalEquinoxTime = 23.09;
			double diffPerYear = 0.242194;
			double movingAmount = (y - 2000) * diffPerYear;
			int nLeapYears = (y - 2000) / 4 + (y - 2000) / 100
					- (y - 2000) / 400;
			int ve = (int)(exactVernalEquinoxTime + movingAmount - nLeapYears);
			int ae = (int)(exactAutumnalEquinoxTime + movingAmount - nLeapYears);

			if ((isWeekend(date)) // Weekend
					|| (isAddedHoliday(date)) // Added Holiday
					|| (d == 1 && m == 1) // New Year's Day 1
					//  Coming of Age Day (2nd Monday in 1), was 1 15th until 2000
					|| (w == Weekday.MONDAY && (d >= 8 && d <= 14)
							&& m == 1 && y >= 2000)
					|| ((d == 15 || (d == 16 && w == Weekday.MONDAY)) && m == 1 && y < 2000)
					// National Foundation Day
					|| ((d == 11 || (d == 12 && w == Weekday.MONDAY)) && m == 2)
					// Vernal Equinox
					|| ((d == ve || (d == ve + 1 && w == Weekday.MONDAY)) && m == 3)
					// Greenery Day
					|| ((d == 29 || (d == 30 && w == Weekday.MONDAY)) && m == 4)
					|| (d == 3 && m == 5) // Constitution Memorial Day
					|| (d == 4 && m == 5) // Holiday for a Nation
					// Children's Day
					|| ((d == 5 || (d == 6 && w == Weekday.MONDAY)) && m == 5)
					// Marine Day (3rd Monday in JULY), was JULY 20th until 2003,
					// not a holiday before 1996
					|| (w == Weekday.MONDAY && (d >= 15 && d <= 21) && m == 7 && y >= 2003)
					|| ((d == 20 || (d == 21 && w == Weekday.MONDAY)) && m == 7
							&& y >= 1996 && y < 2003)
					// Respect for the Aged Day (3rd Monday in SEPTEMBER),
					// was SEPTEMBER 15th until 2003
					|| (w == Weekday.MONDAY && (d >= 15 && d <= 21) && m == 9 && y >= 2003)
					|| ((d == 15 || (d == 16 && w == Weekday.MONDAY)) && m == 9 && y < 2003)
					// If a single day falls between Respect for the Aged Day
					// and the Autumnal Equinox, it is holiday
					|| (w == Weekday.TUESDAY && d + 1 == ae && d >= 16
							&& d <= 22 && m == 9 && y >= 2003)
					// Autumnal Equinox
					|| ((d == ae || (d == ae + 1 && w == Weekday.MONDAY)) && m == 9)
					// Health and Sports Day (2nd Monday in OCTOBER),
					// was OCTOBER 10th until 2000
					|| (w == Weekday.MONDAY && (d >= 8 && d <= 14) && m == 10 && y >= 2000)

					|| ((d == 10 || (d == 11 && w == Weekday.MONDAY))
							&& m == 10 && y < 2000)
					// National Culture Day
					|| ((d == 3 || (d == 4 && w == Weekday.MONDAY)) && m == 11)
					// Labor Thanksgiving Day
					|| ((d == 23 || (d == 24 && w == Weekday.MONDAY)) && m == 11)
					|| ((d == 23 || (d == 24 && w == Weekday.MONDAY))
							&& m == 12 && y >= 1989) // Emperor's Birthday
					|| (d == 31 && m == 12) // Bank Holiday
					// Marriage of Prince Akihito
					|| (d == 10 && m == 4 && y == 1959)
					// Rites of Imperial Funeral
					|| (d == 24 && m == 2 && y == 1989)
					// Enthronement Ceremony
					|| (d == 12 && m == 11 && y == 1990)
					// Marriage of Prince Naruhito
					|| (d == 9 && m == 6 && y == 1993)
					// Holidays observed (moved) to 6-MAY
					|| ((d == 6) && (m == 5) && (y == 2008 || y == 2009))
			) {
				return true;
			}

			return false;
		}
	}

	private class JapanTSECalendar extends JapanSettlementCalendar {

		/**
		 * 이 객체의 이름을 반환한다.
		 *
		 * @return Calendar 이름
		 */
		public String getName() {
			return "Japan TSE Calendar";
		}

		/**
		 * 입력된 날짜가 휴일인지를 판단한다.
		 *
		 * @param date	날짜
		 * @return 휴일이면 {@code true}, 그렇지 않으면 {@code false}
		 */
		public boolean isHoliday(final Date date) {
			int m = date.getMonth();
			int d = date.getDayOfMonth();
			Weekday w = getDayOfWeek(date);

			if ((super.isHoliday(date)) // SouthKorea SettlementCalendar Holiday
					|| ((m == 1) && (d == 2)) // New Year's Day 2
					|| ((m == 1) && (d == 3)) // New Year's Day 3
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
