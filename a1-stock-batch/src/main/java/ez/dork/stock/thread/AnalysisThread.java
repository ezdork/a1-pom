package ez.dork.stock.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ez.dork.stock.batch.AnalysisCron;
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
	int amount = 0;

	private Double[] high240 = new Double[240];
	private Double[] ma5 = new Double[5];
	
	private Strategy strategy = null;

	@Autowired
	private StockService stockService;

	@Override
	public void run() {
		while (!AnalysisCron.CODE_QUEUE.isEmpty()) {
			alreadyBuy = false;
			currentStock = null;
			yesterDayStock = null;
			highest = 0d;
			amount = 0;

			high240 = new Double[240];
			ma5 = new Double[5];

			try {
				String code = AnalysisCron.CODE_QUEUE.take();
				List<Stock> stockList = stockService.selectByCode(code);
				int i = 0;
				for (Stock stock : stockList) {
					i++;
					currentStock = stock;
					if (currentStock.getHigh().compareTo(highest) > 0) {
						highest = currentStock.getHigh();
					}

					high240[i % 240] = currentStock.getHigh();

					if (yesterDayStock != null) {
						if (!alreadyBuy && wantBuy()) { // 買進日期,價格,手續費
							alreadyBuy = true;
							doBuy();
						} else if (alreadyBuy && wantSell()) { // 賣出...
							alreadyBuy = false;
							doSell();
						}
					}
					ma5[i % 5] = currentStock.getClose();
					yesterDayStock = stock;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("done!");
	}

	private boolean wantBuy() {
		Double currentHigh = currentStock.getHigh();
		Double highStopPrice = PriceUtil.getNextHighestPrice(yesterDayStock.getClose());
		
		Double comparedHigh = PriceUtil.highest(high240);
//		Double comparedHigh = highest;
		
		return currentStock.getVolumn() > 10 && currentHigh.compareTo(highStopPrice) == 0
				&& currentHigh.compareTo(comparedHigh) == 0;
	}

	private void doBuy() {

		double buyPrice = currentStock.getHigh();
		amount = amount(buyPrice);

		strategy = new Strategy();
		strategy.setCode(currentStock.getCode());
		strategy.setBuyDate(currentStock.getDate());
		strategy.setBuyPrice(buyPrice);
		
		strategy.setBuyAmount(amount);
		
		stockService.insert(strategy);

		System.out.println(strategy);
	}

	private boolean wantSell() {
		return PriceUtil.average(ma5).compareTo(currentStock.getLow()) > 0;
	}

	private void doSell() {

		strategy.setSellDate(currentStock.getDate());
		strategy.setSellPrice(PriceUtil.getLowerPrice(PriceUtil.average(ma5))); // 用五日均賣掉
		
		strategy.setSellAmount(amount);
		
		stockService.update(strategy);

		System.out.println(strategy);
	}

	private int amount(Double d) {
		return (int) Math.floor(200 / d);
	}

}
