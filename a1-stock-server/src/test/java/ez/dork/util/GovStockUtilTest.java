package ez.dork.util;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import ez.dork.stock.util.GovStockUtil;

public class GovStockUtilTest {

	@Test
	public void test() throws IOException {
		GovStockUtil.getStockList(Calendar.getInstance(), "5533");
	}

}
