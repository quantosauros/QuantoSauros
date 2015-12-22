package com.quantosauros.common.math.matrix;

/**
 * 
 * Ax=b라는 Linear System에서 matrix A가 Symmetric Positive Definite Matrix일 때
 * 적용가능한 Lineaer System Solver이다. 현재는 A를 dense matrix로 가정하여 입력받는다. 
 * 
 * @author Jaeoh Woo
 * @author Jaeoh Woo (javadoc)
 * @since 3.2 
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: 2010.06.17
------------------------------------------------------------------------------*/

public class ConjugateGradientMethod {
	
	public static Matrix solve(Matrix matrixA, Matrix vectorb) {
		
		double[][] dmatrixA = matrixA.getArray();
		double[][] mvectorb = vectorb.getArray();
		double[] dvectorb = new double[vectorb.getRowDimension()];
		for (int i = 0; i < vectorb.getRowDimension(); i++) {
			dvectorb[i] = mvectorb[i][0];
		}
		
		double[] vresult = solve(dmatrixA, dvectorb);
		Matrix result = new Matrix(vresult);
		
		return result.transpose();
	}

	public static double[] solve(double[][] matrixA, double[] vectorb) {
		
		int rowsize = matrixA.length;
		int columnsize = matrixA[0].length;
		
		if (rowsize == columnsize) {
			
			return cgSolver(matrixA, vectorb);
		}
		else {
			
			return squaredSolver(matrixA, vectorb);
		}
	}
	
	private static double[] cgSolver(double[][] matrixA, double[] vectorb) {
		
		int size = vectorb.length;
		int maxiter = 10000;
		int iterator = 0;
		
		double tolerance = 1e-10;
		double[] x = new double [size];
		double[] r = new double [size];
		double[] p = new double [size];
		double[] q = new double [size];
		double v = 0, mu = 0, alpha, beta, vp;
		double error = Double.MAX_VALUE;
		
		for (int i = 0; i < size; i++) {
			x[i] = 0;
			r[i] = vectorb[i];
			p[i] = r[i];
			v += r[i] * r[i];
		}
		
		while ((error > tolerance) && (iterator < maxiter)) {
			mu = 0;
			for (int i = 0; i < size; i++) {
				q[i] = 0;
				for (int j = 0; j < size; j++) {
					q[i] += matrixA[i][j] * p[j];
				}
				mu += p[i] * q[i];
			}	
			alpha = v / mu;
			
			vp = 0;
			for (int i = 0; i < size; i++) {
				x[i] += alpha * p[i];
				r[i] -= alpha * q[i];
				vp += r[i] * r[i];
			}

			beta = vp / v;
			
			for (int i = 0; i < size; i++) {
				p[i] = r[i] + beta * p[i];
			}

			v = vp;
			error = vp;
			iterator++;
		}
		
		return x;
	}
	
	private static double[] squaredSolver(double[][] matrixA, double[] vectorb) {
		
		int rowSize = matrixA.length;
		int columnSize = matrixA[0].length;
		
		double[][] ATA = new double[columnSize][columnSize]; 
		double[] ATb = new double [columnSize];
		
		// A^TA
		for (int i = 0; i < columnSize; i++) {
			for (int j = i; j < columnSize; j++) {
				ATA[i][j] = 0;
				for (int k = 0; k < rowSize; k++) {
					ATA[i][j] += matrixA[k][i] * matrixA[k][j];
				}
			}
		}
		// by symmetry
		for (int i = 0; i < columnSize; i++) {
			for (int j = 0;  j < i; j++) {
				ATA[i][j] = ATA[j][i];
			}
		}
		
		// A^Tb
		for (int i = 0; i < columnSize; i++) {
			ATb[i] = 0;
			for (int j = 0; j < rowSize; j++) {
				ATb[i] += matrixA[j][i] * vectorb[j];
			}
		}
		
		return cgSolver(ATA, ATb);
	}
}
