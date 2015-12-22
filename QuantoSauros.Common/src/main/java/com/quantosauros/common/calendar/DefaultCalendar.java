package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code DefaultCalendar}는 주말만 휴일로 지정되어 있는 기본 달력이다.
 * <p>이 클래스에는 인스턴스가 1개만 존재하므로 공개된 생성자는 두지 않으며,
 * {@code getInstance} 함수를 통해 객체를 가져온다.
 * 
 * @author Kang Seok-Chan
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public final class DefaultCalendar extends AbstractCalendar {

	private static DefaultCalendar instance = null;

	/**
	 * {@code DefaultCalendar} 객체를 가져온다.
	 *
	 * @return	{@code DefaultCalendar} 객체
	 */
	public static DefaultCalendar getInstance() {
		if (instance == null) {
			instance = new DefaultCalendar();
		}
		return instance;
	}

	private DefaultCalendar() {
		// cannot be directly instantiated - Singleton instance
	}

	/**
	 * 이 {@code DefaultCalendar} 객체의 이름을 반환한다.
	 *
	 * @return	이름
	 */
	public String getName() {
		return "Default Calendar";
	}

	/**
	 * 입력된 날짜가 휴일인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return 주말 외의 휴일이 없으므로 주말인 경우에는 {@code true}, 
	 * 			그렇지 않으면 {@code false}
	 */
	public boolean isHoliday(final Date date) {
		// Check Weekend
		if (isWeekend(date)) {
			return true;
		}

		return false;
	}
}
