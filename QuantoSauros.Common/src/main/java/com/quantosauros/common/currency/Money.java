package com.quantosauros.common.currency;

import java.io.Serializable;
import java.math.BigDecimal;
//[ifndef jdk1.4] 
import java.math.MathContext;
//[endif]
/**
 * <div id = "ko"> 
 * {@code Money} 클래스는 통화를 표시하며, 통화 코드, 금액을 가지고 있다
 * </div>
 * <div id="en">
 * {@code Money} represents a currency and has a currency code and an amount
 * as a member field
 * </div>.
 * <div id = "ko"> 
 * Note: This class has a natural ordering that is inconsistent with equals.
 * </div>
 * <div id="en">
 * Note: This class has a natural ordering that is inconsistent with equals.
 * </div>
 * 
 * @author Oh, Sung Hoon
 * @author Kang Seok-Chan (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class Money implements Serializable, Comparable {

	private static final long serialVersionUID = -3959459514228009295L;

	/**
	 * <div id="ko">
	 * 소수점 이하 계산의 정확도를 {@code java.math.MathContext.UNLIMITED}로 설정
	 * </div>
	 * <div id="en">
	 * Set the accuracy of calculation below decimal point to {@code java.math.MathContext.UNLIMITED}
	 * </div>.
	 */
	// Jae-Heon Kim on 2009.02.26
//[ifndef jdk1.4]
	public static MathContext _MATH_CONTEXT = MathContext.UNLIMITED;
//[endif]
	//public static MathContext _MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);

    private Currency currency;

    private double amount;

	/**
	 * <div id="ko">
	 * Default 생성자
	 * </div>
	 * <div id="en">
	 * Default constructor
	 * </div>.
	 */
    public Money() {
    }

	/**
	 * <div id="ko">
	 * 추가된 생성자
	 * </div>
	 * <div id="en">
	 * Additional constructor
	 * </div>.
	 * 
	 * 2013/03/20, Ryoo, Sangwook
	 * 
	 * @param currency	
	 * 		<div id="ko">통화</div>
	 * 		<div id="en">currency</div>
	 * @param amount	
	 * 		<div id="ko">금액</div>
	 * 		<div id="en">The amount</div>
	 */
    public Money(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

	/**
	 * <div id="ko">
	 * 생성자
	 * </div>
	 * <div id="en">
	 * Constructor
	 * </div>.
	 *
	 * @param currency	
	 * 		<div id="ko">통화</div>
	 * 		<div id="en">Currency</div>
	 * @param amount	
	 * 		<div id="ko">금액</div>
	 * 		<div id="en">The amount</div>
	 * 
	 * @deprecated
	 */
    public Money(java.util.Currency currency, double amount) {
        this.currency = Currency.getInstance(currency.getCurrencyCode());
        this.amount = amount;
    }

	/**
	 * <div id="ko">
	 * 주어진 통화 코드와 금액을 가지는 새 {@code Money} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code Money} object which has a given currency code and 
	 * an amount
	 * </div>.
	 *
	 * @param ccyCode	
	 * 		<div id="ko">통화 code</div>
	 * 		<div id="en">Currency code</div>
	 * @param amount	
	 * 		<div id="ko">금액</div>
	 * 		<div id="en">The amount of money</div>
	 * @return			
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    public static Money valueOf(String ccyCode, double amount) {
        return new Money(Currency.getInstance(ccyCode), amount);
    }

	/**
	 * <div id="ko">
	 * 주어진 통화 코드와 금액을 가지는 새 {@code Money} 객체의 배열을  가져온다
	 * </div>
	 * <div id="en">
	 * Returns a new array of {@code Money} objects which have has a given 
	 * array of currency codes and a given array of amounts</div>.
	 *
	 * @param ccyCode	
	 * 		<div id="ko">통화 code</div>
	 * 		<div id="en">Currency code</div>
	 * @param amount	
	 * 		<div id="ko">금액</div>
	 * 		<div id="en">The amount of money</div>
	 * @return			
	 * 		<div id="ko">새 {@code Money} 객체 배열</div>
	 * 		<div id="en">A new array of {@code Money} obejects</div>
	 */
    public static Money[] valuesOf(String[] ccyCodes, double[] amounts) {
    	Money[] moneys = new Money[ccyCodes.length];
		for (int i = 0; i < ccyCodes.length; i++) {
			moneys[i] = new Money(Currency.getInstance(ccyCodes[i]), amounts[i]);
		}
        return moneys;
    }

	/**
	 * <div id="ko">
	 * 주어진 통화 코드와 단위 금액을 가지는 새 {@code Money} 객체를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code Money} object which has a given currency code and
	 * a unit amount
	 * </div>.
	 * 
	 * @param ccyCode	
	 * 		<div id="ko">통화 code</div>
	 * 		<div id="en">Currency code</div>
	 * @return
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    public static Money valueOf(String ccyCode) {
        return new Money(Currency.getInstance(ccyCode), 1);
    }

	/**
	 * <div id="ko">
	 * 주어진 통화 코드와 금액을 가지는 새 {@code Money} 객체의 배열을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns a new array of {@code Money} objects which have an array of given 
	 * currency codes and a unit amount</div>.
	 *
	 * @param ccyCode	
	 * 		<div id="ko">통화 code</div>
	 * 		<div id="en">Currency code</div>
	 * @return			
	 * 		<div id="ko">새 {@code Money} 객체 배열</div>
	 * 		<div id="en">A new array of {@code Money} obejects</div>
	 */
    public static Money[] valuesOf(String[] ccyCodes) {
    	Money[] moneys = new Money[ccyCodes.length];
		for (int i = 0; i < ccyCodes.length; i++) {
			moneys[i] = new Money(Currency.getInstance(ccyCodes[i]), 1);
		}
        return moneys;
    }

	/**
	 * use <code>getAppCurrency()</code>.
	 *
	 * @return	
	 * 		{@code java.util.Currency}
	 * 
	 * @deprecated
	 */
    public java.util.Currency getCurrency() {
        return currency.getIsoStandardCurrency();
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 통화를 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the currency of this {@code Money} object
	 * </div>.
	 * <div id="ko">
	 * 추가된 메써드</div>
	 * <div id="en">
	 * Additional method
	 * </div>
	 * 
	 * 2013/03/20, Ryoo, Sangwook
	 *
	 * @return	
	 * 		{@code com.fistglobal.common.Currency}
	 * 
	 */
    public Currency getAppCurrency() {
        return currency;
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 금액을 가져온다
	 * </div>
	 * <div id="en">
	 * Returns the amount of money of this {@code Money} object
	 * </div>.
	 *
	 * @return	
	 * 		<div id="ko">금액</div>
	 * 		<div id="en">The amount</div>
	 */
    public double getAmount() {
        return amount;
    }

    private Money calculate(Money money, Operation oper) {
        if (this.currency != money.currency) {
            throw new IllegalArgumentException("Different currency: "
                    + this.currency + " <> " + money.currency);
        }

        return calculate(money.amount, oper);
    }

    private Money calculate(double amount, Operation oper) {

        double result = oper.operate(this.amount, amount);
        return new Money(this.currency, result);
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 금액에 입력받은 {@code Money}의 금액을 더한 금액을 가지는 
	 * 새 {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en">
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object plus the amount of a given {@code Money} object  
	 * </div>.
	 *
	 * @param money
	 * 		<div id="ko">money 금액을 더할 {@code Money} 객체</div>
	 * 		<div id="en">The {@code Money} object whose amount is added</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    public Money add(Money money) {
        return calculate(money, Operation.ADD);
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 금액에서 입력받은 {@code Money}의 금액을 뺀 금액을 가지는 
	 * 새 {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object difference the amount of a given {@code Money}
	 * object  
	 * </div>.
	 *
	 * @param money 
	 * 		<div id="ko">빼야 할 금액을 가진 {@code Money} 객체</div>
	 * 		<div id="en">The {@code Money} object the amount of which the amount is subtracted by</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    public Money subtract(Money money) {
        return calculate(money, Operation.SUBTRACT);
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 금액에 입력받은 {@code Money}의 금액을 곱한 금액을 가지는
	 * 새 {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object multiplied by the amount of a given {@code Money}
	 * </div>.
	 * 
	 * @param money
	 * 		<div id="ko">곱해야 할 금액을 가진 {@code Money} 객체</div>
	 * 		<div id="en">The {@code Money} object whose amount is multiplied</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    // TODO:: 금액에 금액을 곱한 금액을 갖는다는 것이 타당한가?
    public Money multiply(Money money) {
        return calculate(money, Operation.MULTIPLY);
    }

    /**
     * <div id="ko">
	 * 이 {@code Money} 객체의 금액을 입력받은 {@code Money}의 금액으로 나눈 금액을 가지는
	 * 새 {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object divided by the amount of a given {@code Money}
	 * </div>.
	 * 
	 * @param money 
	 * 		<div id="ko">나눠야 할 금액을 가진 {@code Money} 객체</div>
	 * 		<div id="en">The {@code Money} object the amount of which the amount is divided by</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    // TODO:: 금액을 금액으로 나눈 금액을 갖는다는 것이 타당한가?
    public Money divide(Money money) {
        return calculate(money, Operation.DIVIDE);
    }

    /**
     * <div id="ko">
	 * 이 {@code Money} 객체의 금액에 입력받은 {@code amount}를 더한 금액을 가지는 새
	 * {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object plus a given {@code amount}  
	 * </div>.
	 *
	 * @param amount 
	 * 		<div id="ko">더할 금액 </div>
	 * 		<div id="en">The amount to be added</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    public Money addAmount(double amount) {
        return calculate(amount, Operation.ADD);
    }

    /**
     * <div id="ko">
	 * 이 {@code Money} 객체의 금액에서 입력받은 {@code amount}만큼을 뺀 금액을 가지는 
	 * 새 {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object difference a given {@code amount}  
	 * </div>.
	 *
	 * @param amount 
	 * 		<div id="ko">뺄 금액</div>
	 * 		<div id="en">The amount which the amount of the {@code Money} is differenced by</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A  new {@code Money} object</div>
	 */
    public Money subtractAmount(double amount) {
        return calculate(amount, Operation.SUBTRACT);
    }

    /**
     * <div id="ko">
	 * 이 {@code Money} 객체의 금액에 입력받은 {@code amount}를 곱한 금액을 가지는 새 
	 * {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object multilplied by a given {@code amount}  
	 * </div>.
	 * 
	 * @param amount 
	 * 		<div id="ko">곱할 금액</div>
	 * 		<div id="en">The amount to be multiplied</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    // TODO:: 금액에 금액을 곱한 금액을 갖는다는 것이 타당한가?
    public Money multiplyAmount(double amount) {
        return calculate(amount, Operation.MULTIPLY);
    }

    /**
     * <div id="ko">
	 * 이 {@code Money} 객체의 금액을 입력받은 {@code amount}로 나눈 금액을 가지는 새
	 * {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object whose amount is equal to the amount of
	 * this {@code Money} object divided by a given {@code amount}  
	 * </div>.
	 *
	 * @param amount 
	 * 		<div id="ko">나눌 금액</div>
	 * 		<div id="en">The amount which the amount of the {@code Money} is divided by</div>
	 * @return		
	 * 		<div id="ko">새 {@code Money} 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    // TODO:: 금액을 금액으로 나눈 금액을 갖는다는 것이 타당한가?
    public Money divideAmount(double amount) {
        return calculate(amount, Operation.DIVIDE);
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 통화로 입력받은 {@code amount} 만큼의 금액을 가지는
	 * 새 {@code Money} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object with the currency of this {@code Money}
	 * object and a given {@code amount}
	 * </div>.
	 *
	 * @param amount 
	 * 		<div id="ko">금액</div>
	 * 		<div id="en">The amount</div>
	 * 
	 * @return		 
	 * 		<div id="ko">입력받은 금액의 새 Money 객체</div>
	 * 		<div id="en">A new {@code Money} object</div>
	 */
    public Money get(double amount) {
        return new Money(currency, amount);
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 문자열 표현을 가져온다.
	 * </div>
	 * <div id="en"> 
	 * Return the string representation of this {@code Money} object
	 * </div>.
	 * 
	 * @return	
	 * 		<div id="ko">'통화 기호 + 금액' 형태의 문자열</div>
	 * 		<div id="en">The string representation of 'currency code + amount' </div>
	 */
    public String toString() {
        return currency.getSymbol() + "" + amount;
    }

	/**
	 * <div id="ko"> 
	 * 이 {@code Money} 객체의 금액을 입력된 {@code fxRate} 객체의 환율을 
	 * 적용하여 변환한 금액과 {@code fxRate} 객체가 나타내는 통화를 가지는
	 * 새{@code fxRate} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object with the converted currency and amount
	 * by coverting this {@code Money} object by a given {@code fxRate} object 
	 * </div>.
	 *
	 * @param fxRate 
	 * 		<div id="ko">환율</div>
	 * 		<div id="en">FX rate</div>
	 * @return		 
	 * 		<div id="ko">환전된 새{@code fxRate} 객체</div>
	 * 		<div id="en">A new converted {@code fxRate} object</div>
	 * 
	 */
    public Money exchange(FxRate fxRate) {

        if (currency == fxRate.getBaseAppCurrency()) {
        	// Jae-Heon Kim on 2009.02.11
        	/*double a = (new BigDecimal("" + amount, _MATH_CONTEXT))
        		.divide(new BigDecimal("" + fxRate.getFxRate(), _MATH_CONTEXT))
        		.doubleValue();
        	return new Money(fxRate.getCounterCcy(), a);*/
        	return new Money(fxRate.getCounterAppCurrency(), amount / fxRate.getFxRate());
            //return new Money(fxRate.getCounterCcy(), amount * fxRate.getFxRate());
        }
        else if (currency == fxRate.getCounterAppCurrency()) {
        	// Jae-Heon Kim on 2009.02.11
        	/*double a = getBigDecimal(amount)
        		.multiply(getBigDecimal(fxRate.getFxRate()))
        		.doubleValue();*/
        	//System.out.println("Money.exchange multiply returns = " + a);
        	//return new Money(fxRate.getBaseCcy(), a);
        	return new Money(fxRate.getBaseAppCurrency(), amount * fxRate.getFxRate());
            //return new Money(fxRate.getBaseCcy(), amount / fxRate.getFxRate());
        }

        return this;
    }
    
    private BigDecimal getBigDecimal(double value) {
    	return new BigDecimal("" + value
//[ifndef jdk1.4]
    			, _MATH_CONTEXT
//[endif]
    			);
    }

    /**
	 * <div id="ko"> 
	 * 이 {@code Money} 객체의 금액을 입력된 {@code fxRate} 객체의 환율을 
	 * 적용하여 변환한 금액과 {@code fxRate} 객체가 나타내는 통화를 가지는
	 * 새{@code fxRate} 객체를 반환한다
	 * </div>
	 * <div id="en"> 
	 * Returns a new {@code Money} object with the converted currency and amount
	 * by coverting this {@code Money} object by a given {@code fxRate} object 
	 * </div>.
	 * <div id="ko">
	 * <p>
	 * 이 함수는 {@code BigDecimal}을 사용하므로 VaR 계산 시 부동소수점 오류가
	 * 나타나지 않는다. 
	 * </div>
	 * <div id="en">
	 * <p>
	 * Floating-point error does not occur when VaR is calculated because the 
	 * method is using {@code BigDecimal}.
	 * </div>
	 * 
	 *
	 * @param fxRate 
	 * 		<div id="ko">환율</div>
	 * 		<div id="en">FX rate</div>
	 * @return		 
	 * 		<div id="ko">환전된 새{@code fxRate} 객체</div>
	 * 		<div id="en">A new converted {@code fxRate} object</div>
	 * 
	 */
    // Jae-Heon Kim on 2009.02.26
    public BigDecimal exchange2(FxRate fxRate) {

        if (currency == fxRate.getBaseAppCurrency()) {
        	BigDecimal bd1 = getBigDecimal(amount);
        	BigDecimal bd2 = getBigDecimal(fxRate.getFxRate());
        	
        	BigDecimal a = bd1.divide(bd2, bd1.scale() - bd2.scale(),
        			   BigDecimal.ROUND_HALF_UP);
        	return a;
        }
        else if (currency == fxRate.getCounterAppCurrency()) {
        	BigDecimal a = (getBigDecimal(amount))
        		.multiply(getBigDecimal(fxRate.getFxRate()));
        	//System.out.println("Money.exchange multiply returns = " + a);
        	return a;
        }
        return new BigDecimal("" + amount);
    }

    private static class Operation {

        private static final Operation ADD = new Operation("+");
        private static final Operation SUBTRACT = new Operation("-");
        private static final Operation MULTIPLY = new Operation("*");
        private static final Operation DIVIDE = new Operation("/");

        private String operSymbol;

        private Operation(String operSymbol) {
            this.operSymbol = operSymbol;
        }

        private double operate(double operand1, double operand2) {
        	// Jae-Heon Kim on 2009.02.26
        	BigDecimal o1 = getBigDecimal(operand1);
        	BigDecimal o2 = getBigDecimal(operand2);
            if ("+".equals(operSymbol)) {
            	return o1.add(o2).doubleValue();
            	//return operand1 + operand2;
            }
            if ("-".equals(operSymbol)) {
            	return o1.subtract(o2).doubleValue();
            	//return operand1 - operand2;
            }
            if ("*".equals(operSymbol)) {
            	return o1.multiply(o2).doubleValue();
            	//return operand1 * operand2;
            }
            if ("/".equals(operSymbol)) {
            	//return o1.divide(o2).doubleValue();
            	// Jae-Heon Kim, 2009.07.15
            	return o1.divide(o2, o1.scale() - o2.scale(),
            			BigDecimal.ROUND_HALF_UP).doubleValue();
            	//return operand1 / operand2;
            }
            throw new IllegalArgumentException("Unsupported operation : " + operSymbol);
        }
        
        private BigDecimal getBigDecimal(double value) {
        	return new BigDecimal("" + value
//[ifndef jdk1.4]
        			, _MATH_CONTEXT
//[endif]
        			);
        }

    }

	/**
	 * use <code>setAppCurrency(Currency currency)</code>.
	 *
	 * @param currency 
	 * 		<div id="ko"> 환율 </div>
	 * 		<div id="en"> currency</div>
	 * 
	 * @deprecated
	 */
    public void setCurrency(java.util.Currency currency) {
        this.currency = Currency.getInstance(currency.getCurrencyCode());
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체에 통화를 설정한다
	 * </div>
	 * <div id="en">
	 * Sets the curreny to this {@code Money} object
	 * </div>.
	 * <div id="ko">
	 * 추가된 메써드
	 * </div>
	 * <div id="en">
	 * Additinoal method
	 * </div>
	 * 
	 * @param currency 
	 * 		<div id="ko">통화</div>
	 * 		<div id="en">The currency</div>
	 */
    // 2013-03-20, Ryoo,Sangwook
    public void setAppCurrency(Currency currency) {
        this.currency = currency;
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체에 금액을 설정한다
	 * </div>
	 * <div id="en">
	 * Sets the amount to this {@code Money} object
	 * </div>.
	 *
	 * @param amount 
	 * 		<div id="ko">금액</div>
	 * 		<div id="en">The amount</div>
	 */
    public void setAmount(double amount) {
        this.amount = amount;
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체에 통화 코드를 설정한다
	 * </div>
	 * <div id="en">
	 * Sets the currency code to this {@code Money} object
	 * </div>.
	 *
	 * @param currencyCode 
	 * 		<div id="ko">통화 코드</div>
	 * 		<div id="en">The currency code</div>
	 */
    public void setCurrencyCode(String currencyCode) {
        this.currency = Currency.getInstance(currencyCode);
    }

	/**
	 * <div id="ko">
	 * 이 {@code Money} 객체의 금액과 주어진 {@code Money} 객체의 금액을 비교한다
	 * </div>
	 * <div id="en">
     * Compares the amount of this {@code Money} object with the amount of a given 
     * {@code Money} object
	 * </div>.
	 * <div id="ko">
	 * 이 {@code Money} 객체의 금액이 더 클 경우 1, 작을 경우 -1, 그리고
	 * 두 금액이 같을 경우 0을 반환한다.
	 * </div>
	 * <div id="en">
     * Returns 1 if the the amount of this {@code Money} is larger than the 
     * amount of a given {@code Money} object, -1 otherwise.
	 * </div>
	 * 
	 * @param o
	 * 			  <div id="ko">금액/div>
	 * 			  <div id="en">The amount</div>
	 * 
	 * @return <div id="ko">비교 결과</div>
	 * 		   <div id="en">1 if the amount of the {@code Money} is larger than the amount of the given {@code Money} object, -1 otherwise</div>
	 */
    // Chan-Soo Jeon on 2009.11.10
    public int compareTo(Object o) {
    	if (this.getAmount() > ((Money)o).getAmount()) {
    		return 1;
    	} else if (this.getAmount() == ((Money)o).getAmount()) {
    		return 0;
    	} else {
    		return -1;
    	}
    }

}

