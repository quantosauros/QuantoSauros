package com.quantosauros.test.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * 
 * @author Jae-Heon Kim
 * @since 2010.02.08
 * 
 */
public class TestLogUtil {
	protected DateFormat _dateFormat, _ddf;
	protected PrintStream _log;
	protected boolean _logging = true;
	protected boolean _stdout = true;

	public TestLogUtil(String fname) {
		this(new File(fname + ".log"));
	}

	public TestLogUtil(File f) {
		try {
			setLog(new FileOutputStream(f, true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized void setDateFormat(DateFormat df) {
		_dateFormat = df;
	}

	public synchronized void setLog(OutputStream out) {
		if (_log != null) {
			close();
		}
		if (out != null) {
			_log = new PrintStream(out);
		}
		_logging = true;
	}

	public synchronized void setLog(PrintStream ps) {
		if (_log != null) {
			close();
		}
		_log = ps;
		_logging = true;
	}

	public PrintStream getLogStream() {
		return _log;
	}

	public synchronized void log(String prefix, String logEntry) {
		if (!_logging) {
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		if (_dateFormat != null) {
			sb.append(_dateFormat.format(new Date()));
		} else {
			if (_ddf == null) {
				_ddf = DateFormat.getDateTimeInstance(DateFormat.LONG,
						DateFormat.LONG);
			}
			sb.append(_ddf.format(new Date()));
		}

		sb.append(": ");
		if (prefix != null) {
			sb.append(prefix);
		}
		sb.append(logEntry);
		if (_log != null) {
			synchronized (_log) {
				_log.println(sb.toString());
				// _log.flush();
			}
		}
		if (_stdout) {
			System.out.println(sb);
		}
	}

	public void log(String logEntry) {
		log("", logEntry);
	}

	public void log(Throwable e, String prefix, String logEntry) {
		if (!_logging) {
			return;
		}
		log(prefix, logEntry);
		e.printStackTrace(_log);
		_log.flush();
	}

	public void log(Throwable e, String logEntry) {
		log(e, "", logEntry);
	}

	public void log(Throwable e) {
		log(e, e.getMessage());
	}

	public synchronized void close() {
		_logging = false;
		if (_log != null) {
			_log.flush();
			if (!isSystemLog()) {
				_log.close();
			}
		}
		_log = null;
	}

	private boolean isSystemLog() {
		return (!_log.equals(System.out) && !_log.equals(System.err));
	}

	public void setLogging(boolean b) {
		_logging = b;
	}

	public boolean isLogging() {
		return _logging;
	}

	public void setStdout(boolean b) {
		_stdout = b;
	}

	public boolean isStdout() {
		return _stdout;
	}
}
