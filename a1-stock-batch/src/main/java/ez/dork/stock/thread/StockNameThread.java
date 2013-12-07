package ez.dork.stock.thread;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import ez.dork.stock.batch.StockNameCron;
import ez.dork.stock.domain.StockName;
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
public class StockNameThread extends Thread {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private StockService stockService;

	@Override
	public void run() {
		while (!StockNameCron.STOCK_NAME_QUEUE.isEmpty()) {
			StockQueue queue = StockNameCron.STOCK_NAME_QUEUE.poll();
			Calendar calendar = queue.getCalendar();
			String stockCode = queue.getCode();
			int kind = queue.getKind();

			String formatDate = DATE_FORMAT.format(calendar.getTime());
			String msg = String.format("%s : %s kind=>%d", stockCode, formatDate, kind);
			try {

				System.out.println(msg);
				StockName stockName = null;
				if (kind == 0) {
					stockName = GovStockUtil.getStockName(calendar, stockCode);
					if (stockName == null) {
						queue.setKind(kind == 0 ? 1 : 0);
						StockNameCron.STOCK_NAME_QUEUE.offer(queue);
						continue;
					}
				} else {
					stockName = OrgStockUtil.getStockName(calendar, stockCode);
				}
				if (stockName != null) {
					stockService.insert(stockName);
				}

			} catch (DuplicateKeyException e) {
				System.err.println(String.format("%s already insert", msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("done!");
	}
}
