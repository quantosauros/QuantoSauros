package com.quantosauros.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <div id="ko">
 * {@code Frequency}는 어떤 이벤트의 발생 빈도, 주기를 표현하기 위한 클래스이다
 * </div>
 * <div id="en">
 * {@code Frequency} is for expressing how often an event occurs
 * </div>.
 * <div id="ko">
 * <p>
 * 예를 들어, 이자 지급일 등의 연간 발생 횟수, 발생 주기를 나타낼 수 있다.
 * <p>빈도를 나타내는 코드 값은 다음과 같다:
 * <ul>
 * <li> N: 0, 즉, 발생하지 않음.
 * <li> C: 연속
 * <li> D: 매일
 * <li> W: 매주
 * <li> W2: 매 2주
 * <li> M: 매월
 * <li> M2: 매 2개월
 * <li> Q: 매분기
 * <li> M4: 매 4개월
 * <li> S: 반년마다
 * <li> A: 1년마다
 * </ul>
 * </div>
 * <div id="en">
 * <p>
 * For example, the class represents the number of annual coupon payments.
 * <p>Codes for frequency used here are given by:
 * <ul>
 * <li> N: 0, never occurs
 * <li> C: Continuous
 * <li> D: Daily
 * <li> W: Weekly
 * <li> W2: Biweekly
 * <li> M: Monthly
 * <li> M2: Bimonthly
 * <li> Q: Quarterly
 * <li> M4: Once four months
 * <li> S: Semi-annually
 * <li> A: Anually
 * </ul>
 * </div>
 * @author Oh, Sung Hoon
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class Frequency implements Serializable, Comparable {

	private static Map supportedFrequencyMap;

	static {
		String [][] supportedFrequencies = {
				{ "N", "NONE", "0" },
				{ "C", "CONTINUOUS", "999" },
				{ "D", "DAILY", "365" },
				{ "W", "WEEKLY", "52" },
				{ "W2", "BI-WEEKLY", "26" },
				{ "M", "MONTHLY", "12" },
				{ "M2", "BI-MONTHLY", "6" },
				{ "Q", "QUARTERLY", "4" },
				{ "M4", "4-MONTHLY", "3" },
				{ "S", "SEMI ANNUALLY", "2" },
				{ "A", "ANNUALLY", "1" }
		};
		supportedFrequencyMap = new HashMap();
		for (int i = 0; i < supportedFrequencies.length; ++i) {
			String [] frequencies = supportedFrequencies[i];
			String frequencyCode = frequencies[0];
			String frequencyName = frequencies[1];
			int frequencyInYear = Integer.parseInt(frequencies[2]);
			supportedFrequencyMap.put(frequencyCode,
					new Frequency(frequencyCode, frequencyName, frequencyInYear));
		}
	}

	/**
	 * <div id="ko">
	 * 발생 횟수가 0인 {@code Frequency} 객체
 	 * </div>
 	 * <div id="en">
 	 * A {@code Frequency} object for non-occurring event 
 	 * </div>.
	 */
	public static final Frequency NONE = (Frequency)supportedFrequencyMap.get("N");

	/**
	 * <div id="ko">
	 * 연속적으로 발생하는 {@code Frequency} 객체.
 	 * </div>
 	 * <div id="en">
 	 * A {@code Frequency} object for continuously occurring event 
 	 * </div>.
	 */
	public static final Frequency CONTINUOUS = (Frequency)supportedFrequencyMap.get("C");

	private String code;

	private String name;

	private double frequencyInYear;

	/**
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>.
	 * 
	 * @param code
	 * 			  <div id="ko">Frequency의 코드</div>
	 * 			  <div id="en">The frequency code</div>
	 * @param name
	 * 			  <div id="ko">Frequency의 이름</div>
	 * 			  <div id="en">The frequency name</div>
	 * @param frequencyInYear
	 * 			  <div id="ko">연간 발생 횟수</div>
	 * 			  <div id="en">The number of occurrence each year</div>
	 */
	private Frequency(String code, String name, double frequencyInYear) {
		this.code = code;
		this.name = name;
		this.frequencyInYear = frequencyInYear;
	}

	/**
	 * <div id="ko">
	 * 입력된 {@code code} 값에 해당하는 {@code Frequency} 객체를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns a {@code Frequency} object corresponding to a given {@code code}
	 * </div>.
	 *
	 * @param code
	 * 			  <div id="ko">빈도 코드</div>
	 * 			  <div id="en">The frequency code</div>
	 * 
	 * @return <div id="ko">해당 {@code Frequency} 객체</div>
	 * 		   <div id="en">The corresponding {@code Frequency} object</div>
	 */
	public static Frequency valueOf(String code) {
		if (code == null) {
			return null;
		}
		if (supportedFrequencyMap.containsKey(code)) {
			return (Frequency)supportedFrequencyMap.get(code);
		}
		throw new IllegalArgumentException("Unsupported Frequency [" + code + "]");
	}

	/**
	 * <div id="ko">
	 * 입력된 {@code codes} 값에 해당하는 {@code Frequency} 객체의 배열을 반환한다
	 * </div>
	 * <div id="en">
	 * Returns an array of {@code Frequency} objects corresponding to an array 
	 * of given {@code codes} 
	 * </div>.
	 *
	 * @param codes
	 * 			  <div id="ko">빈도 코드 배열</div>
	 * 			  <div id="en">An array of the frequency codes</div>
	 * 
	 * @return <div id="ko">해당 {@code Frequency} 객체 배열</div>
	 * 		   <div id="en">An array of corresponding{@code Frequency} objects</div>
	 */
	public static Frequency[] valuesOf(String[] codes) {
		Frequency[] frequencies = new Frequency[codes.length];
		for(int i = 0; i < frequencies.length; i++) {
			frequencies[i] = valueOf(codes[i]);
		}
		return frequencies;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Frequency} 객체의 코드를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the code of this {@code Frequency} object
	 * </div>.
	 * 
	 * @return <div id="ko">코드 값</div>
	 * 		   <div id="en">The freuquency code</div>
	 */
	public String getCode() {
		return code;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Frequency} 객체의 빈도를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the number of occurrences each year in this {@code Frequency} object
	 * </div>.
	 * 
	 * @return <div id="ko">빈도 수</div>
	 * 		   <div id="en">The number of occurrence each year</div>
	 */
	public double getFrequencyInYear() {
		return this.frequencyInYear;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Frequency} 객체의 빈도 수를 월 단위로 구한다
	 * </div>
	 * <div id="en">
	 * Returns the occurrence interval of this {@code Frequency} object expressed in month
	 * </div>.
	 * 
	 * @return <div id="ko">발생 주기의 월 단위 표시</div>
	 * 		   <div id="en">The occurrence interval expressed in month</div>
	 */
	public int toMonthUnit() {
		return (int)(12.0 / getFrequencyInYear());
	}

	/**
	 * <div id="ko">
	 * 이 {@code Frequency} 객체의 주기를 연 단위로 표시해 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the occurrence interval of this {@code Frequency} object expressed in year
	 * </div>.
	 * 
	 * @return <div id="ko">발생 주기의 연 단위 표시</div>
	 * 		   <div id="en">The occurrence interval expressed in year</div>
	 */
	public double getInterval() {
		if ("NONE".equals(name) || "CONTINUOUS".equals(name)) {
			return 0;
		}

		return 1.0 / getFrequencyInYear();
	}

	/**
	 * <div id="ko">
	 * 이 {@code Frequency} 객체의 이름을 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the name of this {@code Frequency} object
	 * </div>.
	 * 
	 * @return <div id="ko">Frequency의 이름</div>
	 * 		   <div id="en">The frequency name</div>
	 */
	public String toString() {
		return name;
	}

	// This method is called immediately after an object of this class is deserialized.
    // This method returns the singleton instance.
    protected Object readResolve() {
    	return supportedFrequencyMap.get(this.code);
    }

    // Jae-Heon Kim 2010.12.29
	public int compareTo(Object o) {
		Frequency f = (Frequency)o;
		if (frequencyInYear > f.frequencyInYear) {
			return 1;
		} else if (frequencyInYear == f.frequencyInYear) {
			return 0;
		} else if (frequencyInYear < f.frequencyInYear) {
			return -1;
		}
		return 0;
	}

//	public static final Frequency NONE = new Frequency("0", "None", 0);
//	public static final Frequency CONTINUOUS = new Frequency("1", "Continuous", Integer.MAX_VALUE);
//	public static final Frequency DAILY = new Frequency("2", "Daily", 365);
//	public static final Frequency WEEKLY = new Frequency("3", "Weekly", 52);
//	public static final Frequency MONTHLY = new Frequency("4", "Monthly", 12);
//	public static final Frequency QUARTERLY = new Frequency("5", "Quarterly", 4);
//	public static final Frequency SEMI_ANNUALLY = new Frequency("6", "Semi-annually", 2);
//	public static final Frequency ANNUALLY = new Frequency("7", "Annually", 1);

}
