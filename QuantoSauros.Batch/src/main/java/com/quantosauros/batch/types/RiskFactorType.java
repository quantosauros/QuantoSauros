package com.quantosauros.batch.types;

public class RiskFactorType {

	public enum RiskFactor {
		//IR CURVE
		P_COUPON_IRC_CD1,
		P_COUPON_IRC_CD2,
		P_COUPON_IRC_CD3,
		R_COUPON_IRC_CD1,
		R_COUPON_IRC_CD2,
		R_COUPON_IRC_CD3,		
		DISC_IRC_CD,
		P_LEG_IRC_CD,
		R_LEG_IRC_CD,
		
		//HW VOL
		P_COUPON_IRC_CD1_HWVOL,
		P_COUPON_IRC_CD2_HWVOL,
		P_COUPON_IRC_CD3_HWVOL,
		R_COUPON_IRC_CD1_HWVOL,
		R_COUPON_IRC_CD2_HWVOL,
		R_COUPON_IRC_CD3_HWVOL,
		DISC_HWVOL,
		
		//HW PARAMS
		P_HWPARAMS1,
		P_HWPARAMS2,
		P_HWPARAMS3,
		R_HWPARAMS1,
		R_HWPARAMS2,
		R_HWPARAMS3,
		DISC_HWPARAMS,		
		
		//StlLeg FX RATE
		STLPAY_FXRATE,
		STLRCV_FXRATE,
		
		//Correlation
		CORRELATION,
		FXASSET_CORRELATION,
		
		//vol
		FX_VOLATILITY,
		
		
		NONE,
	}
	
	public static RiskFactor valueOf(String str){
		
		//IR CURVE
		if (str.equals("P_COUPON_IRC_CD1")){
			return RiskFactor.P_COUPON_IRC_CD1;
		} else if (str.equals("P_COUPON_IRC_CD2")){
			return RiskFactor.P_COUPON_IRC_CD2;
		} else if (str.equals("P_COUPON_IRC_CD3")){
			return RiskFactor.P_COUPON_IRC_CD3;
		} else if (str.equals("R_COUPON_IRC_CD1")){
			return RiskFactor.R_COUPON_IRC_CD1;
		} else if (str.equals("R_COUPON_IRC_CD2")){
			return RiskFactor.R_COUPON_IRC_CD2;
		} else if (str.equals("R_COUPON_IRC_CD3")){
			return RiskFactor.R_COUPON_IRC_CD3;
		} else if (str.equals("DISC_IRC_CD")){
			return RiskFactor.DISC_IRC_CD;
		} else if (str.equals("P_LEG_IRC_CD")){
			return RiskFactor.P_LEG_IRC_CD;
		} else if (str.equals("R_LEG_IRC_CD")){
			return RiskFactor.R_LEG_IRC_CD;
		} 
		//HW VOL
		else if (str.equals("P_COUPON_IRC_CD1_HWVOL")){
			return RiskFactor.P_COUPON_IRC_CD1_HWVOL;
		} else if (str.equals("P_COUPON_IRC_CD2_HWVOL")){
			return RiskFactor.P_COUPON_IRC_CD2_HWVOL;
		} else if (str.equals("P_COUPON_IRC_CD3_HWVOL")){
			return RiskFactor.P_COUPON_IRC_CD3_HWVOL;
		} else if (str.equals("R_COUPON_IRC_CD1_HWVOL")){
			return RiskFactor.R_COUPON_IRC_CD1_HWVOL;
		} else if (str.equals("R_COUPON_IRC_CD2_HWVOL")){
			return RiskFactor.R_COUPON_IRC_CD2_HWVOL;
		} else if (str.equals("R_COUPON_IRC_CD3_HWVOL")){
			return RiskFactor.R_COUPON_IRC_CD3_HWVOL;
		} else if (str.equals("DISC_HWVOL")){
			return RiskFactor.DISC_HWVOL;
		} 
		//HW PARAMS
		else if (str.equals("P_HWPARAMS1")){
			return RiskFactor.P_HWPARAMS1;
		} else if (str.equals("P_HWPARAMS2")){
			return RiskFactor.P_HWPARAMS2;
		} else if (str.equals("P_HWPARAMS3")){
			return RiskFactor.P_HWPARAMS3;
		} else if (str.equals("R_HWPARAMS1")){
			return RiskFactor.R_HWPARAMS1;
		} else if (str.equals("R_HWPARAMS2")){
			return RiskFactor.R_HWPARAMS2;
		} else if (str.equals("R_HWPARAMS3")){
			return RiskFactor.R_HWPARAMS3;
		} else if (str.equals("DISC_HWPARAMS")){
			return RiskFactor.DISC_HWPARAMS;
		}
		//StlLeg FX Rate
		else if (str.equals("STLPAY_FXRATE")){
			return RiskFactor.STLPAY_FXRATE;
		} else if (str.equals("STLRCV_FXRATE")){
			return RiskFactor.STLRCV_FXRATE;
		}
		//Correlation
		else if (str.equals("CORRELATION")){
			return RiskFactor.CORRELATION;
		} else if (str.equals("FXASSET_CORRELATION")){
			return RiskFactor.FXASSET_CORRELATION;
		}	
		//vol
		else if (str.equals("FX_VOLATILITY")){
			return RiskFactor.FX_VOLATILITY;
		}
		
		return RiskFactor.NONE;
	}
	
}
