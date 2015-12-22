package com.quantosauros.common.currency;

import java.io.Serializable;

/**
 * <div id="ko">
 * 서로 다른 두 통화 간의 환율을 표현하기 위한 클래스이다
 * </div>
 * <div id="en">
 * The class represents an exchange rate between two currencies 
 * </div>.
 * 
 * @author Oh, Sung Hoon
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class FxRate implements Serializable, Cloneable {

	private static final long serialVersionUID = -7236972713196622894L;

	private String key;

	private Currency baseCcy;

	private Currency counterCcy;

	private double fxRate;

	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param baseCcy		<div id="ko">기준 통화</div><div id="en">The base currency</div>
	 * @param counterCcy	<div id="ko">대상 통화</div><div id="en">The counter currency/div>
	 * @param fxRate		<div id="ko">환율</div><div id="en">The exchange rate</div>
	 * @deprecated
	 */
	public FxRate(java.util.Currency baseCcy, java.util.Currency counterCcy, double fxRate) {
		this.baseCcy = Currency.getInstance(baseCcy.getCurrencyCode());
		this.counterCcy = Currency.getInstance(counterCcy.getCurrencyCode());
		this.fxRate = fxRate;
	}
	
	/**
	 * <div id="ko">생성자</div>
	 * <div id="en">Constructor</div>.
	 *
	 * @param baseCcy		<div id="ko">기준 통화</div><div id="en">The base currency</div>
	 * @param counterCcy	<div id="ko">대상 통화</div><div id="en">The counter currency/div>
	 * @param fxRate		<div id="ko">환율</div><div id="en">The exchange rate</div>
	 */
	// 추가된 생성자 2013-03-20, Ryoo, Sangwook
	public FxRate(Currency baseCcy, Currency counterCcy, double fxRate) {
		this.baseCcy = baseCcy;
		this.counterCcy = counterCcy;
		this.fxRate = fxRate;
	}
	
	// Jae-Heon Kim 2012.07.04
	public FxRate() {
		// Empty
	}

	/**
	 * <div id="ko">
	 * 주어진 두 통화 간의 환율이 {@code fxRate}인 {@code FxRate} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a {@code FxRate} object consisting of a given foreign exchange
	 * rate between a given base currency and a counter currency
	 * </div>.
	 *
	 * @param baseCcyCode		<div id="ko">기준 통화 코드</div><div id="en">The base currency code</div>
	 * @param counterCcyCode	<div id="ko">대상 통화 코드</div><div id="en">The counter currency code</div>
	 * @param fxRate			<div id="ko">환율</div><div id="en">The exchange rate</div>
	 * @return					<div id="ko">{@code FxRate} 객체</div><div id="en">A {@code FxRate} object</div>
	 */
	// TODO:: Make sure that baseCcyCode and counterCcyCode are different
	public static FxRate valueOf(String baseCcyCode, String counterCcyCode,
			double fxRate) {
		return new FxRate(Currency.getInstance(baseCcyCode),
				Currency.getInstance(counterCcyCode), fxRate);
	}

	/**
	 * <div id="ko">
	 * 주어진 두 통화 간의 환율이 {@code payAmount} / {@	code receiveAmount}인 
	 * {@code FxRate} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a {@code FxRate} object consisting of the foreign exchange equal 
	 * to  {@code payAmount} / {@code receiveAmount} between a given base 
	 * currency and a counter currency
	 * </div>.
	 *
	 * @param baseCcyCode		<div id="ko">기준 통화 코드</div><div id="en">The base currency code</div>
	 * @param counterCcyCode	<div id="ko">대상 통화 코드</div><div id="en">The counter currency code</div>
	 * @param receiveAmount		<div id="ko">수취 금액</div><div id="en">The amount received</div>
	 * @param payAmount			<div id="ko">지급 금액</div><div id="en">The amount paid</div>
	 * @return					<div id="ko">{@code FxRate} 객체</div><div id="en">A {@code FxRate} object</div>
	 */
	public static FxRate valueOf(String baseCcyCode, String counterCcyCode,
			double receiveAmount, double payAmount) {
		return new FxRate(Currency.getInstance(baseCcyCode),
				Currency.getInstance(counterCcyCode), payAmount / receiveAmount);
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 key를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the key value of this {@code FxRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">FxRate의 key</div>
	 * 		   <div id="en">The key value</div>
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 key를 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a key value of this {@code FxRate} object
	 * </div>.
	 * 
	 * @param key	<div id="ko">FxRate 객체의 key</div><div id="en">The key value</div>
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * use <code>getAppBaseCcy()</code>
	 * @return <div id="ko">FxRate의 기준 통화</div>
	 * 		   <div id="en">The base currency</div>
	 * @deprecated
	 */
	public java.util.Currency getBaseCcy() {
		return baseCcy.getIsoStandardCurrency();
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 기준 통화를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the base currency of this {@code FxRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">FxRate의 기준 통화</div>
	 * 		   <div id="en">The base currency</div>
	 */
	// 추가된 메써드   2013-03-20 , Ryoo, Sangwook
	public Currency getBaseAppCurrency() {
		return baseCcy;
	}

	/**
	 * use <code>setAppBaseCcy(Currency baseCcy)</code>.
	 * @param baseCcy	<div id="ko">기준 통화</div><div id="en">The base currency</div>
	 * @deprecated
	 */
	public void setBaseCcy(java.util.Currency baseCcy) {
		this.baseCcy = Currency.getInstance(baseCcy.getCurrencyCode());
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 기준 통화를 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a base currency of this {@code FxRate} object
	 * </div>.
	 * 
	 * @param baseCcy	<div id="ko">기준 통화</div><div id="en">The base currency</div>
	 */
	// 추가된 메써드   2013-03-20 , Ryoo, Sangwook
	public void setBaseAppCurrency(Currency baseCcy) {
		this.baseCcy = baseCcy;
	}

	/**
	 * use <code>getAppCounterCcy()</code>.
	 * 
	 * @return <div id="ko">FxRate의 대상 통화</div>
	 * 		   <div id="en">The counter currency</div>
	 * @deprecated
	 */
	public java.util.Currency getCounterCcy() {
		return counterCcy.getIsoStandardCurrency();
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 대상 통화를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the base currency of this {@code FxRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">FxRate의 대상 통화</div>
	 * 		   <div id="en">The counter currency</div>
	 */
	// 추가된 메써드   2013-03-20 , Ryoo, Sangwook
	public Currency getCounterAppCurrency() {
		return counterCcy;
	}

	/**
	 * use <code>setAppCounterCcy(Currency counterCcy) </code>.
	 *
	 * @param counterCcy	<div id="ko">대상 통화</div><div id="en">The counter currency</div>
	 * @deprecated
	 */
	public void setCounterCcy(java.util.Currency counterCcy) {
		this.counterCcy = Currency.getInstance(counterCcy.getCurrencyCode());
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 대상 통화를 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a base currency of this {@code FxRate} object
	 * </div>.
	 * 
	 * @param counterCcy	<div id="ko">대상 통화</div><div id="en">The counter currency</div>
	 */
	// 추가된 메써드   2013-03-20 , Ryoo, Sangwook
	public void setCounterAppCurrency(Currency counterCcy) {
		this.counterCcy = counterCcy;
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 환율을 반환한다
	 * </div>
	 * <div id="en">
	 * Returns the fx rate of this {@code FxRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">환율</div>
	 * 		   <div id="en">The fx rate</div>
	 */
	public double getFxRate() {
		return fxRate;
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 환율을 설정한다
	 * </div>
	 * <div id="en">
	 * Sets a fx rate of this {@code FxRate} object
	 * </div>.
	 * 
	 * @param fxRate	<div id="ko">환율</div><div id="en">The fx rate</div>
	 */
	public void setFxRate(double fxRate) {
		this.fxRate = fxRate;
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체를 복제한다
	 * </div>
	 * <div id="en">
	 * Clones the {@code FxRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">복제된 {@code FxRate} 객체</div>
	 * 		   <div id="en">A cloned object</div>
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <div id="ko">
	 * 이 {@code FxRate} 객체의 문자열 표현을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the string representation of this {@code FxRate} object
	 * </div>.
	 * 
	 * @return <div id="ko">이 {@code FxRate} 객체의 문자열 표현</div>
	 * 		   <div id="en">The string representation of the {@code FxRate} object</div>
	 */
	public String toString() {
		return fxRate + " " + baseCcy.getSymbol() + "/"
				+ counterCcy.getSymbol();
	}
}
