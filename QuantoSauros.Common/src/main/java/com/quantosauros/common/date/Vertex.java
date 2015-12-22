package com.quantosauros.common.date;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.PiecewiseConstantInterpolation;

/**
 * <div id="ko">
 * {@code Vertex} 클래스는 특정 기간의 길이를 6자리 문자열로 표시한다
 * </div>
 * <div id="en">
 * {@code Vertex} represents year, month and day as a 6-digit string
 * </div>.
 * <div id="ko">
 * <p>
 * 6자리 문자열은 해당 기간을 각각 2자리의 연, 월, 일로 나타낸다.
 * <p>
 * 예를 들면, '하루'를 의미하는 'D1'은 "000001', '1years'을 나타내는 'Y1'은
 * '010000', '6 months'은 '000600'으로 표시한다.
 * <p>유효한 기간 코드는 다음과 같다:
 * <ul>
 * <li> D1: 1일
 * <li> W1: 1주
 * <li> W2: 2주
 * <li> D15: 보름
 * <li> M1: 1 months
 * <li> M2: 2 months
 * <li> M3: 3 months
 * <li> M4: 4 months
 * <li> M5: 5 months
 * <li> M6: 6 months
 * <li> M7: 7 months
 * <li> M8: 8 months
 * <li> M9: 9 months
 * <li> M10: 10 months
 * <li> M11: 11 months
 * <li> Y1: 1years
 * <li> Y1Q: 1years 3 months
 * <li> Y1H: 1years 6 months
 * <li> Y1T: 1years 9 months
 * <li> Y2: 2years
 * <li> Y2Q: 2years 3 months
 * <li> Y2H: 2years 6 months
 * <li> Y2T: 2years 9 months
 * <li> Y3: 3years
 * <li> Y3Q: 3years 3 months
 * <li> Y3H: 3years 6 months
 * <li> Y3T: 3years 9 months
 * <li> Y4: 4years
 * <li> Y4Q: 4years 3 months
 * <li> Y4H: 4years 6 months
 * <li> Y4T: 4years 9 months
 * <li> Y5: 5years
 * <li> Y5Q: 5years 3 months
 * <li> Y5H: 5years 6 months
 * <li> Y5T: 5years 9 months
 * <li> Y6: 6years
 * <li> Y6Q: 6years 3 months
 * <li> Y6H: 6years 6 months
 * <li> Y6T: 6years 9 months
 * <li> Y7: 7years
 * <li> Y7Q: 7years 3 months
 * <li> Y7H: 7years 6 months
 * <li> Y7T: 7years 9 months
 * <li> Y8: 8years
 * <li> Y8Q: 8years 3 months
 * <li> Y8H: 8years 6 months
 * <li> Y8T: 8years 9 months
 * <li> Y9: 9years
 * <li> Y9Q: 9years 3 months
 * <li> Y9H: 9years 6 months
 * <li> Y9T: 9years 9 months
 * <li> Y10: 10years
 * <li> Y12: 12years
 * <li> Y15: 15years
 * <li> Y20: 20years
 * <li> Y30: 30years
 * </ul>
 * </div>
 * <div id="en">
 * <p>
 * 6-digit code consist of 2-digit year, 2-digit month and 2-digit day.
 * <p>
 * For example, 'one day' meaning 'D1' can be represented by '000001', '1 year' 
 * meaning 'Y1' can be represented by '010000' and '6 months' meaning 'M6' can
 * be represented by '000600'.
 * <p> The followings are validate codes.
 * <ul>
 * <li> D1: 1 day
 * <li> W1: 1 week
 * <li> W2: 2 weeks
 * <li> D15: 15 days
 * <li> M1: 1 month
 * <li> M2: 2 months
 * <li> M3: 3 months
 * <li> M4: 4 months
 * <li> M5: 5 months
 * <li> M6: 6 months
 * <li> M7: 7 months
 * <li> M8: 8 months
 * <li> M9: 9 months
 * <li> M10: 10 months
 * <li> M11: 11 months
 * <li> Y1: 1 year
 * <li> Y1Q: 1 year and 3 months
 * <li> Y1H: 1 year and 6 months
 * <li> Y1T: 1 year and 9 months
 * <li> Y2: 2 years
 * <li> Y2Q: 2 years and 3 months
 * <li> Y2H: 2 years and 6 months
 * <li> Y2T: 2 years and 9 months
 * <li> Y3: 3 years
 * <li> Y3Q: 3 years and 3 months
 * <li> Y3H: 3 years and 6 months
 * <li> Y3T: 3 years and 9 months
 * <li> Y4: 4 years
 * <li> Y4Q: 4 years and 3 months
 * <li> Y4H: 4 years and 6 months
 * <li> Y4T: 4 years and 9 months
 * <li> Y5: 5 years
 * <li> Y5Q: 5 years and 3 months
 * <li> Y5H: 5 years and 6 months
 * <li> Y5T: 5 years and 9 months
 * <li> Y6: 6 years
 * <li> Y6Q: 6 years and 3 months
 * <li> Y6H: 6 years and 6 months
 * <li> Y6T: 6 years and 9 months
 * <li> Y7: 7 years
 * <li> Y7Q: 7 years and 3 months
 * <li> Y7H: 7 years and 6 months
 * <li> Y7T: 7 years and 9 months
 * <li> Y8: 8 years
 * <li> Y8Q: 8 years and 3 months
 * <li> Y8H: 8 years and 6 months
 * <li> Y8T: 8 years and 9 months
 * <li> Y9: 9 years
 * <li> Y9Q: 9 years and 3 months
 * <li> Y9H: 9 years and 6 months
 * <li> Y9T: 9 years and 9 months
 * <li> Y10: 10 years
 * <li> Y12: 12 years
 * <li> Y15: 15 years
 * <li> Y20: 20 years
 * <li> Y30: 30 years
 * </ul>
 * </div>
 *
 * @author Oh, Sung Hoon
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class Vertex implements Serializable {

	private static Map _supportedVertexMap;

	static {
		String [][] supportedVertices = {				
				{ "D1", "000001", },
				{ "W1", "000007", },
				{ "W2", "000014", },
				{ "D15", "000015", },
				{ "M1", "000100", },
				{ "M2", "000200", },
				{ "M3", "000300", },
				{ "M4", "000400", },
				{ "M5", "000500", },
				{ "M6", "000600", },
				{ "M7", "000700", },
				{ "M8", "000800", },
				{ "M9", "000900", },
				{ "M10", "001000", },
				{ "M11", "001100", },
				{ "Y1", "010000", },
				{ "Y1Q", "010300", },
				{ "Y1H", "010600", },
				{ "Y1T", "010900", },
				{ "Y2", "020000", },
				{ "Y2Q", "020300", },
				{ "Y2H", "020600", },
				{ "Y2T", "020900", },
				{ "Y3", "030000", },
				{ "Y3Q", "030300", },
				{ "Y3H", "030600", },
				{ "Y3T", "030900", },
				{ "Y4", "040000", },
				{ "Y4Q", "040300", },
				{ "Y4H", "040600", },
				{ "Y4T", "040900", },
				{ "Y5", "050000", },
				{ "Y5Q", "050300", },
				{ "Y5H", "050600", },
				{ "Y5T", "050900", },
				{ "Y6", "060000", },
				{ "Y6Q", "060300", },
				{ "Y6H", "060600", },
				{ "Y6T", "060900", },
				{ "Y7", "070000", },
				{ "Y7Q", "070300", },
				{ "Y7H", "070600", },
				{ "Y7T", "070900", },
				{ "Y8", "080000", },
				{ "Y8Q", "080300", },
				{ "Y8H", "080600", },
				{ "Y8T", "080900", },
				{ "Y9", "090000", },
				{ "Y9Q", "090300", },
				{ "Y9H", "090600", },
				{ "Y9T", "090900", },
				{ "Y10", "100000", },
				{ "Y12", "120000", },
				{ "Y15", "150000", },
				{ "Y20", "200000", },
				{ "Y30", "300000" }
				
				// vertex 추가 - 2011.7.3 농협
				,{"D2","000002",},
				{"D3","000003",},
				{"Y11","110000",},
				{"Y13","130000",},
				{"Y14","140000",},
				{"Y16","160000",},
				{"Y17","170000",},
				{"Y18","180000",},
				{"Y19","190000",},
				{"Y21","210000",},
				{"Y22","220000",},
				{"Y23","230000",},
				{"Y24","240000",},
				{"Y25","250000",},
				{"Y26","260000",},
				{"Y27","270000",},
				{"Y28","280000",},
				{"Y29","290000",},
				{"Y35","350000",},
				{"Y40","400000",},
				{"Y45","450000",},
				{"Y50","500000",}
				//vertex 추가 - bootstrapping 용
				,{ "NONE", "000000"},
				{"Y10Q", "100300", },
				{"Y10H","100600", },
				{"Y10T","100900", },
				{"Y11Q","110300", },
				{"Y11H","110600", },
				{"Y11T","110900", },
				{"Y12Q","120300", },
				{"Y12H","120600", },
				{"Y12T","120900", },
				{"Y13Q","130300", },
				{"Y13H","130600", },
				{"Y13T","130900", },
				{"Y14Q","140300", },
				{"Y14H","140600", },
				{"Y14T","140900", },
				{"Y15Q","150300", },
				{"Y15H","150600", },
				{"Y15T","150900", },
				{"Y16Q","160300", },
				{"Y16H","160600", },
				{"Y16T","160900", },
				{"Y17Q","170300", },
				{"Y17H","170600", },
				{"Y17T","170900", },
				{"Y18Q","180300", },
				{"Y18H","180600", },
				{"Y18T","180900", },
				{"Y19Q","190300", },
				{"Y19H","190600", },
				{"Y19T","190900", },
				{"Y20Q","200300", },
				{"Y20H","200600", },
				{"Y20T","200900", },
				{"Y21Q","210300", },
				{"Y21H","210600", },
				{"Y21T","210900", },
				{"Y22Q","220300", },
				{"Y22H","220600", },
				{"Y22T","220900", },
				{"Y23Q","230300", },
				{"Y23H","230600", },
				{"Y23T","230900", },
				{"Y24Q","240300", },
				{"Y24H","240600", },
				{"Y24T","240900", },
				{"Y25Q","250300", },
				{"Y25H","250600", },
				{"Y25T","250900", },
				{"Y26Q","260300", },
				{"Y26H","260600", },
				{"Y26T","260900", },
				{"Y27Q","270300", },
				{"Y27H","270600", },
				{"Y27T","270900", },
				{"Y28Q","280300", },
				{"Y28H","280600", },
				{"Y28T","280900", },
				{"Y29Q","290300", },
				{"Y29H","290600", },
				{"Y29T","290900", },
				{"Y30Q","300300", },
				{"Y30H","300600", },
				{"Y30T","300900", },
				
		};
		_supportedVertexMap = new HashMap();
		for (int i = 0; i < supportedVertices.length; ++i) {
			String [] vertex = supportedVertices[i];
			String vertexCode = vertex[0];
			String vertexValue = vertex[1];
			_supportedVertexMap.put(vertexCode, new Vertex(vertexCode, vertexValue));
		}
	}

	protected String _code;

	protected int _years;

	protected int _months;

	protected int _days;

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param code
	 * 		<div id="ko">기간 코드</div>
	 * 		<div id="en">The term code</div>
	 * @param value	
	 * 		<div id="ko">문자열로 표현한 연월일 정보(YYMMDD)</div>
	 * 		<div id="en">The date information represented by a string(YYMMDD)</div>
	 */
	private Vertex(String code, String value) {
		this(code,
			Integer.parseInt(value.substring(0, 2)),	// Years
			Integer.parseInt(value.substring(2, 4)),	// Months
			Integer.parseInt(value.substring(4))); 		// Days
	}

	/**
	 * <div id="ko">
	 * 주어진 {@code code}에 해당하는 {@code Vertex} 객체를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the {@code Vertex} object corresponding to a given {@code code}
	 * </div>.
	 *
	 * @param code	
	 * 		<div id="ko">기간 코드</div>
	 * 		<div id="en">The term code</div>
	 * 
	 * @return		
	 * 		<div id="ko">해당 객체</div>
	 * 		<div id="en">The corresponding object</div>
	 */
	public static Vertex valueOf(String code) {
		if (_supportedVertexMap.containsKey(code)) {
			return (Vertex)_supportedVertexMap.get(code);
		} else {
			return Vertex.valueOf("NONE");
		}
		//throw new IllegalArgumentException("Unsupported Vertex [" + code + "]");
	}

	/**
	 * <div id="ko">
	 * 주어진 {@code code}에 해당하는 {@code Vertex} 객체의 연 수를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the year of the {@code Vertex} object which corresponds to a given 
	 * {@code code}
	 * </div>.
	 *
	 * @param code	
	 * 		<div id="ko">기간 코드</div>
	 * 		<div id="en">The term code</div>
	 * @return		
	 * 		<div id="ko">연 수</div>
	 * 		<div id="en">The year</div>
	 */
	public static int getVertexYears(String code) {
		Vertex vertex = valueOf(code);
		return vertex.getYears();
	}

	protected Vertex(String code, int years, int months, int days) {
		_code = code;
		_years = years;
		_months = months;
		_days = days;
	}

	/**
	 * <div id="ko"> 
	 * 표준 만기코드를 사용하는 대신 실제 만기(vertexDate)를 사용하여 vertex를 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates a vertex by using an actual maturity date(vertexDate) instead 
	 * of a standard term code
	 * </div>.
	 * 
	 * @param measureDate
	 * 		<div id="ko">측정일</div>
	 * 		<div id="en">The valuation date</div>
	 * @param vertexDate
	 * 		<div id="ko">실제 만기(vertexDate)</div>
	 * 		<div id="en">The vertex date</div>
	 * @return
	 * 		<div id="ko">새로운 {@code Vertex} 객체</div>
	 * 		<div id="en">A new {@code Vertex} object</div>
	 * 
	 * @since 3.3
	 */
	 // 2012.11.12, 류상욱  
	public static Vertex calculate(Date measureDate, Date vertexDate) {
		// 2012.11.22. Youngseok Lee,
		int years = 0;
		int months = 0;
		while (measureDate.plusYears(years + 1).compareTo(vertexDate) <= 0) {
			years++;
		}
		while (measureDate.plusYears(years).plusMonths(months + 1).compareTo(vertexDate) <= 0) {
			months++;
		}
		int days = (int) measureDate.plusYears(years).plusMonths(months).getDays(vertexDate);
		return new Vertex(vertexDate.toString(), years, months, days) ;
	}

	/**
	 * <div id="ko"> 
	 * 표준 만기코드를 사용하는 대신 실제 만기(vertexDate)를 사용하여 vertex를 계산한다
	 * <div id="en">
	 * Calculates a vertex by using an actual maturity date(vertexDate) instead 
	 * of a standard term code
	 * </div>.
	 * 
	 * <div id="ko">
	 * vertex 사이값에 대한 interpolation 보다는 해당일이 정확히 vertex에 위치하느냐
	 * 안하느냐가 관건이 되는 Piecewise Constant Interpolation에 주로 이용된다.
	 * </div>
	 * <div id="en">
	 * This method is mainly used for a piecewise constant interpolation method
	 * which verifies if a target date is exactly located on the vertex or not.
	 * </div>
	 * 
	 * @param measureDate
	 * 		<div id="ko">측정일</div>
	 * 		<div id="en">The valuation date</div>
	 * @param vertexDate
	 * 		<div id="ko">실제 만기(vertexDate)</div>
	 * 		<div id="en">The vertex date</div>
	 * @param interpolation
	 * 		<div id="ko">보간법</div>
	 * 		<div id="en">The interpolation method</div>
	 * 
	 * @return
	 * 		<div id="ko">새로운 {@code Vertex} 객체</div>
	 * 		<div id="en">A new {@code Vertex} object</div>
	 * 
	 * @since 3.3
	 */
	// 2012.11.22. Youngseok Lee,
	public static Vertex calculate(Date measureDate, Date vertexDate, Interpolation interpolation) {
		int years = 0;
		int months = 0;
		while (measureDate.plusYears(years + 1).compareTo(vertexDate) <= 0) {
			years++;
		}
		while (measureDate.plusYears(years).plusMonths(months + 1).compareTo(vertexDate) <= 0) {
			months++;
		}
		if (interpolation == PiecewiseConstantInterpolation.getInstance() && measureDate.plusYears(years).plusMonths(months).getMonth() != vertexDate.getMonth()) {
			months++;
		}
		int days = (int) measureDate.plusYears(years).plusMonths(months).getDays(vertexDate);
		return new Vertex(vertexDate.toString(), years, months, days) ;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Vertex} 객체의 기간 코드를 가져온다
	 * </div>
	 * <div id="en">
	 * Return the term code of this {@code Vertex} object </div>.
	 *
	 * @return	
	 * 		<div id="ko">Vertex의 기간코드</div>
	 *		<div id="en">The term code</div>
	 */
	public String getCode() {
		return _code;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Vertex} 객체가 나타내는 기간을 주어진 {@code dcf}의 방식을 따라 연 
	 * 단위로 환산한다
	 * </div>
	 * <div id="en">
	 * Returns the annualized term of this {@code Vertex} object using a given {@code dcf}
	 * </div>.
	 *
	 * @param dcf	
	 * 		<div id="ko">Day Count Fraction</div>
	 * 		<div id="en">The day count fraction</div>
	 * 
	 * @return		
	 * 		<div id="ko">연 단위 환산된 기간</div>
	 * 		<div id="en">The annualized term</div>
	 */
	public double getVertex(DayCountFraction dcf) {
		// 2013.02.14. Youngseok Lee
		return (double)_days / dcf.getDaysOfYear() + (double)_months / 12.0 + (double)_years;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Vertex} 객체가 나타내는 기간을 주어진 시스템에 디폴트로 지정된 Day Count 
	 * Fraction의 방식을 따라 연 단위로 환산한다
	 * </div>
	 * <div id="en">
	 * Returns the annualized term of this {@code Vertex} object using a default {@code dcf}
	 * </div>.
	 *
	 * @return	
	 * 		<div id="ko">디폴트로 지정된 dcf에 따라 연 단위 환산된 기간</div>
	 * 		<div id="en">The annualized term</div>
	 */
	public double getVertex() {
		return getVertex(DayCountFraction.DEFAULT);
	}

	/**
	 * <div id="ko">
	 * 이 {@code Vertex} 객체의 연 기간 부분을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the year of this {@code Vertex} object
	 * </div>.
	 * 
	 * @return	
	 * 		<div id="ko">{@code Vertex} 객체의 연 기간</div>
	 * 		<div id="en">The year of this {@code Vertex} object</div>
	 */
	public int getYears() {
		return _years;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Vertex} 객체의 월 기간 부분을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the month of this {@code Vertex} object
	 * </div>.
	 * 
	 * @return	
	 * 		<div id="ko">{@code Vertex} 객체의 월 기간</div>
	 * 		<div id="en">The month of this {@code Vertex} object</div>
	 */
	public int getMonths() {
		return _months;
	}

	/**
	 * <div id="ko">
	 * 이 {@code Vertex} 객체의 일 기간 부분을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the day of this {@code Vertex} object
	 * </div>.
	 * 
	 * @return	
	 * 		<div id="ko">{@code Vertex} 객체의 일 기간</div>
	 * 		<div id="en">The day of this {@code Vertex} object</div>
	 */
	public int getDay() {
		return _days;
	}

	/**
	 * <div id="ko"> 
	 * 주어진 {@code code}에 해당하는 {@code Vertex}가 나타내는 기간을 연 단위로 환산한다
	 * </div> 
	 * <div id="en">
	 * Returns the annualized term of {@code Vertex} corresponding to a given
	 * {@code code}
	 * </div>.
	 *
	 * @param code	
	 * 		<div id="ko">기간 코드</div>
	 * 		<div id="en">The term code</div>
	 * 
	 * @return		
	 * 		<div id="ko">연 단위 환산된 기간</div>
	 * 		<div id="en">The annualized term</div>
	 */
	public static double getVertex(String code) {
		return valueOf(code).getVertex();
	}

	/**
	 * <div id="ko">
	 * 이 {@code Vertex} 객체의 기간 코드를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a term code of this{@code Vertex} object
	 * </div>.
	 *
	 * @return	
	 * 		<div id="ko">기간 코드</div>
	 * 		<div id="en">The term code</div>
	 */
	public String toString() {
		return getCode();
	}

	/**
	 * <div id="ko"> 
	 * 주어진 연 단위 기간인 {@code yearFraction}과 일치하는 {@code Vertex} 객체를 찾는다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code Vertex} object which identifies with a given {@code yearFraction} 
	 * </div>.
	 * <div id="ko">
	 * <p>
	 * RiskCraft.DERIVATIVES의 Bond 클래스의 특정 경우만을 다루기 위해 만들었으므로
	 * 다른 분야에서 이 함수를 사용하고자 할 경우에는 각별한 주의가 필요하다.
	 * </div>
	 * <div id="en">
	 * <p>
	 * This function is only for a special case of Bond Class of RiskCraft.DERIVATIVES.
	 * Particular attention is necessary when the function is used in another class.
	 * </div> 
	 *
	 * @param yearFraction	
	 * 		<div id="ko">연 단위 기간</div>
	 * 		<div id="en">The annualized term</div>
	 * @return
	 * 		<div id="ko">주어진 연 단위 기간과 일치하는 {@code Vertex} 객체</div>
	 * 		<div id="en">A new {@code Vertex} object which identifies with the given {@code yearFraction}</div>
	 */
	// Jae-Heon Kim, 2009.09.14
	// For the convenience when treating bond
	public static Vertex valueOf(double yearFraction) {
		double remainder = yearFraction % 0.25;
		if (remainder != 0) {
			throw new IllegalArgumentException("Unsupported year fraction ["
					+ yearFraction + "]");
		}

		int years = (int)(yearFraction / 1);
		double decimalPart = yearFraction - (double)years;

		if (years >= 10 && decimalPart != 0) {
			throw new IllegalArgumentException("Unsupported year fraction ["
					+ yearFraction + "]");
		}

		if (years == 0) {
			if (decimalPart == 0.25) {
				return valueOf("M3");
			} else if (decimalPart == 0.5) {
				return valueOf("M6");
			} else if (decimalPart == 0.75) {
				return valueOf("M9");
			}
		}
		String code = "Y" + years;
		if (decimalPart == 0.25) {
			code += "Q";
		} else if (decimalPart == 0.5) {
			code += "H";
		} else if (decimalPart == 0.75) {
			code += "T";
		}
		return valueOf(code);
	}
	/**
	 * 입력받은 Vertex와 객체를 비교하여 같으면 true를
	 * 그렇지 않으면 false를 반환한다.
	 * 
	 * @param vertex
	 * 
	 * @return boolean
	 */
	// 20140317 Jihoon Lee
	public boolean isEqual(Vertex vertex){
		
		if (!(vertex.getCode() == _code)){
			return false;
		}
		return true;
		
	}
	// 20140821 Jihoon Lee
	/**
	 * 입수된 날짜(asOfDate)를 기준으로한 Vertex의 날짜를 리턴한다.
	 * 
	 * @param asOfDate
	 * @return
	 */
	public Date getDate(Date asOfDate){
		return asOfDate.plusDays(_days).plusMonths(_months).plusYears(_years);
//		if (_days != 0){
//			return asOfDate.plusDays(_days);
//		} else if (_months != 0){
//			return asOfDate.plusMonths(_months);
//		} else if (_years != 0){
//			return asOfDate.plusYears(_years);
//		} else {
//			return asOfDate;
//		}
	}
	/**
	 * 기존 Vertex에서 입수된 yearFraction을 더한만큼의 Vertex를 리턴한다.
	 * 
	 * @param yearFraction
	 * @return
	 */
	// 20140821 Jihoon Lee	
	public Vertex addVertex(double yearFraction){
		
		int addYear = (int) (yearFraction / 1);
		int addMonth = (int)((yearFraction % 1) * 12);
		
		int newYear = _years + addYear;
		int newMonth = _months + addMonth;
		if (newMonth == 12){
			newYear++;
			newMonth = 0;
		}
//		String value = newYear + newMonth + "00";
		
		String code = "";
		if (newYear != 0){
			code += "Y" + newYear;
			if (newMonth != 0){
				//3 Q, 6 H, 9 T
				if (newMonth == 3){
					code += "Q";
				} else if (newMonth == 6){
					code += "H";					
				} else if (newMonth == 9){
					code += "T";
				}				  
			}
			return Vertex.valueOf(code);
		} else {
			if (newMonth != 0){
				code += "M" + newMonth;
				return Vertex.valueOf(code);
			} else {
				return null;
			}
		}		
	}
}
