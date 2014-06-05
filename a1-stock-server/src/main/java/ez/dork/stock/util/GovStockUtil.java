package ez.dork.stock.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;

import ez.dork.stock.domain.Stock;

/**
 * 上市股票
 * 
 * @author tim
 * 
 */
public class GovStockUtil {
	// private static final String KIND_ROOT_URL =
	// "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/data2.php";
	// private static final String KIND_URL =
	// "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/STK_words.php?STK_NAME=%d";

	private static final String CHARSET = "MS950";
	// private static final String CATEGORY_ROOT_URL =
	// "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/data_Top.htm";
	// private static final String CATEGORY_URL =
	// "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/STK%s.php?STK=%s";

	// private static final String URL =
	// "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report%s/%s_F3_1_8_%s.php&type=csv";
	private static final SimpleDateFormat YYYY_MM_FORMAT = new SimpleDateFormat(
			"yyyyMM");
	private static final SimpleDateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	private static final String URL = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX3_print.php?genpage=genpage/Report%s/A112%sALLBUT0999_1.php&type=csv";

	public static List<Stock> getStockList(Calendar calendar)
			throws IOException {
		String yyyyMM = YYYY_MM_FORMAT.format(calendar.getTime());
		String yyyyMMdd = YYYY_MM_DD_FORMAT.format(calendar.getTime());
		String url = String.format(URL, yyyyMM, yyyyMMdd);

		InputStream openStream = null;
		InputStreamReader reader = null;
		CSVReader<String[]> csvPersonReader = null;

		List<Stock> list = new ArrayList<Stock>();
		try {
			openStream = new URL(url).openStream();

			reader = new InputStreamReader(openStream, CHARSET);

			csvPersonReader = new CSVReaderBuilder<String[]>(reader)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new DefaultCSVEntryParser()).build();

			List<String[]> stockList = csvPersonReader.readAll();
			for (int index = 0; index < stockList.size(); index++) {
				String[] row = stockList.get(index);
				try {
					if (row.length < 8) {
						continue;
					}
					Stock stock = new Stock();

					stock.setCode(row[0].replace("=", "").trim());

					stock.setDate(yyyyMMdd); // 日期

					Double floor = Math.floor(Integer.valueOf(row[2].replace(
							",", "")) / 1000);
					stock.setVolumn(floor.intValue()); // 成交股數

					stock.setOpen(Double.valueOf(row[5].replace(",", ""))); // 開盤
					stock.setHigh(Double.valueOf(row[6].replace(",", ""))); // 最高
					stock.setLow(Double.valueOf(row[7].replace(",", ""))); // 最低
					stock.setClose(Double.valueOf(row[8].replace(",", ""))); // 收盤

					list.add(stock);
				} catch (Exception e) {
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			if (csvPersonReader != null) {
				csvPersonReader.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (openStream != null) {
				openStream.close();
			}
		}

		return list;
	}
}
