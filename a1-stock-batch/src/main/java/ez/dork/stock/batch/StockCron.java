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
import ez.dork.stock.domain.StockName;
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
			insertGovStock(calendar);
			insertOrgStock(calendar);
			analysisCron.analysisStock();
		}
	}

	private void insertOrgStock(Calendar calendar) {
		List<String[]> rowList = OrgStockUtil.getRowList(calendar);
		List<Stock> stockList = OrgStockUtil.getStockList(calendar, rowList);
		List<StockName> stockNameList = OrgStockUtil.getStockNameList(rowList);
		for (Stock stock : stockList) {
			try {
				stockService.insert(stock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (StockName stockName : stockNameList) {
			try {
				stockService.insert(stockName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void insertGovStock(Calendar calendar) {
		List<String[]> rowList = GovStockUtil.getRowList(calendar);
		List<Stock> stockList = GovStockUtil.getStockList(calendar, rowList);
		List<StockName> stockNameList = GovStockUtil.getStockNameList(rowList);
		for (Stock stock : stockList) {
			try {
				stockService.insert(stock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (StockName stockName : stockNameList) {
			try {
				stockService.insert(stockName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
