package com.quantosauros.common.calendar;

import com.sun.org.apache.bcel.internal.Constants;

/**
 * {@code Weekday} 클래스는 요일의 코드를 정의한다.
 * <p>각 요일의 코드 값은 다음과 같다:
 * <ul>
 * <li> 토요일: 0
 * <li> 일요일: 1
 * <li> 월요일: 2
 * <li> 화요일: 3
 * <li> 수요일: 4
 * <li> 목요일: 5
 * <li> 금요일: 6
 * </ul>
 * @author Kang Seok-Chan
 * @author Jae-Heon Kim
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
* SC Kang implemented it as Enumeration, but changed to Class by JH Kim
* to be compatible with JDK 1.4
------------------------------------------------------------------------------*/
public final class Weekday {

	// enumValue equals the result of Zeller's Congruence
	/**
	 * 월요일을 지칭하며 코드 값은 2.
	 */
	public static final Weekday MONDAY = new Weekday(2);
	
	/**
	 * 화요일을 지칭하며 코드 값은 3.
	 */
	public static final Weekday TUESDAY = new Weekday(3);
	
	/**
	 * 수요일을 지칭하며 코드 값은 4.
	 */
	public static final Weekday WEDNESDAY = new Weekday(4);
	
	/**
	 * 목요일을 지칭하며 코드 값은 5.
	 */
	public static final Weekday THURSDAY = new Weekday(5);
	
	/**
	 * 금요일을 지칭하며 코드 값은 6.
	 */
	public static final Weekday FRIDAY = new Weekday(6);
	
	/**
	 * 토요일을 지칭하며 코드 값은 0.
	 */
	public static final Weekday SATURDAY = new Weekday(0);
	
	/**
	 * 일요일을 지칭하며 코드 값은 1.
	 */
	public static final Weekday SUNDAY = new Weekday(1);

    private int enumValue;

    private Weekday(int weekday) {
        this.enumValue = weekday;
    }

	/**
	 * 입력된 {@code value}에 해당하는 {@code Weekday} 객체를 반환한다.
	 *
	 * @param value	요일 코드
	 * @return		{@code Weekday} 객체
	 */
    static public Weekday valueOf(int value) {
        switch (value) {
	        case 0:
	            return SATURDAY;
	        case 1:
	            return SUNDAY;
	        case 2:
	            return MONDAY;
	        case 3:
	            return TUESDAY;
	        case 4:
	            return WEDNESDAY;
	        case 5:
	            return THURSDAY;
	        case 6:
	            return FRIDAY;
	        default:
	        	return null;
        }
    }

	/**
	 * 이 {@code Weekday} 객체의 코드 값을 가져온다.
	 *
	 * @return	이 {@code Weekday} 객체의 코드 값
	 */
    public int toInteger() {
        return enumValue;
    }

	/**
	 * 이 {@code Weekday} 객체의 요일 이름을 영어로 된 문자열로 가져온다.
	 *
	 * @return	Weekday의 요일 이름
	 */
    public String toString() {
        switch (enumValue) {
	        case 0:
	            return "Saturday";
	        case 1:
	            return "Sunday";
	        case 2:
	            return "Monday";
	        case 3:
	            return "Tuesday";
	        case 4:
	            return "Wednesday";
	        case 5:
	            return "Thursday";
	        case 6:
	            return "Friday";
        }
        return "UNDEFINED";
    }

	// This method is called immediately after an object of this class is deserialized.
    // This method returns the singleton instance.
    protected Object readResolve() {
    	return valueOf(this.enumValue);
    }

}
