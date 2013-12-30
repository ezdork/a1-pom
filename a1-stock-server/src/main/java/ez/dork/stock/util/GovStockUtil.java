package ez.dork.stock.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
	// private static final String KIND_ROOT_URL =
	// "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/data2.php";
	// private static final String KIND_URL =
	// "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/STK_words.php?STK_NAME=%d";

	private static final String CHARSET = "MS950";
	private static final String CATEGORY_ROOT_URL = "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/data_Top.htm";
	private static final String CATEGORY_URL = "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/STK%s.php?STK=%s";

	private static final String URL = "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report%s/%s_F3_1_8_%s.php&type=csv";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMM");

	public static List<String> getCodeList() throws IOException {
		List<String> list = new ArrayList<String>();
		InputStream openStream = null;
		try {
			openStream = new URL(CATEGORY_ROOT_URL).openStream();
			Document document = Jsoup.parse(openStream, "MS950", CATEGORY_ROOT_URL);
			Elements elements = document.select("[name=STK] > option");
			for (int index = 0; index < elements.size(); index++) {
				Element element = elements.get(index);
				String val = element.val();
				List<String> tmpList = getStockByStk(val);
				list.removeAll(tmpList);
				list.addAll(tmpList);
			}
			Collections.sort(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (openStream != null) {
				openStream.close();
			}
		}
		return list;
	}

	private static List<String> getStockByStk(String stk) throws IOException {
		List<String> list = new ArrayList<String>();
		if (stk == null
				|| "".equals(stk.trim())
				|| stk.matches("(0049|019919T|0999|0999P|0999F|0999Q|0999C|0999B|0999I|0999O|0999GA|0999GD|0999G9|9299|CB)")) {
			return list;
		}
		String url = String.format(CATEGORY_URL, stk, stk);
		InputStream openStream = null;
		try {
			openStream = new URL(url).openStream();
			Document doc = Jsoup.parse(openStream, CHARSET, url);
			Elements newsHeadlines = doc.select("a");
			// System.out.println(doc);
			for (int index = 0; index < newsHeadlines.size(); index++) {
				String text = newsHeadlines.get(index).text();
				// System.out.println(text);
				String stockCode = text.split(" ")[0];
				// System.out.println(stockCode);
				list.add(stockCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (openStream != null) {
				openStream.close();
			}
		}
		return list;
	}

	// private static List<String> getAllStockList() throws IOException {
	// List<String> list = new ArrayList<String>();
	// for (int i = 0; i < 10; i++) {
	// Map<String, String> data = new HashMap<String, String>();
	// data.put("STK_NAME", String.valueOf(i));
	// data.put("STK", "");
	// data.put("INDEX_NAME", "");
	// Connection.Response res =
	// Jsoup.connect(KIND_ROOT_URL).data(data).method(Method.POST).execute();
	//
	// Map<String, String> cookies = res.cookies();
	//
	// String url = String.format(KIND_URL, i);
	//
	// // Document doc = Jsoup.parse(new URL(url).openStream(), "Big5",
	// // url);
	// Document doc = Jsoup.connect(url).cookies(cookies).get();
	// Elements newsHeadlines = doc.select("a");
	// // System.out.println(doc);
	// List<String> tmpList = new ArrayList<String>();
	// for (int index = 0; index < newsHeadlines.size(); index++) {
	// String text = newsHeadlines.get(index).text();
	// // System.out.println(text);
	// String stockCode = text.split(" ")[0];
	// // System.out.println(stockCode);
	// tmpList.add(stockCode);
	// }
	// list.removeAll(tmpList);
	// list.addAll(tmpList);
	//
	// }
	// Collections.sort(list);
	// return list;
	// }

	public static StockName getStockName(Calendar calendar, String stockCode) throws IOException {
		String yyyyMM = DATE_FORMAT.format(calendar.getTime());
		String url = String.format(URL, yyyyMM, yyyyMM, stockCode);

		InputStream openStream = null;
		InputStreamReader reader = null;
		CSVReader<String[]> csvPersonReader = null;

		try {
			openStream = new URL(url).openStream();

			reader = new InputStreamReader(openStream, CHARSET);

			csvPersonReader = new CSVReaderBuilder<String[]>(reader).strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new DefaultCSVEntryParser()).build();

			List<String[]> stockList = csvPersonReader.readAll();
			for (String[] row : stockList) {
				StockName stockName = new StockName();
				stockName.setCode(stockCode);
				stockName.setKind(0);
				String tmpName = row[0].replace("各日成交資訊(元,股)", "");
				tmpName = tmpName.substring(tmpName.indexOf(stockCode) + stockCode.length()).trim();
//				tmpName = new String(tmpName.getBytes(CHARSET), "UTF8");
				stockName.setName(tmpName);
				return stockName;
			}
		} catch (Exception e) {
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

		return null;
	}

	public static List<Stock> getStockList(Calendar calendar, String stockCode) throws IOException {
		String yyyyMM = DATE_FORMAT.format(calendar.getTime());
		String url = String.format(URL, yyyyMM, yyyyMM, stockCode);

		InputStream openStream = null;
		InputStreamReader reader = null;
		CSVReader<String[]> csvPersonReader = null;

		List<Stock> list = new ArrayList<Stock>();
		try {
			openStream = new URL(url).openStream();

			reader = new InputStreamReader(openStream, CHARSET);

			csvPersonReader = new CSVReaderBuilder<String[]>(reader).strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new DefaultCSVEntryParser()).build();

			List<String[]> stockList = csvPersonReader.readAll();
			for (int index = 0; index < stockList.size(); index++) {
				String[] row = stockList.get(index);
				try {
					Stock stock = new Stock();

					stock.setCode(stockCode);

					String date = String.valueOf(Integer.valueOf(row[0].trim().replace("/", "")) + 19110000);
					stock.setDate(date); // 日期

					Double floor = Math.floor(Integer.valueOf(row[1].replace(",", "")) / 1000);
					stock.setVolumn(floor.intValue()); // 成交股數

					stock.setOpen(Double.valueOf(row[3].replace(",", ""))); // 開盤
					stock.setHigh(Double.valueOf(row[4].replace(",", ""))); // 最高
					stock.setLow(Double.valueOf(row[5].replace(",", ""))); // 最低
					stock.setClose(Double.valueOf(row[6].replace(",", ""))); // 收盤

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
