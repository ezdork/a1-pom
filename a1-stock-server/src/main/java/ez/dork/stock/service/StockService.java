package ez.dork.stock.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import ez.dork.stock.domain.EarnMoney;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.StockName;
import ez.dork.stock.domain.Strategy;

public interface StockService {
	int insert(Stock stock);

	List<Stock> selectHeighestStockList(Calendar calendar, Integer howManyYears);

	List<String> selectGroupByCode();

	List<Stock> selectByCode(String code);

	int insert(Strategy strategy);

	List<Strategy> getStrategyList(String code);

	List<EarnMoney> getAllStockOrderByEarnMoney();

	void truncate();

	void update(Strategy strategy);

	Double getHighestPrice(String code, Calendar calendar, Integer howManyYears);

	String getLatestStockDate();

	List<Strategy> selectCurrentBuyList(String date) throws ParseException;
	
	List<Stock> selectLast5(String code, String date);

	int insert(StockName stockName);

	List<StockName> selectAllStockName();

	void insertOrgStock(Calendar calendar);

	void insertGovStock(Calendar calendar);

}
