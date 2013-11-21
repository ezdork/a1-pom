package ez.dork.stock.batch;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ez.dork.stock.queue.StockQueue;
import ez.dork.stock.service.StockService;
import ez.dork.stock.thread.StockThread;
import ez.dork.stock.util.GovStockUtil;
import ez.dork.stock.util.OrgStockUtil;

@Component
public class StockCron {

	public static LinkedBlockingQueue<StockQueue> STOCK_QUEUE = new LinkedBlockingQueue<StockQueue>();

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private StockService stockService;

	// @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void getStock(String wantScanStockCode) throws IOException {

		if (wantScanStockCode == null) {
			List<String> codeList = GovStockUtil.getCodeList();
			for (String stockCode : codeList) {
				STOCK_QUEUE.offer(new StockQueue(stockCode, Calendar.getInstance(), 0));
			}
			codeList = OrgStockUtil.getCodeList();
			for (String stockCode : codeList) {
				STOCK_QUEUE.offer(new StockQueue(stockCode, Calendar.getInstance(), 1));
			}
		} else {
			STOCK_QUEUE.offer(new StockQueue(wantScanStockCode, Calendar.getInstance(), 0));
		}

		for (int i = 0; i < 20; i++) {
			StockThread stockThread = ctx.getBean(StockThread.class);
			stockThread.start();
		}
	}

}
