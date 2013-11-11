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

//		List<Stock> resultList1 = stockService.selectHeighestStockList(Calendar.getInstance(), 1);
//		printStockList(resultList1);
//		List<Stock> resultList2 = stockService.selectHeighestStockList(Calendar.getInstance(), 2);
//		printStockList(resultList2);
//		List<Stock> resultList3 = stockService.selectHeighestStockList(Calendar.getInstance(), 3);
//		printStockList(resultList3);
//		List<Stock> resultList5 = stockService.selectHeighestStockList(Calendar.getInstance(), 5);
//		printStockList(resultList5);
//		List<Stock> resultList10 = stockService.selectHeighestStockList(Calendar.getInstance(), 10);
//		printStockList(resultList10);
		List<Stock> resultListAll = stockService.selectHeighestStockList(Calendar.getInstance(), null);
		printStockList(resultListAll);
//
//		resultList1.removeAll(resultList2);
//		resultList1.removeAll(resultList3);
//		resultList1.removeAll(resultList5);
//		resultList1.removeAll(resultList10);
//		resultList1.removeAll(resultListAll);
//
//		printStockList(resultListAll);
	}
	
	private static void printStockList(List<Stock> resultList) {
		System.out.println(String.format("==== %s ====", resultList.get(0).getDate()));
		System.out.println("code, close");
		for (Stock stock : resultList) {
			System.out.println(String.format("%s, %.2f", stock.getCode(), stock.getClose()));
		}
	}
}
