package com.quantosauros.common.date.daycounter;

import com.quantosauros.common.date.Date;
/**
* 
* 1. "30U/360": NOT IMPLEMENTED
* <ul>
* <li> "30/360"
* <li> "US"
* </ul>
* 
* <p>2. "30U/360": DayCountFraction has it as D30_360
* <ul>
* <li> "30/360"
* <li> "Bond Basis"
* <li> "360/360"
* </ul>
* 
* <p>3. "30E/360": DayCountFraction has it as D30E_360
* <ul>
* <li> "Eurobond Basis" (ISDA 2006)
* <li> "30S/360"
* <li> "Special German"
* <li> "30/360 ICMA"
* </ul>
* 
* <p>4. "30E/360 ISDA": NOT IMPLEMENTED
* <ul>
* <li> "30E/360 (ISDA)"
* <li> "30E/360"
* <li> "Eurobond Basis" (ISDA 2000)
* <li> "German"
* <li> "German Master"
* <li> "360/360"
* </ul>
*  
* <p>5. "30E+/360": NOT IMPLEMENTED
* 
* @author Jae-Heon Kim
* @since 3.2 
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09.04
* 
* TODO:: Should we implement other 30/360 conventions?
------------------------------------------------------------------------------*/
public class D30360 extends AbstractDayCounter {

	public static final int US = 11;
	
	public static final int D30U_360 = 21;
	public static final int BOND_BASIS = 22;
	
	public static final int EU = 31;
	public static final int D30E_360 = 32;
	public static final int D30S_360 = 33;
	public static final int EUROBOND_BASIS = 34;
	
	private DayCounter _delegate;
	
	private static final D30360 US_INSTANCE = new D30360(US);
	private static final D30360 EU_INSTANCE = new D30360(EU);
	
	public static final D30360 getInstance(int convention) {
		switch (convention) {
			case US:
			case DayCountConvention.D30_360:
			case D30U_360:
			case BOND_BASIS:
				return US_INSTANCE;
			case DayCountConvention.D30E_360:
			case EU:
			case D30E_360:
			case D30S_360:
			case EUROBOND_BASIS:
				return EU_INSTANCE;
			default:
				throw new DayCounterException("Unknown 30/360 convention: "
						+ convention);
		}
	}
	
	public String getName() {
		return _delegate.getName();
	}

	public double getYearFraction(Date start, Date end) {
		return countDays(start, end) / 360.0;
	}
	
	public double countDays(Date start, Date end) {
		return _delegate.countDays(start, end);
	}
	
	private D30360(int convention) {
		super();
		
		switch (convention) {
			case US:
			case DayCountConvention.D30_360:
			case D30U_360:
			case BOND_BASIS:
				_delegate = new USImpl();
				break;
			case DayCountConvention.D30E_360:
			case EU:
			case D30E_360:
			case D30S_360:
			case EUROBOND_BASIS:
				_delegate = new EUImpl();
				break;
			default:
				throw new DayCounterException("Unknown 30/360 convention: "
						+ convention);
		}
	}
	
	private abstract static class AbstractD30360 extends AbstractDayCounter {
		private static final String NAME = "";
		
		protected abstract int adjustStartDay(int startDay);
		
		protected abstract int adjustEndDay(int startDay, int endDay);
						
		public String getName() {
			return NAME;
		}
		
		public double getYearFraction(Date start, Date end) {
			return (countDays(start, end) / 360.0);
		}
		
		public double countDays(Date start, Date end) {
			int d1 = adjustStartDay(start.getDayOfMonth());
			int d2 = adjustEndDay(start.getDayOfMonth(), end.getDayOfMonth());
			
			double ndays 
				= (end.getYear() - start.getYear()) * 360.0
					+ (end.getMonth() - start.getMonth()) * 30.0
					+ (d2 - d1);
			return ndays;
		}
	}

	private static final class USImpl extends AbstractD30360 {
		private static final String NAME = "30/360";
		
		public String getName() {
			return NAME;
		}
		
		protected int adjustStartDay(int startDay) {
			return Math.min(startDay, 30);
		}
		
		protected int adjustEndDay(int startDay, int endDay) {
			int d2 = endDay;
			if (d2 == 31 && startDay == 30) {
				d2 = 30;
			}
			return d2;
		}
	}
	
	private static final class EUImpl extends AbstractD30360 {
		private static final String NAME = "30E/360";
		
		public String getName() {
			return NAME;
		}

		protected int adjustStartDay(int startDay) {
			return Math.min(startDay, 30);
		}
		
		protected int adjustEndDay(int startDay, int endDay) {
			return Math.min(endDay, 30);
		}
	}
}
