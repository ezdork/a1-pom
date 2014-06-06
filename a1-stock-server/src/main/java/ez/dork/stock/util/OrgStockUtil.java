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
 * 上櫃股票
 * 
 * @author tim
 * 
 */
public class OrgStockUtil {

	private static final String CHARSET = "MS950";
	private static final SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	private static final SimpleDateFormat YY_FORMAT = new SimpleDateFormat(
			"yyyy");
	private static final SimpleDateFormat MM_dd_FORMAT = new SimpleDateFormat(
			"/MM/dd");

	private static final String URL = "http://www.gretai.org.tw/ch/stock/aftertrading/DAILY_CLOSE_quotes/stk_quote_download.php?d=%s%s&s=0,asc,0";

	public static List<Stock> getStockList(Calendar calendar) {
		List<String[]> rowList = getRowList(calendar);
		return getStockList(calendar, rowList);
	}

	public static List<String[]> getRowList(Calendar calendar) {
		String yy = String.valueOf(Integer.valueOf(YY_FORMAT.format(calendar
				.getTime())) - 1911);
		String mm = MM_dd_FORMAT.format(calendar.getTime());

		InputStream openStream = null;
		InputStreamReader reader = null;
		CSVReader<String[]> csvPersonReader = null;

		List<String[]> rowList = new ArrayList<String[]>();
		try {

			String url = String.format(URL, yy, mm);

			openStream = new URL(url).openStream();
			reader = new InputStreamReader(openStream, CHARSET);

			csvPersonReader = new CSVReaderBuilder<String[]>(reader)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new DefaultCSVEntryParser()).build();

			rowList = csvPersonReader.readAll();

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
		return rowList;
	}

	public static List<StockName> getStockNameList(List<String[]> stockList) {

		List<StockName> list = new ArrayList<StockName>();
		for (int index = 0; index < stockList.size(); index++) {
			String[] row = stockList.get(index);
			try {
				if (row.length < 10) {
					continue;
				}
				StockName stockName = new StockName();

				stockName.setCode(row[0].replace("=", "").trim());
				stockName.setName(row[1].trim());
				stockName.setKind(1);

				list.add(stockName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static List<Stock> getStockList(Calendar calendar,
			List<String[]> stockList) {

		List<Stock> list = new ArrayList<Stock>();
		for (int index = 0; index < stockList.size(); index++) {
			String[] row = stockList.get(index);
			try {
				if (row.length < 10) {
					continue;
				}
				Stock stock = new Stock();

				stock.setCode(row[0].replace("=", "").trim());

				stock.setDate(YYYYMMDD_FORMAT.format(calendar.getTime())); // 日期

				stock.setVolumn((int) Math.floor(Integer.valueOf(row[8]
						.replace(",", "")) / 1000)); // 成交仟股

				stock.setOpen(Double.valueOf(row[4].replace(",", ""))); // 開盤
				stock.setHigh(Double.valueOf(row[5].replace(",", ""))); // 最高
				stock.setLow(Double.valueOf(row[6].replace(",", ""))); // 最低
				stock.setClose(Double.valueOf(row[2].replace(",", ""))); // 收盤

				list.add(stock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
