package ez.dork.stock.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.util.OrgStockUtil;

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
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 7);
		List<Stock> list = OrgStockUtil.getStockList(cal, "5904");
		for (Stock stock : list) {
			System.out.println(stock);
		}
	}

}
