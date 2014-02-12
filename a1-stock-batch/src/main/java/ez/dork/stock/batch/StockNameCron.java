package ez.dork.stock.batch;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ez.dork.stock.queue.StockQueue;
import ez.dork.stock.service.StockService;
import ez.dork.stock.thread.StockNameThread;

@Component
public class StockNameCron {

	public static LinkedBlockingQueue<StockQueue> STOCK_NAME_QUEUE = new LinkedBlockingQueue<StockQueue>();

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private StockService stockService;

	@Scheduled(cron = "0 30 17 * * ?")
	public void getStockName() throws IOException {
		getStockName(null);
	}

	public void getStockName(String wantKnowStockCode) throws IOException {

		if (wantKnowStockCode != null) {

		} else {
			List<String> stockCodeList = stockService.selectGroupByCode();
			for (String stockCode : stockCodeList) {
				STOCK_NAME_QUEUE.offer(new StockQueue(stockCode.trim(),
						Calendar.getInstance(), 0));
			}
		}

		for (int i = 0; i < 20; i++) {
			StockNameThread stockNameThread = ctx
					.getBean(StockNameThread.class);
			stockNameThread.start();
		}
	}

}
