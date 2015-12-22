package com.quantosauros.test.util;

import java.util.Calendar;

import com.quantosauros.common.date.Date;

/**
 * 
 * @author Jae-Heon Kim
 * @since 2010.02.08
 * 
 */
public class DateTime extends Date {
	private int _hour;
	private int _minute;
	private int _second;
	private String _canonicalForm;

	public DateTime() {
		this(Calendar.getInstance());
	}

	public DateTime(Calendar calendar) {
		//super(calendar);
		this._hour = calendar.get(Calendar.HOUR_OF_DAY);
		this._minute = calendar.get(Calendar.MINUTE);
		this._second = calendar.get(Calendar.SECOND);
		this._canonicalForm = super.toString() + "." 
			+ canonicalForm(_hour, _minute, _second);
	}

	public String toString() {
		return _canonicalForm;
	}
	
	private String canonicalForm(int year, int month, int day) {
        return (_hour < 10 ? "0" : "") + _hour
            + (_minute < 10 ? "0" : "") + _minute
            + (_second < 10 ? "0" : "") + _second;
    }
}
