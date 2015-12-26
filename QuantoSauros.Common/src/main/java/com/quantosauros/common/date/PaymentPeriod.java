package com.quantosauros.common.date;

import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.date.PaymentPeriodGenerator.StubPeriod;

public class PaymentPeriod {

	protected Date _resetDate;
	protected Date _startDate;
	protected Date _endDate;
	protected Date _paymentDate;
	protected Date _stubDate;
	protected StubPeriod _stubType;
	
	public PaymentPeriod(Date resetDate, Date startDate, Date endDate, Date paymentDate) {
		_resetDate = resetDate;
		_startDate = startDate;
		_endDate = endDate;
		_paymentDate = paymentDate;	
	}
	public PaymentPeriod(Date startDate, Date endDate, Date paymentDate) {
		_startDate = startDate;
		_endDate = endDate;
		_paymentDate = paymentDate;
		_resetDate = startDate;
	}
	public void setResetDate(Date resetDate) {
		this._resetDate = resetDate;
	}
	public Date getResetDate() {
		return _resetDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this._paymentDate = paymentDate;
	}
	public Date getPaymentDate() {
		return _paymentDate;
	}
	public void setEndDate(Date endDate) {
		this._endDate = endDate;
	}
	public Date getStartDate() {
		return _startDate;
	}
	public Date getEndDate() {
		return _endDate;
	}
	public void setStubDate(Date stubDate) {
		_stubDate = stubDate;
	}
	public void setStubType(StubPeriod stubType) {
		_stubType = stubType;
	}
	public void adjustPaymentDate(Calendar calendar, BusinessDayConvention convention) {		
		_resetDate = calendar.adjustDate(_resetDate, convention);
		_startDate = calendar.adjustDate(_startDate, convention);
		_endDate = calendar.adjustDate(_endDate, convention);
		_paymentDate = calendar.adjustDate(_paymentDate, convention);
		 
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n");
		buf.append("[" + 
				_resetDate.getDt() + ", " +
				_startDate.getDt() + ", " +
				_endDate.getDt() + ", " +
				_paymentDate.getDt() + "]");
		
		return buf.toString();
	}
}
