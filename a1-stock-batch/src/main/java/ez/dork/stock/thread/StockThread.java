package ez.dork.stock.thread;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.service.StockService;
import ez.dork.stock.util.GovStockUtil;
import ez.dork.stock.util.OrgStockUtil;

/**
 * 
 * @author tim
 * 
 */
@Component
@Scope("prototype")
// The bean scope must be “prototype“, so that each request will return a new
// instance, to run each individual thread.
public class StockThread extends Thread {

	@Autowired
	private StockService stockService;

	@Override
	public void run() {
		List<Stock> stockList;
		try {
			stockList = OrgStockUtil.getStockList(Calendar.getInstance());
			for (Stock stock : stockList) {
				stockService.insert(stock);
			}
			stockList = GovStockUtil.getStockList(Calendar.getInstance());
			for (Stock stock : stockList) {
				stockService.insert(stock);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
