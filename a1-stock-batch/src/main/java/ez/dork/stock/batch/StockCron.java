package ez.dork.stock.batch;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ez.dork.stock.queue.StockQueue;
import ez.dork.stock.service.StockService;

@Component
public class StockCron {

	public static LinkedBlockingQueue<StockQueue> STOCK_QUEUE = new LinkedBlockingQueue<StockQueue>();

	@Autowired
	private StockService stockService;

	@Autowired
	private AnalysisCron analysisCron;

	@Scheduled(cron = "0 0 17 * * ?")
	public void getStock() throws IOException {
		if (AnalysisCron.CODE_QUEUE.isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			stockService.insertGovStock(calendar);
			stockService.insertOrgStock(calendar);
			analysisCron.analysisStock();
		}
	}

}
