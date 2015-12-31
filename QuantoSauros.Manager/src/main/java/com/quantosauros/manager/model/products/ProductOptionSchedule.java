package com.quantosauros.manager.model.products;

public class ProductOptionSchedule {

	private String instrumentCd;
	private String optionTypeCd;
	private String optionStrtDt;
	private String optionEndDt;
	private String strike;

	public void setInstrumentCd(String instrumentCd){this.instrumentCd=instrumentCd;}
	public void setOptionTypeCd(String optionTypeCd){this.optionTypeCd=optionTypeCd;}
	public void setOptionStrtDt(String optionStrtDt){this.optionStrtDt=optionStrtDt;}
	public void setOptionEndDt(String optionEndDt){this.optionEndDt=optionEndDt;}
	public void setStrike(String strike){this.strike=strike;}
	public String getInstrumentCd(){return instrumentCd;}
	public String getOptionTypeCd(){return optionTypeCd;}
	public String getOptionStrtDt(){return optionStrtDt;}
	public String getOptionEndDt(){return optionEndDt;}
	public String getStrike(){return strike;}

}
