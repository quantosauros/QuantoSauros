package com.quantosauros.common.calendar;

import java.io.Serializable;


/**
 * Business Day Convention이란 현금 지급일(Payment Date)이 영업일이 아니거나
 * 휴일인 경우 지급일을 변경하는 방법을 의미한다.
 * 이 클래스에서는 다음 네 가지 방법을 명시한다:
 * <ul>
 * <li> FOLLOWING
 * <li> MODIFIED_FOLLOWING
 * <li> PRECEDING
 * <li> MODIFIED_PRECEDING
 * <li> NEAREST
 * </ul>
 *
 * @author Kang Seok-Chan
 * @author Jae-Heon Kim
 * @since 3.2
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
* - SC Kang implemented it as Enumeration, but changed to Class by JH Kim
* to be compatible with JDK 1.4
------------------------------------------------------------------------------*/
public final class BusinessDayConvention implements Serializable {

	/**
	 * FOLLOWING 방식.
	 */
	public static final BusinessDayConvention FOLLOWING
		= new BusinessDayConvention(0, "Following");

	/**
	 * MODIFIED_FOLLOWING 방식.
	 */
	public static final BusinessDayConvention MODIFIED_FOLLOWING
		= new BusinessDayConvention(1, "Modified Following");

	/**
	 * PRECEDING 방식.
	 */
	public static final BusinessDayConvention PRECEDING
		= new BusinessDayConvention(2, "Preceding");

	/**
	 * MODIFIED_PRECEDING 방식.
	 */
	public static final BusinessDayConvention MODIFIED_PRECEDING
		= new BusinessDayConvention(3, "Modified Preceding");

	/**
	 * NEAREST 방식.
	 */
	public static final BusinessDayConvention NEAREST
		= new BusinessDayConvention(4, "Nearest");

	private final int _code;
	private final String _name;

	private BusinessDayConvention(int code, String name) {
		_code = code;
		_name = name;
	}

	/**
	 * 이 {@code BusinessDayConvention} 객체의 유형 코드를 반환한다.
	 *
	 * @return
	 * <ul>
	 * <li> 0: FOLLOWING</li>
	 * <li> 1: MODIFIED_FOLLOWING</li>
	 * <li> 2: PRECEDING</li>
	 * <li> 3: MODIFIED_PRECEDING</li>
	 * <li> 4: NEAREST</li>
	 * </ul>
	 */
	public int toInteger() {
		return _code;
	}

	/**
	 * 이 {@code BusinessDayConvention} 객체의 이름을 가져온다.
	 * @return 객체의 이름
	 */
	public String toString() {
		return _name;
	}

	/**
	 * 입력된 {@code code}에 해당하는 {@code BusinessDayConvention} 객체를
	 * 가져온다.
	 * 코드 값은 다음과 같다:
	 * <ul>
	 * <li> 0: FOLLOWING</li>
	 * <li> 1: MODIFIED_FOLLOWING</li>
	 * <li> 2: PRECEDING</li>
	 * <li> 3: MODIFIED_PRECEDING</li>
	 * <li> 4: NEAREST</li>
	 * </ul>
	 * @param code	유형 코드
	 * @return		해당 {@code BusinessDayConvention} 객체
	 */
	public static BusinessDayConvention getInstance(int code) {
		switch (code) {
			case 0:
				return FOLLOWING;
			case 1:
				return MODIFIED_FOLLOWING;
			case 2:
				return PRECEDING;
			case 3:
				return MODIFIED_PRECEDING;
			case 4:
				return NEAREST;
			default:
				throw new CalendarException("Unknown business day convention: "
						+ code);
		}
	}

	// This method is called immediately after an object of this class is deserialized.
    // This method returns the singleton instance.
    protected Object readResolve() {
    	return getInstance(this._code);
    }

}
