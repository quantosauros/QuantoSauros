package com.quantosauros.common.date.daycounter;
/**
* 
* @author Jae-Heon Kim
* @since 3.2 
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09.01
------------------------------------------------------------------------------*/
public class DayCounterException extends RuntimeException {
	public DayCounterException() {
        super();
    }

    public DayCounterException(String msg) {
        super(msg);
    }

    public DayCounterException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DayCounterException(Throwable cause) {
        super(cause);
    }
}
