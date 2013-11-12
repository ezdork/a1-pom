package ez.dork.stock.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;

import ez.dork.stock.domain.Stock;

/**
 * 上櫃股票
 * 
 * @author tim
 * 
 */
public class OrgStockUtil {

	private static final SimpleDateFormat YY_FORMAT = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat MM_FORMAT = new SimpleDateFormat("MM");

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

	public static List<Stock> getStockList(Calendar calendar, String stockCode) throws IOException {
		String yy = YY_FORMAT.format(calendar.getTime());
		String mm = MM_FORMAT.format(calendar.getTime());

		InputStream openStream = null;
		InputStreamReader reader = null;
		CSVReader<String[]> csvPersonReader = null;

		List<Stock> list = new ArrayList<Stock>();
		try {
			String charset = "BIG5";
			String query = String.format("yy=%s&mm=%s&stk_no=%s", URLEncoder.encode(yy, charset),
					URLEncoder.encode(mm, charset), URLEncoder.encode(stockCode, charset));
			String url = String.format("%s?%s", URL, query);

			URLConnection connection = new URL(url).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			OutputStream output = connection.getOutputStream();
			try {
				output.write(query.getBytes(charset));
			} finally {
				try {
					output.close();
				} catch (IOException logOrIgnore) {
				}
			}
			openStream = connection.getInputStream();

			reader = new InputStreamReader(openStream, charset);

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

					stock.setVolumn(Integer.valueOf(row[1].replace(",", ""))); // 成交仟股

					stock.setOpen(Double.valueOf(row[3].replace(",", ""))); // 開盤
					stock.setHigh(Double.valueOf(row[4].replace(",", ""))); // 最高
					stock.setLow(Double.valueOf(row[5].replace(",", ""))); // 最低
					stock.setClose(Double.valueOf(row[6].replace(",", ""))); // 收盤

					list.add(stock);
				} catch (Exception e) {
				}
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

		return list;
	}
}
