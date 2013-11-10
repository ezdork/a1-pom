package ez.dork.stock.batch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.service.StockService;
import ez.dork.stock.util.GovStockUtil;
import ez.dork.stock.util.OrgStockUtil;

@Component
public class Cron {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private StockService stockService;

	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void getGovStock() throws IOException {
		List<String> codeList = GovStockUtil.getCodeList();
		for (String stockCode : codeList) {
			Calendar calendar = Calendar.getInstance();

			while (true) {
				try {

					System.out.println("Gov " + stockCode + " : " + DATE_FORMAT.format(calendar.getTime()));
					List<Stock> stockList = GovStockUtil.getStockList(calendar, stockCode);
					if (stockList.isEmpty()) {
						System.err.println("Gov " + stockCode + " : empty " + DATE_FORMAT.format(calendar.getTime()));
						break;
					}
					for (int index = stockList.size() - 1; index > 0; index--) {
						Stock stock = stockList.get(index);
						stockService.insert(stock);
					}
					calendar.add(Calendar.MONTH, -1);

				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}

	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void getOrgStock() throws IOException {
		List<String> codeList = OrgStockUtil.getCodeList();
		for (String stockCode : codeList) {
			Calendar calendar = Calendar.getInstance();

			while (true) {
				try {

					System.out.println(stockCode + " : " + DATE_FORMAT.format(calendar.getTime()));
					List<Stock> stockList = OrgStockUtil.getStockList(calendar, stockCode);
					if (stockList.isEmpty()) {
						System.err.println(stockCode + " : empty " + DATE_FORMAT.format(calendar.getTime()));
						break;
					}
					for (int index = stockList.size() - 1; index > 0; index--) {
						Stock stock = stockList.get(index);
						stockService.insert(stock);
					}
					calendar.add(Calendar.MONTH, -1);

				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
}
