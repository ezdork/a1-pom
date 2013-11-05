package ez.dork.stock.batch;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoServiceBasicUsageCron {
	@Scheduled(cron = "*/1 * * * * ?")
	public void demoServiceMethod() {
		System.out.println("Method executed. Current time is :: " + new Date());
	}
}
