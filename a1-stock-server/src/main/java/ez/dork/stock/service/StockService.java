package ez.dork.stock.service;

import java.util.Calendar;
import java.util.List;

import ez.dork.stock.domain.Stock;

public interface StockService {
	int insert(Stock stock);

	List<Stock> selectHeighestStockList(Calendar calendar, Integer howManyYears);
}
