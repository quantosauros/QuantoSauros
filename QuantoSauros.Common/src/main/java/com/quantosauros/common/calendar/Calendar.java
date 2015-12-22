package com.quantosauros.common.calendar;

import java.io.Serializable;

import com.quantosauros.common.date.Date;
/**
 * {@code Calendar}는 Calendar 클래스 구현을 위한 공통 인터페이스이다.
 * 
 * @author Kang Seok-Chan
 * @since 3.2
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public interface Calendar extends Serializable {
	// Jae-Heon Kim, 2009.11.17
	public static final int USE_NULL = 0;
	public static final int USE_DEFAULT = 1;

    public String getName();

    public boolean isBusinessDay(final Date date);

    public boolean isHoliday(final Date date);

    public boolean isWeekend(final Date date);

    public boolean isEndOfMonth(final Date date);

    public Date getEndOfMonth(final Date date);

    public boolean isLastBusinessDayOfMonth(final Date date);

    public Date getLastBusinessDayOfMonth(final Date date);

    public Weekday getDayOfWeek(final Date date);

    public void addHoliday(final Date date);

    public void removeHoliday(final Date date);

    public Date adjustDate(final Date date,
    		final BusinessDayConvention convention);

    public boolean isAddedHoliday(final Date date);

    //public long businessDaysBetween(final Date from, final Date to, boolean includeFirst, boolean includeLast);
    //public List<Date> getHolidayList(final Date from, final Date to, boolean includeWeekEnds);

}
