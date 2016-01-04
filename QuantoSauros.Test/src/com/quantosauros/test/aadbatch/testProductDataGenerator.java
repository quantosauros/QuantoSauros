package com.quantosauros.test.aadbatch;

import com.quantosauros.batch.instrument.ProductDataGenerator;
import com.quantosauros.common.date.Date;
import com.quantosauros.test.util.TestBase;

public class testProductDataGenerator extends TestBase {

	public void test(){
		
		Date asOfDate = Date.valueOf("20141202");
		String instrumentCd = "APSSWAP002";
		String countryCd = "KR";
		
		ProductDataGenerator productDataGenerator = 
				new ProductDataGenerator(asOfDate, instrumentCd, countryCd);

		productDataGenerator.generate();
		
	}
}
