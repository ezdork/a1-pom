package ez.dork.stock.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

	private static final String URL_920801_951229 = "http://hist.gretai.org.tw/Hist/STOCK/AFTERTRADING/DAILY_CLOSE_QUOTES/RSTA3104_%s.HTML";
	private static final String URL_960101_960420 = "http://www.gretai.org.tw/ch/stock/aftertrading/daily_close_quotesB/stk_quote_result.php?timestamp=%d";
	private static final String URL = "http://www.gretai.org.tw/ch/stock/aftertrading/DAILY_CLOSE_quotes/stk_quote_print.php?d=%s%s&s=0,asc,0";
	private static Calendar c951229 = Calendar.getInstance();
	private static Calendar c960420 = Calendar.getInstance();
	static {
		c951229.set(2006, 11, 29);
		c960420.set(2007, 3, 20);
	}

	public static List<Stock> getStockList(Calendar calendar) {
		List<String[]> rowList = getRowList(calendar);
		return getStockList(calendar, rowList);
	}

	private static List<String[]> getRowList960101_960420(Calendar calendar) {
		List<String[]> result = new ArrayList<String[]>();

		try {
			String url = String.format(URL_960101_960420, new Date().getTime());
			String twDate = DateUtil.format(calendar, "tw/MM/dd");
			Document doc = Jsoup.connect(url).data("input_date", twDate)
					.data("ajax", "true").post();
			Elements tables = doc.getElementsByTag("table");
			Element lastTable = tables.get(tables.size() - 2);
			Elements trs = lastTable.getElementsByTag("tr");
			for (Element tr : trs) {
				Elements tds = tr.getElementsByTag("td");
				String[] record = new String[tds.size()];
				try {
					record[0] = tds.get(0).text(); // 股票代號 (0)
					record[1] = tds.get(1).text(); // 股票名稱 (1)
					record[8] = tds.get(10).text(); // 成交千股 (8)
					record[4] = tds.get(4).text(); // 開盤 (4)
					record[5] = tds.get(5).text(); // 最高 (5)
					record[6] = tds.get(6).text(); // 最低 (6)
					record[2] = tds.get(2).text(); // 收盤 (2)
					result.add(record);
				} catch (Exception e) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static List<String[]> getRowList920801_951229(Calendar calendar) {
		List<String[]> result = new ArrayList<String[]>();

		InputStream openStream = null;
		try {
			String twDate = DateUtil.format(calendar, "twMMdd");
			String url = String.format(URL_920801_951229, twDate);
			openStream = new URL(url).openStream();
			Document doc = Jsoup.parse(openStream, "MS950", url);
			Elements tables = doc.getElementsByTag("table");
			Element lastTable = tables.last();
			Elements trs = lastTable.getElementsByTag("tr");
			for (Element tr : trs) {
				Elements tds = tr.getElementsByTag("td");
				String[] record = new String[tds.size()];
				try {
					record[0] = tds.get(0).text(); // 股票代號 (0)
					record[1] = tds.get(1).text(); // 股票名稱 (1)
					record[8] = tds.get(10).text(); // 成交千股 (8)
					record[4] = tds.get(6).text(); // 開盤 (4)
					record[5] = tds.get(7).text(); // 最高 (5)
					record[6] = tds.get(8).text(); // 最低 (6)
					record[2] = tds.get(3).text(); // 收盤 (2)
					result.add(record);
				} catch (Exception e) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(openStream);
		}
		return result;
	}

	public static List<String[]> getRowList(Calendar calendar) {
		List<String[]> rowList = null;
		if (calendar.compareTo(c951229) <= 0) {
			rowList = getRowList920801_951229(calendar);
		} else if (calendar.compareTo(c960420) <= 0) {
			rowList = getRowList960101_960420(calendar);
		} else {
			rowList = getRowList960421(calendar);
		}
		return rowList;
	}

	private static List<String[]> getRowList960421(Calendar calendar) {
		String twy = DateUtil.format(calendar, "twy");
		String mmdd = DateUtil.format(calendar, "/MM/dd");

		InputStream openStream = null;
		InputStreamReader reader = null;
		CSVReader<String[]> csvPersonReader = null;

		List<String[]> rowList = new ArrayList<String[]>();
		try {

			String url = String.format(URL, twy, mmdd);

			openStream = new URL(url).openStream();
			Document doc = Jsoup.parse(openStream, "UTF8", url);
			Elements tables = doc.getElementsByTag("table");
			Element lastTable = tables.last();
			Elements trs = lastTable.getElementsByTag("tr");
			for (Element tr : trs) {
				Elements tds = tr.getElementsByTag("td");
				String[] record = new String[tds.size()];
				try {
					record[0] = tds.get(0).text(); // 股票代號 (0)
					record[1] = tds.get(1).text(); // 股票名稱 (1)
					record[8] = tds.get(8).text(); // 成交千股 (8)
					record[4] = tds.get(4).text(); // 開盤 (4)
					record[5] = tds.get(5).text(); // 最高 (5)
					record[6] = tds.get(6).text(); // 最低 (6)
					record[2] = tds.get(2).text(); // 收盤 (2)
					rowList.add(record);
				} catch (Exception e) {
				}
			}

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
				Math.floor(Integer.valueOf(row[8].replace(",", "")) / 1000);
				StockName stockName = new StockName();

				stockName.setCode(row[0].replace("=", "").trim());
				stockName.setName(row[1].trim());
				stockName.setKind(1);

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
				stock.setDate(DateUtil.format(calendar, "yyyyMMdd")); // 日期

				Double floor = Math.floor(Integer.valueOf(row[8].replace(",",
						"")) / 1000);
				stock.setVolumn(floor.intValue()); // 成交仟股

				stock.setOpen(Double.valueOf(row[4].replace(",", ""))); // 開盤
				stock.setHigh(Double.valueOf(row[5].replace(",", ""))); // 最高
				stock.setLow(Double.valueOf(row[6].replace(",", ""))); // 最低
				stock.setClose(Double.valueOf(row[2].replace(",", ""))); // 收盤

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
