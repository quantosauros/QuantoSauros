package com.quantosauros.common.date.daycounter;

import java.io.Serializable;

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
public interface DayCounter extends Serializable {

	public String getName();

	public double countDays(Date start, Date end);
	public double getYearFraction(Date start, Date end);

}
