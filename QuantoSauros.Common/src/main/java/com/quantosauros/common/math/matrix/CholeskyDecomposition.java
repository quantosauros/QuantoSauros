package com.quantosauros.common.math.matrix;

/**
 * 촐레스키 분해를 수행하는 클래스이다.
 * <P>실제 내부 연산은 Jama Library를 이용한다.
 * 촐레스키 분해를 원하는 행렬 입력하면, Lower Triangular Matrix를 반환한다.
 * 
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @since 3.0 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class CholeskyDecomposition {

	private Jama.CholeskyDecomposition chol;
	
	/**
	 * 촐레스키 분해 사전 작업을 수행한다. 촐레스키 분해를 원하는 행렬을 
	 * {@code Matrix} 객체로 입력받아 생성자에 저장한다.
	 * 
	 * @param matrix  촐레스키 분해를 원하는 행렬
	 */
	public CholeskyDecomposition(Matrix matrix) {
		this.chol = new Jama.CholeskyDecomposition(matrix.matrixJama);
	}
	
	/**
	 * 촐레스키 분해를 수행하여 Lower Triangular Matrix를 반환한다.
	 * 
	 * <P>자세한 내용은 로직설계서를 참조하라.
	 * 
	 * @return Lower Triangular Matrix
	 */
	public Matrix getL() {
		return new Matrix(chol.getL());
	}
	
	// Symmetric Positive Definite
	/**
	 * 해당 촐레스키 분해가 양정치 조건을 만족하는지 판단한다.
	 * 
	 * @return Symmetric Positive Definite를 만족하는 경우에는 {@code true}, 
	 * 			그렇지 않으면 {@code false}
	 */
	public boolean isSpd() {
		return chol.isSPD();
	}
}
