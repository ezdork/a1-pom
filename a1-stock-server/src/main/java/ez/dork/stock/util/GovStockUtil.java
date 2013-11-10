package ez.dork.stock.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ez.dork.stock.domain.Stock;

/**
 * 上市股票
 * 
 * @author tim
 * 
 */
public class GovStockUtil {
	private static final String KIND_ROOT_URL = "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/data2.php";
	private static final String KIND_URL = "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/STK_words.php?STK_NAME=%d";

	private static final String CATEGORY_ROOT_URL = "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/data_Top.htm";
	private static final String CATEGORY_URL = "http://www.twse.com.tw/ch/trading/inc/STKCHOICE/STK%s.php?STK=%s";

	private static final String URL = "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report%s/%s_F3_1_8_%s.php&type=csv";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMM");

	public static List<String> getCodeList() throws IOException {
		List<String> list = new ArrayList<String>();
		InputStream openStream = null;
		try {
			openStream = new URL(CATEGORY_ROOT_URL).openStream();
			Document document = Jsoup.parse(openStream, "Big5", CATEGORY_ROOT_URL);
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
			Document doc = Jsoup.parse(openStream, "Big5", url);
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

	private static List<String> getAllStockList() throws IOException {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("STK_NAME", String.valueOf(i));
			data.put("STK", "");
			data.put("INDEX_NAME", "");
			Connection.Response res = Jsoup.connect(KIND_ROOT_URL).data(data).method(Method.POST).execute();

			Map<String, String> cookies = res.cookies();

			String url = String.format(KIND_URL, i);

			// Document doc = Jsoup.parse(new URL(url).openStream(), "Big5",
			// url);
			Document doc = Jsoup.connect(url).cookies(cookies).get();
			Elements newsHeadlines = doc.select("a");
			// System.out.println(doc);
			List<String> tmpList = new ArrayList<String>();
			for (int index = 0; index < newsHeadlines.size(); index++) {
				String text = newsHeadlines.get(index).text();
				// System.out.println(text);
				String stockCode = text.split(" ")[0];
				// System.out.println(stockCode);
				tmpList.add(stockCode);
			}
			list.removeAll(tmpList);
			list.addAll(tmpList);

		}
		Collections.sort(list);
		return list;
	}

	public static List<Stock> getStockList(Calendar calendar, String stockCode) throws IOException {
		String yyyyMM = DATE_FORMAT.format(calendar.getTime());
		String url = String.format(URL, yyyyMM, yyyyMM, stockCode);
		Document doc = Jsoup.connect(url).ignoreContentType(true).get();

		String text = doc.text();
		String[] split = text.split(" ");

		List<Stock> list = new ArrayList<Stock>();
		for (int i = 5; i < split.length; i++) {

			String line = split[i];
			if (line.contains("--")) {
				continue;
			}

			String[] split2 = line.split(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");
			Stock stock = new Stock();
			stock.setCode(stockCode);
			String date = String.valueOf(Integer.valueOf(split2[0].replace("/", "")) + 19110000);
			stock.setDate(date); // 日期

			Double floor = Math.floor(Integer.valueOf(split2[1].replaceAll("[,\"]", "")) / 1000);
			stock.setVolumn(floor.intValue()); // 成交股數

			stock.setOpen(Double.valueOf(split2[3].replace("[,\"]", ""))); // 開盤
			stock.setHeigh(Double.valueOf(split2[4].replace("[,\"]", ""))); // 最高
			stock.setLow(Double.valueOf(split2[5].replace("[,\"]", ""))); // 最低
			stock.setClose(Double.valueOf(split2[6].replace("[,\"]", ""))); // 收盤

			list.add(stock);
		}
		return list;
	}
}
