package ez.dork.stock.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.util.GovStockUtil;

public class GovStockUtilTest {

	@Test
	public void testGetCodeList() throws IOException {
		for (String stockCode : GovStockUtil.getCodeList()) {
			System.out.println(stockCode);
		}
	}

	@Test
	public void testGetStockList() throws IOException {
		List<Stock> stockList = GovStockUtil.getStockList(Calendar.getInstance(), "5533");
		for (Stock stock : stockList) {
			System.out.println(stock);
		}
	}

}
