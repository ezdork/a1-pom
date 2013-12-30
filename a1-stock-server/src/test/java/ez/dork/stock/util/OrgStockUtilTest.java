package ez.dork.stock.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.StockName;

public class OrgStockUtilTest {

	@Test
	public void testGetCodeList() throws IOException {
		List<String> codeList = OrgStockUtil.getCodeList();
		for (String code : codeList) {
			System.out.println(code);
		}
	}

	@Test
	public void testGetStockList() throws IOException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 11); // 11 => 表示12月
		List<Stock> list = OrgStockUtil.getStockList(cal, "3523");
		for (Stock stock : list) {
			System.out.println(stock);
		}
	}
	
	@Test
	public void testGetStockName() throws IOException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 7);
		StockName stockName = OrgStockUtil.getStockName(cal, "5904");
		System.out.println(stockName);
	}

}
