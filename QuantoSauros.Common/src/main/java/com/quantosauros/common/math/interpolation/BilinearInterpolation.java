package com.quantosauros.common.math.interpolation;

/**
 * Surface(2차원)에 대한 선형보간법 클래스이다.
 * 
 * @author Jae-Heon Kim
 * @author Yang Jaechul (javadoc)
 * @since 3.1 
 * 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2009.04.28
* 
* TODO:: Check validity of ranges of input data
------------------------------------------------------------------------------*/
public class BilinearInterpolation extends AbstractInterpolation2D {

	/**
	 * X축 Y축에 따른 Z축 값을 객체에 저장한다.
	 * 
	 * @param vx X축 배열
	 * @param vy Y축 배열
	 * @param mz Z축 배열, 보간해낼 최종 배열
	 */
	public BilinearInterpolation(double[] vx, double[] vy, double[][] mz) {
		super(vx, vy, mz);
	}

	/**
	 * {@code x}, {@code y} 값이 입력되면 생성자에 저장된 X축, Y축, Z축 
	 * 값으로부터 선형 보간을 수행해 z값을 구한다.
	 * <p>x,y 축 순서에 주의해야 한다.
	 * 
	 * @param x 찾기를 원하는 X축의 값
	 * @param y 찾기를 원하는 Y축의 값
	 * @return 입력된 {@code x}, {@code y} 값에 따라 선형보간되어 나온 z 값
	 */
	public double evaluate(double x, double y) {

		int i = locateX(x);
		int i_ = i + 1;
        int j = locateY(y);
        int j_ = j + 1;
        /*System.out.println("x = " + x + ", index = " + i);
        System.out.println("y = " + y + ", index = " + j);*/

        if (i == _BELOW_MIN) {
        	i = i_ = 0;
        } else if (i == _ABOVE_MAX) {
        	i = i_ = _vX.length - 1;
        }
        if (j == _BELOW_MIN) {
        	j = j_ = 0;
        } else if (j == _ABOVE_MAX) {
        	j = j_ = _vY.length - 1;
        }
        double z1 = _mZ[i][j];
        double z2 = _mZ[i][j_];
        double z3 = _mZ[i_][j];
        double z4 = _mZ[i_][j_];

        double t = 0;
        if (i != i_) {
        	t = (x - _vX[i]) / (_vX[i_] - _vX[i]);;
        }
        double u = 0;
        if (j != j_) {
        	u = (y - _vY[j]) / (_vY[j_] - _vY[j]);
        }
        return (1.0 - t) * (1.0 - u) * z1 + t * (1.0 - u) * z3 
        		+ (1.0 - t) * u * z2 + t * u * z4;
        
        /*double z1 = _mZ[i][j];
        double z2 = _mZ[i][j + 1];
        double z3 = _mZ[i + 1][j];
        double z4 = _mZ[i + 1][j + 1];

        double t = (x - _vX[i]) / (_vX[i + 1] - _vX[i]);
        double u = (y - _vY[j]) / (_vY[j + 1] - _vY[j]);

        return (1.0 - t) * (1.0 - u) * z1 + t * (1.0 - u) * z3 
        		+ (1.0 - t) * u * z2 + t * u * z4;*/
	}

}
