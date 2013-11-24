package ez.dork.stock.util;

import java.util.List;

import ez.dork.stock.domain.Stock;

public class PriceUtil {

	public static Double trueRage(Stock yesterdayStock, Stock currentStock) {
		if (yesterdayStock == null) {
			return 0d;
		}
		Double a = Math.abs(yesterdayStock.getClose() - currentStock.getHigh());
		Double b = Math.abs(yesterdayStock.getClose() - currentStock.getLow());
		Double c = currentStock.getHigh() - currentStock.getLow();
		return highest(a, b, c);
	}

	public static Double average(Double... doubles) {
		Double total = 0d;
		for (Double doubleValue : doubles) {
			if (doubleValue != null) {
				total = total + doubleValue;
			}
		}
		return total / doubles.length;
	}

	public static Double highest(List<Stock> stockList, int cursor, Integer howManyYears) {
		Stock stock = stockList.get(cursor);
		String targetDate = "";
		try {
			Integer date = Integer.valueOf(stock.getDate());
			targetDate = String.valueOf(date - (howManyYears * 10000));
		} catch (Exception e) {
			System.err.println(stock);
		}

		Double result = stock.getHigh();
		for (int j = cursor; j > -1; j--) {
			Stock cursorStock = stockList.get(j);
			if (targetDate.compareTo(cursorStock.getDate()) > 0) {
				break;
			}
			Double cursorResult = cursorStock.getHigh();
			result = (cursorResult.compareTo(result) > 0 ? cursorResult : result);
		}
		return result;
	}

	public static Double highest(Double... doubles) {
		Double result = 0d;
		for (Double doubleValue : doubles) {
			if (doubleValue != null) {
				result = (doubleValue.compareTo(result) > 0 ? doubleValue : result);
			}
		}
		return result;
	}

	public static Double lowest(Double... doubles) {
		Double result = 99999d;
		for (Double doubleValue : doubles) {
			if (doubleValue != null) {
				result = (doubleValue.compareTo(result) < 0 ? doubleValue : result);
			}
		}
		return result;
	}

	public static Double getNextHighestPrice(Double price) {
		double targetPrice = price * 1.07;
		return getLowerPrice(targetPrice);
	}

	public static Double getLowerPrice(Double targetPrice) {
		double stockStandard = getStockStandard(targetPrice);
		return Math.floor(targetPrice * stockStandard) / stockStandard;
	}

	public static Double getNextLowestPrice(Double price) {
		double targetPrice = price * 0.93;
		return getHigherPrice(targetPrice);
	}

	public static Double getHigherPrice(Double targetPrice) {
		double stockStandard = getStockStandard(targetPrice);
		return Math.ceil(targetPrice * stockStandard) / stockStandard;
	}

	private static int getStockStandard(Double d) {
		int stockStandard;
		int intValue = d.intValue();
		if (intValue < 10) {
			// stockStandard = 0.01;
			stockStandard = 100;
		} else if (intValue < 50) {
			// stockStandard = 0.05;
			stockStandard = 20;
		} else if (intValue < 100) {
			// stockStandard = 0.1;
			stockStandard = 10;
		} else if (intValue < 500) {
			// stockStandard = 0.5;
			stockStandard = 2;
		} else {
			stockStandard = 1;
		}
		return stockStandard;
	}
}
