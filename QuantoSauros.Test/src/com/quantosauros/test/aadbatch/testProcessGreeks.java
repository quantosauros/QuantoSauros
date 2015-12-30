package com.quantosauros.test.aadbatch;

import com.quantosauros.batch.process.ProcessGreeks;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.SouthKoreaCalendar;
import com.quantosauros.common.date.Date;
import com.quantosauros.test.util.TestBase;

public class testProcessGreeks extends TestBase {

	public void test(){
		//INPUT VARIABLES
		String procId = "2";
		Date startDate = Date.valueOf("20131202");
		Date endDate = Date.valueOf("20131203");				
		int simNum = 100;
		boolean calcBump = true;
		int monitorFrequency = 1;
		
		Calendar cal = SouthKoreaCalendar.getCalendar(1);
		Date processDate = cal.adjustDate(startDate, BusinessDayConvention.FOLLOWING);		
		while (!endDate.equals(processDate)){
			log("processDate: " + processDate.getDt());
			
			long start = System.currentTimeMillis();
			ProcessGreeks process = new ProcessGreeks(
					processDate, procId);
			process.setSimNum(simNum);
			process.setMonitorFrequency(monitorFrequency);
			process.setCalcBump(calcBump);
			process.setSpecificInstrument("APSSWAP001");
			//DB기록 여부 입력(true:기록, false:미기록)
			process.setInsertResults(true);
			process.execute();
			
			long end = System.currentTimeMillis();
			log("TIME: " + (double)(end-start)/1000);
			
			processDate = cal.adjustDate(processDate.plusDays(1), BusinessDayConvention.FOLLOWING);
		}
	}
}
