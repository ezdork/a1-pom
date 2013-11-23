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
	private Double[] high480 = new Double[480];
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

			high240 = new Double[240];
			high480 = new Double[480];
			ma5 = new Double[5];
			ma10 = new Double[10];

			try {
				String code = AnalysisCron.CODE_QUEUE.poll();
				stockList = stockService.selectByCode(code);
				for (cursor = 0; cursor < stockList.size(); cursor++) {
					currentStock = stockList.get(cursor);

					if (currentStock.getHigh().compareTo(highest) > 0) {
						highest = currentStock.getHigh();
					}

					high240[cursor % 240] = currentStock.getClose();
					high480[cursor % 480] = currentStock.getClose();

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

		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(DATE_FORMAT.parse(currentStock.getDate()));
		// Double comparedHigh =
		// stockService.getHighestPrice(currentStock.getCode(), calendar, 1);

		// Double comparedHigh = PriceUtil.highest(stockList, cursor, 1);
		Double comparedHigh = PriceUtil.highest(high240);
		// Double comparedHigh = highest;

		return currentStock.getVolumn() > 100 && currentHigh.compareTo(nextHighestPrice) == 0
				&& currentHigh.compareTo(comparedHigh) >= 0;
//				&& PriceUtil.highest(high480).compareTo(comparedHigh) > 0;
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
