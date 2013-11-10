package ez.dork.batch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CronTest {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:META-INF/spring/stock-batch.xml");
	}

}
