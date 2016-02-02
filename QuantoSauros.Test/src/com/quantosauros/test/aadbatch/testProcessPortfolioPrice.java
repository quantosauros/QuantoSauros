package com.quantosauros.test.aadbatch;



import com.quantosauros.batch.process.ProcessPortfolio;
import com.quantosauros.common.date.Date;
import com.quantosauros.test.util.TestBase;

public class testProcessPortfolioPrice extends TestBase{

	
	public void test(){
		
		Date processDate = Date.valueOf("20131202");
		String procId = "0";
		
		
		ProcessPortfolio processPortfolio = new ProcessPortfolio(processDate, procId);
		
		processPortfolio.execute();
		
		
	}
}
