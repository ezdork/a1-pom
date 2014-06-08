package ez.dork.stock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	private static final String tw = "tw";
	private static final String twy = "twy";

	public static String format(Calendar calendar, String format) {
		if (format.contains(twy)) {
			String twy = String.valueOf(calendar.get(Calendar.YEAR) - 1911);
			while (twy.length() < 3) {
				twy = "0" + twy;
			}
			format = format.replace(tw, twy);

		} else if (format.contains(tw)) {
			String twy = String.valueOf(calendar.get(Calendar.YEAR) - 1911);
			while (twy.length() < 2) {
				twy = "0" + twy;
			}
			format = format.replace(tw, twy);
		}
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

}
