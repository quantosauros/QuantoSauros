package com.quantosauros.common.currency;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <div id="ko">
 * java.util.Currency 클래스가 ISO-4217 이외의 다른 통화코드를 받을 수 없는 문제를 해결하기 위해서 생성한 클래스
 * </div>
 * <div id="en">
 * The class replaces java.util.Currency to represent currencies more than what is in ISO-421  
 * </div>.
 * <div id="ko">
 * API 는 java.util.Currency 의 것을 유지하고 필요에 따라 추가한다.
 * </div> 
 * <div id="en">
 * API is maintained as in java.util.Currency and a method will be added if necessary.
 * </div>
 * 
 * @author swyoo@fistglobal.com, 2013-03-20
 *
 */
public class Currency implements Serializable {

	private static final long serialVersionUID = -3442910017476615520L;

	private static Map<String, Currency> table = Collections.synchronizedMap(new HashMap<String, Currency>());

	private String currencyCode;
	private java.util.Currency isoCurrency;
	
	private Currency(String currencyCode, java.util.Currency isoCurrency) {
		this.currencyCode = currencyCode;
		this.isoCurrency = isoCurrency;
	}
	
	public static Currency getInstance(Locale locale) {
		try {
			java.util.Currency isoCurrency = java.util.Currency.getInstance(locale);
			return getInstance(isoCurrency.getCurrencyCode());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	public static Currency getInstance(String currencyCode) {
		if (!table.containsKey(currencyCode)) {
			synchronized (table) {
				Currency currency = null;
				try {
					java.util.Currency isoCurrency = java.util.Currency.getInstance(currencyCode);
					currency = new Currency(currencyCode, isoCurrency);
				} catch (IllegalArgumentException e) {
					currency = new Currency(currencyCode, null);
				}
				table.put(currencyCode, currency);
			}
		}
		
		return table.get(currencyCode);
	}
	
	public String getCurrencyCode() {
		if (isoCurrency != null)
			return isoCurrency.getCurrencyCode();
		else
			return this.currencyCode;
	}
	
	public int getDefaultFractionDigits() {
		if (isoCurrency != null)
			return isoCurrency.getDefaultFractionDigits();
		else
			return 0;
	}
	
	public String getSymbol() {
		if (isoCurrency != null)
			return isoCurrency.getSymbol();
		else
			return "?";
	}
	
	public String getSymbol(Locale locale) {
		if (isoCurrency != null)
			return isoCurrency.getSymbol(locale);
		else
			return "?";
	}
	
	public String toString() {
		if (isoCurrency != null)
			return isoCurrency.toString();
		else
			return this.currencyCode;
	}

	public boolean isIsoStandard() {
		if (isoCurrency != null)
			return true;
		else
			return false;
	}

	public java.util.Currency getIsoStandardCurrency() {
		return isoCurrency;
	}

	// This method is called immediately after an object of this class is deserialized.
    // This method returns the singleton instance.
    protected Object readResolve() {
    	return Currency.getInstance(this.currencyCode);
    }

}
