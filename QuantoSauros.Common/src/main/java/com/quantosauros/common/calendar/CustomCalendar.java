package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;


/**
 * {@code CustomCalendar}는 객체 생성 시에는 주말이 휴일로 지정되어 있으며,
 * 원하는 휴일을 추가할 수 있는 달력이다.
 * <p> {@code getCalendar} 함수를 통해 객체를 생성한다.
 * @author Kang Seok-Chan
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public class CustomCalendar extends DelegateCalendar {

	private final static CustomCalendar CUSTOM_CALENDAR = new CustomCalendar(
			Market.Custom.DEFAULT);

	private CustomCalendar(int market) {
		Calendar delegate;
		if (market == Market.Custom.DEFAULT) {
			delegate = new CustomWeekendCalendar();
		} else {
			throw new IllegalArgumentException("Unknown Market Code: " + market);
		}
		/*switch (market) {
			case Market.Custom.DEFAULT:
				delegate = new CustomWeekendCalendar();
				break;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " 
						+ market);
		}*/
		setDelegate(delegate);
	}

	/**
	 * 주어진 {@code market} 값에 해당하는 {@code CustomCalendar} 객체를 반환한다.
	 * 유효한 시장 코드의 값은 다음과 같다:
	 * <ul>
	 * <li> {@link Market.Custom#DEFAULT}
	 * </ul> 
	 *
	 * @param market	시장 코드
	 * @return		 	CustomCalendar 객체
	 */
	public static CustomCalendar getCalendar(int market) {
		if (market == Market.Custom.DEFAULT) {
			return CUSTOM_CALENDAR;
		}
		throw new IllegalArgumentException("Unknown Market Code: " + market);

		/*switch (market) {
			case Market.Custom.DEFAULT:
				return CUSTOM_CALENDAR;
			default:
				throw new IllegalArgumentException("Unknown Market Code: " 
						+ market);
		}*/
	}

	private class CustomWeekendCalendar extends AbstractCalendar {

		/**
		 * 이 {@code CustomCalendar} 객체의 이름을 반환한다.
		 *
		 * @return 이름
		 */
		public String getName() {
			return "Custom Weekend Calendar";
		}

		/**
		 * 입력된 날짜가 휴일인지를 판단한다.
		 *
		 * @param date	날짜
		 * @return 휴일이면 {@code true}, 그렇지 않으면 {@code false}
		 */
		public boolean isHoliday(final Date date) {
			if ((isWeekend(date)) // Weekend
					|| (isAddedHoliday(date)) // Added Holiday
			// Add additional holidays here...
			) {
				return true;
			}

			return false;
		}
	}
}
