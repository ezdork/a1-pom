package ez.dork.stock.batch;

import java.io.IOException;
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

	@Autowired
	private StockService stockService;

//	@Scheduled(cron = "*/3 * * * * ?")
	public void getGovStock() throws IOException {
		// List<Stock> resultList = GovStockUtil.getStockList(
		// Calendar.getInstance(), "5533");
		//
		// for (Stock map : resultList) {
		// System.out.println(map);
		// }
	}

	@Scheduled(fixedDelay = 1)
	public void getOrgStock() throws IOException {
		List<String> codeList = OrgStockUtil.getCodeList();
		for (String stockCode : codeList) {
			System.out.println(stockCode);
			try {
				Calendar calendar = Calendar.getInstance();
				List<Stock> stockList = OrgStockUtil.getStockList(calendar,
						stockCode);
				for (int index = stockList.size() - 1; index > 0; index--) {
					Stock stock = stockList.get(index);
					stockService.insert(stock);
				}
				calendar.add(Calendar.MONTH, -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
