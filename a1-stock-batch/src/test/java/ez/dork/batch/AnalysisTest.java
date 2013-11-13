package ez.dork.batch;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ez.dork.stock.batch.Analysis;

public class AnalysisTest {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
		Analysis analysis = ctx.getBean(Analysis.class);
		analysis.analysisStock();
	}

}
