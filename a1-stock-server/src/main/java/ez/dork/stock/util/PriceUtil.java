package ez.dork.stock.util;

public class PriceUtil {
	public static Double getNextHighestPrice(Double price) {
		double targetPrice = price * 1.07;
		double stockStandard = getStockStandard(targetPrice);
		return Math.floor(targetPrice * stockStandard) / stockStandard;
	}

	public static Double getNextLowestPrice(Double price) {
		double targetPrice = price * 0.93;
		double stockStandard = getStockStandard(targetPrice);
		return Math.ceil(targetPrice * stockStandard) / stockStandard;
	}

	private static double getStockStandard(Double d) {
		double stockStandard;
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
