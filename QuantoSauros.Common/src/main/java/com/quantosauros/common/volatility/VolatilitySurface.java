package com.quantosauros.common.volatility;

import java.io.Serializable;

import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.hullwhite.HWVolatility;
import com.quantosauros.common.hullwhite.HWVolatilitySurface;

/**
 * <div id="ko">
 * {@code VolatilitySurface} 클래스는 변동성 곡면을 나타낸다
 * </div>
 * <div id="en">
 * {@code VolatilitySurface} represents a volatility surface
 * </div>.
 *
 * @author Jae-Heon Kim
 * @author Kang Seok-Chan (javadoc)
 * @since 3.1
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.04.28
------------------------------------------------------------------------------*/
public class VolatilitySurface implements Serializable {

	protected Surface _surface;
	DayCountFraction _dcf;
	Date _date;
	//20131227 Jihoon Lee, 변동성커브배열 저장
	protected VolatilityCurve[] _volCurves;
	
	protected VolatilitySurface() { }
	
	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>
	 *
	 * @param volCurves	<div id="ko">변동성 곡선의 배열</div><div id="en">The volatility curve </div>
	 * @param date		<div id="ko">기준일/div><div id="en">The valuation date</div>
	 * @param dcf		<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 */
	public VolatilitySurface(VolatilityCurve[] volCurves, Date date, DayCountFraction dcf) {
		// TODO:: Ensure all the date in vol curves are the same
		_date = date;
		// TODO:: Ensure all the day count fractions in vol curves are the same
		_dcf = dcf;
		//20131226 Jihoon Lee		
		_volCurves = volCurves;
		int nCurves = volCurves.length;
		// TODO:: Ensure all the curves have the same length
		int lenCurve = volCurves[0].length();
		double[] vx = new double[nCurves];
		double[] vy = new double[lenCurve];
		double[][] mz = new double[nCurves][lenCurve];
		for (int n = 0; n < nCurves; n++) {
			VolatilityCurve curve = volCurves[n];
			Vertex swapMaturity = curve.getSwapMaturity();
			vx[n] = swapMaturity.getVertex(curve.getDayCountFraction());
			//System.out.println("vx[" + n + "] = " + vx[n]);
			Volatility[] vols = curve.getVolatilities();
			int nVols = vols.length;
			for (int k = 0; k < nVols; k++) {
				Volatility vol = vols[k];
				if (n == 0) {
					vy[k] = vol.getX();
					//System.out.println("vy[k] = " + vy[k]);
				}
				mz[n][k] = vol.getY();
				//System.out.println("mz[" + n + "][" + k + "] = " + mz[n][k]);
			}
		}
		_surface = new Surface(vx, vy, mz);
	}
	//added by jihoon lee, 20140917
	private VolatilitySurface(Surface surface, Date date, DayCountFraction dcf){
		_date = date;
		_dcf = dcf;
		_surface = surface;
	}
	/**
	 * <div id="ko">
	 * 주어진 두 기간을 이용하여 2차원 선형 보간을 수행하여 변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized volatility using the swap maturity and the 
	 * swaption maturity by 2-dimensional linear interpolation
	 * </div>.
	 * 
	 *
	 * @param tenor		<div id="ko">스왑션 테너</div><div id="en">The swaption tenor</div>
	 * @param maturity	<div id="ko">스왑션 만기</div><div id="en">The swaption maturity</div>
	 * @return					<div id="ko">계산된 변동성</div><div id="en">The annualized volatility</div>
	 */
	public double getVol(double tenor, double maturity) {
		//double t = _surface.getYearFraction(_date, x);
		return _surface.get(tenor, maturity);
	}

	/**
	 * <div id="ko">
	 * 주어진 기간({@code swapMaturity})과 이 {@code VolatilitySurface} 객체의
	 * 측정일부터 주어진 날짜({@code swaptionMaturity})까지의 기간을 이용하여
	 * 2차원 선형 보간을 수행하여 변동성을 계산한다
	 * </div>
	 * <div id="en">
	 * Calculates the annualized volatility using the {@code swapMaturity} and 
	 * the term between the valuation date of this {@code VolatilitySurface} 
	 * object and the {@code swaptionMaturity} by 2-dimensional linear interpolation
	 * </div>.
	 *
	 * @param swapMaturity		<div id="ko">스와프 만기</div><div id="en">The swap maturity</div>
	 * @param swaptionMaturity	<div id="ko">스왑션 만기</div><div id="en">The swaption maturity</div>
	 * @return					<div id="ko">변동성</div><div id="en">The annualized volatility</div>
	 */
	public double getVol(double swapMaturity, Date swaptionMaturity) {
		double t = _dcf.getYearFraction(_date, swaptionMaturity);
		return _surface.get(swapMaturity, t);
	}

	/**
	 * 
	 * <div id="ko">
	 * 테스트를 위한 샘플 {@code VolatilitySurface} 객체를 생성한다.
	 * </div>
	 * <div id="en">
	 * Generates a sample {@code VolatilitySurface} objective for a test.
	 * </div>.
	 * <div id="ko">
	 * <p>
	 * 언제든지 없어질 수 있는 함수이므로 사용을 권장하지 않는다.
	 * </div>
	 * <div id="en">
	 * <p>
	 * The method can be removed any time so it is not recommended to use it.
	 * </div>
	 *
	 * @param date				<div id="ko">기준일</div><div id="en">The valuation date</div>
	 * @param dcf				<div id="ko">Day Count Fraction</div><div id="en">The day count fraction</div>
	 * @param swapMaturity		<div id="ko">스와프 만기</div><div id="en">The swap maturity</div>
	 * @param swaptionMaturity	<div id="ko">스왑션 만기</div><div id="en">The swaption maturity</div>
	 * @return					<div id="ko">변동성 곡면 샘플 객체</div><div id="en">A sample volatility surface objective</div>
	 */
	
	// 20131227 Jihoon Lee, 변동성 곡면에서 index번의 변동성 곡선 리턴
	/**
	 * 변동성 곡면에서 같은 swaption maturity에 해당하는 변동성 곡선을 반환한다.
	 * @param index				같은 swaption maturity의 변동성 순서
	 * 
	 * @return					같은 swaption maturity의 변동성 곡선
	 */
	public VolatilityCurve getVolatiltyCurve(int index){						
		return _volCurves[index];		
	}
	/**
	 * 변동성 곡면의 갯수를 리턴한다.
	 * 
	 * @return
	 */
	public int Length(){
		if (_volCurves == null){
			return 0;
		}
		return _volCurves.length;
	}
	/**
	 * 입력받은 Surface와 객체를 비교하여 각각의 만기 및 테너별로 변동성이 같으면 true를
	 * 그렇지 않으면 false를 반환한다.
	 * 
	 * @param surface
	 * 
	 * @return boolean
	 */
	// 20140317 Jihoon Lee
	public boolean isEqual(VolatilitySurface surface) {
		
		if (surface.Length() == 0){
			if (_surface.equals(surface.getSurface())){
				return true;
			} else {
				return false;
			}
		}		
		if (_volCurves.length != surface.Length()){
			return false;
		}
		
		for (int i = 0; i < surface.Length(); i++){
			VolatilityCurve curve = surface.getVolatiltyCurve(i);
			if (_volCurves[i].length() != curve.length()){
				return false;
			}
			if (!curve.isEqual(_volCurves[i])){
				return false;
			}
			
		}
		return true;
	}	
	
	/**
	 * <div id="ko">
	 * 이 {@code VolatilitySurface} 객체의 변동성에 {@code parallelShift}만큼 증가시킨
	 * 변동성을 가지는 새 {@code VolatilitySurface} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code VolatilityCurve} object by adding a given {@code parallelShift} 
	 * to the annualized volatility of this {@code VolatilityCurve} object
	 * </div>.
	 *
	 * @param parallelShift	<div id="ko">변동성의 증가분</div><div id="en">The parallel shift</div>
	 * @return 				<div id="ko">변동성을 증가시킨 새 {@code VolatilitySurface} 객체</div><div id="en">A new {@code VolatilitySurface} object</div>
	 */
	public VolatilitySurface copy(double parallelShift) {
		
		VolatilitySurface volSurface = new VolatilitySurface();
		
		volSurface._dcf  = this._dcf;
		volSurface._date = this._date;
		volSurface._volCurves = this._volCurves;
		
		double[][] grids = this._surface.getGrid();
		double[][] newGrids = new double[grids.length][grids[0].length];
		for (int i = 0; i<grids.length ;i++) {
			for (int j = 0; j<grids[i].length ;j++) {
				newGrids[i][j] = grids[i][j]+parallelShift;
			}
		}
		
		volSurface._surface = new Surface(this._surface.getXs(), this._surface.getYs(), newGrids);
		
		volSurface._volCurves = new VolatilityCurve[this._volCurves.length];
		for (int i=0 ; i<this._volCurves.length ; i++) {
			volSurface._volCurves[i] = this._volCurves[i].copy(parallelShift);
		}
		return volSurface;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (this instanceof HWVolatilitySurface){
			buf.append("Hull-White Volatility Surface");
		} else {
			buf.append("Volatility Surface");
		}		
		buf.append("\r\n");
		
		//swap tenor
		buf.append("Swaption Tenors \r\n");
		for (int i = 0; i < _volCurves.length; ++i) {
			buf.append(_volCurves[i].getSwapMaturity().toString() + ", ");
		}
		buf.append("\r\n");
		
		//swap maturity
		buf.append("Swaption Maturity \r\n");
		Volatility[] vols = _volCurves[0].getVolatilities();
		for (int j = 0; j < vols.length; j++){
			buf.append(vols[j].getVertex().toString() + ", ");
		}
		buf.append("\r\n");
		
		buf.append("Volatility Values (vertical : maturity, horizental : tenor) \r\n");
		if (this instanceof HWVolatilitySurface){
			String vol_hw1f = "";
			String vol_hw2f_1 = "";
			String vol_hw2f_2 = "";
			for (int i = 0; i < _volCurves.length; ++i) {
				vols = _volCurves[i].getVolatilities();
				
				//Vol Value
				for (int j = 0; j < vols.length; j++){				
					vol_hw1f += ((HWVolatility)vols[j]).getHW1F() + ", ";
					vol_hw2f_1 += ((HWVolatility)vols[j]).getHW2F_1() + ", ";
					vol_hw2f_2 += ((HWVolatility)vols[j]).getHW2F_2() + ", ";
				}
				vol_hw1f += "\r\n";
				vol_hw2f_1 += "\r\n";
				vol_hw2f_2 += "\r\n";
			}
			buf.append("  *Hull-White 1F Volatilities \r\n");
			buf.append(vol_hw1f);
			buf.append("\r\n");
			buf.append("  *Hull-White 2F Volatilities 1 \r\n");
			buf.append(vol_hw2f_1);
			buf.append("\r\n");
			buf.append("  *Hull-White 2F Volatilities 2 \r\n");
			buf.append(vol_hw2f_2);				
			buf.append("\r\n");
			
		} else if (this instanceof VolatilitySurface){
			for (int i = 0; i < _volCurves.length; ++i) {
				vols = _volCurves[i].getVolatilities();									
				//Vol Value
				for (int j = 0; j < vols.length; j++){				
					buf.append(vols[j].getRate() + ", ");
				}			
				buf.append("\r\n");
			}
		}				
		
		return buf.toString();
	}
	
	//added by jihoon lee, 20140918
	public VolatilitySurface parallelShift(double vol){
		double[][] oldValues = _surface.getGrid();
		double[][] newValues = new double[oldValues.length][oldValues[0].length];
		for (int i = 0; i < oldValues.length; i++){
			for (int j = 0; j < oldValues[0].length; j++){
				newValues[i][j] = oldValues[i][j] + vol;
			}
		}
		double[] vx = _surface.getXs();
		double[] vy = _surface.getYs();
		Surface newSurface = new Surface(vx, vy, newValues);
		
		
		return new VolatilitySurface(newSurface, _date, _dcf);
	}
	
	public double getVolByIndex(int tenorIndex, int mrtyIndex){
		return _volCurves[tenorIndex].getVolatilities()[mrtyIndex].getRate();
	}
	
	public Vertex getSwaptionMaturityVertex(int tenorIndex, int mrtyIndex){
		return _volCurves[tenorIndex].getVolatilities()[mrtyIndex].getVertex();
	}
	
	public Vertex getSwaptionTenorVertex(int tenorIndex){
		return _volCurves[tenorIndex].getSwapMaturity();
	}
	
	public Surface getSurface(){
		return _surface;
	}
		
	@Deprecated
	/**
	 * <div id="ko">
	 * 변동성 표면의 테스트 함수
	 * </div>
	 * <div id="en">
	 * The test method for volatility surface
	 * </div>.
	 *
	 * @param args	<div id="ko">미사용 파라미터</div><div id="en">The unused parameter</div>
	 */

 	public static void main(String[] args) {
		Date date = new Date("20090428");
		DayCountFraction dcf = DayCountFraction.DEFAULT;
		double swaptionMaturity = 0.484931507;
		double inSwapMaturity = 5.0;
		VolatilitySurface vs = VolatilitySurface.getSample(date, dcf,
				inSwapMaturity, swaptionMaturity);

		double ret = vs.getVol(inSwapMaturity, swaptionMaturity);
		System.out.println(ret);
	}

	@Deprecated	
	public static VolatilitySurface getSample(Date date, DayCountFraction dcf,
			double inSwapMaturity, double swaptionMaturity) {


		VolatilityCurve[] curves = new VolatilityCurve[6];
		Volatility[] extra = new Volatility[6];
		Volatility [] vols = new Volatility[8];
		vols [0] = new Volatility(Vertex.valueOf("M3"), 0.203);
		vols [1] = new Volatility(Vertex.valueOf("M6"), 0.193);
		vols [2] = new Volatility(Vertex.valueOf("Y1"), 0.168);
		vols [3] = new Volatility(Vertex.valueOf("Y2"), 0.144);
		vols [4] = new Volatility(Vertex.valueOf("Y3"), 0.140);
		vols [5] = new Volatility(Vertex.valueOf("Y5"), 0.127);
		vols [6] = new Volatility(Vertex.valueOf("Y7"), 0.124);
		vols [7] = new Volatility(Vertex.valueOf("Y10"), 0.115);
		Vertex swapMaturity = Vertex.valueOf("Y1");
		curves[0] = new VolatilityCurve(swapMaturity, vols, date, dcf);
		//System.out.println("vol[0] = " + curves[0].getVol(swaptionMaturity));
		extra[0] = new Volatility(swapMaturity, curves[0].getVol(swaptionMaturity));

		Volatility [] vols1 = new Volatility[8];
		vols1 [0] = new Volatility(Vertex.valueOf("M3"), 0.198);
		vols1 [1] = new Volatility(Vertex.valueOf("M6"), 0.189);
		vols1 [2] = new Volatility(Vertex.valueOf("Y1"), 0.163);
		vols1 [3] = new Volatility(Vertex.valueOf("Y2"), 0.140);
		vols1 [4] = new Volatility(Vertex.valueOf("Y3"), 0.134);
		vols1 [5] = new Volatility(Vertex.valueOf("Y5"), 0.122);
		vols1 [6] = new Volatility(Vertex.valueOf("Y7"), 0.118);
		vols1 [7] = new Volatility(Vertex.valueOf("Y10"), 0.108);
		swapMaturity = Vertex.valueOf("Y2");
		curves[1] = new VolatilityCurve(swapMaturity, vols1, date, dcf);
		//System.out.println("vol[1] = " + curves[1].getVol(swaptionMaturity));
		extra[1] = new Volatility(swapMaturity, curves[1].getVol(swaptionMaturity));

		Volatility [] vols2 = new Volatility[8];
		vols2 [0] = new Volatility(Vertex.valueOf("M3"), 0.198);
		vols2 [1] = new Volatility(Vertex.valueOf("M6"), 0.189);
		vols2 [2] = new Volatility(Vertex.valueOf("Y1"), 0.159);
		vols2 [3] = new Volatility(Vertex.valueOf("Y2"), 0.135);
		vols2 [4] = new Volatility(Vertex.valueOf("Y3"), 0.130);
		vols2 [5] = new Volatility(Vertex.valueOf("Y5"), 0.120);
		vols2 [6] = new Volatility(Vertex.valueOf("Y7"), 0.117);
		vols2 [7] = new Volatility(Vertex.valueOf("Y10"), 0.108);
		swapMaturity = Vertex.valueOf("Y3");
		curves[2] = new VolatilityCurve(swapMaturity, vols2, date, dcf);
		//System.out.println("vol[2] = " + curves[2].getVol(swaptionMaturity));
		extra[2] = new Volatility(swapMaturity, curves[2].getVol(swaptionMaturity));

		Volatility [] vols3 = new Volatility[8];
		vols3 [0] = new Volatility(Vertex.valueOf("M3"), 0.193);
		vols3 [1] = new Volatility(Vertex.valueOf("M6"), 0.187);
		vols3 [2] = new Volatility(Vertex.valueOf("Y1"), 0.154);
		vols3 [3] = new Volatility(Vertex.valueOf("Y2"), 0.126);
		vols3 [4] = new Volatility(Vertex.valueOf("Y3"), 0.120);
		vols3 [5] = new Volatility(Vertex.valueOf("Y5"), 0.111);
		vols3 [6] = new Volatility(Vertex.valueOf("Y7"), 0.106);
		vols3 [7] = new Volatility(Vertex.valueOf("Y10"), 0.104);
		swapMaturity = Vertex.valueOf("Y5");
		curves[3] = new VolatilityCurve(swapMaturity, vols3, date, dcf);
		//System.out.println("vol[3] = " + curves[3].getVol(swaptionMaturity));
		extra[3] = new Volatility(swapMaturity, curves[3].getVol(swaptionMaturity));

		Volatility [] vols4 = new Volatility[8];
		vols4 [0] = new Volatility(Vertex.valueOf("M3"), 0.190);
		vols4 [1] = new Volatility(Vertex.valueOf("M6"), 0.181);
		vols4 [2] = new Volatility(Vertex.valueOf("Y1"), 0.146);
		vols4 [3] = new Volatility(Vertex.valueOf("Y2"), 0.123);
		vols4 [4] = new Volatility(Vertex.valueOf("Y3"), 0.114);
		vols4 [5] = new Volatility(Vertex.valueOf("Y5"), 0.104);
		vols4 [6] = new Volatility(Vertex.valueOf("Y7"), 0.101);
		vols4 [7] = new Volatility(Vertex.valueOf("Y10"), 0.100);
		swapMaturity = Vertex.valueOf("Y7");
		curves[4] = new VolatilityCurve(swapMaturity, vols4, date, dcf);
		//System.out.println("vol[4] = " + curves[4].getVol(swaptionMaturity));
		extra[4] = new Volatility(swapMaturity, curves[4].getVol(swaptionMaturity));

		Volatility [] vols5 = new Volatility[8];
		vols5 [0] = new Volatility(Vertex.valueOf("M3"), 0.181);
		vols5 [1] = new Volatility(Vertex.valueOf("M6"), 0.174);
		vols5 [2] = new Volatility(Vertex.valueOf("Y1"), 0.140);
		vols5 [3] = new Volatility(Vertex.valueOf("Y2"), 0.115);
		vols5 [4] = new Volatility(Vertex.valueOf("Y3"), 0.106);
		vols5 [5] = new Volatility(Vertex.valueOf("Y5"), 0.097);
		vols5 [6] = new Volatility(Vertex.valueOf("Y7"), 0.095);
		vols5 [7] = new Volatility(Vertex.valueOf("Y10"), 0.092);
		swapMaturity = Vertex.valueOf("Y10");
		curves[5] = new VolatilityCurve(swapMaturity, vols5, date, dcf);
		//System.out.println("vol[5] = " + curves[5].getVol(swaptionMaturity));
		extra[5] = new Volatility(swapMaturity, curves[5].getVol(swaptionMaturity));

		VolatilitySurface volSurface = new VolatilitySurface(curves, date, dcf);

		//VolatilityCurve c = new VolatilityCurve(date, extra, dcf);
		//System.out.println(c.getVol(inSwapMaturity));
		return volSurface;
	}

}
