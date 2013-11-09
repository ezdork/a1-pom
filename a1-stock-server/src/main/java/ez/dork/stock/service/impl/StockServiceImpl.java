package ez.dork.stock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.mapper.StockMapper;
import ez.dork.stock.service.StockService;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockMapper stockMapper;

	@Override
	public int insert(Stock stock) {
		return stockMapper.insert(stock);
	}

}
