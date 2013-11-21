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
			StockQueue queue = StockCron.STOCK_QUEUE.poll();
			Calendar calendar = queue.getCalendar();
			String stockCode = queue.getCode();
			int kind = queue.getKind();

			String formatDate = DATE_FORMAT.format(calendar.getTime());
			String msg = String.format("%s : %s kind=>%d", stockCode, formatDate, kind);
			try {

				System.out.println(msg);
				List<Stock> stockList = null;
				if (kind == 0) {
					stockList = GovStockUtil.getStockList(calendar, stockCode);
				} else {
					stockList = OrgStockUtil.getStockList(calendar, stockCode);
				}
				if (stockList.isEmpty()) {
					queue.setEmptyTimes(queue.getEmptyTimes() + 1);

					if (queue.getEmptyTimes() > 3) {
						System.err.println(String.format("%s emptyTimes=>%d", msg, queue.getEmptyTimes()));
						continue;
					} else {
						System.err.println(String.format("%s empty", msg));
					}

					if (kind == 0) { // 上市讀完, 改讀上櫃資料
						// 先加一月 去得上櫃資料(空的三次可以接受)
						Calendar calendar2 = Calendar.getInstance();
						calendar2.setTime(calendar.getTime());
						calendar2.add(Calendar.MONTH, 1);
						StockQueue queue2 = new StockQueue(queue.getCode(), calendar2, 1);
						StockCron.STOCK_QUEUE.offer(queue2);
					}
				} else {
					queue.setEmptyTimes(0);
					for (int index = stockList.size() - 1; index > -1; index--) {
						Stock stock = stockList.get(index);
						stockService.insert(stock);
					}
				}
				calendar.add(Calendar.MONTH, -1);
				StockCron.STOCK_QUEUE.offer(queue);

			} catch (DuplicateKeyException e) {
				System.err.println(String.format("%s already insert", msg));
			} catch (NumberFormatException e) {
				System.err.println(String.format("%s has no data", msg));
			} catch (Exception e) {
				e.printStackTrace();
				StockCron.STOCK_QUEUE.offer(queue);
			}
		}
		System.out.println("done!");
	}
}
