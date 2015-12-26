package com.quantosauros.common;

public class TypeDef {

	public enum PayRcv {
		PAY, RCV,
	}
	
	public enum OptionType {
		CALL, PUT, NONE,
	}
	
	public enum RateType {
		SPOT, SWAP, RMS, EQ,
	}
	
	public enum CouponType {
		RESET, ACCRUAL, AVERAGE, FIXED,
	}
	
	public enum UnderlyingType {
		NONE, R1, R1mR2, R1nR2, R1nR2mR3, R1nEQ,
	}
	
	public enum ConditionType {
		NONE, R1, R2, R3, R1mR2, R2mR3, R1nR2, R1nR3, R2nR3, R1nR2mR3,
	}
	
	public enum ModelType {
		HW1F, HW2F, BS,
	}
	
	public enum LegType{
		APS, FLOAT, FIXED, RA,
	}
	
	public enum RiskFreeType{
		L1U1, L1U2, L1U3, L2U1, L2U2, L2U3,
	}			
			
	public enum ResultType{
		LEGPRICE_RESULT,
		COUPON_RESULT,
		TENOR_RESULT,
		EXERCISEINDEX_RESULT,
		REFERENCERATE_RESULT,
		DISCOUNTRATE_RESULT,
	}
	
	public static PayRcv getPayRcv(Object payRcv){
		if (payRcv.equals("PAY") || payRcv.equals(0) || payRcv.equals("0") || payRcv.equals("P")){
			return PayRcv.PAY;
		} else if (payRcv.equals("RCV") || payRcv.equals(1) || payRcv.equals("1") || payRcv.equals("R")){
			return PayRcv.RCV;
		} else {
			return null;
		}
	}
	
	public static OptionType getOptionType(Object optionType){
		if (optionType.equals("CALL") || optionType.equals(0) || optionType.equals("0")){
			return OptionType.CALL;
		} else if (optionType.equals("PUT") || optionType.equals(1)|| optionType.equals("1")){
			return OptionType.PUT;
		} else {
			return OptionType.NONE;
		}
	}

	public static RateType getRateType(Object rateType){
		if (rateType == null){
			return null;
		} else {
			if (rateType.equals("SPOT") || rateType.equals(0)|| rateType.equals("0")){
				return RateType.SPOT;
			} else if (rateType.equals("SWAP") || rateType.equals(1)|| rateType.equals("1")){
				return RateType.SWAP;
			} else if (rateType.equals("RMS") || rateType.equals(2)|| rateType.equals("2")){
				return RateType.RMS;
			} else {
				return null;
			}
		}		
	}
	
	public static CouponType getCouponType(Object couponType){
		if (couponType.equals("RESET") || couponType.equals(0) || couponType.equals("1")){
			return CouponType.RESET;
		} else if (couponType.equals("ACCRUAL") || couponType.equals(1)|| couponType.equals("2")){
			return CouponType.ACCRUAL;
		} else if (couponType.equals("AVERAGE") || couponType.equals(2)|| couponType.equals("3")){
			return CouponType.AVERAGE;
		} else if (couponType.equals("FIXED") || couponType.equals(3)|| couponType.equals("4")){
			return CouponType.FIXED;
		} else {
			return null;
		}
	}
	
	public static int getNumOfUnderNum(UnderlyingType underlyingType){
		int undNum = 0;
		switch (underlyingType){
			case NONE:
				undNum = 0;
				break;				
			case R1 :
				undNum = 1;
				break;
			case R1mR2 :
				undNum = 2;
				break;
			case R1nR2 :
				undNum = 2;
				break;
			case R1nR2mR3 :
				undNum = 3;
				break;		
			case R1nEQ :
				undNum = 2;
				break;
		}
		
		return undNum;
	}
	
	public static UnderlyingType getUnderlyingType(Object underlyingType){
		if (underlyingType.equals("NONE") || underlyingType.equals(0) || underlyingType.equals("0")){
			return UnderlyingType.NONE;
		} else if (underlyingType.equals("R1") || underlyingType.equals(1)|| underlyingType.equals("1")){
			return UnderlyingType.R1;
		} else if (underlyingType.equals("R1mR2") || underlyingType.equals(2)|| underlyingType.equals("2")){
			return UnderlyingType.R1mR2;
		} else if (underlyingType.equals("R1nR2") || underlyingType.equals(3)|| underlyingType.equals("3")){
			return UnderlyingType.R1nR2;
		} else if (underlyingType.equals("R1nR2mR3") || underlyingType.equals(4)|| underlyingType.equals("4")){
			return UnderlyingType.R1nR2mR3;
		} else {
			return null;
		}
	}
	
	public static ConditionType getConditionType(Object conditionType){
		if (conditionType.equals("NONE") || conditionType.equals(0) || conditionType.equals("0")){
			return ConditionType.NONE;
		} else if (conditionType.equals("R1") || conditionType.equals(1)|| conditionType.equals("1")){
			return ConditionType.R1;
		} else if (conditionType.equals("R2") || conditionType.equals(2)|| conditionType.equals("2")){
			return ConditionType.R2;
		} else if (conditionType.equals("R3") || conditionType.equals(3)|| conditionType.equals("3")){
			return ConditionType.R3;
		} else if (conditionType.equals("R1mR2") || conditionType.equals(4)|| conditionType.equals("4")){
			return ConditionType.R1mR2;
		} else if (conditionType.equals("R2mR3") || conditionType.equals(5)|| conditionType.equals("5")){
			return ConditionType.R2mR3;
		} else if (conditionType.equals("R1nR2") || conditionType.equals(6)|| conditionType.equals("6")){
			return ConditionType.R1nR2;
		} else if (conditionType.equals("R1nR3") || conditionType.equals(7)|| conditionType.equals("7")){
			return ConditionType.R1nR3;
		} else if (conditionType.equals("R2nR3") || conditionType.equals(8)|| conditionType.equals("8")){
			return ConditionType.R2nR3;
		} else if (conditionType.equals("R1nR2mR3") || conditionType.equals(9)|| conditionType.equals("9")){
			return ConditionType.R1nR2mR3;
		} else {
			return null;
		}
	}
	
	public static int getNumOfCondition(ConditionType conditionType){
		int condiNum = 0;
		switch (conditionType){
			case NONE:
				condiNum = 0;
				break;				
			case R1 :
				condiNum = 1;
				break;
			case R2 :
				condiNum = 1;
				break;
			case R3 :
				condiNum = 1;
				break;
			case R1mR2 :
				condiNum = 2;
				break;		
			case R2mR3 :
				condiNum = 2;
				break;
			case R1nR2 :
				condiNum = 2;
				break;
			case R1nR3 :
				condiNum = 2;
				break;
			case R2nR3 :
				condiNum = 2;
				break;
			case R1nR2mR3 :
				condiNum = 2;
				break;			
		}
		
		return condiNum;
	}
	public static String getResultTypeStr(ResultType resultType){
		String result = "";
		switch (resultType){
		case COUPON_RESULT :
			result = "CPN_RATE";
			break;
		case DISCOUNTRATE_RESULT :
			result = "DISC_RATE";
			break;
		case EXERCISEINDEX_RESULT :
			result = "EX_IDX";
			break;
		case LEGPRICE_RESULT :
			result = "LEG_PRICE";
			break;
		case REFERENCERATE_RESULT :
			result = "REF_RATE";
			break;
		case TENOR_RESULT :
			result = "TENOR";
			break;
		}
		
		return result;
	}
	
}