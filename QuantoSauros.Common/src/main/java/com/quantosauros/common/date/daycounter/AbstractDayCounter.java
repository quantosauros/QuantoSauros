package com.quantosauros.common.date.daycounter;

import com.quantosauros.common.date.Date;

/**
* 
* @author Jae-Heon Kim
* @since 3.2 
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.08.28
------------------------------------------------------------------------------*/
public abstract class AbstractDayCounter implements DayCounter {
	
	protected AbstractDayCounter() {
		// Empty
	}
	
	public double countDays(Date start, Date end) {
		double count = (double)start.getDays(end);
		return count;
	}

}
