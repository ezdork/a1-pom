package ez.dork.batch;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ez.dork.stock.batch.Cron;

public class CronTest {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
		Cron cron = ctx.getBean(Cron.class);
		cron.getStock();
	}

}
