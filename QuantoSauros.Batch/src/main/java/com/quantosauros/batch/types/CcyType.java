package com.quantosauros.batch.types;

public class CcyType {

	public enum CcyCode {
		KRW,
		USD,
		EUR,
		JPY,
		
		None,		
	}
	
	public static CcyCode valueof(String str){
		if (str.equals("KRW")){
			return CcyCode.KRW;
		} else if (str.equals("USD")){
			return CcyCode.USD;
		} else if (str.equals("EUR")){
			return CcyCode.EUR;
		} else if (str.equals("JPY")){
			return CcyCode.JPY;
		} else {
			return CcyCode.None;
		}
	}
}
