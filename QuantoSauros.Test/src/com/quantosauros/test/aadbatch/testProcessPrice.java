package com.quantosauros.test.aadbatch;

import com.quantosauros.batch.process.ProcessPrices;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.SouthKoreaCalendar;
import com.quantosauros.common.date.Date;
import com.quantosauros.test.util.TestBase;

public class testProcessPrice extends TestBase {

	public void test(){
		//INPUT VARIABLES
		//프로세스 ID
		String procId = "0";		
		//프로세스 시작일
		Date startDate = Date.valueOf("20131202");
		//프로세스 마지막일
		Date endDate = Date.valueOf("20131203");
		//시뮬레이션 횟수				
		int simNum = 5000;
		//모니터링 간격(0: 모든구간 1일간격 시뮬레이션, 1: 조밀하게 셋팅, 10 : 10일간격 베이스 셋팅, 30: 30일간격 베이스 셋팅)
		int monitorFrequency = 1;
		
		Calendar cal = SouthKoreaCalendar.getCalendar(1);		
		Date processDate = cal.adjustDate(startDate, BusinessDayConvention.FOLLOWING);		
		
		while (!endDate.equals(processDate)){
			log("processDate: " + processDate.getDt());
			
			long start = System.currentTimeMillis();
			ProcessPrices process = new ProcessPrices(processDate, procId);
			process.setSimNum(simNum);
			process.setMonitorFrequency(monitorFrequency);		
			//상품코드입력(미입력시 모든상품 계산)
			process.setSpecificInstrument("KR_IRS131202001");
			//DB기록 여부 입력(true:기록, false:미기록)
			process.setInsertResults(true);
			process.execute();
			
			long end = System.currentTimeMillis();
			log("TIME: " + (double)(end-start)/1000);
			
			processDate = cal.adjustDate(
					processDate.plusDays(1), BusinessDayConvention.FOLLOWING);
			
		}	
	}
}
