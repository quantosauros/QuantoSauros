package com.quantosauros.common.math.interpolation;

import java.io.Serializable;

/**
 * 2D Interpolation 인터페이스이다.
 * <p> 찾기 원하는 변수 입력 시 보간된 값을 반환하는 함수로 구성되어 있다.
 *  
 * @author Jae-Heon Kim
 * @author Yang Jaechul (javadoc)
 * @since 3.1 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.04.28
------------------------------------------------------------------------------*/
public interface Interpolation2D extends Serializable {
	
    /**
     * X축 구간 중에서 {@code x}가 몇 번째 구간에 존재하는지 찾는다.
     * 
     * @param x X축에서 찾기 원하는 지점
     * @return Index
     */
    public int locateX(double x);
    
    /**
     * Y축 구간 중에서 {@code y}가 몇 번째 구간에 존재하는지 찾는다.
     * 
     * @param y Y축에서 찾기 원하는 지점
     * @return Index
     */
    public int locateY(double y);
    
    /**
     * {@code x}, {@code y} 값이 입력되면 생성자에 저장된 X축, Y축, Z축 
	 * 값으로부터 선형 보간을 수행해 z값을 구한다.
	 * 
     * @param x X축에서 찾기 원하는 지점
     * @param y Y축에서 찾기 원하는 지점
     * @return 보간되어 나온 값
     */
    public double evaluate(double x, double y);
}
