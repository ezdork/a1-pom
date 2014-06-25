package ez.dork.stock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	private static final String TW = "tw";
	private static final String TWY = "twy";

	public static String format(Calendar calendar, String format) {
		if (format.contains(TWY)) {
			String twy = String.valueOf(calendar.get(Calendar.YEAR) - 1911);
			while (twy.length() < 3) {
				twy = "0" + twy;
			}
			format = format.replace(TWY, twy);

		} else if (format.contains(TW)) {
			String twy = String.valueOf(calendar.get(Calendar.YEAR) - 1911);
			while (twy.length() < 2) {
				twy = "0" + twy;
			}
			format = format.replace(TW, twy);
		}
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

}
