package com.quantosauros.common.date.daycounter;
/**
* To be compatible with the existing implementation of 
* {@link com.quantosauros.common.date.fistglobal.common.DayCountFraction},
* we define five code values in this class. It should be used in conjunction
* with {@link DayCounterFactory} class.
* 
* @author Jae-Heon Kim
* @since 3.2 
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09.01
------------------------------------------------------------------------------*/
public final class DayCountConvention {

	public static final int ACTUAL_365 = 1;
	public static final int ACTUAL_360 = 2;
	public static final int ACTUAL_ACTUAL = 3;
	public static final int D30_360 = 4;
	public static final int D30E_360 = 5;
	
}
