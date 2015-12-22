package com.quantosauros.common.calendar;
/**
* {@code calendar} 패키지에서 사용할 exception
* 
* @author Jae-Heon Kim
* @since 3.2
*
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.11.17
------------------------------------------------------------------------------*/
public class CalendarException extends RuntimeException {
	public CalendarException() {
        super();
    }

    public CalendarException(String msg) {
        super(msg);
    }

    public CalendarException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CalendarException(Throwable cause) {
        super(cause);
    }
}
