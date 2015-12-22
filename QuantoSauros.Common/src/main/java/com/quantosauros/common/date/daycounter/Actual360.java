package com.quantosauros.common.date.daycounter;

import com.quantosauros.common.date.Date;
/**
* "Actual/360" a.k.a.:
* <ul>
* <li> "Act/360"
* <li> "A/360"
* <li> "French"
* </ul>
* @author Jae-Heon Kim
* @since 3.2 
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09.04
------------------------------------------------------------------------------*/
public class Actual360 extends AbstractDayCounter {
	
	private static final String NAME = "Actual/360";

	private static final Actual360 ACTUAL_360 = new Actual360();
	
	public static final Actual360 getInstance() {
		return ACTUAL_360;
	}
	
	private Actual360() {
		// empty
	}
	
	public String getName() {
		return NAME;
	}

	public double getYearFraction(Date start, Date end) {
		double numerator = countDays(start, end);
		double denominator = 360.0;
		return (numerator / denominator);
	}

    // This method is called immediately after an object of this class is deserialized.
    // This method returns the singleton instance.
    protected Object readResolve() {
        return ACTUAL_360;
    }
}
