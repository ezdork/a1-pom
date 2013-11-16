package ez.dork.stock.service;

import java.util.Calendar;
import java.util.List;

import ez.dork.stock.domain.EarnMoney;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.Strategy;

public interface StockService {
	int insert(Stock stock);

	List<Stock> selectHeighestStockList(Calendar calendar, Integer howManyYears);

	List<String> selectGroupByCode();

	List<Stock> selectByCode(String code);

	int insert(Strategy strategy);

	List<Strategy> getStrategyList(String code);
	
	List<EarnMoney> getAllStockOrderByEarnMoney();
	
}
