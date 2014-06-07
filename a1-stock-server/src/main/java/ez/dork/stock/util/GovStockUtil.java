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
import ez.dork.stock.domain.StockName;

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
		List<String[]> rowList = getRowList(calendar);
		return getStockList(calendar, rowList);
	}

	public static List<String[]> getRowList(Calendar calendar) {
		String yyyyMM = YYYY_MM_FORMAT.format(calendar.getTime());
		String yyyyMMdd = YYYY_MM_DD_FORMAT.format(calendar.getTime());
		String url = String.format(URL, yyyyMM, yyyyMMdd);

		InputStream openStream = null;
		InputStreamReader reader = null;
		CSVReader<String[]> csvPersonReader = null;

		List<String[]> result = new ArrayList<String[]>();
		try {
			openStream = new URL(url).openStream();

			reader = new InputStreamReader(openStream, CHARSET);

			csvPersonReader = new CSVReaderBuilder<String[]>(reader)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new DefaultCSVEntryParser()).build();

			result = csvPersonReader.readAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (csvPersonReader != null) {
				try {
					csvPersonReader.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (openStream != null) {
				try {
					openStream.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}

	public static List<StockName> getStockNameList(List<String[]> rowList) {
		List<StockName> list = new ArrayList<StockName>();
		for (int index = 0; index < rowList.size(); index++) {
			String[] row = rowList.get(index);
			try {
				if (row.length < 8) {
					continue;
				}
				Math.floor(Integer.valueOf(row[2].replace(",", "")) / 1000);

				StockName stockName = new StockName();
				stockName.setCode(row[0].replace("=", "").trim());
				stockName.setName(row[1].trim());
				stockName.setKind(0);

				list.add(stockName);
			} catch (Exception e) {
				if (!(e instanceof NumberFormatException)) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public static List<Stock> getStockList(Calendar calendar,
			List<String[]> rowList) {
		String yyyyMMdd = YYYY_MM_DD_FORMAT.format(calendar.getTime());
		List<Stock> list = new ArrayList<Stock>();
		for (int index = 0; index < rowList.size(); index++) {
			String[] row = rowList.get(index);
			try {
				if (row.length < 8) {
					continue;
				}
				Stock stock = new Stock();

				stock.setCode(row[0].replace("=", "").trim());

				stock.setDate(yyyyMMdd); // 日期

				Double floor = Math.floor(Integer.valueOf(row[2].replace(",",
						"")) / 1000);
				stock.setVolumn(floor.intValue()); // 成交股數

				stock.setOpen(Double.valueOf(row[5].replace(",", ""))); // 開盤
				stock.setHigh(Double.valueOf(row[6].replace(",", ""))); // 最高
				stock.setLow(Double.valueOf(row[7].replace(",", ""))); // 最低
				stock.setClose(Double.valueOf(row[8].replace(",", ""))); // 收盤

				list.add(stock);
			} catch (Exception e) {
				if (!(e instanceof NumberFormatException)) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
