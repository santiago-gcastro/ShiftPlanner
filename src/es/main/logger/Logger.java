/**
 * 
 */
package es.main.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * 
 */
public class Logger {
	private static volatile Logger _instance;
	private static Object mutex = new Object();
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Logger getInstance() {
		Logger result = _instance;
		if (result == null) {
			synchronized (mutex) {
				result = _instance;
				if (result == null)
					_instance = result = new Logger();
			}
		}
		return result;
	}
	public final void log(String message) {
		String timestamp  = dateFormat.format(new Date());
		System.out.println("[" + timestamp + "] " + message);
	}
	public final void log(Exception message) {
		message.printStackTrace();
	}
	public final void log(Iterator<String> message) {
		String out = "";
		while (message.hasNext()) {
			out = String.join("|", out, message.next());
		}
		log(out);
	}
}
