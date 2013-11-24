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

	int high240int = 240;
	private Double[] high240 = new Double[high240int];
	int high480int = 480;
	private Double[] high480 = new Double[high480int];
	private Double[] ma5 = new Double[5];
	private Double[] ma10 = new Double[10];

	private Strategy strategy = null;

	private List<Stock> stockList = null;
	int cursor = 0;

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

			high240 = new Double[high240int];
			high480 = new Double[high480int];
			ma5 = new Double[5];
			ma10 = new Double[10];

			try {
				String code = AnalysisCron.CODE_QUEUE.poll();
				stockList = stockService.selectByCode(code);

				for (cursor = 0; cursor < stockList.size(); cursor++) {
					// currentStock = stockList.get(cursor);
					// if ((cursor - 1) >= 0) {
					// yesterDayStock = stockList.get(cursor - 1);
					// }
					// int nextDays = 7;
					// if ((cursor + nextDays) < stockList.size() &&
					// yesterDayStock != null) {
					// Stock after5dayStock = stockList.get(cursor + nextDays);
					// if (after5dayStock.getClose().compareTo(0d) == 0 ||
					// currentStock.getClose().compareTo(0d) == 0) {
					// continue;
					// }
					// if (new Double(1.30d).compareTo((after5dayStock.getLow()
					// / currentStock.getHigh())) < 0
					// &&
					// PriceUtil.getNextHighestPrice(yesterDayStock.getClose()).compareTo(
					// currentStock.getHigh()) == 0) {
					//
					// strategy = new Strategy();
					// Double buyPrice = currentStock.getHigh();
					// amount = amount(buyPrice);
					// strategy.setCode(currentStock.getCode());
					// strategy.setBuyDate(currentStock.getDate());
					// strategy.setBuyPrice(buyPrice);
					// strategy.setBuyAmount(amount);
					//
					// strategy.setSellDate(after5dayStock.getDate());
					// strategy.setSellPrice(after5dayStock.getLow());
					// strategy.setSellAmount(amount);
					//
					// stockService.insert(strategy);
					// System.out.println(strategy);
					//
					// cursor = cursor + nextDays;
					// }
					//
					// }

					currentStock = stockList.get(cursor);

					if (currentStock.getClose().compareTo(highest) > 0) {
						highest = currentStock.getClose();
					}

					high240[cursor % high240int] = currentStock.getClose();
					high480[cursor % high480int] = currentStock.getClose();

					if (yesterDayStock != null) {
						if (!alreadyBuy && wantBuy()) { // 買進日期,價格,手續費
							alreadyBuy = true;
							doBuy();
						} else if (alreadyBuy && wantSell()) { // 賣出...
							alreadyBuy = false;
							doSell();
						}
					}
					ma5[cursor % 5] = currentStock.getClose();
					ma10[cursor % 10] = currentStock.getClose();
					yesterDayStock = currentStock;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("done!");
	}

	private boolean wantBuy() {
		
		Double currentHigh = currentStock.getHigh();
		Double nextHighestPrice = PriceUtil.getNextHighestPrice(yesterDayStock.getClose());

		// Double comparedHigh = PriceUtil.highest(stockList, cursor, 1);
		Double comparedHigh = PriceUtil.highest(high240);
//		 Double comparedHigh = highest;

		return currentStock.getVolumn() > 100 && currentHigh.compareTo(nextHighestPrice) == 0
				&& currentHigh.compareTo(comparedHigh) >= 0;
		// && PriceUtil.highest(high480).compareTo(comparedHigh) > 0;
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

		// return currentStock.getLow().compareTo(yesterDayStock.getLow()) <= 0;
		// return currentStock.getLow().compareTo(PriceUtil.average(ma5)) <= 0;

		return PriceUtil.getNextLowestPrice(yesterDayStock.getClose()).compareTo(currentStock.getLow()) == 0
				|| currentStock.getClose().compareTo(PriceUtil.average(ma5)) <= 0;

		// return
		// PriceUtil.getNextLowestPrice(yesterDayStock.getClose()).compareTo(currentStock.getLow())
		// == 0
		// || currentStock.getLow().compareTo(PriceUtil.average(ma5)) <= 0;
	}

	private void doSell() {

		strategy.setSellDate(currentStock.getDate());

		// strategy.setSellPrice(PriceUtil.getLowerPrice(PriceUtil.average(ma5)));//用五日均賣掉
		if (PriceUtil.getNextLowestPrice(yesterDayStock.getClose()).compareTo(currentStock.getLow()) == 0) {
			strategy.setSellPrice(currentStock.getLow());
		} else {
			strategy.setSellPrice(currentStock.getClose());
		}

		// if(PriceUtil.getNextLowestPrice(yesterDayStock.getClose()).compareTo(currentStock.getLow())
		// == 0){
		// strategy.setSellPrice(currentStock.getLow());
		// } else {
		// strategy.setSellPrice(PriceUtil.getLowerPrice(PriceUtil.average(ma5)));
		// }

		// if (currentStock.getHigh().compareTo(yesterDayStock.getLow()) > 0
		// && currentStock.getLow().compareTo(yesterDayStock.getLow()) < 0) {
		// strategy.setSellPrice(yesterDayStock.getLow());
		// } else {
		// strategy.setSellPrice(currentStock.getOpen());
		// }

		strategy.setSellAmount(amount);

		stockService.update(strategy);

		System.out.println(strategy);
	}

	private int amount(Double d) {
		return (int) Math.floor(200 / d);
	}

}
