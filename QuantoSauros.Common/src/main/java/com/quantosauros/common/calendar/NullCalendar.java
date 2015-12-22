package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code NullCalendar}는 휴일이 없는 달력이다. 
 * 주말을 포함해 모든 날짜가 Business Day로 처리된다.
 * <p>이 클래스에는 인스턴스가 1개만 존재하므로 공개된 생성자는 두지 않으며,
 * {@code getInstance} 함수를 통해 객체를 가져온다.
 * 
 * @author Kang Seok-Chan
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.11.11
------------------------------------------------------------------------------*/

public final class NullCalendar extends AbstractCalendar {

	private static NullCalendar instance = null;

	/**
	 * {@code NullCalendar} 객체를 가져온다.
	 *
	 * @return	{@code NullCalendar} 객체
	 */
	public static NullCalendar getInstance() {
		if (instance == null) {
			instance = new NullCalendar();
		}
		return instance;
	}

	private NullCalendar() {
		// cannot be directly instantiated - Singleton instance
	}

	/**
	 * 이 {@code NullCalendar}의 이름을 반환한다.
	 *
	 * @return 이름
	 */
	public String getName() {
		return "Null Calendar";
	}

	/**
	 * 입력된 날짜가 휴일인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return 휴일이 없는 달력이므로 항상 {@code false}를 반환한다.
	 */
	public boolean isHoliday(final Date date) {
		// No Holiday
		return false;
	}
}
