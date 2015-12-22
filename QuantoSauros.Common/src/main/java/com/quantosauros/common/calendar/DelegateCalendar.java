package com.quantosauros.common.calendar;

import com.quantosauros.common.date.Date;

/**
 * {@code DelegateCalendar} 클래스는 국가별 달력 클래스를 구현 시 상속받아야 하는
 * 추상 클래스이다. 국 각가의 시장별 달력은 이 클래스를 상속받는 국가별 달력
 * 클래스의 inner class로 구현한다.
 *    
 * @author Kang Seok-Chan
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public abstract class DelegateCalendar extends AbstractCalendar {

	private Calendar delegate;

	protected DelegateCalendar() {
	}

	protected void setDelegate(final Calendar calendar) {
		this.delegate = calendar;
	}

	/**
	 * 이 {@code DelegateCalendar} 객체의 이름을 가져온다.
	 *
	 * @return	이 {@code DelegateCalendar} 객체의 이름
	 */
	public String getName() {
		return delegate.getName();
	}

	/**
	 * 입력된 날짜가 business day인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return business day이면 {@code true}, 그렇지 않으면 {@code false}
	 */
	public boolean isBusinessDay(final Date date) {
		return delegate.isBusinessDay(date);
	}

	/**
	 * 입력된 날짜가 휴일인지를 판단한다.
	 *
	 * @param d	날짜
	 * @return 휴일이면 {@code true}, 그렇지 않으면 {@code false}
	 */
	public boolean isHoliday(final Date d) {
		return delegate.isHoliday(d);
	}

	/**
	 * 입력된 날짜가 주말인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return 주말이면 {@code true}, 그렇지 않으면 {@code false}
	 */
	public boolean isWeekend(final Date date) {
		return delegate.isWeekend(date);
	}

	/**
	 * 이 달력에 휴일을 추가한다.
	 *
	 * @param date	추가할 휴일의 날짜
	 */
	public void addHoliday(final Date date) {
		delegate.addHoliday(date);
	}

	/**
	 * {@code addHoliday} 함수를 사용해 추가된 휴일을 제거한다.
	 * <p>이 객체 생성 시 포함되어 있던 기본 휴일은 제거할 
	 * 수 없다.
	 *
	 * @param date	삭제할 휴일의 날짜
	 */
	public void removeHoliday(final Date date) {
		delegate.removeHoliday(date);
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
		return delegate.isAddedHoliday(date);
	}

	/**
	 * 입력된 날짜가 business day인지를 판단하여, business day가 아닌 경우에는 
	 * 주어진 {@code BusinessDayConvention}에 따라 날짜를 조정한다.
	 *
	 * @param date	날짜
	 * @return 	주어진 business day convention에 따라 조정된 날짜
	 */
	public Date adjustDate(final Date date, 
			final BusinessDayConvention convention) {
		return delegate.adjustDate(date, convention);
	}

	/**
	 * 입력된 날짜가 해당 월의 마지막 날인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return 	말일인 경우 {@code true}, 그렇지 않으면 {@code false}
	 */
	public boolean isLastBusinessDayOfMonth(final Date date) {
		return delegate.isLastBusinessDayOfMonth(date);
	}

	/**
	 * 입력된 날짜가 해당 월의 마지막 business day인지를 판단한다.
	 *
	 * @param date	날짜
	 * @return 	마지막 business day인 경우 {@code true}, 그렇지 않으면 {@code false}
	 */
	public Date getLastBusinessDayOfMonth(final Date date) {
		return delegate.getLastBusinessDayOfMonth(date);
	}
}
