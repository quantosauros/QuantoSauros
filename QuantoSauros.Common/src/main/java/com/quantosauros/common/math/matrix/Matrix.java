package com.quantosauros.common.math.matrix;

import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Jama 라이브러리의 행렬을 R3 구조에 맞춰서 사용하기 위한 Wrapper 클래스이다.
 * <p>
 * 행렬 객체를 사용하기 위한 기본적인 값 입출력을 비롯하여
 * 행렬 크기, 행렬 계산(곱, 합, 제곱) 등을 계산하는 함수를 지원한다.
 * 
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @author Ji-Hun Kang (add method)
 * 
 * @since 3.0 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class Matrix implements Serializable{

	/**
	 * Jama Matrix를 등록한다.
	 */
	Jama.Matrix matrixJama;
	
	/**
	 * 열과 행을 입력하여 빈(Null) 행렬을 생성한다.
	 * 객체를 생성할 때 기본적으로 객체의 크기를 선언한 후에야 사용 가능하다.
	 * 따라서 {@code rowCnt}, {@code colCnt} 값에 해당하는 Jama Matrix를 생성한다.
	 * 
	 * @param rowCnt 열 크기
	 * @param colCnt 행 크기
	 */
	public Matrix(int rowCnt, int colCnt) {
		matrixJama = new Jama.Matrix(rowCnt, colCnt);
	}
	
	/**
	 * 1차원 배열로 구성된 행렬을 생성한다.
	 * Matrix[0][0], Matrix[0][1], Matrix[0][2], ... Matrix[0][n]
	 * 의 행렬로 구성된다.
	 * 
	 * @param values double의 1차원 배열
	 */
	public Matrix(double [] values) {
		matrixJama = new Jama.Matrix(values, 1);		
	}
	
	/**
	 * 2차원 배열로 구성된 행렬을 생성한다.
	 * 
	 * @param values double의 2차원 배열
	 */
	public Matrix(double [][] values) {
		matrixJama = new Jama.Matrix(values);
	}
	
	/**
	 * 행렬을 입력받아 새로운 행렬로 복사한다.
	 * 
	 * @param matrixJama 복사된 행렬
	 */
	// Scope change to public by Jae-Heon Kim 2012.07.04
	public Matrix(Jama.Matrix matrixJama) {
		this.matrixJama = matrixJama;
	}
	
	/**
	 * 열의 개수를 반환한다.
	 * <p>
	 * new Matrix[m][n]로 행렬이 정의된 경우 m을 리턴한다.
	 * 
	 * @return 열의 개수
	 */
	public int getRowDimension() {
		return matrixJama.getRowDimension();
	}
	
	/**
	 * 행의 개수를 반환한다.
	 * <p>
	 * new Matrix[m][n]로 행렬이 정의된 경우 n을 리턴한다.
	 * 
	 * @return 행의 개수
	 */
	public int getColumnDimension() {
		return matrixJama.getColumnDimension();
	}
	
	/**
	 * 행렬의 곱을 리턴하는 함수이다.
	 * 
	 * @param matrix 이 행렬 객체에 곱할 행렬
	 * @return 행렬 곱의 결과 행렬
	 */
	public Matrix multiply(Matrix matrix) {
		return new Matrix(matrixJama.times(matrix.matrixJama));
	}
	
	/**
	 * 행렬에 상수를 곱하는 함수이다.
	 * 
	 * @param value 이 행렬 객체에 곱할 상수
	 * @return 계산 결과 행렬
	 */
	public Matrix multiply(double value) {
		return new Matrix(matrixJama.times(value));
	}
	
	/**
	 * 행렬에 상수를 더하는 함수이다.
	 * 
	 * @param matrix 이 행렬 객체에 더할 상수
	 * @return 계산 결과 행렬
	 */
	public Matrix plus(Matrix matrix) {
		return new Matrix(matrixJama.plus(matrix.matrixJama));
	}
		
	/**
	 * 행렬에서 상수를 빼는 함수이다.
	 * 
	 * @param matrix 이 행렬 객체에서 뺄 상수
	 * @return 계산된 행렬
	 */
	public Matrix minus(Matrix matrix) {
		return new Matrix(matrixJama.minus(matrix.matrixJama));
	}
	
	/**
	 * 전치행렬을 구한다.
	 * 이 행렬의 열을 행으로, 행을 열으로 바꾼 행렬을 반환한다.
	 * 
	 * @return 계산된 행렬
	 */
	public Matrix transpose() {
		return new Matrix(matrixJama.transpose());
	}
	
	/**
	 * 이 행렬의 역행렬을 구한다.
	 * 
	 * @return 역행렬
	 */
	public Matrix inverse() {
		return new Matrix(matrixJama.inverse());
	}
	
	/**
	 * 주어진 열에 대한 벡터를 반환한다.
	 * <P>
	 * <code>Matrix[0][0] ~ Matrix[0][n]</code>
	 * 
	 * @param row 열
	 * @return 열벡터
	 */
	public Matrix getRowVector(int row) {
		return new Matrix(matrixJama.getMatrix(row, row, 0, matrixJama.getColumnDimension() - 1));
	}
	
	/**
	 * 주어진 행에 대한 벡터를 반환한다.
	 * <P>
	 * <code>Matrix[0][0] ~ Matrix[m][0]</code>
	 * 
	 * @param col 행
	 * @return 행백터
	 */
	public Matrix getColumnVector(int col) {
		return new Matrix(matrixJama.getMatrix(0, matrixJama.getRowDimension() - 1, col, col));
	}
	
	/**
	 * 주어진 ({@code row}, {@code col}) 위치에 {@code value} 값을 설정한다.
	 * <p>
	 * <code>Matrix[row][col] = value;</code>
	 * 
	 * @param row 입력하기 원하는 열
	 * @param col 입력하기 원하는 행
	 * @param value 입력할 값
	 */
	public void set(int row, int col, double value) {
		matrixJama.set(row, col, value);
	}
	
	/**
	 * 행렬의 특정 범위를 주어진 행렬로 채운다.
	 * 특정 범위의 크기와 주어진 행렬의 사이즈가 동일해야 한다.
	 * 
	 * @param initRow 변경하기 원하는 행렬 시작 열 Index
	 * @param finalRow 변경하기 원하는 행렬 끝 열 Index
	 * @param initCol 변경하기 원하는 행렬 시작 행 Index
	 * @param finalCol 변경하기 원하는 행렬 끝 행 Index
	 * @param matrix 삽입할 행렬
	 */
	public void setMatrix(int initRow, int finalRow, int initCol, int finalCol, Matrix matrix) {
		matrixJama.setMatrix(initRow, finalRow, initCol, finalCol, matrix.matrixJama);
	}
	
	/**
	 * 해당 열과 행의 값을 반환한다.
	 * 
	 * @param row 열 Index
	 * @param col 행 Index
	 * @return 열과 행의 값
	 */
	public double get(int row, int col) {
		return matrixJama.get(row, col);
	}
	
	/**
	 * 전체 행렬에서 해당 범위에 해당하는 Sub Matrix를 반환한다.
	 * 
	 * @param initRow 반환하기 원하는 행렬의 시작열 Index
	 * @param finalRow 반환하기 원하는 행렬의 끝열 Index
	 * @param initCol 반환하기 원하는 행렬의 시작행 Index
	 * @param finalCol 반환하기 원하는 행렬의 끝행 Index
	 * @return Sub Matrix
	 */
	public Matrix getMatrix(int initRow, int finalRow, int initCol, int finalCol) {
		return new Matrix(matrixJama.getMatrix(initRow, finalRow, initCol, finalCol));
	}
	
	/**
	 * 특정 열과 행으로 구성된 Sub Matrix를 반환한다.
	 * 특정 열은 배열로 구성되고, 특정 행 역시 동일하다.
	 * 
	 * @param rowIndices 열 인덱스 배열
	 * @param colIndices 행 인덱스 배열
	 * @return Sub Matrix
	 */
	public Matrix getMatrix(int [] rowIndices, int [] colIndices) {
		return new Matrix(matrixJama.getMatrix(rowIndices, colIndices));
	}
	
	/**
	 * 행렬(Matrix)객체를 2차원 배열(<code>double[][]</code>)로 데이터 타입을 
	 * 변환하여 반환한다.
	 * 
	 * @return 2차원 배열
	 */
	public double [][] getArray() {
		return matrixJama.getArray();
	}
	
	/**
	 * 정방행렬에서 대각값을 추출하여 반환한다.
	 * 
	 * @return 대각 배열
	 * @exception UnsupportedOperationException "this is NOT symmetric Matrix."
	 */
	public double [] getDiagonal() {
		int row = matrixJama.getRowDimension();
		int col = matrixJama.getColumnDimension();
		if (row != col) {
			throw new UnsupportedOperationException("this is NOT symmetric matrix.");
		}
		double [] diagonal = new double[row];
		for (int i = 0; i < row; ++i) {
			diagonal[i] = matrixJama.get(i, i);
		}
		return diagonal;
	}
	
	/**
	 * 행렬을 제곱하여 반환한다.
	 * 동일한 행렬 간의 행렬곱이 아니라, 행렬의 각 원소에 제곱을 취한다.
	 * 
	 * @return 제곱된 행렬
	 */
	public Matrix square() {
		Jama.Matrix matrixSquare = matrixJama.copy();
		for (int row = 0; row < matrixSquare.getRowDimension(); ++row) {
			for (int col = 0; col < matrixSquare.getColumnDimension(); ++col) {
				matrixSquare.set(row, col, Math.pow(matrixSquare.get(row, col), 2.0));
			}
		}
		return new Matrix(matrixSquare);
	}
	
	/**
	 * 같은 차원의 두 행렬의 각 원소간의 곱을 반환한다.
	 * 행렬곱이 아니라, 원소간의 1:1 곱을 반환한다.
	 * 
	 * @return 두행렬의 원소간 곱의 행렬
	 */	
	public Matrix simpleMultiply(Matrix m){
		
		if(matrixJama.getRowDimension() != m.getRowDimension() ||
				matrixJama.getColumnDimension() != m.getColumnDimension()){
			return null;
		}
			
		
		double[][] r = new double[matrixJama.getRowDimension()][matrixJama
				.getColumnDimension()];
		for (int i = 0; i < matrixJama.getRowDimension(); i++) {
			for(int j = 0 ; j < matrixJama.getColumnDimension(); j++){
				r[i][j] = matrixJama.get(i, j) * m.get(i, j);
			}
		}
		return  new Matrix(r) ;
	}	
	
	/**
	 * 같은 차원의 두 행렬의 각 원소간의 나눈값을 반환한다.
	 * 
	 * @return 두행렬의 원소간 나눈값의 행렬
	 */	
	public Matrix simpleDivide(Matrix m){
		
		if(matrixJama.getRowDimension() != m.getRowDimension() ||
				matrixJama.getColumnDimension() != m.getColumnDimension()){
			return null;
		}
		
		double[][] r = new double[matrixJama.getRowDimension()][matrixJama
				.getColumnDimension()];
		for (int i = 0; i < matrixJama.getRowDimension(); i++) {
			for(int j = 0 ; j < matrixJama.getColumnDimension(); j++){
				if(m.get(i, j) == 0 || Double.isNaN(m.get(i, j))){
					r[i][j] = 0d ;
				} else {
					r[i][j] = matrixJama.get(i, j) / m.get(i, j);
				}
			}
		}
		return  new Matrix(r) ;
	}	
	
	/**
	 * 행렬 각원소에 특정값을 더해준다.
	 * 
	 * @return 특정값이 더해진 행렬
	 */	
	
	public Matrix simplePlus(double d){
		double[][] r = new double[matrixJama.getRowDimension()][matrixJama
				.getColumnDimension()];
		for (int i = 0; i < matrixJama.getRowDimension(); i++) {
			for(int j = 0 ; j < matrixJama.getColumnDimension(); j++){
					r[i][j] = matrixJama.get(i, j) + d ;
			}
		}
		return  new Matrix(r) ;
	}
	
	/**
	 * 행렬 각원소에 특정값을 빼준다.
	 * 
	 * @return 특정값이 빼진 행렬
	 */	
	public Matrix simpleMinus(double d){
		double[][] r = new double[matrixJama.getRowDimension()][matrixJama
				.getColumnDimension()];
		for (int i = 0; i < matrixJama.getRowDimension(); i++) {
			for(int j = 0 ; j < matrixJama.getColumnDimension(); j++){
					r[i][j] = matrixJama.get(i, j) - d ;
			}
		}
		return  new Matrix(r) ;
	}
	
	
	/**
	 * 행렬 각원소에 특정값을 나눠준다.
	 * 
	 * @return 특정값으로 나눠진 행렬
	 */	
	public Matrix simpleDivide(double d){
		double[][] r = new double[matrixJama.getRowDimension()][matrixJama
				.getColumnDimension()];
		for (int i = 0; i < matrixJama.getRowDimension(); i++) {
			for(int j = 0 ; j < matrixJama.getColumnDimension(); j++){
				if(d == 0 || Double.isNaN(d)){
					r[i][j] = 0d ;	
				} else {
					r[i][j] = matrixJama.get(i, j) / d ;
				}
			}
		}
		return  new Matrix(r) ;
	}	
	
	/**
	 * 행렬을 복사한 행렬을 반환한다.
	 * <P>
	 * 단순하게 행렬 객체를 넘겨주면, 동일한 주소값을 사용하므로 
	 * 이를 방지하기 위해 새로운 행렬 객체를 생성하는 방식이다.
	 * @return 복사된 행렬
	 */
	public Matrix copy() {
		return new Matrix(matrixJama.copy());
	}
	
	/**
	 * 행렬의 모든 원소를 컬럼기준으로 1차원 배열로 붙여 반환한다.
	 * <P>
	 * @return 배열
	 */
	public double[] getColumnPackedCopy(){
		return matrixJama.getColumnPackedCopy();
	}
	
	/**
	 * 행렬의 모든 원소를 열기준으로 1차원 배열로 붙여 반환한다.
	 * <P>
	 * @return 배열
	 */
	public double[] getRowPackedCopy(){
		return matrixJama.getRowPackedCopy();
	}	
	
	/**
	 * 행렬 각원소에 abs()를 취해준다.
	 * 
	 * @return abs 적용된 행렬
	 */	
	public Matrix abs(){
		double[][] r = new double[matrixJama.getRowDimension()][matrixJama
				.getColumnDimension()];
		for (int i = 0; i < matrixJama.getRowDimension(); i++) {
			for(int j = 0 ; j < matrixJama.getColumnDimension(); j++){
					r[i][j] = Math.abs(matrixJama.get(i, j)) ;
			}
		}
		return  new Matrix(r) ;
	}	

	
	/**
	 * 행렬을 출력한다.
	 * 
	 * @param w 행 길이.
	 * @param d 소수점 자릿수.
	 */
	public void print(int w, int d) {
		matrixJama.print(w, d);
	}
	
	public void print(PrintWriter pw, int w, int d) {
		matrixJama.print(pw, w, d);
	}
	/**
	 * 행렬의 Norm을 리턴한다.
	 */
	public double getNorm(){
		
		return matrixJama.normF();		
	}
}
