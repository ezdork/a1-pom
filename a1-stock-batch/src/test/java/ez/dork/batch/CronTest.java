package ez.dork.batch;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ez.dork.stock.batch.StockCron;

public class CronTest {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
		StockCron cron = ctx.getBean(StockCron.class);
		cron.getStock(null);
	}

}
