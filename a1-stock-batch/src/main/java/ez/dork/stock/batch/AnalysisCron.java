package ez.dork.stock.batch;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ez.dork.stock.service.StockService;
import ez.dork.stock.thread.AnalysisThread;

@Component
public class AnalysisCron {

	public static LinkedBlockingQueue<String> CODE_QUEUE = new LinkedBlockingQueue<String>();

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private StockService stockService;

	public void analysisStock() throws IOException {
		stockService.truncate();
		List<String> codeList = stockService.selectGroupByCode();
		CODE_QUEUE.addAll(codeList);

		for (int i = 0; i < 20; i++) {
			AnalysisThread analysisThread = ctx.getBean(AnalysisThread.class);
			analysisThread.start();
		}
	}

}
