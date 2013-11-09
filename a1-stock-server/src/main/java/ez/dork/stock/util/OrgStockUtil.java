package ez.dork.stock.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ez.dork.stock.domain.Stock;

/**
 * 上櫃股票
 * 
 * @author tim
 * 
 */
public class OrgStockUtil {
	private static final String KIND_URL = "http://www.otc.org.tw/ch/inc/stksum_twce.php";
	private static final String URL = "http://www.otc.org.tw/ch/stock/aftertrading/daily_trading_info/download_st43.php";

	public static List<String> getCodeList() throws IOException {
		Document doc = Jsoup.connect(KIND_URL).get();
		Elements newsHeadlines = doc.select("a");

		List<String> list = new ArrayList<String>();
		for (int index = 0; index < newsHeadlines.size(); index++) {
			list.add(newsHeadlines.get(index).text().split(" ")[0]);
		}
		return list;
	}

	public static List<Stock> getStockList(Calendar calendar, String stockCode)
			throws IOException {
		Map<String, String> data = new HashMap<String, String>();
		String yy = String.valueOf(calendar.get(Calendar.YEAR));
		String mm = String.valueOf(calendar.get(Calendar.MONTH));
		data.put("yy", yy);
		data.put("mm", mm);
		data.put("stk_no", stockCode);
		Document doc = Jsoup.connect(URL).data(data).ignoreContentType(true)
				.post();
		String text = doc.text();
		String[] split = text.replace("\"", "").split(" ");

		List<Stock> list = new ArrayList<Stock>();
		for (int i = 5; i < split.length; i++) {
			String line = split[i];
			if (line.contains("--")) {
				continue;
			}
			String[] split2 = line.split(",");
			Stock stock = new Stock();
			stock.setCode(stockCode);
			String date = String.valueOf(Integer.valueOf(split2[0]) + 19000000);
			stock.setDate(date); // 日期
			stock.setVolumn(Integer.valueOf(split2[1])); // 成交仟股

			stock.setOpen(Double.valueOf(split2[3])); // 開盤
			stock.setHeigh(Double.valueOf(split2[4])); // 最高
			stock.setLow(Double.valueOf(split2[5])); // 最低
			stock.setClose(Double.valueOf(split2[6])); // 收盤
			list.add(stock);
		}
		return list;
	}
}
