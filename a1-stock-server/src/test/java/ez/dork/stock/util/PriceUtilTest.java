package ez.dork.stock.util;

import org.junit.Test;

public class PriceUtilTest {

	@Test
	public void test() {
		double price = PriceUtil.getLowerPrice(0.24d);
		System.out.println(price);
	}

}
