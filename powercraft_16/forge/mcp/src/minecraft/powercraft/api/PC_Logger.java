package powercraft.api;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PC_Logger {

	private static final Logger logger = Logger.getLogger("PowerCraft");

	private static boolean loggingEnabled = true;

	private static boolean printToStdout = false;

	public static void init(File file) {
		try {
			FileHandler handler = new FileHandler(file.getPath());
			handler.setFormatter(new PC_LogFormatter());
			logger.addHandler(handler);
			loggingEnabled = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.setLevel(Level.ALL);
		logger.info("PowerCraft logger initialized.");
		logger.info((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"))
				.format(new Date()));
	}

	public static void enableLogging(boolean flag) {
		loggingEnabled = flag;
	}

	public static void setPrintToStdout(boolean printToStdout) {
		PC_Logger.printToStdout = printToStdout;
	}

	public static void log(Level level, String msg, Object...param) {
		if (!loggingEnabled) {
			return;
		}
		logger.log(level, String.format(msg, param));
	}
	
	public static void info(String msg, Object...param) {
		log(Level.INFO, msg, param);
	}

	public static void fine(String msg, Object...param) {
		log(Level.FINE, msg, param);
	}

	public static void finer(String msg, Object...param) {
		log(Level.FINER, msg, param);
	}

	public static void finest(String msg, Object...param) {
		log(Level.FINEST, msg, param);
	}

	public static void warning(String msg, Object...param) {
		log(Level.WARNING, msg, param);
	}

	public static void severe(String msg, Object...param) {
		log(Level.SEVERE, msg, param);
	}

	public static void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
		if (!loggingEnabled) {
			return;
		}

		logger.throwing(sourceClass, sourceMethod, thrown);
	}

	private static class PC_LogFormatter extends Formatter {
		private static final String nl = System.getProperty("line.separator");

		@Override
		public String format(LogRecord record) {
			StringBuffer buf = new StringBuffer(180);

			if (record.getMessage().equals("\n")) {
				return nl;
			}

			if (record.getMessage().charAt(0) == '\n') {
				buf.append(nl);
				record.setMessage(record.getMessage().substring(1));
			}

			Level level = record.getLevel();
			String trail = "";

			if (level == Level.CONFIG) {
				trail = "CONFIG: ";
			}

			if (level == Level.FINE) {
				trail = "";
			}

			if (level == Level.FINER) {
				trail = "\t";
			}

			if (level == Level.FINEST) {
				trail = "\t\t";
			}

			if (level == Level.INFO) {
				trail = "INFO: ";
			}

			if (level == Level.SEVERE) {
				trail = "SEVERE: ";
			}

			if (level == Level.WARNING) {
				trail = "WARNING: ";
			}

			buf.append(trail);
			buf.append(formatMessage(record));
			buf.append(nl);
			Throwable throwable = record.getThrown();

			if (throwable != null) {
				buf.append("at ");
				buf.append(record.getSourceClassName());
				buf.append('.');
				buf.append(record.getSourceMethodName());
				buf.append(nl);
				StringWriter sink = new StringWriter();
				throwable.printStackTrace(new PrintWriter(sink, true));
				buf.append(sink.toString());
				buf.append(nl);
			}

			if (PC_Logger.printToStdout) {
				System.out.print(buf.toString());
			}

			return buf.toString();
		}
	}
}
