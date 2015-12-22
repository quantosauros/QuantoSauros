package com.quantosauros.common.math;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
/**
* 사용자 정의 연산자를 담고 있는 클래스이다.
* 
* @author Oh, Sung Hoon
* @author Jae-Heon Kim
* @author Yang Jaechul (javadoc)
* @since 3.0
* 
*/
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class MathUtil {

//	/**
//	 * 팩토리얼 연산자를 계산하는 클래스이다.
//	 * <code>5! = 5 * 4 * 3 * 2 * 1</code>
//	 * 과 같이 계산된다.
//	 * 
//	 * @param i 팩토리얼 최대 값
//	 * @return 팩토리얼 계산 결과
//	 */
//	public static double factorialDouble(int i) {
//		return 
//	}

	/**
	 * 주어진 값들 중 {@code val} 값보다 큰 값들 중 최소인 값의 위치를 찾는다.
	 * 
	 * @param list 대상 배열
	 * @param val 찾을 값
	 * @return Index of upper bound
	 */
	// Jae-Heon Kim, 2009.04.28
	public static int upperBound(final double[] list, final double val) {
		return upperBound(list, 0, list.length - 1, val);
	}

	/**
	 * 주어진 값들 중 {@code val} 값보다 큰 값들 중 최소인 값의 위치를 찾는다.
	 * 
	 * @param list 대상 배열
	 * @param first 찾는 구간 시작 Index
	 * @param last 찾는 구간 끝 Index
	 * @param val 찾을 값
	 * @return Index of upper bound
	 */
	// Jae-Heon Kim, 2009.04.28
	private static int upperBound(final double[] list, int first, int last,
			final double val) {
		int len = last - first;
		int half;
		int middle;
		
		while (len > 0) {
			half = len >> 1;
			middle = first;
			middle = middle + half;
			
			if (val < list[middle]){
				len = half;
			} else {
				first = middle;
				first++;
				len = len - half -1;
			}
		}
		return first;
	}
	
	/**
	 * 주어진 값들 중 {@code val} 값보다 작은 값들 중 최대인 값의 위치를 찾는다.
	 * 
	 * @param list 대상 배열
	 * @param val 찾을 값
	 * @return Index of lower bound
	 */
	// Jae-Heon Kim, 2009.04.28
	public static int lowerBound(final double[] list, final double val) {
        return lowerBound(list, 0, list.length - 1, val);
    }

	/**
	 * 주어진 값들 중 {@code val} 값보다 작은 값들 중 최대인 값의 위치를 찾는다.
	 * 
	 * @param list 대상배열
	 * @param first 찾는 구간 시작 Index
	 * @param last 찾는 구간 끝 Index
	 * @param val 찾을 값
	 * @return Index of lower bound
	 */
	// Jae-Heon Kim, 2009.04.28
    private static int lowerBound(final double[] list, int first, int last, final double val) {
        int len = last - first;
        int half;
        int middle;
        
        while (len > 0) {
            half = len >> 1;
            middle = first;
            middle = middle + half;
            
            if (list[middle] < val) {
                first = middle;
                first++;
                len = len - half -1;
            } else {
                len = half;
            }
        }
        return first;
    }
    
    //Added by Jaeoh, March 16, 2010
    /**
     * 
     * Gamma 함수를  계산한다.
     * 
     * @param x Parameter
     * @return Gamma(x)의 계산 결과
     */
    public static double gamma(double x) {
    	
    	return Math.exp(Gamma.logGamma(x));
    }
    
    //Added by Jaeoh, March 18, 2010
    /**
     * 
     * Math.exp()보다 빨리 계산하는 함수이다.
     * 
     * @param x 입력값
     * @return Exp(x)의 계산 결과
     */
    public static double fastExp(double x) {
        final long tmp = (long) (1512775 * x + (1072693248 - 60801));
        return Double.longBitsToDouble(tmp << 32);
    }
    
    //Added by Jaeoh, March 18, 2010
    /**
     * 
     * Math.exp()보다 빨리 계산하는 함수이다.
     * 
     * @param x 입력값
     * @return Exp(x)의 계산 결과
     */
    public static double fastExp2(double val) {
        final long tmp = (long) (1512775 * val) + 1072693248;
        final long mantissa = tmp & 0x000FFFFF;
        long error = mantissa >> 7;   // remove chance of overflow
        error = (error - mantissa * mantissa) / 186; // subtract mantissa^2 * 64
                                       // 64 / 186 = 1/2.90625
        return Double.longBitsToDouble((tmp - error) << 32);
    }
}
