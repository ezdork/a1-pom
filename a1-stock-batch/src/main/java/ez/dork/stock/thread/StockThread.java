package ez.dork.stock.thread;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import ez.dork.stock.batch.Cron;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.queue.StockQueue;
import ez.dork.stock.service.StockService;
import ez.dork.stock.util.GovStockUtil;
import ez.dork.stock.util.OrgStockUtil;

@Component
public class StockThread extends Thread {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private StockService stockService;

	@Override
	public void run() {
		while (true) {
			try {
				StockQueue queue = Cron.STOCK_QUEUE.take();
				Calendar calendar = queue.getCalendar();
				String stockCode = queue.getCode();
				int kind = queue.getKind();

				try {
					System.out.println(String.format("%s : %s kind=>%d", stockCode,
							DATE_FORMAT.format(calendar.getTime()), kind));
					List<Stock> stockList = null;
					if (kind == 0) {
						stockList = GovStockUtil.getStockList(calendar, stockCode);
					} else {
						stockList = OrgStockUtil.getStockList(calendar, stockCode);
					}
					if (stockList.isEmpty()) {
						System.err.println(String.format("%s : empty %s kind=>%d", stockCode,
								DATE_FORMAT.format(calendar.getTime()), kind));
					} else {
						for (int index = stockList.size() - 1; index > 0; index--) {
							Stock stock = stockList.get(index);
							stockService.insert(stock);
						}
						calendar.add(Calendar.MONTH, -1);
						queue.setCalendar(calendar);
						Cron.STOCK_QUEUE.add(queue);
					}

				} catch (DuplicateKeyException e) {
					System.err.println(String.format("%s : already insert %s kind=>%d", stockCode,
							DATE_FORMAT.format(calendar.getTime()), kind));
				} catch (NumberFormatException e) {
					System.err.println(String.format("%s : has no data %s kind=>%d", stockCode,
							DATE_FORMAT.format(calendar.getTime()), kind));
				} catch (Exception e) {
					e.printStackTrace();
					Cron.STOCK_QUEUE.add(queue);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (Cron.STOCK_QUEUE.isEmpty()) {
				break;
			}
		}
		System.out.println("done!");
	}
}
