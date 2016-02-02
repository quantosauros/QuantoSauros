package com.quantosauros.batch.process;

import com.quantosauros.batch.dao.MySqlProcPriceDataDao;
import com.quantosauros.batch.dao.ProcPriceDataDao;
import com.quantosauros.common.date.Date;

public class ProcessPortfolio {

	protected Date _processDate;
	protected String _procId;
	
	public ProcessPortfolio(Date processDate, String procId){
		_processDate = processDate;
		_procId = procId;
	}
	
	public final void execute(){
		ProcPriceDataDao procPriceDataDao = new MySqlProcPriceDataDao();
		
		procPriceDataDao.insertPortfolioPriceData(_processDate.getDt(), _procId);
		procPriceDataDao.insertPortfolioDeltaGammaData(_processDate.getDt(), _procId);
		
	}
}
