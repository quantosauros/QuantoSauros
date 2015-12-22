package com.quantosauros.common.date.daycounter;
/**
* This class should not be used other than by 
* {@link com.quantosauros.common.date.fistglobal.common.DayCountFraction} class.
* To get the specific {@code DayCounter} instance, use {@code getInstance()} 
* methods defined in the classes extending {@link AbstractDayCounter}. 
*
* @author Jae-Heon Kim
* @since 3.2
*
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09.01
------------------------------------------------------------------------------*/
public class DayCounterFactory {

	public static DayCounter getInstance(int convention) {
		switch(convention) {
			case DayCountConvention.ACTUAL_365:
				return Actual365.getInstance();
			case DayCountConvention.ACTUAL_360:
				return Actual360.getInstance();
			case DayCountConvention.ACTUAL_ACTUAL:
				return ActualActual.getInstance(convention);
			case DayCountConvention.D30_360:
				return D30360.getInstance(convention);
			case DayCountConvention.D30E_360:
				return D30360.getInstance(convention);
			default:
				throw new DayCounterException("Unknown day count convention: "
						+ convention);
		}
	}
	
	public static final DayCounter getInstance(String code) {
		int convention = Integer.parseInt(code);
		return getInstance(convention);
	}
}
