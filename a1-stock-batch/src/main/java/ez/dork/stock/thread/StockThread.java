package ez.dork.stock.thread;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import ez.dork.stock.batch.StockCron;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.queue.StockQueue;
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
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private StockService stockService;

	@Override
	public void run() {
		while (!StockCron.STOCK_QUEUE.isEmpty()) {
			try {
				StockQueue queue = StockCron.STOCK_QUEUE.take();
				Calendar calendar = queue.getCalendar();
				String stockCode = queue.getCode();
				int kind = queue.getKind();

				String format = DATE_FORMAT.format(calendar.getTime());
				try {
					if (queue.getEmptyTimes() == 3) {
						System.out.println(String.format("%s : %s kind=>%d emptyTimes=3", stockCode, format, kind));
						continue;
					}

					System.out.println(String.format("%s : %s kind=>%d", stockCode, format, kind));
					List<Stock> stockList = null;
					if (kind == 0) {
						stockList = GovStockUtil.getStockList(calendar, stockCode);
					} else {
						stockList = OrgStockUtil.getStockList(calendar, stockCode);
					}
					if (stockList.isEmpty()) {
						queue.setEmptyTimes(queue.getEmptyTimes() + 1);
						System.err.println(String.format("%s : %s kind=>%d empty", stockCode, format, kind));
						if (kind == 0) { // 上市讀完, 改讀上櫃資料
							// 先加一月 去得上櫃資料(空的三次可以接受)
							Calendar calendar2 = Calendar.getInstance();
							calendar2.setTime(calendar.getTime());
							calendar2.add(Calendar.MONTH, 1);
							StockQueue queue2 = new StockQueue(queue.getCode(), calendar2, 1);
							StockCron.STOCK_QUEUE.add(queue2);
						}
					} else {
						for (int index = stockList.size() - 1; index > -1; index--) {
							Stock stock = stockList.get(index);
							stockService.insert(stock);
						}
						calendar.add(Calendar.MONTH, -1);
						queue.setCalendar(calendar);
						StockCron.STOCK_QUEUE.add(queue);
					}

				} catch (DuplicateKeyException e) {
					System.err.println(String.format("%s : %s kind=>%d already insert", stockCode, format, kind));
				} catch (NumberFormatException e) {
					System.err.println(String.format("%s : %s kind=>%d has no data", stockCode, format, kind));
				} catch (Exception e) {
					e.printStackTrace();
					StockCron.STOCK_QUEUE.add(queue);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("done!");
	}
}
