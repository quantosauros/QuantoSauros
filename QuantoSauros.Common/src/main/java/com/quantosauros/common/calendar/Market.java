package com.quantosauros.common.calendar;

import java.io.Serializable;

/**
 * {@code Market} 클래스는 각 국가별 시장을 정의하기 위한 것이다.
 * <p>현재 대한민국, 영국, 미국, 중국, 일본, 이렇게 다섯 나라에 대해 
 * 각각 공휴일만 있는 기본 달력과 주요 증권거래소에서 사용하는 달력을 정의하고 있다.
 * <p>{@code CustomCalendar} 객체를 지칭하기 위해서 {@code Custom.DEFAULT} 값을
 * 추가로 정의한다.
 *
 * @author Kang Seok-Chan
 * @author Jae-Heon Kim
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.09
* - SC Kang implemented it as Enumeration, but changed to Class by JH Kim 
* to be compatible with JDK 1.4
------------------------------------------------------------------------------*/
public final class Market implements Serializable {
	/**
	 * 대한민국에서 사용하는 달력을 정의한다.
	 */
	public static final class SouthKorea {
		/**
		 * 대한민국의 기본 달력을 지칭한다.
		 */
		public static final int SETTLEMENT = 0;
		/**
		 * 대한민국 증권거래소 달력을 지칭한다.
		 */
		public static final int KRX = 1;
	}

	/**
	 * 영국에서 사용하는 달력을 정의한다.
	 */
	public static final class UnitedKingdom {
		/**
		 * 영국의 기본 달력을 지칭한다.
		 */
		public static final int SETTLEMENT = 0;
		/**
		 * 영국 런던 증권거래소 달력을 지칭한다.
		 */
		public static final int LSE = 1;
	}

	/**
	 * 미국에서 사용하는 달력을 정의한다.
	 */
	public static final class UnitedStates {
		/**
		 * 미국의 기본 달력을 지칭한다.
		 */
		public static final int SETTLEMENT = 0;
		/**
		 * 미국 뉴욕 증권거래소를 지칭한다.
		 */
		public static final int NYSE = 1;
	}

	/**
	 * 중국에서 사용하는 달력을 정의한다.
	 */
	public static final class China {
		/**
		 * 중국의 기본 달력을 지칭한다.
		 */
		public static final int SETTLEMENT = 0;
		/**
		 * 중국 상하이 증권거래소의 달력을 지칭한다.
		 */
		public static final int SSE = 1;
	}

	/**
	 * 일본에서 사용하는 달력을 정의한다.
	 */
	public static final class Japan {
		/**
		 * 일본의 기본 달력을 지칭한다.
		 */
		public static final int SETTLEMENT = 0;
		/**
		 * 일본 도쿄 증권거래소의 달력을 지칭한다.
		 */
		public static final int TSE = 1;
	}

	/**
	 * 맞춤 달력을 정의한다.
	 */
	public static final class Custom {
		/**
		 * {@code CustomCalendar} 객체를 생성할때 사용한다.
		 */
		public static final int DEFAULT = 0;
	}
}
