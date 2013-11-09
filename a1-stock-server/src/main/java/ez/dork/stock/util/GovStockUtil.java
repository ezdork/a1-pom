package ez.dork.stock.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ez.dork.stock.domain.Stock;

/**
 * 上市股票
 * @author tim
 *
 */
public class GovStockUtil {
	private static final String URL = "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?genpage=genpage/Report%s/%s_F3_1_8_%s.php&type=html";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMM");

	public static List<Stock> getStockList(Calendar calendar, String stockCode)
			throws IOException {
		String yyyyMM = DATE_FORMAT.format(calendar.getTime());
		String url = String.format(URL, yyyyMM, yyyyMM, stockCode);
		Document doc = Jsoup.connect(url).get();
		Elements newsHeadlines = doc.select("td");

		List<Stock> list = new ArrayList<Stock>();
		Stock stock = null;
		for (int index = 10; index < newsHeadlines.size(); index++) {
			String value = newsHeadlines.get(index).text();
			int mod = index % 9;
			switch (mod) {
			case 1: // 日期
				stock = new Stock();
				stock.setCode(stockCode);
				String date = String.valueOf(Integer.valueOf(value.replace("/",
						"")) + 19110000);
				stock.setDate(date);
				break;
			case 2: // 成交股數
				break;
			case 3: // 成交金額
				break;
			case 4: // 開盤價
				stock.setOpen(Double.valueOf(value));
				break;
			case 5: // 最高價
				stock.setHeigh(Double.valueOf(value));
				break;
			case 6: // 最低價
				stock.setLow(Double.valueOf(value));
				break;
			case 7: // 收盤價
				stock.setClose(Double.valueOf(value));
				break;
			case 8: // 漲跌價差
				break;
			case 0: // 成交筆數
				stock.setVolumn(Integer.valueOf(value));
				list.add(stock);
				break;

			default:
				break;
			}
		}
		return list;
	}
}
