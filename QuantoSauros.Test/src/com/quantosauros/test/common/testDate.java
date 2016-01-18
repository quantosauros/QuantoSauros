package com.quantosauros.test.common;

import com.quantosauros.common.date.Date;
import com.quantosauros.test.util.TestBase;

public class testDate  extends TestBase{

	public void test(){
		Date startDate = Date.valueOf("20160102");
		Date endDate = Date.valueOf("20170113");
		
		log(startDate.diff(endDate));
		
	}
}
