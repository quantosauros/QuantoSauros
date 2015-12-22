package com.quantosauros.common.math.distribution;

import java.io.Serializable;
import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.math.matrix.CholeskyDecomposition;
import com.quantosauros.common.math.matrix.Matrix;
import com.quantosauros.common.math.random.AbstractRandom;
import com.quantosauros.common.math.random.RandomFactory;


/**
 * 이 클래스는 정규분포를 난수를 생성하기 위한 클래스이다.
 * 다차원의 난수를 생성할 수 있도록 Cholesky Decomposition을 지원한다.
 * 
 * @author Oh, Sung Hoon
 * @author Yang Jaechul (javadoc)
 * @since 3.0
 */
/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2008
------------------------------------------------------------------------------*/
public class NormalDistributionRandomGenerator implements Serializable{
	
	/**
	 * 난수 초기화 상수
	 */
	// Jae-Heon Kim 2010.01.12
	AbstractRandom _random;
	/*private long seed;
	private Random jdkRandomGen;*/
	
	private int dimension = 1;
	private Matrix cholL;
	
	/**
	 * 1차원 난수를 정의한다.
	 * <p>
	 * Seed의 경우 해당 시스템의 시간을 기초로 한다.
	 */
	public NormalDistributionRandomGenerator() {
		this(null);
	}
	
	/**
	 * 2차원 이상의 난수를 정의한다.
	 * <p>
	 * 2차원 이상의 난수를 생성하는 경우 각 차원별 난수의 상관관계를 반영하여
	 * 최종 난수가 생성되어야 하므로 상관관계 행렬을 입력받아
	 * 촐레스키 분해를 수행하여 행렬에 저장한다.
	 * <p>Seed의 경우 해당 시스템의 시간을 기초로 한다.
	 * <p>상관관계 행렬의 크기로 생성될 난수의 차원을 판단한다.
	 * 
	 * @param corr 상관관계 행렬 
	 */
	public NormalDistributionRandomGenerator(double [][] corr) {
		this(corr, RandomFactory.getRandom());
		/*this.seed = System.currentTimeMillis();
		this.setSeed(seed);
		
		if (corr != null) {
			this.dimension = corr.length;
			// Cholesky decomposition
			Matrix corrMatrix = new Matrix(corr);
			CholeskyDecomposition chol = new CholeskyDecomposition(corrMatrix);
			cholL = chol.getL();
		}*/
	}
	
	/**
	 * 2차원 이상의 난수를 seed, 난수 생성 방식과 함께 정의한다.
	 * <p>
	 * 2차원 이상의 난수를 생성하는 경우 각 차원별 난수의 상관관계를 반영하여
	 * 최종 난수가 생성되어야 하므로 상관관계 행렬을 입력받아
	 * 촐레스키 분해를 수행하여 행렬에 저장한다.
	 * <p>Seed의 경우 해당 시스템의 시간을 기초로 한다.
	 * <p>상관관계 행렬의 크기로 생성될 난수의 차원을 판단한다.
	 * 
	 * @param corr 상관관계 행렬
	 * @param random Random 난수 생성 객체
	 * @since 3.2
	 * 
	 */
	/*--------------------------------------------------------------------------
	* Implementation Note
	* - 2009.11.23 난수 생성 방식을 설정하게끔 추가함. Yang Jaechul. 
	--------------------------------------------------------------------------*/
	public NormalDistributionRandomGenerator(double [][] corr,
			AbstractRandom random) {
		_random = random;
		/*this.seed = seed;
		this.setSeed(seed, jdkRandomGen);*/
		
		if (corr != null) {
			this.dimension = corr.length;
			// Cholesky decomposition
			Matrix corrMatrix = new Matrix(corr);
			CholeskyDecomposition chol = new CholeskyDecomposition(corrMatrix);
			cholL = chol.getL();
		}
	}
	
	/**
	 * 난수 초기화 Seed를 설정한다.
	 * <p>
	 * 생성자를 정의하는 순간 이미 Seed가 초기화 되어 있으나,
	 * 임의로 지정한 Seed를 사용하려면 해당 Method로 재설정 가능하다.
	 * @param seed 난수 초기화 설정 값
	 */
	/*public final void setSeed(long seed) {
		this.seed = seed;
		this.jdkRandomGen = new Random(seed);
	}*/
	
	/**
	 * 난수 초기화 Seed를 설정함과 동시에 난수생성방식을 등록시킨다.
	 * @since 2009.11.23 Yang Jaechul. 난수생성방식을 설정하게 함.
	 */
	/*public final void setSeed(long seed, Random jdkRandomGen) {
		this.seed = seed;
		this.jdkRandomGen = jdkRandomGen;
 	}*/
	
	/**
	 * 난수 초기화 설정 값을 반환한다.
	 * @return seed
	 */
	public final long getSeed() {
		return _random.getSeed();
	}
	
	/**
	 * 평균 0, 분산 1인 정규분포 난수를 생성한다.
	 * <p>
	 * 반환되는 2차원 배열은 [차원의 개수][n] 모양을 가지고 있다.
	 * <p> 
	 * 1차원인 경우 [0][0], [0][1], ... , [0][n - 1]의 상관관계가 반영되지 않은 
	 * 난수를 반환한다.
	 * <p>
	 * 2차원 이상인 경우:
	 * <br> [0][0], [0][1], ... , [0][n - 1]
	 * <br> [1][0], [1][1], ... , [1][n - 1]
	 * <br> .
	 * <br> .
	 * <br> .
	 * <br> [dimension][0], [dimension][1], ... , [dimension][n - 1]
	 * <P>
	 * 과 같이 난수가 발생한다. 여기서 동일한 난수 발생 시점에서 
	 * 차원간 상관관계가 반영된다.
	 * 
	 * @param n 반환할 난수의 개수
	 * @return 난수
	 */
	public double [][] generate(int n) {
		return generate(0, 1, n);
	}
	
	/**
	 * 정규분포 난수를 반환한다.
	 * <p>
	 * 반환될 배열의 경우 [차원의 개수][n]의 배열을 반환한다.
	 * 1차원인 경우 [0][0], [0][1], ... , [0][n - 1]의 상관관계가 반영되지 않은 
	 * 난수를 반환한다.
	 * <p>
	 * 2차원 이상인 경우:
	 * <br> [0][0], [0][1], ... , [0][n - 1]
	 * <br> [1][0], [1][1], ... , [1][n - 1]
	 * <br> .
	 * <br> .
	 * <br> .
	 * <br> [dimension][0], [dimension][1], ... , [dimension][n - 1]
	 * <p>
	 * 과 같이 난수가 발생한다. 여기서 동일한 난수 발생 시점에서 
	 * 차원간 상관관계가 반영된다.
	 * 
	 * @param mean 발생시킬 난수의 평균
	 * @param variance 발생시킬 난수의 분산
	 * @param n 반환할 난수의 개수
	 * @return 난수
	 */
	public double [][] generate(double mean, double variance, int n) {
					
		// generate random 
		double [][] random = new double[dimension][n];
		for (int i = 0; i < dimension; ++i) {
			for (int j = 0; j < n; ++j) {
				random[i][j] = mean + Math.sqrt(variance) * _random.nextGaussian();
			}
		}
				
		if (dimension == 1) {
			return random;
		}
		
		// correlated random
		Matrix randomMatrix = new Matrix(random);
		randomMatrix = cholL.multiply(randomMatrix);

		return randomMatrix.getArray();
	}
	
	public double [][] generate(double mean, double variance, int n, 
			double[][] multiFactorPath, ArrayList<Integer> multiFactorLocation ) {
		
		// generate random 
		double [][] random = new double[dimension][n];
		for (int i = 0; i < dimension; ++i) {
			for (int j = 0; j < n; ++j) {
				random[i][j] = mean + Math.sqrt(variance) * _random.nextGaussian();
			}
		}
		
		for (int i = 0; i < multiFactorLocation.size(); i ++){
			random[i] = multiFactorPath[multiFactorLocation.get(i)];
		}
				
		if (dimension == 1) {
			return random;
		}
		
		// correlated random
		Matrix randomMatrix = new Matrix(random);
		randomMatrix = cholL.multiply(randomMatrix);

		return randomMatrix.getArray();
	}
	
	/**
	 * 상관관계가 반영되지 않은 정규분포 난수에 상관관계를 반영시켜 반환한다.
	 * <p>
	 * 난수[생성 차원 수][생성 개수]
	 * 상관관계가 반영되지 않은 난수에 생성자에서 정의된 촐레스키 매트릭스를 
	 * 이용해서 상관관계를 반영시킨다.
	 * 촐레스키 매트릭스와 난수 매트릭스의 행렬 곱으로 이루어져 있다.
	 * 1차원인 경우 입력된 값을 그대로 리턴한다.
	 * @param random 상관관계가 반영되지 않은 정규분포 난수 배열
	 * @return 상관관계 반영 난수 배열
	 */
	// 2009.10.26 yangJaechul 난수 배열 입력 시  상관관계 반영 로직
	public double[][] generate(double[][] random) {
		if (dimension == 1) {
			return random;
		}
		// correlated random
		Matrix randomMatrix = new Matrix(random);
		randomMatrix = cholL.multiply(randomMatrix);

		return randomMatrix.getArray();
	}
	public boolean imsiFinder(int[] array, int value){
		for (int i = 0; i < array.length; i++){
			if (array[i] == value){
				return true;
			}
		}
		return false;
	}
	
	public int imsiMaxArray(int[] array){
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < array.length; i++) {
		      if(array[i] > max) {
		         max = array[i];
		      }
		}
		return max;
	}
//	통함 random generator
	public double [][][] generate(int assetNum, int n, ModelType[] hwTypeArray, 
			int[] motionIndices, double[] time, int simIndex,
			HullWhiteParameters[] hwParams, HullWhiteVolatility[][] hwVols) {
		ArrayList<Integer> MultiFactorLocation = new ArrayList<>();
		boolean has2F = false;
		double[][][] generatedNum = new double[assetNum][][];
		double[][] factorCorrelation = null;
		int distinctProcessNum = imsiMaxArray(motionIndices);
		for (int i = 0; i < hwTypeArray.length; i ++){
//			if (imsiFinder(motionIndices, i)){
				if (hwTypeArray[i].equals(ModelType.HW2F)){
					MultiFactorLocation.add(i);
					factorCorrelation = new double[][]{{1, hwParams[i].getCorrelation()},{hwParams[i].getCorrelation(), 1}};
					has2F = true;
					break;
				}
//			}
		}
		
		double[][][] multiFactorRands = new double[MultiFactorLocation.size()][][];
		double[][] multiFactorShortRands = new double[MultiFactorLocation.size()][n];
		if (!has2F){
			double[][] RandArray = generate(0, 1, n);
			generatedNum = new double[assetNum][1][];
			for (int i = 0; i < motionIndices.length; i++){
					generatedNum[i][0] = RandArray[motionIndices[i]];					
			}
		} else {
			NormalDistributionRandomGenerator[] _tmpGen = new NormalDistributionRandomGenerator[MultiFactorLocation.size()];
			
			for (int i = 0; i < MultiFactorLocation.size(); i++){
				_tmpGen[i] = 
						new NormalDistributionRandomGenerator(factorCorrelation,
						RandomFactory.getRandom(AbstractRandom.MERSENNE_TWISTER_FAST, getSeed() + ((simIndex + Math.round(time[time.length - 1]* 1000) ) * 123)));
				multiFactorRands[i] = _tmpGen[i].generate(0, 1, n);
//				multiFactorShortRands[i] =
				for (int j = 0; j < n; j++){
					double vol1 = hwVols[MultiFactorLocation.get(i)][0].getVolatility(time[j]);
					double vol2 = hwVols[MultiFactorLocation.get(i)][1].getVolatility(time[j]);
					double vol = Math.sqrt(vol1 * vol1 + vol2 * vol2);
					multiFactorShortRands[i][j] = (vol1 / vol * multiFactorRands[i][0][j] + vol2 / vol * multiFactorRands[i][1][j]);					
				} 
			}	
			
			double[][] generatedShort = generate(0, 1, n, multiFactorShortRands, MultiFactorLocation);
			double[][][] RandArray = new double[dimension][][];
			for (int i = 0; i < dimension; i++){
				if (MultiFactorLocation.contains(i)){
					RandArray[i] = new double[2][];
					RandArray[i] = multiFactorRands[i];
				} else {
					RandArray[i] = new double[1][];
					RandArray[i][0] = generatedShort[i];
				}
			}		
			
			for (int i = 0; i < motionIndices.length; i++){
				generatedNum[i] = RandArray[motionIndices[i]];					
			}
			
		}
		
		
		return generatedNum;
	}
}
