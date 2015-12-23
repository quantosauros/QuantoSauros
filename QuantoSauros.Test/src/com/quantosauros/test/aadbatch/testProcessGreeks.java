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
		String procId = "101";
		Date startDate = Date.valueOf("20150428");
		Date endDate = Date.valueOf("20150512");
		String idxId = "102";		
		int simNum = 5000;
		boolean calcBump = false;
		int monitorFrequency = 1;
		
		Calendar cal = SouthKoreaCalendar.getCalendar(1);
		Date processDate = cal.adjustDate(startDate, BusinessDayConvention.FOLLOWING);		
		while (!endDate.equals(processDate)){
			log("processDate: " + processDate.getDt());
			
			long start = System.currentTimeMillis();
			ProcessGreeks process = new ProcessGreeks(
					processDate, procId, idxId);
			process.setSimNum(simNum);
			process.setMonitorFrequency(monitorFrequency);
			process.setCalcBump(calcBump);
			process.setSpecificInstrument("APS002");
			//DB기록 여부 입력(true:기록, false:미기록)
			process.setInsertResults(true);
			process.execute();
			
			long end = System.currentTimeMillis();
			log("TIME: " + (double)(end-start)/1000);
			
			processDate = cal.adjustDate(processDate.plusDays(1), BusinessDayConvention.FOLLOWING);
		}
	}
}
