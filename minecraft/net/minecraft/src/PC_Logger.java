package net.minecraft.src;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;


/**
 * PowerCraft's static logger class.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_Logger {
	/**
	 * Global PowerCraft's logger.
	 */
	private static final Logger logger = Logger.getLogger("PowerCraft");
	/** Logging enabled */
	public static boolean loggingEnabled = true;
	/** Stdout printing enabled */
	public static boolean printToStdout = false;

	static {
		try {
			FileHandler handler = new FileHandler(new File(Minecraft.getMinecraftDir(), "PowerCraft.log").getPath());
			handler.setFormatter(new PC_LogFormatter());
			logger.addHandler(handler);
			loggingEnabled = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.setLevel(Level.ALL);
		logger.info("PowerCraft logger initialized.");
		logger.info((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()));
	}

	/**
	 * Enable logging.
	 */
	public static void enableLogging(boolean flag) {
		loggingEnabled = flag;
	}



	/**
	 * Enable debug mode - log also printed to stdout.
	 * @param printToStdout
	 */
	public static void setPrintToStdout(boolean printToStdout) {
		PC_Logger.printToStdout = printToStdout;
	}

	/**
	 * Log INFO message
	 * 
	 * @param msg message
	 */
	public static void info(String msg) {
		if (!loggingEnabled) {
			return;
		}
		logger.log(Level.INFO, msg);
	}

	/**
	 * Log FINE (important) message
	 * 
	 * @param msg message
	 */
	public static void fine(String msg) {
		if (!loggingEnabled) {
			return;
		}
		logger.log(Level.FINE, msg);
	}

	/**
	 * Log FINER (loss important) message
	 * 
	 * @param msg message
	 */
	public static void finer(String msg) {
		if (!loggingEnabled) {
			return;
		}
		logger.log(Level.FINER, msg);
	}

	/**
	 * Log FINEST (least important) message
	 * 
	 * @param msg message
	 */
	public static void finest(String msg) {
		if (!loggingEnabled) {
			return;
		}
		logger.log(Level.FINEST, msg);
	}

	/**
	 * Log WARNING message
	 * 
	 * @param msg message
	 */
	public static void warning(String msg) {
		if (!loggingEnabled) {
			return;
		}
		logger.log(Level.WARNING, msg);
	}

	/**
	 * Log SEVERE (critical warning) message
	 * 
	 * @param msg message
	 */
	public static void severe(String msg) {
		if (!loggingEnabled) {
			return;
		}
		logger.log(Level.SEVERE, msg);
	}

	/**
	 * Log THROWING message
	 * 
	 * @param sourceClass class throwing the exception
	 * @param sourceMethod method of the exception
	 * @param thrown thrown exception
	 */
	public static void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
		if (!loggingEnabled) {
			return;
		}
		logger.throwing(sourceClass, sourceMethod, thrown);
	}



	/**
	 * PowerCraft Log file formatter.
	 * 
	 * @author MightyPork
	 * @copy (c) 2012
	 */
	private static class PC_LogFormatter extends Formatter {

		/** Newline string constant */
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

			/*
			 * if(record.getMessage().charAt(record.getMessage().length()-1) == '\n'){
			 * buf.append(nl);
			 * }
			 */

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
			
			if(PC_Logger.printToStdout) System.out.print(buf.toString());

			return buf.toString();
		}
	}


}
