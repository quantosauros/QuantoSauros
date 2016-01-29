package com.quantosauros.test.aadbatch;

import java.text.SimpleDateFormat;

import com.quantosauros.batch.instrument.ProductDataGenerator;
import com.quantosauros.common.date.Date;
import com.quantosauros.test.util.TestBase;

public class testProductDataGenerator extends TestBase {

	public void test(){
		
		Date asOfDate = Date.valueOf("20141202");
		String instrumentCd = "KR_IRS131202001";
		String countryCd = "KR";
		
		log(Date.valueOf(new SimpleDateFormat("yyyyMMdd").format(new java.util.Date())));
		
		ProductDataGenerator productDataGenerator = 
				new ProductDataGenerator(asOfDate, instrumentCd, countryCd);

		productDataGenerator.generate();
		
	}
}
