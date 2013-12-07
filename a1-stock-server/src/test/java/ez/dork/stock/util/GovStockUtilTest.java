package ez.dork.stock.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.StockName;

public class GovStockUtilTest {

	@Test
	public void testGetCodeList() throws IOException {
		for (String stockCode : GovStockUtil.getCodeList()) {
			System.out.println(stockCode);
		}
	}

	@Test
	public void testGetStockList() throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2013);
		calendar.set(Calendar.MONTH, 8);
		List<Stock> stockList = GovStockUtil.getStockList(calendar, "9946");
		for (Stock stock : stockList) {
			System.out.println(stock);
		}
	}

	@Test
	public void testGetStockName() throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2013);
		calendar.set(Calendar.MONTH, 8);
		StockName stockName = GovStockUtil.getStockName(calendar, "9946");
		System.out.println(stockName);
	}

}
