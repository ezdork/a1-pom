package ez.dork.stock.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ez.dork.stock.domain.StockName;
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

		if (wantScanStockCode != null) {
			STOCK_QUEUE.offer(new StockQueue(wantScanStockCode, Calendar.getInstance(), 0));
		} else {

			List<StockName> selectAllStockName = stockService.selectAllStockName();
			List<String> codeList = GovStockUtil.getCodeList();
			List<String> codeList2 = OrgStockUtil.getCodeList();

			List<String> alreadyAddCodeList = new ArrayList<String>();
			for (StockName stockName : selectAllStockName) {
				String code = stockName.getCode().trim();
				STOCK_QUEUE.offer(new StockQueue(code, Calendar.getInstance(), stockName.getKind()));
				alreadyAddCodeList.add(code);
			}
			codeList.removeAll(alreadyAddCodeList);
			for (String stockCode : codeList) {
				System.out.println(stockCode);
				STOCK_QUEUE.offer(new StockQueue(stockCode, Calendar.getInstance(), 0));
			}
			
			codeList2.removeAll(alreadyAddCodeList);
			for (String stockCode : codeList2) {
				System.out.println(stockCode);
				STOCK_QUEUE.offer(new StockQueue(stockCode, Calendar.getInstance(), 1));
			}
		}

		int num = 20;
		StockThread[] stockThreadArray = new StockThread[num];
		for (int i = 0; i < num; i++) {
			StockThread stockThread = ctx.getBean(StockThread.class);
			stockThreadArray[i] = stockThread;
			stockThread.start();
		}
		for (int i = 0; i < num; i++) {
			try {
				stockThreadArray[i].join();
			} catch (InterruptedException e) {
			}
		}
	}
}
