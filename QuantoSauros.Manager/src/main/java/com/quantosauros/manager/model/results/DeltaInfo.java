package com.quantosauros.manager.model.results;

import java.text.DecimalFormat;
import java.util.Currency;

public class DeltaInfo {

	protected String processDt;
	protected String instrumentCd;
	protected String ircCd;
	protected String mrtyCd;	
	protected String mrtyListCd;
	protected String greekCd;
	protected String nonCallCd;
	protected double greek;
	protected double nonCallGreek;
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
	public String getIrcCd(){
		return ircCd;
	}
	public void setIrcCd(String ircCd){
		this.ircCd = ircCd;
	}
	public String getMrtyCd(){
		return mrtyCd;
	}
	public void setMrtyCd(String mrtyCd){
		this.mrtyCd = mrtyCd;
	}
	public String getMrtyListCd(){
		return mrtyListCd;
	}
	public void setMrtyListCd(String mrtyListCd){
		this.mrtyListCd = mrtyListCd;
	}
	public String getGreekCd(){
		return greekCd;
	}
	public void setGreekCd(String greekCd){
		this.greekCd = greekCd;
	}
	public String getNonCallCd(){
		return this.nonCallCd;
	}
	public void setNonCallCd(String nonCallCd){
		this.nonCallCd = nonCallCd;
	}
	public double getGreek(){
		return greek;
	}
	public String getGreekWithFormat(){
		return df.format(greek);
	}
	public void setGreek(double greek){
		this.greek = greek;
	}
	public void setNonCallGreek(double nonCallGreek){
		this.nonCallGreek = nonCallGreek;
	}
	public String getNonCallGreek(){
		return df.format(nonCallGreek);		
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
