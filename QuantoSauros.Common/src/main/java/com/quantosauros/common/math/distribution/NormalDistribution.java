package com.quantosauros.common.math.distribution;



/**
 * {@code NormalDistribution} 클래스는 정규분포(Normal Distribution)를 사용하여,
 * Cumulative Probability, InverseCumulative Probability 등을 구하는 함수를
 * 제공한다.
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/

public class NormalDistribution {

	// Jae-Heon Kim 2012.07.04
	public enum Type {
		R2, Jakarta, Hart 
	}
	
	/**
	 * 표준정규분포 누적 확률 값을 구한다.
	 * 
	 * @param x x 값
	 * @return 누적정규분포 확률
	 * @see #getCumulativeProbability_R2
	 */
	public static double getCumulativeProbability(double x) {
		return getCumulativeProbability_R2(x);
	}
	
	/**
	 * 표준정규분포 누적 확률 값을 구한다.
	 * 
	 * @param x x 값
	 * @return 누적정규분포 확률
	 * @see #getCumulativeProbability_R2
	 */
	public static double getCumulativeProbability_Hart(double x) {
		double d = 0;
		double y = Math.abs(x);
	    if (y > 37) {
	        d = 0;
	    }
	    else {
	    	double exp = Math.exp(- y * y / 2);
	        if (y < 7.07106781186547) {
	        	double SumA = 3.52624965998911E-02 * y + 0.700383064443688;
	            SumA = SumA * y + 6.37396220353165;
	            SumA = SumA * y + 33.912866078383;
	            SumA = SumA * y + 112.079291497871;
	            SumA = SumA * y + 221.213596169931;
	            SumA = SumA * y + 220.206867912376;
	            double SumB = 8.83883476483184E-02 * y + 1.75566716318264;
	            SumB = SumB * y + 16.064177579207;
	            SumB = SumB * y + 86.7807322029461;
	            SumB = SumB * y + 296.564248779674;
	            SumB = SumB * y + 637.333633378831;
	            SumB = SumB * y + 793.826512519948;
	            SumB = SumB * y + 440.413735824752;
	            d = exp * SumA / SumB;
	        }
	        else {
	        	double SumA = y + 0.65;
	            SumA = y + 4 / SumA;
	            SumA = y + 3 / SumA;
	            SumA = y + 2 / SumA;
	            SumA = y + 1 / SumA;
	            d = exp / (SumA * 2.506628274631);
	        }
	    }
	  
	    if (x > 0) {
	    	d = 1 - d;
	    }
	    
	    return d;
	}
	
	/**
	 * Apache Commons Math 라이브러리를 이용하여 표준정규분포 누적 확률 값을 구한다.
	 * 
	 * @param x x 값
	 * @return 누적정규분포 확률
	 * @exception {@code MathException}이 발생하는 경우에는 {@link #getCumulativeProbability_R2}를 사용한다.
	 */
	public static double getCumulativeProbability_JakartaCommonMath(double x) {
		
		return NormalDistribution.getCumulativeProbability(x);
		
	}
	
	/**
	 * Apache Commons Math 라이브러리를 이용하여 역누적분포함수 값을 구한다.
	 * 
	 * @param x 확률, 0 ~ 1사이의 값 
	 * @return 표준정규분포 변수
	 * @exception  MathException이 발생하는 경우에는 {@link #getInverseCumulativeProbability_PJAcklam}를 사용한다.
	 */
	public static double getInverseCumulativeProbability(double x) {
		
		return NormalDistribution.getInverseCumulativeProbability(x);
		
	}
	
	private static double getCumulativeProbability_R2(double x) {
		
		if(x==0.0) return 0.5;
		double d=0.0;
		double L, K;
		double a[] = {0.31938153, -0.356563782, 1.781477937, -1.821255978, 1.330274429};
		L = Math.abs(x);
		K = 1 / (1 + 0.2316419 * L);
		double dTemp = 0.0;
		int len = a.length;
		for(int i=0 ; i<len ; i++) { dTemp += ( a[i] * Math.pow(K, i+1) ); }
		d = 1 - 1 / 2.506628274631 / Math.exp(L*L/2) * dTemp;
		if ( x < 0 ) d = 1.0 - d;

		return d;
	}

	// Drezner(1978) algorithm
	/**
	 * 이변량 정규분포의 누적확률값을 리턴한다.
	 * <p>
	 * Drezner(1978) algorithm
	 * 
	 * @param a x축의 값
	 * @param b y축의 값
	 * @param rho 변량(Variate)간 상관관계
	 * @return 이변량 누적정규분포 확률
	 */
	public static double getBivariateCumulativeProbability(double a, double b,
			double rho) {
		
		double a1 = a / Math.sqrt( 2 * (1 - Math.pow(rho, 2.0) ));
		double b1 = b / Math.sqrt( 2 * (1 - Math.pow(rho, 2.0) ));
		double sum = 0.0;
		double[] X = { 0.24840615, 0.39233107, 0.21141819, 0.03324666, 0.00082485334 };
		double[] Y = { 0.10024215, 0.48281397, 1.0609498, 1.7797294, 2.6697604 };
		
		if ( a<=0 && b<=0 && rho<=0 ) {
			sum = 0.0;
			for(int i=0 ; i<5 ; i++ ) {
				for(int j=0 ; j<5 ; j++) {
					double dTemp = a1 * (2.0 * Y[i] - a1) + b1 * (2.0 * Y[j] - b1);
					dTemp += 2 * rho * (Y[i] - a1) * (Y[j] - b1);
					sum += X[i] * X[j] * Math.exp(dTemp);
				}
			}
			return Math.sqrt(1.0 - rho * rho) / Math.PI * sum;
		} 
		else if ( a<=0 && b>=0 && rho>=0){
			return getCumulativeProbability(a) - getBivariateCumulativeProbability(a, -b, -rho);
		} 
		else if ( a>=0 && b<=0 && rho>=0) {
			return getCumulativeProbability(b) - getBivariateCumulativeProbability(-a, b, -rho);
		} 
		else if ( a>=0 && b>=0 && rho<=0) {
			return getCumulativeProbability(a) + getCumulativeProbability(b) - 1 + getBivariateCumulativeProbability(-a, -b, rho);
		} 
		else if ( a * b * rho > 0 ) {
			double sgnA = ( a >= 0 ) ? 1.0 : -1.0;
			double sgnB = ( b >= 0 ) ? 1.0 : -1.0;
			double dTemp = Math.sqrt(a*a - 2*rho*a*b + b*b);
			double rho1 = (rho * a - b) * sgnA / dTemp;
			double rho2 = (rho * b - a) * sgnB / dTemp;
			double delta = ( 1.0-sgnA*sgnB ) / 4.0;
			return getBivariateCumulativeProbability(a,0.0,rho1) + getBivariateCumulativeProbability(b,0.0,rho2) - delta;
		}
		return 0.0;
	}

	private static final double P_LOW  = 0.02425D;
	private static final double P_HIGH = 1.0D - P_LOW;

	private static final double ICDF_A[] = { 
		-3.969683028665376e+01,  2.209460984245205e+02,
		-2.759285104469687e+02,  1.383577518672690e+02,
		-3.066479806614716e+01,  2.506628277459239e+00 };

	private static final double ICDF_B[] = { 
		-5.447609879822406e+01,  1.615858368580409e+02,
		-1.556989798598866e+02,  6.680131188771972e+01,
		-1.328068155288572e+01 };

	private static final double ICDF_C[] = { 
		-7.784894002430293e-03, -3.223964580411365e-01,
		-2.400758277161838e+00, -2.549732539343734e+00,
		4.374664141464968e+00,  2.938163982698783e+00 };

	private static final double ICDF_D[] = { 
		7.784695709041462e-03,  3.224671290700398e-01,
		2.445134137142996e+00,  3.754408661907416e+00 };

	/**
	 * 역누적분포함수 값을 구한다.
	 * <p>
	 * 확률값을 입력받으면 표준정규분포에서 해당 확률에 해당하는 변수 값을 찾는다.
	 * <P>참고: <a href=http://home.online.no/~pjacklam/notes/invnorm/>http://home.online.no/~pjacklam/notes/invnorm/</a>
	 * 
	 * @param p 확률, 0 ~ 1사이의 값 
	 * @return 표준정규분포 변수
	 */
	 // 2009.10.26 yangJaechul Private -> Public
	public static double getInverseCumulativeProbability_PJAcklam(double p) {
		
		// Define break-points.
	    // variable for result
	    double z = 0;

	    if (p == 0) z = Double.NEGATIVE_INFINITY;
	    else if(p == 1) z = Double.POSITIVE_INFINITY;
	    else if(Double.isNaN(p) || p < 0 || p > 1) z = Double.NaN;

	    // Rational approximation for lower region:
	    else if( p < P_LOW ) {
	      double q  = Math.sqrt(-2*Math.log(p));
	      z = (((((ICDF_C[0]*q+ICDF_C[1])*q+ICDF_C[2])*q+ICDF_C[3])*q+ICDF_C[4])*q+ICDF_C[5]) / ((((ICDF_D[0]*q+ICDF_D[1])*q+ICDF_D[2])*q+ICDF_D[3])*q+1);
	    }

	    // Rational approximation for upper region:
	    else if ( P_HIGH < p ) {
	      double q  = Math.sqrt(-2*Math.log(1-p));
	      z = -(((((ICDF_C[0]*q+ICDF_C[1])*q+ICDF_C[2])*q+ICDF_C[3])*q+ICDF_C[4])*q+ICDF_C[5]) / ((((ICDF_D[0]*q+ICDF_D[1])*q+ICDF_D[2])*q+ICDF_D[3])*q+1);
	    }
	    
	    // Rational approximation for central region:
	    else {
	      double q = p - 0.5D;
	      double r = q * q;
	      z = (((((ICDF_A[0]*r+ICDF_A[1])*r+ICDF_A[2])*r+ICDF_A[3])*r+ICDF_A[4])*r+ICDF_A[5])*q / (((((ICDF_B[0]*r+ICDF_B[1])*r+ICDF_B[2])*r+ICDF_B[3])*r+ICDF_B[4])*r+1);
	    }
	    
	    return z;
	}

	/**
	 * 역누적분포함수 값을 구한다.
	 * <p>
	 * 확률값을 입력받으면 표준정규분포에서 해당 확률에 해당하는 변수 값을 찾는다.
	 * <P>
	 * Moro Inverse Cumulative Probability Algorithm(1995)
	 * 
	 * @param p 확률, 0 ~ 1사이의 값 
	 * @return 표준정규분포 변수
	 */
	 // 2009.10.26 yangJaechul Private -> Public	
	public static double getInverseCumulativeProbability_Moro1995(double p) {
		double [] a = { 2.50662823884, -18.61500062529, 41.39119773534, -25.44106049637 };
		double [] b = { -8.4735109309, 23.08336743743, -21.06224101826, 3.13082909833 };
		double [] c = { 0.337475482272615, 0.976169019091719, 0.160797971491821,
				0.0276438810333863, 0.0038405729373609, 0.0003951896511919,
				3.21767881767818E-05, 2.888167364E-07, 3.960315187E-07 };
		double x = p - 0.5;
		double r = 0;
		
		// corrected by Youngseok Lee 2011.05.11  0.92 -> 0.42	
		if (Math.abs(x) < 0.42) {
			r = x * x;
			r = x * (((a[3] * r + a[2]) * r + a[1]) * r + a[0])
				/ ((((b[3] * r + b[2]) * r + b[1]) * r + b[0]) * r + 1.0);
			return r;			
		}
		
		r = (x >= 0 ? 1 - p : p);
		r = Math.log(-Math.log(r));
		 // corrected by Youngseok Lee 2011.05.11  r + (c[4] -> r * (c[4] 	
		r = c[0] + r * (c[1] + r * (c[2] + r * (c[3] + r * (c[4]
                          + r * (c[5] + r * (c[6] + r * (c[7] + r * c[8])))))));
		r = (x < 0 ? -r : r);
		return r;
	}

	/**
	 * 역누적분포함수 값을 구한다.
	 * <p>
	 * 확률값을 입력받으면 표준정규분포에서 해당 확률에 해당하는 변수 값을 찾는다.
	 * <p>R2 패키지에서 사용한 Inverse Cumulative Probability함수.
	 * 
	 * @param p 확률, 0 ~ 1사이의 값 
	 * @return 표준정규분포 변수
	 */
	 // 2009.10.26 yangJaechul Private -> Public	
	public static double getInverseCumulativeProbability_R2(double p) {
		
		if ( p == 0.5 ) return 0.0;
		else if (p == 0) return Double.NEGATIVE_INFINITY;
		else if (p == 1) return Double.POSITIVE_INFINITY;

		double zDown = 0.0;
		double zUp = 0.0;
		if ( p > 0.5 ) {
			zDown = 0;
			zUp = 6.5;
		} else {
			zDown = -6.5;
			zUp = 0;
		}
		
		int count = 0;		
		while(true) {
			count++;
			double z = (zUp + zDown) / 2;
			double d = getCumulativeProbability(z);
			double gap = d - p;
			if ( Math.abs(gap) <= 0.0000000001 || count >= 100 ) {
				return z;
			} 
			if ( gap > 0 ) { zUp = z; }
			else { zDown = z; }
		}
	}

}
