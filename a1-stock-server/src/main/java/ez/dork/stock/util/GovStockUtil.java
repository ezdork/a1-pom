package ez.dork.stock.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Connection;
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
 * 上市股票
 * 
 * @author tim
 * 
 */
public class GovStockUtil {

	private static final String CHARSET = "MS950";

	private static final String URL_890104_930210 = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX_oldtsec.php";
	private static final String URL = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX3_print.php?genpage=genpage/Report%s/A112%sALLBUT0999_1.php&type=csv";

	private static Calendar c930210 = Calendar.getInstance();
	static {
		c930210.set(2004, 1, 10);
	}

	public static List<Stock> getStockList(Calendar calendar)
			throws IOException {
		List<String[]> rowList = getRowList(calendar);
		return getStockList(calendar, rowList);
	}

	private static List<String[]> getRowList890104_930210(Calendar calendar) {
		List<String[]> result = new ArrayList<String[]>();

		try {
			String twDate = DateUtil.format(calendar, "tw/MM/dd");
			Document doc = Jsoup.connect(URL_890104_930210)
					.data("input_date", twDate).post();
			Elements tables = doc.getElementsByTag("table");
			Element lastTable = tables.last();
			Elements trs = lastTable.getElementsByTag("tr");
			for (Element tr : trs) {
				Elements tds = tr.getElementsByTag("td");
				if (tds.size() == 14) {
					String[] record = new String[14];
					int i = 0;
					for (Element td : tds) {
						record[i] = td.text();
						i++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<String[]> getRowList(Calendar calendar) {
		List<String[]> result = null;
		if (calendar.compareTo(c930210) > 0) {
			result = getRowList1040113(calendar);
		} else {
			result = getRowList890104_930210(calendar);
		}
		return result;
	}

	private static List<String[]> getRowList1040113(Calendar calendar) {
		String twy_MM_DD = DateUtil.format(calendar, "twy/MM/dd");
		List<String[]> resultList = new ArrayList<String[]>();
		Connection.Response res = null;
		try {
			res = Jsoup.connect("http://www.twse.com.tw//ch/trading/exchange/MI_INDEX/MI_INDEX.php")
                    .data("download", "html")
                    .data("qdate", twy_MM_DD)
                    .data("selectType", "ALLBUT0999")
                    .method(Connection.Method.POST)
                    .execute();

			Document doc = res.parse();
			Element body = doc.body();
			Elements tables = body.select("table");
			Elements trs = tables.get(1).select("tr");

			for (Element tr : trs) {
				Elements tds = tr.select("td");

				String[] e = new String[tds.size()];
				for (int i = 0; i < tds.size(); i++) {
					e[i] = tds.get(i).text();
				}
				resultList.add(e);
			}
		} catch (IOException e) {
		}
		return resultList;
	}

	private static List<String[]> getRowList930211(Calendar calendar) {
		String yyyyMM = DateUtil.format(calendar, "yyyyMM");
		String yyyyMMdd = DateUtil.format(calendar, "yyyyMMdd");
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
		String yyyyMMdd = DateUtil.format(calendar, "yyyyMMdd");
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
