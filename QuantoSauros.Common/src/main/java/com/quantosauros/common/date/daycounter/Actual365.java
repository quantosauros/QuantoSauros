package com.quantosauros.common.date.daycounter;

import com.quantosauros.common.date.Date;
/**
* "Actual/365" a.k.a.:
* <ul>
* <li> "Actual/365 (Fixed)"
* <li> "Act/365 (Fixed)"
* <li> "A/365 (Fixed)"
* <li> "A/365F"
* <li> "English"
* </ul>
* 
* @author Jae-Heon Kim
* @since 3.2 
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09.04
------------------------------------------------------------------------------*/
public class Actual365 extends AbstractDayCounter {

	private static final Actual365 ACTUAL_365 = new Actual365();
	
	public static final Actual365 getInstance() {
		return ACTUAL_365;
	}
	
	private Actual365() {
		// empty
	}
	
	public String getName() {
		return "Actual/365";
	}

	public double getYearFraction(Date start, Date end) {
		double numerator = countDays(start, end);
		double denominator = 365.0;
		return (numerator / denominator);
	}


    // This method is called immediately after an object of this class is deserialized.
    // This method returns the singleton instance.
    protected Object readResolve() {
        return ACTUAL_365;
    }
}
