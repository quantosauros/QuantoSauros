package com.quantosauros.common.date;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;
import com.quantosauros.common.calendar.SouthKoreaCalendar;

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
	protected Calendar _calendar;
	protected BusinessDayConvention _businessConvention;

	protected PaymentPeriodGenerator() {
		// Empty. Should not be instantiated explicitly.
	}

	public PaymentPeriodGenerator(Date issueDate, Date maturityDate,
			Date firstCouponPaymentDate, Date lastCouponPaymentDate,
			Frequency paymentFrequency) {
		_issueDate = issueDate;
		_maturityDate = maturityDate;
		_firstCouponPaymentDate = firstCouponPaymentDate;
		_lastCouponPaymentDate = lastCouponPaymentDate;
		_paymentFrequency = paymentFrequency;		
		_calendar = SouthKoreaCalendar.getCalendar(1);
		_businessConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
		adjustNullPaymentDates();
	}

	public PaymentPeriodGenerator(Date issueDate, Date maturityDate,
			Date firstCouponPaymentDate, Date lastCouponPaymentDate,
			Frequency paymentFrequency,
			Calendar calendar, BusinessDayConvention businessDayConvention) {
		_issueDate = issueDate;
		_maturityDate = maturityDate;
		_firstCouponPaymentDate = firstCouponPaymentDate;
		_lastCouponPaymentDate = lastCouponPaymentDate;
		_paymentFrequency = paymentFrequency;		
		_calendar = calendar;
		_businessConvention = businessDayConvention;
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
		_calendar = SouthKoreaCalendar.getCalendar(1);
		_businessConvention = BusinessDayConvention.MODIFIED_FOLLOWING;
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

		if (_paymentFrequency.compareTo(Frequency.valueOf("M")) > 0) {
			//20140401 Jihoon Lee, 원래는 Frequency가 M이상이면 에러이지만 Quarterly로 바꿔서 계산
			_paymentFrequency = Frequency.valueOf("Q");
		}

		List<PaymentPeriod> paymentPeriodList = new ArrayList<>();

		Date startDate = _issueDate;
		Date endDate = _firstCouponPaymentDate;
		Date paymentDate = null;

		// Deferred + Fixed Rate Bond
		if (_deferredDate != null) {
			PaymentPeriod paymentPeriod = new PaymentPeriod(_issueDate,
					_deferredDate, _deferredDate);
			paymentPeriodList.add(paymentPeriod);
			startDate = _deferredDate;
		}

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
			endDate = _firstCouponPaymentDate.plusMonths(
					_paymentFrequency.toMonthUnit() * count);
			count++;
		}

		// BusinessDayConvention에 의한 휴일의 이동		
		Iterator it = paymentPeriodList.iterator();
		while (it.hasNext()) {
			PaymentPeriod period = (PaymentPeriod)it.next();
			period.adjustPaymentDate(_calendar, _businessConvention);
		}

		return (PaymentPeriod [])paymentPeriodList.toArray(new PaymentPeriod[paymentPeriodList.size()]);
	}
}
