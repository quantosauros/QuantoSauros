package com.quantosauros.manager.model;

import java.text.DecimalFormat;
import java.util.Currency;

public class PriceInfo {

	protected String processDt;
	protected String instrumentCd;
	protected String nonCallCd;
	protected double price;
	protected double payPrice;
	protected double rcvPrice;
	protected double nonCallPrice;
	protected String ccyCd;
	
	private DecimalFormat df;
	
	public String getProcessDt(){
		return processDt;
	}
	public void setProcessDt(String processDt){
		this.processDt = processDt;
	}
	public String getInstrumentCd(){
		return instrumentCd;
	}
	public void setInstrumentCd(String instrumentCd){
		this.instrumentCd = instrumentCd;
	}
	public String getNonCallCd(){
		return this.nonCallCd;
	}
	public void setNonCallCd(String nonCallCd){
		this.nonCallCd = nonCallCd;
	}
	public String getPrice(){		
		return df.format(price);
	}
	public double getNonFormattedPrice(){
		return price;
	}
	public void setPrice(double price){		
		this.price = price;
	}
	public String getPayPrice(){
		return df.format(payPrice);
	}
	public double getNonFormattedPayPrice(){
		return payPrice;
	}
	public void setPayPrice(double payPrice){
		this.payPrice = payPrice;
	}
	public String getRcvPrice(){
		return df.format(rcvPrice);
	}
	public double getNonFormattedRcvPrice(){
		return rcvPrice;
	}
	public void setRcvPrice(double rcvPrice){
		this.rcvPrice = rcvPrice;
	}
	public String getNonCallPrice(){
		return df.format(nonCallPrice);
	}
	public void setNonCallPrice(double nonCallPrice){
		this.nonCallPrice = nonCallPrice;
	}
	public String getCcyCd(){
		return ccyCd;
	}
	public void setCcyCd(String ccyCd){
		String symbol = Currency.getInstance(ccyCd).getSymbol();
		df = new DecimalFormat(symbol + "#,##0.00");
		this.ccyCd = ccyCd;
	}
	
}
