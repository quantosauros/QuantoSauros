package com.quantosauros.test.util;

import java.io.File;

import junit.framework.TestCase;

/**
 * 
 * @author Jae-Heon Kim
 * @since 2010.02.08
 * 
 */
public class TestBase extends TestCase {
	protected String _path = "log/";
	protected String _dname = "/output/";
	protected TestLogUtil _logger;
	
	protected TestBase() {
		//System.out.println(this.getClass().getName());
		String cname = getClass().getPackage().getName();
		//System.out.println(cname);
		String rname = cname.replaceAll("\\.", "/");
		_path += rname + _dname;
		File f = new File(_path);
		if (!f.exists()) {
			f.mkdirs();
		}
		//System.out.println(_path);
		//int date = cal.get(Calendar.DATE);
		_path += getClass().getSimpleName() + ".";
		DateTime dt = new DateTime();
		_logger = new TestLogUtil(_path + dt.toString());
		//_logger.log("test");
	}
	
	protected void log(String logEntry) {
		_logger.log(logEntry);
	}
	
	protected void log(Object logEntry) {
		_logger.log(logEntry.toString());
	}
	
	protected void log() {
		log("");
	}
	
	protected void log(double logEntry) {
		_logger.log("" + logEntry);
	}

}
