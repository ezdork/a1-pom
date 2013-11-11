package ez.dork.stock.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.mapper.StockMapper;
import ez.dork.stock.service.StockService;

@Service
public class StockServiceImpl implements StockService {

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private StockMapper stockMapper;

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
		return stockMapper.selectHeighestStockList(beginDate, endDate);
	}

}
