package ez.dork.stock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ez.dork.stock.service.StockService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:/Users/tim/Documents/workspace/a1-pom/a1-stock-war/src/main/webapp/WEB-INF/spring/servlet-context.xml" })
public class GetStockTest {

	private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(
			"yyyy-MM-dd");

	@Autowired
	private StockService service;

	@Test
	public void test() {
//		Calendar from = Calendar.getInstance();
//		from.set(2014, 8, 1);
//
//		Calendar to = Calendar.getInstance();
//		to.set(2014, 8, 1);
//		while (to.compareTo(from) >= 0) {
//			long start = new Date().getTime();
//			service.insertGovStock(to);
//			service.insertOrgStock(to);
//
//			long end = new Date().getTime();
//			System.out.println(yyyyMMdd.format(to.getTime()) + " : "
//					+ (end - start));
//			to.add(Calendar.DAY_OF_MONTH, -1);
//		}

	}
}
