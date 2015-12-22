package com.quantosauros.jpl.dto;

import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;

public class LegAmortizationInfo extends AbstractInfo {

	private Date[] _amortizationDates;
	private Money[] _amortizationPrincipals;
	private Money _principal;
	
	public LegAmortizationInfo(Money principal) {
		_principal = principal;
	}
	
	public LegAmortizationInfo(Money principal,
			Date[] amortizationDates, Money[] amortizationPrincipals) {
		_principal = principal;
		_amortizationDates = amortizationDates;
		_amortizationPrincipals = amortizationPrincipals;	
	}
	
	public Money getPrincipal(){
		return _principal;
	}
	public void setPrincipal(Money principal){
		this._principal = principal;
	}
	public Date[] getAmortizationDates(){
		return _amortizationDates;
	}
	public void setAmortizationDates(Date[] amortizationDates){
		this._amortizationDates = amortizationDates;
	}	
	public Money[] getAmortizationPrincipals(){
		return _amortizationPrincipals;
	}
	public void setAmortizationPrincipals(Money[] amortizationPrincipals){
		this._amortizationPrincipals = amortizationPrincipals;
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n");
		buf.append("==" + getClass().getName() + "==");
		buf.append("\r\n");
		if (_amortizationDates != null){
			for (int i = 0; i < _amortizationDates.length; i++){
				buf.append("amortizationDates[" + i + "]=" + _amortizationDates[i].toString());
			}
			buf.append("\r\n");
		} 
		if (_amortizationPrincipals != null){
			for (int i = 0; i < _amortizationPrincipals.length; i++){
				buf.append("amortizationPrincipals[" + i + "]=" + _amortizationPrincipals[i].toString());
			}
			buf.append("\r\n");
		}
		
		
		return buf.toString();
	}
}
