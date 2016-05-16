package com.quantosauros.common.math.interpolation;

public class MonotoneConvexInterpolation implements Interpolation {

	private static MonotoneConvexInterpolation theInstance;
	
	private double[] terms;
	private double[] fdiscrete;
	private double[] f;
	private boolean isGenerated; 
	private double interval = 0.001;
	
	private MonotoneConvexInterpolation() {}
	
	public static MonotoneConvexInterpolation getInstance() {
		if (theInstance == null){
			theInstance = new MonotoneConvexInterpolation();
		}
		return theInstance;
	}
	
	public double getY(Coordinates[] orderedCoordinates, double term) {
	
//		if (!isGenerated)
			generate(orderedCoordinates);

		int index = 0; //getLastIndex(term);
		
		double spotRate = (orderedCoordinates[index].getX() * orderedCoordinates[index].getY() + 
				integrate(orderedCoordinates[index].getX() ,term))/term;
		
//		double forward = getForward(term);
		
//		System.out.println(term + ": " + spotRate);
		
		// 여기까지 코드가 도달할 일은 없음
		return spotRate;
	}
	
    public double integrate(double a, double b) {
    	int N = 1000;                    // precision parameter
    	double h = (b - a) / (N - 1);     // step size
 
    	// 1/3 terms
    	double sum = 1.0 / 3.0 * (getForward(a) + getForward(b));

    	// 4/3 terms
    	for (int i = 1; i < N - 1; i += 2) {
    		double x = a + h * i;
    		sum += 4.0 / 3.0 * getForward(x);
    	}

    	// 2/3 terms
    	for (int i = 2; i < N - 1; i += 2) {
    		double x = a + h * i;
    		sum += 2.0 / 3.0 * getForward(x);
    	}

    	return sum * h;
   }
	
	private void generate(Coordinates[] orderedCoordinates){
		int size = orderedCoordinates.length + 1;
		terms = new double[size];
		double[] values = new double[size];
		
		fdiscrete = new double[size];
		f = new double[size];
		
		terms[0] = 0;		
		//step 1
		for (int i = 1; i < size; i++){
			values[i] = orderedCoordinates[i - 1].getY();
			terms[i] = orderedCoordinates[i - 1].getX();
		}
		values[0] = values[1];
		
		for (int j = 1; j < size; j++){
			fdiscrete[j] = (terms[j] * values[j] - terms[j-1] * values[j-1]) / (terms[j] - terms[j-1]);
		}
		//step 2
		for (int j = 1; j < size - 1; j++){
			f[j] = (terms[j] - terms[j - 1]) / (terms[j + 1] - terms[j - 1]) * 
					fdiscrete[j + 1] + (terms[j + 1] - terms[j]) / (terms[j + 1] - terms[j - 1]) * fdiscrete[j];
		}
		//step 3
		f[0] = bound(0, fdiscrete[1] - 0.5 * (f[1] - fdiscrete[1]), 2 * fdiscrete[1]);
		f[size - 1] = bound(0, fdiscrete[size - 1] - 0.5 * (f[size - 2] - fdiscrete[size - 1]), 2 * fdiscrete[size - 1]);
		for (int j = 1; j < size - 1; j++){
			f[j] = bound(0, f[j], 2 * Math.min(fdiscrete[j], fdiscrete[j + 1]));
		}
		isGenerated = true;
	}

	private double getForward(double term){
		double forward = 0;
		int size = terms.length;
		if (term <= 0){
			forward = f[0];
		} else if (term > terms[size - 1]){
			forward = getForward(terms[size - 1]);
		} else {			
			int index = getLastIndex(term);
			double x = (term - terms[index - 1]) / (terms[index] - terms[index - 1]);
			double g0 = f[index - 1] - fdiscrete[index];
			double g1 = f[index] - fdiscrete[index];
			double G = 0;
			if (x == 0){
				G = g0;
			} else if (x == 1){
				G = g1;
			} else if ((g0 < 0 && -0.5 * g0 <= g1 && g1 <= -2 * g0) || (g0 > 0 && -0.5 * g0 >= g1 && g1 >= -2 * g0)){
				// zone (i)
				G = g0 * (1 - 4 * x + 3 * x * x) + g1 * (-2 * x + 3 * x * x);
			} else if ((g0 <0 && g1 > -2 * g0) || (g0 > 0 && g1 < -2 * g0)){
				// zone (ii)
				// equation (29)
				double eta = (g1 + 2 * g0) / (g1 - g0);
				// equation (28)
				if (x <= eta){
					G = g0;
				} else {
					G = g0 + (g1 - g0) * ((x - eta) / (1 - eta)) * ((x - eta) / (1 - eta));						
				}					
			} else if ((g0 > 0 && 0 > g1 && g1 > -0.5 * g0) || (g0 < 0 && 0 < g1 && g1 < -0.5 * g0)){
				// zone (iii)
				// equation (31)
				double eta = 3 * g1 / (g1 - g0);
				// equation (30)
				if (x < eta){
					G = g1 + (g0 - g1) * ((eta - x) / eta) * ((eta - x) / eta);
				} else {
					G = g1;
				}					
			} else if (g0 == 0 && g1 == 0){
				G = 0;
			} else {
				// zone (iv)
				// equation (33)
				double eta = g1 / (g1 + g0);
				// equation (34)
				double A = -g0 * g1 / (g0 + g1);
				// equation (32)
				if (x <= eta){
					G = A + (g0 - A) * ((eta - x) / eta) * ((eta - x) / eta);
				} else {
					G = A + (g1 - A) * ((eta - x) / (1 - eta)) * ((eta - x) / (1 - eta));
				}
			}
			// equation 26
			forward = G + fdiscrete[index];				
		}
		return forward;
	}
	
	private double bound(double min, double value, double max){
		//forward rate should be positive
		if (value < min){
			return min;
		} else if (value > max){
			return max;
		} else {
			return value;
		}
		//if not
//		return value;		
	}
	
	private int getLastIndex(double term) {
		int lastIndex = 0;
		int size = terms.length;
		for (int i = 1; i < size; i++) {
			if (term > terms[i - 1] && term <= terms[i]){
				lastIndex = i;
			}	
		}
		return lastIndex;
	}
	
	
//	private double interpolant(double term){
//
//	if (x  <= 0){
//		return f0;
//	} else if (x > lastX){
//		return interpolant(lastX, lastX, x1, x2, y1, fi, fii, fdii);
//	} else {
//		double G = 0;
//		double L = x2 - x1;
//		// the x in equation (25)
//		double xValue = (x - x1) / L;
//		double g0 = fi - fdii;
//		double g1 = fii - fdii;
//		if (xValue == 0 || xValue == 1){
//			G = 0;
//		} else if ((g0 < 0 && -0.5 * g0 <= g1 && g1 <= -2 * g0) || (g0 > 0 && -0.5 * g0 >= g1 && g1 >= -2 * g0)){			  
//			// zone (i)
//			G = L * (g0 * (x - 2 * x * x + x * x * x) + g1 * (-x * x + x * x * x));				
//		} else if ((g0 <0 && g1 > -2 * g0) || (g0 > 0 && g1 < -2 * g0)){
//			// zone (ii)
//			// equation (29)
//			double eta = (g1 - 2 * g0) / (g1 - g0);
//			// equation (28)
//			if (x <= eta) {
//				G = g0 * (x - x1);
//			} else {
//				G = g0 * (x - x1) + (g1 - g0) * (x - eta) * (x - eta) * (x - eta) / ((1 - eta) * (1 - eta)) / 3 * L; 
//			}
//		} else if ((g0 > 0 && 0 > g1 && g1 > -0.5 * g0) || (g0 < 0 && 0 < g1 && g1 < -0.5 * g0)){
//			// zone (iii)
//			// equation (31)
//			double eta = 3 * g1 / (g1 - g0);
//			// equation (30)
//			if (x < eta){
//				G = L * (g1 * x - 1 / 3 * (g0 - g1) * ((eta - x) * (eta - x) * (eta - x) / (eta * eta) - eta));
//			} else {
//				G = L * (2 / 3 * g1 + 1 / 3 * g0) * eta + g1 * (x - eta) * L;
//			}				
//		} else if (g0 == 0 && g1 == 0){
//			G = 0;
//		} else {
//			// zone (iv)
//			// equation (33)
//			double eta = g1 / (g1 + g0);
//			// equation (34)
//			double A = -g0 * g1 / (g0 + g1);
//			// equation (32)
//			if (x <= eta){
//				G = L * (A * x - 1 / 3 * (g0 - A) * ((eta - x) * (eta - x) * (eta - x) / (eta * eta) - eta));
//			} else {
//				G = L * (2 / 3 * A + 1 /3 * g0) * eta + L * (A * (x - eta) + (g1 - A) / 3 * (x - eta) * (x - eta) * (x - eta) / ((1 - eta) * (1 - eta)));
//			}
//		}
//		// equation (12)
//		return 1 / x * ((x1 - y1 + fdii * (x - x1)) +G);
//}			

}


//double forward = 0;
//
//if (term <= orderedCoordinates[0].getX()) {
////	// x <= x(0)
////	// i = 0
//	double forward = 0;
//	double x0 = 0.0;
//	double y0 = orderedCoordinates[0].getY(); //y1과 동일하게
//	double x1 = orderedCoordinates[0].getX();
//	double y1 = orderedCoordinates[0].getY();
//	double x2 = orderedCoordinates[1].getX();
//	double y2 = orderedCoordinates[1].getY();
//	
//	double fdi = (x1 * y1 - x0 * y0) / (x1 - x0);
//	double fdii = (x2 * y2 - x1 * y1) / (x2 - x1);
//	
//	double f1 = (x1 - x0) / (x2 - x0) * fdii + (x2 - x1) / (x2 - x0) * fdi;
//	double f0 = bound(0, fdii - 0.5 * (f1 - fdii), 2 * fdii);
//	
//	forward = f0;
//	
//} else if (term >= orderedCoordinates[orderedCoordinates.length - 1].getX()) {
//	//Extrapolation if x >= x(n)
//	double forward = 0;
//	double lastX = orderedCoordinates[orderedCoordinates.length - 1].getX();
//	forward = interpolant(lastX, lastX, x1, x2, y1, fi, fii, fdii);			
//}
//
//for (int i = 1; i < orderedCoordinates.length; ++i) {
//	double x1 = orderedCoordinates[i-1].getX();
//	double y1 = orderedCoordinates[i-1].getY();
//	double x2 = orderedCoordinates[i].getX();
//	double y2 = orderedCoordinates[i].getY();
//	
//	if (term == x2) return y2;
//	
//	if (term > x1 && term < x2) {
//		if (i == orderedCoordinates.length - 1){
//			//last index
//			double forward = 0;
//			double x0 = orderedCoordinates[i-2].getX();
//			double y0 = orderedCoordinates[i-2].getY();				
//								
//			double fdi = (x1 * y1 - x0 * y0) / (x1 - x0);
//			double fdii = (x2 * y2 - x1 * y1) / (x2 - x1); 
//			double fn_1 = (x1 - x0) / (x2 - x0) * fdii + (x2 - x1) / (x2 - x0) * fdi;
//			double fn = bound(0, fdii - 0.5 * (fn_1 - fdii), 2 * fdii);
//			
//			
//		} else {
//			//Interpolation if x(1) < x < x(n - 1)					
//			double x0 = orderedCoordinates[i-2].getX();
//			double y0 = orderedCoordinates[i-2].getY();				
//			double x3 = orderedCoordinates[i + 1].getX();
//			double y3 = orderedCoordinates[i + 1].getY();
//
//			double y = 0;
//			double forward = 0;
//
//			double fdi = (x1 * y1 - x0 * y0) / (x1 - x0);
//			double fdii = (x2 * y2 - x1 * y1) / (x2 - x1); 
//			double fdiii = (x3 * y3 - x2 * y2) / (x3 - x2);
//			double fi = (x1 - x0) / (x2 - x0) * fdii + (x2 - x1) / (x2 - x0) * fdi;
//			double fii = (x2 - x1) / (x3 - x1) * fdiii + (x3 - x2) / (x3 - x1) * fdii;
//
//			// the x in equation (25)
//			double x = (term - x1) / (x2 - x1);
//			double g0 = fi - fdii;
//			double g1 = fii - fdii;
//			double G = 0;
//			if (x == 0){
//				G = g0;
//			} else if (x == 1){
//				G = g1;
//			} else if ((g0 < 0 && -0.5 * g0 <= g1 && g1 <= -2 * g0) || (g0 > 0 && -0.5 * g0 >= g1 && g1 >= -2 * g0)){
//				// zone (i)
//				G = g0 * (1 - 4 * x + 3 * x * x) + g1 * (-2 * x + 3 * x * x);
//			} else if ((g0 <0 && g1 > -2 * g0) || (g0 > 0 && g1 < -2 * g0)){
//				// zone (ii)
//				// equation (29)
//				double eta = (g1 + 2 * g0) / (g1 - g0);
//				// equation (28)
//				if (x <= eta){
//					G = g0;
//				} else {
//					G = g0 + (g1 - g0) * ((x - eta) / (1 - eta)) * ((x - eta) / (1 - eta));						
//				}					
//			} else if ((g0 > 0 && 0 > g1 && g1 > -0.5 * g0) || (g0 < 0 && 0 < g1 && g1 < -0.5 * g0)){
//				// zone (iii)
//				// equation (31)
//				double eta = 3 * g1 / (g1 - g0);
//				// equation (30)
//				if (x < eta){
//					G = g1 + (g0 - g1) * ((eta - x) / eta) * ((eta - x) / eta);
//				} else {
//					G = g1;
//				}					
//			} else if (g0 == 0 && g1 == 0){
//				G = 0;
//			} else {
//				// zone (iv)
//				// equation (33)
//				double eta = g1 / (g1 + g0);
//				// equation (34)
//				double A = -g0 * g1 / (g0 + g1);
//				// equation (32)
//				if (x <= eta){
//					G = A + (g0 - A) * ((eta - x) / eta) * ((eta - x) / eta);
//				} else {
//					G = A + (g1 - A) * ((eta - x) / (1 - eta)) * ((eta - x) / (1 - eta));
//				}
//			}
//			// equation 26
//			//TODO
//			forward = G + fii;
//			
//		}
//	}
//	
//	return y;
//}
	

