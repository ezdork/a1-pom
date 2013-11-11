package ez.dork.stock.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.service.StockService;

public class StockServiceImplTest {
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
		StockService stockService = ctx.getBean(StockService.class);

		Calendar calendar = Calendar.getInstance();
		Integer howManyYears = null;
		List<Stock> resultList = stockService.selectHeighestStockList(calendar, howManyYears);
		String date = DATE_FORMAT.format(calendar.getTime());
		System.out.println(String.format("==== %s ====", date));
		System.out.println("code, close");
		for (Stock stock : resultList) {
			System.out.println(String.format("%s, %.2f", stock.getCode(), stock.getClose()));
		}
	}
}
