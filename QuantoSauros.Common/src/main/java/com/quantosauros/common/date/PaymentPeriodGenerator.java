package com.quantosauros.common.date;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.Configuration;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;

public class PaymentPeriodGenerator {

	public enum StubPeriod {
		NORMAL, SHORT_STUB, MIDDLE_STUB, LONG_STUB,
	}
	
	protected Date _issueDate;
	protected Date _maturityDate;
	protected Date _firstCouponPaymentDate;
	protected Date _lastCouponPaymentDate;
	protected Frequency _paymentFrequency;
	protected Date _deferredDate;

	protected PaymentPeriodGenerator() {
		// Empty. Should not be instantiated explicitly.
	}

	public PaymentPeriodGenerator(Date issueDate, Date maturityDate,
			Date firstCouponPaymentDate, Date lastCouponPaymentDate,
			Frequency paymentFrequency) {
		_issueDate = issueDate;
		_maturityDate = maturityDate;
		_firstCouponPaymentDate = firstCouponPaymentDate;
		// Moved to adjustNullPaymentDates() 2009.05.13
		/*
		if (this.firstCouponPaymentDate == null && paymentFrequency != Frequency.NONE) {
			this.firstCouponPaymentDate = issueDate.plusMonths(paymentFrequency.toMonthUnit());
			if (this.firstCouponPaymentDate.compareTo(maturityDate) > 0) {
				this.firstCouponPaymentDate = maturityDate;
			}
		}*/

		_lastCouponPaymentDate = lastCouponPaymentDate;
		/*
		if (this.lastCouponPaymentDate == null) {
			this.lastCouponPaymentDate = maturityDate;
		}*/
		_paymentFrequency = paymentFrequency;

		adjustNullPaymentDates();
	}

	// Kang Seok-Chan, 2009.09.18
	// Deferred + Fixed Rate Bond
	public PaymentPeriodGenerator(Date issueDate, Date maturityDate,
			Date firstCouponPaymentDate, Date lastCouponPaymentDate,
			Frequency paymentFrequency, Date deferredDate) {
		_issueDate = issueDate;
		_maturityDate = maturityDate;
		_firstCouponPaymentDate = firstCouponPaymentDate;
		_lastCouponPaymentDate = lastCouponPaymentDate;
		_paymentFrequency = paymentFrequency;
		_deferredDate = deferredDate;
		adjustNullPaymentDates();
	}

	protected void adjustNullPaymentDates() {
		if (_firstCouponPaymentDate == null
				&& _paymentFrequency != Frequency.NONE) {
			// Kang Seok-Chan, 2009.09.18
			if (_deferredDate == null) {
				_firstCouponPaymentDate = _issueDate.plusMonths(
						_paymentFrequency.toMonthUnit());
			} else {
				_firstCouponPaymentDate = _deferredDate.plusMonths(
						_paymentFrequency.toMonthUnit());
			}

			if (_firstCouponPaymentDate.compareTo(_maturityDate) > 0) {
				_firstCouponPaymentDate = _maturityDate;
			}
		}
		if (_lastCouponPaymentDate == null) {
			_lastCouponPaymentDate = _maturityDate;
		}
	}

	public PaymentPeriod [] generate() {
		
		// Jae-Heon Kim 2010.12.29
		if (_paymentFrequency.compareTo(Frequency.valueOf("M")) > 0) {
			//20140401 Jihoon Lee, 원래는 Frequency가 M이상이면 에러이지만 Quarterly로 바꿔서 계산
			_paymentFrequency = Frequency.valueOf("Q");
//			throw new DerivativesException(ErrorCode.OUT_OF_RANGE);
		}

		List paymentPeriodList = new ArrayList();

		Date startDate = _issueDate;
		Date endDate = _firstCouponPaymentDate;
		Date paymentDate = null;

		// Kang Seok-Chan, 2009.09.18
		// Deferred + Fixed Rate Bond
		if (_deferredDate != null) {
			PaymentPeriod paymentPeriod = new PaymentPeriod(_issueDate,
					_deferredDate, _deferredDate);
			paymentPeriodList.add(paymentPeriod);
			startDate = _deferredDate;
		}

		// Jae-Heon Kim 2010.12.29
		int count = 1;
		
		boolean prepayment = false;
		if (startDate.equals(_firstCouponPaymentDate)) {
			prepayment = true;
			endDate = endDate.plusMonths(_paymentFrequency.toMonthUnit());
			count++;
		}

		boolean done = false;
		StubPeriod stubType = StubPeriod.NORMAL;
		Date stubDate = null;
		int deltaT = 0;
		while (!done) {
//		while (!done && endDate.compareTo(lastCouponPaymentDate) <= 0) {

			if (endDate.compareTo(_maturityDate) >= 0) {
				if (endDate.compareTo(_maturityDate) > 0) {
					deltaT = (int)startDate.getDays(_maturityDate);
					int t1 = 0;
					int t2 = 0;

					if (deltaT <= t1) {
						stubType = StubPeriod.SHORT_STUB;
						stubDate = startDate;
					}
					else if ((deltaT > startDate.getDays(endDate) - t2)) {
						stubType = StubPeriod.LONG_STUB;
						stubDate = endDate;
					}
					else {
						stubType = StubPeriod.MIDDLE_STUB;
						stubDate = startDate;
					}
				}

				endDate = _maturityDate;
				done = true;
			}

			if (prepayment) {
				paymentDate = startDate;
			}
			else {
				paymentDate = endDate;
			}

			PaymentPeriod paymentPeriod = null;
			if ((stubType == StubPeriod.NORMAL) || (stubType == StubPeriod.LONG_STUB)) {
				paymentPeriod = new PaymentPeriod(startDate, endDate, paymentDate);
				paymentPeriodList.add(paymentPeriod);
			}
			else {
				if (paymentPeriodList.size() > 0) {
					paymentPeriod = (PaymentPeriod)paymentPeriodList.get(paymentPeriodList.size() - 1);
					paymentPeriod.setEndDate(endDate);
				}
			}
			if (paymentPeriod != null) {
				paymentPeriod.setStubType(stubType);
				paymentPeriod.setStubDate(stubDate);
			}

			startDate = endDate;
			//endDate = endDate.plusMonths(_paymentFrequency.toMonthUnit());
			endDate = _firstCouponPaymentDate.plusMonths(
					_paymentFrequency.toMonthUnit() * count);
			count++;
		}

//		if (lastCouponPaymentDate.compareTo(maturityDate) != 0) {
//			// 마지막 이자지급일과 만기일이 일치하지 않는 경우로,
//			// 이 경우는 만기에 원금이 발생하도록 한다.
//
//			// TODO:: Jae-Heon Kim, 2009.04.22
//			// How to handle periods that don't have the regular length...?
//			//startDate = maturityDate;
//			endDate = maturityDate;
//
//			if (prepayment) {
//				paymentDate = startDate;
//			}
//			else {
//				paymentDate = maturityDate;
//			}
//
//			PaymentPeriod paymentPeriod = new PaymentPeriod(startDate, endDate, paymentDate);
//			paymentPeriodList.add(paymentPeriod);
//		}

		// BusinessDayConvention에 의한 휴일의 이동
		Calendar calendar = CalendarFactory.getInstance("KR", 1);
		BusinessDayConvention convention = BusinessDayConvention.getInstance(1);
		Iterator it = paymentPeriodList.iterator();
		while (it.hasNext()) {
			PaymentPeriod period = (PaymentPeriod)it.next();
			period.adjustPaymentDate(calendar, convention);
		}

		return (PaymentPeriod [])paymentPeriodList.toArray(new PaymentPeriod[paymentPeriodList.size()]);
	}
}
