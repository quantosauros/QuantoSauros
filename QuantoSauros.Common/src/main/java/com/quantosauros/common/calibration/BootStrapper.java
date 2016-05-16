package com.quantosauros.common.calibration;

import java.util.ArrayList;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.YTMRateType;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.common.date.PaymentPeriodGenerator;
import com.quantosauros.common.interestrate.AbstractRate;
import com.quantosauros.common.interestrate.AbstractRateCurve;
import com.quantosauros.common.interestrate.BondRate;
import com.quantosauros.common.interestrate.ZeroRate;
import com.quantosauros.common.interestrate.SwapRate;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.LinearInterpolation;

public class BootStrapper {

	private Date asOfDate;
	private AbstractRateCurve originalCurve;	
	private ZeroRateCurve zeroRateCurve;
		
	public BootStrapper(AbstractRateCurve originalCurve) {
		this.originalCurve = originalCurve; 
		this.zeroRateCurve = new ZeroRateCurve(originalCurve.getDate(), 
				Frequency.CONTINUOUS, originalCurve.getDayCountFraction(), LinearInterpolation.getInstance());		
	}	
	
	public BootStrapper(AbstractRateCurve originalCurve, Interpolation interpolation) {
		this.originalCurve = originalCurve; 
		this.zeroRateCurve = new ZeroRateCurve(originalCurve.getDate(), 
				Frequency.CONTINUOUS, originalCurve.getDayCountFraction(), interpolation);		
	}
	
	public ZeroRateCurve calculate(){
				
		ArrayList<AbstractRate> rates = originalCurve.getRates();
		
		asOfDate = originalCurve.getDate();
		DayCountFraction dcf = originalCurve.getDayCountFraction();
				
		//DB 구현
		BusinessDayConvention bsConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
		int settlementDay = 1;		
		Calendar calendar = CalendarFactory.getInstance("KR", 0);
		
		Date effectiveDate = calendar.adjustDate(asOfDate.plusDays(settlementDay), bsConvention);
		Date maturityDate = effectiveDate; 
				
		for (int i = 0; i < rates.size(); i++){
			
			AbstractRate originalRate = rates.get(i);
			
			if (originalRate instanceof BondRate){
				//국고채
				BondRate bondRate = (BondRate) originalRate;
				if (bondRate.getRateType().equals(YTMRateType.BOND)){
					maturityDate = bondRate.getMaturityDate();
					Date accrualStartDate = bondRate.getAccrualStartDate();
					Frequency paymentFrequency = bondRate.getCouponFrequency();
					DayCountFraction rateDcf = bondRate.getDayCountFraction();
					
					double bondCoupon = bondRate.getCouponRate();
					double ytmRate = bondRate.getRate();
//					System.out.println("================" + maturityDate  + "=======================");				
//					System.out.println(ytmRate);
					
					//period generate
					PaymentPeriod[] newPeriod = generatePaymentPeriod(accrualStartDate, 
							maturityDate, paymentFrequency, calendar, bsConvention);

					//calculate bond price applied 30360 convention
					int totalDays = newPeriod[0].getEndDate().diff(newPeriod[0].getStartDate());
					int tenor = effectiveDate.diff(asOfDate);
					double df = 1 / (1 + ytmRate / totalDays * tenor * paymentFrequency.getInterval());
					double bondPrice = getBondPrice(effectiveDate, newPeriod, bondCoupon, paymentFrequency, ytmRate) * df;
					
					zeroRateCurve.putZeroRate(
							getBondZero(bondPrice, dcf.getYearFraction(asOfDate, maturityDate), 
									effectiveDate, newPeriod, paymentFrequency, Frequency.CONTINUOUS, bondCoupon));
				} else if (bondRate.getRateType().equals(YTMRateType.ZERO)){
					maturityDate = bondRate.getMaturityDate();				
					double ytmRate = bondRate.getRate();				
					zeroRateCurve.putZeroRate(new ZeroRate(dcf.getYearFraction(asOfDate, maturityDate), ytmRate));
				}	
			} else if (originalRate instanceof SwapRate){
				//Swap캘리
				SwapRate swapRate = (SwapRate) originalRate;
								
				if (swapRate.getRateType().equals(YTMRateType.ZERO)){
					maturityDate = calendar.adjustDate(swapRate.getVertex().getDate(effectiveDate), bsConvention);
					double rate = swapRate.getRate();
					double matT = dcf.getYearFraction(asOfDate, maturityDate);					
							
					if (i == 0){
						double df0 =1 + dcf.getYearFraction(asOfDate, maturityDate) * rate;
						if (matT == 0) {
							double zeroRate = 0;
							zeroRateCurve.putZeroRate(new ZeroRate(matT, zeroRate));
						} else {
							double zeroRate = Math.log(df0) / matT;
							zeroRateCurve.putZeroRate(new ZeroRate(matT, zeroRate));
						}						
					} else {
						double startTime = dcf.getYearFraction(asOfDate, effectiveDate);
						double tenor = dcf.getYearFraction(effectiveDate, maturityDate);
						
						double zeroRate = (Math.log(1 + tenor * rate) + startTime * zeroRateCurve.getSpotRate(startTime)) / matT;
						zeroRateCurve.putZeroRate(new ZeroRate(matT, zeroRate));
					}
					
				} else if (swapRate.getRateType().equals(YTMRateType.YTM)){
				
					Date prevMaturityDate = maturityDate;
					maturityDate = calendar.adjustDate(swapRate.getVertex().getDate(effectiveDate), bsConvention);
					Frequency paymentFrequency = swapRate.getPayCouponFrequency();
					BusinessDayConvention businessDayConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
					DayCountFraction payDcf = swapRate.getPayDcf();
					double rate = swapRate.getRate();
					
					PaymentPeriod[] periods = generatePaymentPeriod(effectiveDate, maturityDate, 
							paymentFrequency, calendar, businessDayConvention);
					
					double leftT = dcf.getYearFraction(effectiveDate, prevMaturityDate);
					double rightT = dcf.getYearFraction(effectiveDate, maturityDate);
					double leftZero = zeroRateCurve.getSpotRate(leftT);
					double numerator = zeroRateCurve.getDiscountFactor(periods[0].getStartDate()); 
					
					zeroRateCurve.putZeroRate(getSwapZero(rate, leftT, rightT, leftZero, numerator,
							periods, payDcf, swapRate.getCompoundFrequency()));
				}	
				
//				System.out.println(maturityDate);
			}				

		}
		
		return zeroRateCurve;
		
	}
	
	private ZeroRate getSwapZero(double targetValue,
			double leftT, double rightT, double leftZero, double numerator,
			PaymentPeriod[] paymentPeriod, DayCountFraction dcf,
			Frequency targetCmpdFreq){
		
		double error = 1.0e-15;
		double left = -1.0;
		double right = 1.0;
		double mid = 0.0;
		
		double swapRate = getParSwapRate(paymentPeriod, dcf, 
				leftT, rightT, leftZero, numerator, mid, targetCmpdFreq);
		
		while (Math.abs(swapRate - targetValue) > error){
			
			if (swapRate - targetValue > error){
				right = mid;
				mid = (left + right) / 2;
				swapRate = getParSwapRate(paymentPeriod, dcf, 
						leftT, rightT, leftZero, numerator, mid, targetCmpdFreq);
			} else if (swapRate - targetValue < - error){
				left = mid;
				mid = (left + right) / 2;
				swapRate = getParSwapRate(paymentPeriod, dcf, 
						leftT, rightT, leftZero, numerator, mid, targetCmpdFreq);
			} else {
				break; 
			}			
		}
		return new ZeroRate(rightT, mid);
				
	}
	
	private double getParSwapRate(PaymentPeriod[] paymentPeriods,
			DayCountFraction dcf,
			double leftT, double rightT, double leftZero, double numerator, double zero,
			Frequency targetCmpdFreq){
		
		double denominator = 0;
		double df = 0; 
		double t = 0;
//		double r = 0;
		
		zeroRateCurve.putZeroRate(new ZeroRate(rightT, zero));
		
		int size = paymentPeriods.length;
		for (int i = 0; i < size; i++){
			
			t = zeroRateCurve.getDayCountFraction().getYearFraction(asOfDate, paymentPeriods[i].getPaymentDate());
			
			if (t <= leftT){
				df = zeroRateCurve.getDiscountFactor(t, targetCmpdFreq);
			} else {
//				if (zeroRateCurve.getInterpolation() instanceof LinearInterpolation){
//					//Linear Interpolation
//					r = leftZero + (t - leftT) * (zero - leftZero) / (rightT - leftT);
//				} else {
//					//TODO Cublic
//					
//				}
//				df = getDiscountFactor(r, t, targetCmpdFreq);
				df = zeroRateCurve.getDiscountFactor(t, targetCmpdFreq);
			}
			
			denominator += df * dcf.getYearFraction(paymentPeriods[i].getStartDate(), paymentPeriods[i].getEndDate());
		}
		numerator -= df;
		
		zeroRateCurve.popBackZeroRate();
		
		return numerator / denominator;
	}
	
	private double getDiscountFactor(double r, double t,
			Frequency targetCmpdFreq) {

		if (targetCmpdFreq == Frequency.NONE) { // Simple
			return 1.0 / (1.0 + r * t);
		} else if (targetCmpdFreq == Frequency.CONTINUOUS) {
			return Math.exp(-r * t);
		}
		// Discrete Compounding
		double m = targetCmpdFreq.getFrequencyInYear();
		return 1.0 / Math.pow(1.0 + r / m, m * t);
	}
	
	private ZeroRate getBondZero(double targetValue, double maturityTenor,
			Date effectiveDate, PaymentPeriod[] paymentPeriod,
			Frequency paymentFrequency, Frequency compoundFrequency,
			double bondCoupon){
		
		double error = 1.0e-15;
		double left = 0.0;
		double right = 1.0;
		double mid = 0.5 * (left + right);
		double price = 0;
		price = getParBondPrice(paymentPeriod, bondCoupon, paymentFrequency, compoundFrequency, mid, maturityTenor);
		while (Math.abs(price - targetValue) > error){
			double diff = price - targetValue;
			if (diff > error){
				left = mid;
				mid = 0.5 * (left + right);
				price = getParBondPrice(paymentPeriod, bondCoupon, paymentFrequency, compoundFrequency, mid, maturityTenor);
			} else if (diff < -error){
				right = mid;
				mid = 0.5 * (left + right);
				price = getParBondPrice(paymentPeriod, bondCoupon, paymentFrequency, compoundFrequency, mid, maturityTenor);
			} else {
				break;
			}
		}		
		return new ZeroRate(maturityTenor, mid);
	}	
		
	private double getParBondPrice(PaymentPeriod[] paymentPeriod,
			double bondCoupon, Frequency paymentFrequency,
			Frequency compoundFrequency,
			double zero, double rightT){
	
		int size = paymentPeriod.length;
		double price = 0;
		double df = 0;
			
		zeroRateCurve.putZeroRate(new ZeroRate(rightT, zero));
		
		for (int i = 0; i < size; i++){
			df = zeroRateCurve.getDiscountFactor(paymentPeriod[i].getPaymentDate(), compoundFrequency);
			price += df * bondCoupon * paymentFrequency.getInterval();
		}
		
		zeroRateCurve.popBackZeroRate();
		
		price += df;		
		
		return price;
	}
	
	//ytm을 이용하여 bond의 가격을 계산
	
	private double getBondPrice(Date effectiveDate, PaymentPeriod[] paymentPeriod,
			double bondCoupon, Frequency paymentFrequency, double ytmRate){
		double bondPrice = 0;
		double intervalTenor = paymentFrequency.getInterval();
		double coupon = intervalTenor * bondCoupon;			
		
		int totalDays = paymentPeriod[0].getEndDate().diff(paymentPeriod[0].getStartDate());
		int accruedDays = effectiveDate.diff(paymentPeriod[0].getStartDate());
		int stubDays = paymentPeriod[0].getEndDate().diff(effectiveDate);
		double stubTenor = (double) stubDays / (double) totalDays * intervalTenor;
		double accruedTenor = (double) accruedDays / (double) totalDays * intervalTenor;
							
//		double stubTenor = dcf.getYearFraction(effectiveDate, paymentPeriod[0].getEndDate());
//		double accruedTenor = dcf.getYearFraction(paymentPeriod[0].getStartDate(), effectiveDate);
		
		double stubDF = 1 / (1 + ytmRate * stubTenor); 
		double intervalDF = 1 / (1 + ytmRate * intervalTenor);
		
		double df = 1;
		for (int j = 0; j < paymentPeriod.length; j++){
			
			df *= j == 0 ? stubDF : intervalDF;
			double cashflow = coupon * df;
			bondPrice += cashflow;
			
//			Date endDate = paymentPeriod[j].getEndDate();			
//			double tmpTenor = stubTenor + intervalTenor * j;
//			double tmpTenor = rateDcf.getYearFraction(effectiveDate, newPeriod[j].getEndDate());
//			System.out.println(paymentPeriod[j].getStartDate() + ", " + paymentPeriod[j].getEndDate() + ", " + coupon + ", " + tmpTenor + ", " + df);
						
											
		}
		bondPrice += df;
		
//		double tmpTenor = (stubTenor + intervalTenor * (paymentPeriod.length - 1));
//		System.out.println(paymentPeriod[paymentPeriod.length - 1].getStartDate() + ", " 
//				+ paymentPeriod[paymentPeriod.length - 1].getEndDate() + ", 1, " + tmpTenor + ", " + df);
		
		double accruedCoupon = bondCoupon * accruedTenor;
		
		double cleanPrice = bondPrice - accruedCoupon;
		

//		System.out.println(accruedCoupon);
//		System.out.println(bondPrice);
//		System.out.println(cleanPrice);
		
		
		return bondPrice;
		//calculate bond price applied daycountconvention
//		for (int j = 0; j < newPeriod.length; j++){
//			Date startDate = newPeriod[j].getStartDate();
//			Date endDate = newPeriod[j].getEndDate();
//			if (endDate.diff(asOfDate) <= 0){
//				continue;
//			}
//			double tenor = rateDcf.getYearFraction(startDate, endDate);
//			double totalTenor = rateDcf.getYearFraction(asOfDate, endDate);
////			double df = 1 / (1 + ytmRate / paymentFrequency.getFrequencyInYear());
//			double df = Math.exp(-ytmRate * totalTenor);
//			double coupon = tenor * bondCoupon;
//			
//			double cashflow = (j == newPeriod.length - 1) ? (coupon + 1) * df : coupon * df ;
//			
//			bondPrice += cashflow;
//			System.out.println(tenor + "," + totalTenor + "," + df + "," + cashflow + " : " + startDate + ", " + endDate);
//			
//		}	
	}
	
	private PaymentPeriod[] generatePaymentPeriod(Date issueDate, Date maturityDate, 
			Frequency paymentFrequency, Calendar calendar, BusinessDayConvention businessDayConvention){

		PaymentPeriodGenerator pg = new PaymentPeriodGenerator(issueDate, 
				maturityDate, null, maturityDate, paymentFrequency, 
				calendar, businessDayConvention);			
		PaymentPeriod[] period = pg.generate();
		int idx = 0;
		while (period[idx].getEndDate().diff(asOfDate) <= 0){
			if (idx == period.length)
				break;
			idx++;
		}
		int periodNum = period.length - idx;
		PaymentPeriod[] newPeriod = new PaymentPeriod[periodNum];
		System.arraycopy(period, idx, newPeriod, 0, periodNum);
		
		return newPeriod;
	}

}
