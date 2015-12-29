package com.quantosauros.jpl.dto;

import com.quantosauros.common.currency.Currency;
import com.quantosauros.common.date.Date;

public class ProductInfo extends AbstractInfo {

	private Date _issueDate;
	private Date _maturityDate;
	private Currency _currency;
	private boolean _hasPrincipalExchange;
	
	public ProductInfo(Date issueDate, Date maturityDate, Currency currency,
			boolean hasPrincipalExchange) {
		_issueDate = issueDate;
		_maturityDate = maturityDate;
		_currency = currency;
		_hasPrincipalExchange = hasPrincipalExchange;
	}
	
	public Date getIssueDate(){
		return _issueDate;
	}
	public Date getMaturityDate(){
		return _maturityDate;
	}
	public Currency getCurrency(){
		return _currency;		
	}
	public boolean hasPrincipalExchange(){
		return _hasPrincipalExchange;
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n");
		buf.append("==" + getClass().getName() + "==");
		buf.append("\r\n");
		buf.append("ISSUEDATE: " + _issueDate.getDt());
		buf.append("\r\n");
		buf.append("MATURITYDATE: " + _maturityDate.getDt());
		buf.append("\r\n");
		buf.append("Currency: " + _currency.getCurrencyCode());			
		buf.append("\r\n");
		buf.append("hasPrincipalExchange: " + _hasPrincipalExchange);
		
		return buf.toString();
	}
}
