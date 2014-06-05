package ez.dork.stock.batch;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.queue.StockQueue;
import ez.dork.stock.service.StockService;
import ez.dork.stock.util.GovStockUtil;
import ez.dork.stock.util.OrgStockUtil;

@Component
public class StockCron {

	public static LinkedBlockingQueue<StockQueue> STOCK_QUEUE = new LinkedBlockingQueue<StockQueue>();

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private StockService stockService;

	@Autowired
	private AnalysisCron analysisCron;

	@Scheduled(cron = "0 0 17 * * ?")
	public void getStock() throws IOException {
		if (AnalysisCron.CODE_QUEUE.isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			List<Stock> stockList = GovStockUtil.getStockList(calendar);
			for (Stock stock : stockList) {
				stockService.insert(stock);
			}
			List<Stock> stockList2 = OrgStockUtil.getStockList(calendar);
			for (Stock stock : stockList2) {
				stockService.insert(stock);
			}
			analysisCron.analysisStock();
		}
	}

}
