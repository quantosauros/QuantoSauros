package com.quantosauros.jpl.dto;

import com.quantosauros.common.currency.Currency;
import com.quantosauros.common.date.Date;

public class ProductInfo extends AbstractInfo {

	private Date _issueDate;
	private Date _maturityDate;
	private Currency _currency;
	
	public ProductInfo(Date issueDate, Date maturityDate, Currency currency) {
		_issueDate = issueDate;
		_maturityDate = maturityDate;
		_currency = currency;
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
		
		return buf.toString();
	}
}
