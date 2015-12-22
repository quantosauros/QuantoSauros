package com.quantosauros.common.calendar;
/**
 * {@code CalendarFactory}는 Factory Class로서 요청한 {@code Calendar} 객체를 
 * 생성해 준다.
 * 
 * @author Jae-Heon Kim
 * @since 3.2
 *
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.11.17
------------------------------------------------------------------------------*/
public class CalendarFactory {

	/**
	 * 주어진 {@code code}에 해당하는 {@code Calendar} 객체를 가져온다. 
	 * <p>
	 * 유효한 코드 값들은 다음과 같다:
	 * <ul>
	 * <li> Calendar.USE_NULL: {@link NullCalendar}</li>
	 * <li> Calendar.USE_DEFAULT: {@link DefaultCalendar}</li>
	 * </ul>
	 *
	 * @param code	생성하고자 하는 {@code Calendar}의 코드
	 * @return 해당 {@code Calendar} 객체
	 */
	public static Calendar getInstance(int code) {
		if (Calendar.USE_NULL == code) {
			return NullCalendar.getInstance();
		} else if (Calendar.USE_DEFAULT == code) {
			return DefaultCalendar.getInstance();
		}
		throw new CalendarException("Unknown calendar code: " + code);
	}

	/**
	 * 입력된 국가, 시장에 해당하는 {@code Calendar} 객체를 가져온다.
	 * <p>국가 코드는 다음과 같다:
	 * <ul>
	 * <li> {@code KR}: 대한민국, {@link SouthKoreaCalendar}
	 * <li> {@code US}: 미국, {@link UnitedStatesCalendar}
	 * <li> {@code UK}: 영국, {@link UnitedKingdomCalendar}
	 * <li> {@code CN}: 중국, {@link ChinaCalendar}
	 * <li> {@code JP}: 일본, {@link JapanCalendar}
	 * 위의 목록에 없는 국가의 경우 미국 캘린더를 리턴한다.
	 * 
	 * @param country	국가 코드
	 * @param market	시장 코드. 자세한 사항은 각 국가별 달력 참고.
	 * @return		 	{@code Calendar} 객체
	 */
	public static Calendar getInstance(String country, int market) {
		if ("KR".equalsIgnoreCase(country)) {
			return SouthKoreaCalendar.getCalendar(market);
		} else if ("US".equalsIgnoreCase(country)) {
			return UnitedStatesCalendar.getCalendar(market);
		} else if ("UK".equalsIgnoreCase(country) || "EU".equalsIgnoreCase(country)) {
			return UnitedKingdomCalendar.getCalendar(market);
		} else if ("CN".equalsIgnoreCase(country) || "CH".equalsIgnoreCase(country)) {
			return ChinaCalendar.getCalendar(market);
		} else if ("JP".equalsIgnoreCase(country)) {
			return JapanCalendar.getCalendar(market);
		} else {
			//그 외 국가의 경우 US로 리턴
			return UnitedStatesCalendar.getCalendar(market);
		}
	}
}
