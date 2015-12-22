package com.quantosauros.common.math.matrix;

/**
 * PCA(Principal Component Analysis)를 수행하기 위해 아이겐밸류 분해를 지원하는 
 * 클래스이다.
 * <p>
 * Eigenvalue Decomposition을 통해 eigenvalue matrix와 eigenvector matrix를 
 * 반환하는 함수를 지원한다.
 * 
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @since 3.0 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class EigenvalueDecomposition {

	private Jama.EigenvalueDecomposition eigen;
	
	/**
	 * 아이겐밸류 분해를 하기 위해 행렬을 생성자로 입수한다.
	 * 
	 * @param matrix Eigenvalue Decomposition을 하기 위한 행렬
	 */
	public EigenvalueDecomposition(Matrix matrix) {
		this.eigen = new Jama.EigenvalueDecomposition(matrix.matrixJama);
	}
	
	/**
	 * 아이겐밸류 행렬을 반환한다.
	 * 
	 * @return block diagonal eigenvalue matrix
	 */
	public Matrix getD() {
		return new Matrix(eigen.getD());
	}
	
	/**
	 * 아이겐벡터 행렬을 반환한다.
	 * 
	 * @return eigenvector matrix
	 */
	public Matrix getV() {
		return new Matrix(eigen.getV());
	}
	
}
