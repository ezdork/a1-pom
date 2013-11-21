package ez.dork.stock.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ez.dork.stock.domain.EarnMoney;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.Strategy;
import ez.dork.stock.mapper.StockMapper;
import ez.dork.stock.mapper.StrategyMapper;
import ez.dork.stock.service.StockService;

@Service
public class StockServiceImpl implements StockService {

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private StockMapper stockMapper;
	@Autowired
	private StrategyMapper strategyMapper;

	@Override
	public int insert(Stock stock) {
		return stockMapper.insert(stock);
	}

	@Override
	public List<Stock> selectHeighestStockList(Calendar calendar, Integer howManyYears) {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		String beginDate = null;
		String endDate = DATE_FORMAT.format(calendar.getTime());
		if (howManyYears != null) {
			calendar.add(Calendar.YEAR, -howManyYears);
			beginDate = DATE_FORMAT.format(calendar.getTime());
		}
		return stockMapper.selectHighestStockList(beginDate, endDate);
	}

	@Override
	public List<Stock> selectByCode(String code) {
		return stockMapper.selectByCode(code);
	}

	@Override
	public List<String> selectGroupByCode() {
		return stockMapper.selectGroupByCode();
	}

	@Override
	public int insert(Strategy strategy) {
		return strategyMapper.insert(strategy);
	}

	@Override
	public List<Strategy> getStrategyList(String code) {
		return strategyMapper.getStrategyList(code);
	}

	@Override
	public List<EarnMoney> getAllStockOrderByEarnMoney() {
		return strategyMapper.getAllStockOrderByEarnMoney();
	}

	@Override
	public void truncate() {
		strategyMapper.truncate();
	}

	@Override
	public void update(Strategy strategy) {
		strategyMapper.updateByPrimaryKey(strategy);
	}

	@Override
	public Double getHighestPrice(String code, Calendar calendar, Integer howManyYears) {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		String beginDate = null;
		String endDate = DATE_FORMAT.format(calendar.getTime());
		if (howManyYears != null) {
			calendar.add(Calendar.YEAR, -howManyYears);
			beginDate = DATE_FORMAT.format(calendar.getTime());
		}
		Double result = stockMapper.getHighestPrice(code, beginDate, endDate);
		if (result != null) {
			return result;
		} else {
			return 0d;
		}
	}

	@Override
	public String getLatestStockDate() {
		return stockMapper.getLatestStockDate();
	}

	@Override
	public List<Strategy> selectCurrentBuyList(String date) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DATE_FORMAT.parse(date));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return strategyMapper.selectCurrentBuyList(date, DATE_FORMAT.format(calendar.getTime()));
	}

	@Override
	public List<Stock> selectLast5(String code, String date) {
		return stockMapper.selectLast5(code, date);
	}
}
