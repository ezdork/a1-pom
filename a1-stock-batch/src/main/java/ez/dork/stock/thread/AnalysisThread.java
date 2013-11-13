package ez.dork.stock.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ez.dork.stock.batch.Analysis;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.Strategy;
import ez.dork.stock.service.StockService;
import ez.dork.stock.util.PriceUtil;

/**
 * 
 * @author tim
 * 
 */
@Component
@Scope("prototype")
// The bean scope must be “prototype“, so that each request will return a new
// instance, to run each individual thread.
public class AnalysisThread extends Thread {

	boolean alreadyBuy = false;
	private Stock currentStock = null;
	private Stock yesterDayStock = null;
	private Double highest = 0d;
	int notOverHighDays = 0;
	int amount = 0;

	@Autowired
	private StockService stockService;

	@Override
	public void run() {
		while (!Analysis.CODE_QUEUE.isEmpty()) {
			alreadyBuy = false;
			currentStock = null;
			yesterDayStock = null;
			highest = 0d;
			notOverHighDays = 0;
			amount = 0;

			try {
				String code = Analysis.CODE_QUEUE.take();
				List<Stock> stockList = stockService.selectByCode(code);
				for (Stock stock : stockList) {
					currentStock = stock;
					if (currentStock.getHigh() > highest) {
						highest = currentStock.getHigh();
						notOverHighDays = 0;
					} else {
						notOverHighDays++;
					}
					if (yesterDayStock != null) {
						if (!alreadyBuy && wantBuy()) { // TODO 買進日期,價格,手續費

							double buyPrice = currentStock.getHigh();
							amount = amount(buyPrice);

							Strategy strategy = new Strategy();
							strategy.setCode(currentStock.getCode());
							strategy.setDate(currentStock.getDate());
							strategy.setAmount(amount);
							strategy.setPrice(-1 * buyPrice);
							stockService.insert(strategy);

							System.out.println(strategy.getCode() + " : " + strategy.getDate() + ", "
									+ strategy.getPrice());
							alreadyBuy = true;

						} else if (alreadyBuy && wantSell()) { // TODO 賣出...

							Strategy strategy = new Strategy();
							strategy.setCode(currentStock.getCode());
							strategy.setDate(currentStock.getDate());
							strategy.setAmount(amount);
							strategy.setPrice(currentStock.getLow());
							stockService.insert(strategy);

							System.out.println(strategy.getCode() + " : " + strategy.getDate() + ", "
									+ strategy.getPrice());

							alreadyBuy = false;
						}
					}
					yesterDayStock = stock;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("done!");
	}

	private boolean wantBuy() {
		Double highStopPrice = PriceUtil.getNextHighestPrice(yesterDayStock.getClose());
		return highest.compareTo(highStopPrice) == 0 && highest.compareTo(currentStock.getHigh()) == 0;
	}

	private boolean wantSell() {
		boolean result = false;
		if (notOverHighDays == 3) {
			result = true;
			notOverHighDays = 0;
		}
		return result;
	}

	private int amount(Double d) {
		return (int) Math.floor(200 / d);
	}
}
