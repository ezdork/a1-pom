package ez.dork.stock.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import ez.dork.stock.domain.Stock;

public class GovStockUtilTest {

	@Test
	public void testGetStockList() throws IOException {
		Calendar calendar = Calendar.getInstance();
		List<Stock> stockList = GovStockUtil.getStockList(calendar);
		for (Stock stock : stockList) {
			System.out.println(stock);
		}
	}

}
