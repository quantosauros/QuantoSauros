package com.quantosauros.common.date.daycounter;

import com.quantosauros.common.date.Date;
/**
* ISDA
* <ul>
* <li> "Actual/Actual (ISDA)"
* <li> "Actual/Actual (Historical)"
* <li> "Actual/365" (by ISDA)
* </ul>
* <p>ICMA(formerly ISMA): US treasury convention
* <ul>
* <li> "Actual/Actual (BOND)"
* <li> "ISMA-99"
* </ul>
* 
* <p>AFB
* <ul>
* <li> "Actual/Actual (EURO)"
* <li> "ISMA-Year"
* <li> "Actual/365L"
* </ul>
* 
* @author Jae-Heon Kim
* @since 3.2 
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.08.28
* 
* TODO:: Should we implement ICMA case?
------------------------------------------------------------------------------*/
public class ActualActual extends AbstractDayCounter {

	public static final int ISDA = 11;
	public static final int HISTORICAL = 12;
	public static final int ICMA = 21;
	public static final int ISMA = 22;
	public static final int BOND = 23;
	public static final int AFB = 31;
	public static final int EURO = 32;
	
	private DayCounter _delegate;
	
	private static final ActualActual ISDA_INSTANCE = new ActualActual(ISDA);
	private static final ActualActual ICMA_INSTANCE = new ActualActual(ICMA);
	private static final ActualActual AFB_INSTANCE = new ActualActual(AFB);
	
	public static final ActualActual getInstance(int convention) {
		switch (convention) {
			case DayCountConvention.ACTUAL_ACTUAL:
			case ISDA:
			case HISTORICAL:
				return ISDA_INSTANCE;
			case ICMA:
			case ISMA:
			case BOND:
				return ICMA_INSTANCE;
			case AFB:
			case EURO:
				return AFB_INSTANCE;
			default:
				throw new DayCounterException("Unknown Actual/Actual convention: "
						+ convention);
		}
	}
	
	public static final ActualActual getInstance(String legacyCode) {
		int convention = Integer.parseInt(legacyCode);
		return getInstance(convention);
	}
		
	public String getName() {
		return _delegate.getName();
	}

	public double getYearFraction(Date start, Date end) {
		return _delegate.getYearFraction(start, end);
	}

	private ActualActual(int convention) {
		super();
		
		switch (convention) {
			case DayCountConvention.ACTUAL_ACTUAL:
			case ISDA:
			case HISTORICAL:
				_delegate = new ISDAImpl();
				break;
			case ICMA:
			case ISMA:
			case BOND:
				_delegate = new ICMAImpl();
				break;
			case AFB:
			case EURO:
				_delegate = new AFBImpl();
				break;
			default:
				throw new DayCounterException("Unknown Actual/Actual convention: "
						+ convention);
		}
	}
	
	private static final class ISDAImpl extends AbstractDayCounter {

		private static String _name = "Actual/Actual (ISDA)";
		
		public String getName() {
			return _name;
		}
		
		public double getYearFraction(Date start, Date end) {
			double diy1 = start.isLeapYear() ? 366 : 365;
			double diy2 = end.isLeapYear() ? 366 : 365;
			double doy1 = start.getDayCountOfYear();
			double doy2 = end.getDayCountOfYear();
			
			// Include the first day
			double f1 = (diy1 - doy1 + 1) / diy1;
			// Exclude the last day
			double f2 = (doy2 - 1) / diy2;
			
			double result = f1 + f2 + end.getYear() - start.getYear() - 1;
			return result;
		}
		
	}
	
	// TODO:: Implement getYearFraction() for ICMA convention
	private static final class ICMAImpl extends AbstractDayCounter {

		private static String _name = "Actual/Actual (ICMA)";
		
		public String getName() {
			return _name;
		}
		
		public double getYearFraction(Date start, Date end) {
			throw new DayCounterException("NOT YET Implemented: " + _name);
		}
	}

	private static final class AFBImpl extends AbstractDayCounter {

		private static String _name = "Actual/Actual (AFB)";
		
		public String getName() {
			return _name;
		}
		
		public double getYearFraction(Date start, Date end) {
			Date newEnd = end;
			Date oneYearBack = end;
			double nyears = 0.0;
			while (oneYearBack.compareTo(start) > 0) {
				oneYearBack = newEnd.plusYears(-1);
				if (oneYearBack.getMonth() == 2 
						&& oneYearBack.getDayOfMonth() == 28
						&& oneYearBack.isLeapYear()) {
					oneYearBack = oneYearBack.plusDays(1);
				}
				if (oneYearBack.compareTo(start) >= 0) {
					nyears += 1.0;
					newEnd = oneYearBack;
				}
			}
			double denominator = 365.0;
			Date feb29 = null;
			boolean includeFeb29 = false;
			if (newEnd.isLeapYear()) {
				feb29 = new Date("" + newEnd.getYear() + "0229");
				includeFeb29 = true;
			} else if (start.isLeapYear()) {
				feb29 = new Date("" + start.getYear() + "0229");
				includeFeb29 = true;
			}
			if (includeFeb29) {
				if (start.compareTo(feb29) <= 0 && newEnd.compareTo(feb29) > 0) {
					denominator += 1.0;
				}
			}
			double numerator = countDays(start, newEnd);
			return nyears +  numerator / denominator;
		}
	}


}
