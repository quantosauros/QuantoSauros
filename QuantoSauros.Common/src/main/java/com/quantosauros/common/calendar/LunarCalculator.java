package com.quantosauros.common.calendar;

import java.io.Serializable;

import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.Calendar;

/**
 * {@code LunarCalculator} 클래스는 음력과 양력 날짜 같의 변환 기능을 제공한다.
 *
 * @author Kang Seok-Chan
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
------------------------------------------------------------------------------*/
public class LunarCalculator implements Serializable {

	private Calendar _icuCalendar;
	private ChineseCalendar _icuChineseCalendar;	

	/**
	 * 생성자
	 */
	public LunarCalculator() {
		_icuCalendar = Calendar.getInstance();
		_icuChineseCalendar = new ChineseCalendar();
	}

	/**
	 * 입력한 양력 날짜에 해당하는 음력 날짜를 가져온다.
	 * 음력으로 변환하고자 하는 양력 날짜를 8자리 문자열로 입력한다.
	 *
	 * @param yyyymmdd		양력 날짜
	 * @return				음력 날짜
	 */
	public synchronized String toLunar(String yyyymmdd) {
		if (yyyymmdd == null) {
			return "";
		}

		String date = yyyymmdd.trim();
		if (date.length() != 8) {
			if (date.length() == 4) {
				date = date + "0101";
			} else if (date.length() == 6) {
				date = date + "01";
			} else if (date.length() > 8) {
				date = date.substring(0, 8);
			} else {
				return "";
			}
		}

		_icuCalendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		_icuCalendar.set(Calendar.MONTH,
				Integer.parseInt(date.substring(4, 6)) - 1);
		_icuCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date
				.substring(6)));

		_icuChineseCalendar.setTimeInMillis(_icuCalendar.getTimeInMillis());

		int y = _icuChineseCalendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
		int m = _icuChineseCalendar.get(ChineseCalendar.MONTH) + 1;
		int d = _icuChineseCalendar.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuffer ret = new StringBuffer();
		if (y < 1000) {
			ret.append("0");
		} else if (y < 100) {
			ret.append("00");
		} else if (y < 10) {
			ret.append("000");
		}
		ret.append(y);

		if (m < 10) {
			ret.append("0");
		}
		ret.append(m);

		if (d < 10) {
			ret.append("0");
		}
		ret.append(d);

		return ret.toString();
	}

	/**
	 * 입력한 음력 날짜에 해당하는 양력 날짜를 가져온다.
	 * 양력으로 변환하고자 하는 음력 날짜를 8자리 문자열로 입력한다.
	 *
	 * @param yyyymmdd	음력 날짜
	 * @return			양력 날짜
	 */
	public synchronized String fromLunar(String yyyymmdd) {
		if (yyyymmdd == null) {
			return "";
		}

		String date = yyyymmdd.trim();
		if (date.length() != 8) {
			if (date.length() == 4) {
				date = date + "0101";
			} else if (date.length() == 6) {
				date = date + "01";
			} else if (date.length() > 8) {
				date = date.substring(0, 8);
			} else {
				return "";
			}
		}

		_icuChineseCalendar.set(ChineseCalendar.EXTENDED_YEAR,
				Integer.parseInt(date.substring(0, 4)) + 2637);
		_icuChineseCalendar.set(ChineseCalendar.MONTH,
				Integer.parseInt(date.substring(4, 6)) - 1);
		_icuChineseCalendar.set(ChineseCalendar.DAY_OF_MONTH,
				Integer.parseInt(date.substring(6)));

		_icuCalendar.setTimeInMillis(_icuChineseCalendar.getTimeInMillis());

		int y = _icuCalendar.get(Calendar.YEAR);
		int m = _icuCalendar.get(Calendar.MONTH) + 1;
		int d = _icuCalendar.get(Calendar.DAY_OF_MONTH);

		StringBuffer ret = new StringBuffer();
		if (y < 1000) {
			ret.append("0");
		} else if (y < 100) {
			ret.append("00");
		} else if (y < 10) {
			ret.append("000");
		}
		ret.append(y);

		if (m < 10) {
			ret.append("0");
		}
		ret.append(m);

		if (d < 10) {
			ret.append("0");
		}
		ret.append(d);

		return ret.toString();
	}
}
