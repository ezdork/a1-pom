package ez.dork.stock.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.service.StockService;

public class StockServiceImplTest {
	// private static SimpleDateFormat DATE_FORMAT = new
	// SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
		StockService stockService = ctx.getBean(StockService.class);

		List<Stock> resultList1 = stockService.selectHeighestStockList(getWantedCalendar(), 1);
		List<Stock> resultList2 = stockService.selectHeighestStockList(getWantedCalendar(), 2);
		List<Stock> resultList3 = stockService.selectHeighestStockList(getWantedCalendar(), 3);
		List<Stock> resultList5 = stockService.selectHeighestStockList(getWantedCalendar(), 5);
		List<Stock> resultList10 = stockService.selectHeighestStockList(getWantedCalendar(), 10);
		List<Stock> resultListAll = stockService.selectHeighestStockList(getWantedCalendar(), null);

		resultList1.removeAll(resultList2);
		resultList1.removeAll(resultList3);
		resultList1.removeAll(resultList5);
		resultList1.removeAll(resultList10);
		resultList1.removeAll(resultListAll);

		resultList2.removeAll(resultList3);
		resultList2.removeAll(resultList5);
		resultList2.removeAll(resultList10);
		resultList2.removeAll(resultListAll);

		resultList3.removeAll(resultList5);
		resultList3.removeAll(resultList10);
		resultList3.removeAll(resultListAll);

		resultList5.removeAll(resultList10);
		resultList5.removeAll(resultListAll);

		resultList10.removeAll(resultListAll);

		printStockList(resultList1, "接近1年新高");
		printStockList(resultList2, "接近2年新高");
		printStockList(resultList3, "接近3年新高");
		printStockList(resultList5, "接近5年新高");
		printStockList(resultList10, "接近10年新高");
		printStockList(resultListAll, "接近歷史新高");

	}

	private static Calendar getWantedCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2013);
		calendar.set(Calendar.MONTH, 10);
		calendar.set(Calendar.DAY_OF_MONTH, 15);
		return calendar;
	}

	private static void printStockList(List<Stock> resultList, String msg) {
		System.out.println();
		System.out.println(String.format("==== %s %s 數量:%d====", resultList.get(0).getDate(), msg, resultList.size()));
		System.out.println("code, close");
		for (Stock stock : resultList) {
			System.out.println(String.format("%s, %.2f", stock.getCode(), stock.getClose()));
		}
	}
}
