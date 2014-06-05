package ez.dork.stock.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import ez.dork.stock.domain.Stock;

public class OrgStockUtilTest {

	@Test
	public void testGetStockList() throws IOException {
		Calendar cal = Calendar.getInstance();
		List<Stock> list = OrgStockUtil.getStockList(cal);
		for (Stock stock : list) {
			System.out.println(stock);
		}
	}

}
