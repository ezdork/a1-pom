package ez.dork.stock;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import ez.dork.stock.batch.AnalysisCron;
import ez.dork.stock.batch.StockCron;
import ez.dork.stock.domain.EarnMoney;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.Strategy;
import ez.dork.stock.service.StockService;

@Controller
public class StockController {

	@Autowired
	private StockService stockService;
	@Autowired
	private StockCron stockCron;
	@Autowired
	private AnalysisCron analysisCron;

	@RequestMapping(value = "/getData")
	public @ResponseBody
	String getData(@RequestParam("stockCode") String stockCode) {
		List<Stock> resultList = stockService.selectByCode(stockCode);
		return new Gson().toJson(resultList);
	}

	@RequestMapping(value = "/getAnalysisData")
	public @ResponseBody
	String getAnalysisData(@RequestParam("stockCode") String stockCode) {
		List<Strategy> resultList = stockService.getStrategyList(stockCode);
		return new Gson().toJson(resultList);
	}

	@RequestMapping(value = "/getAllStockOrderByEarnMoney")
	public @ResponseBody
	String getAllStockOrderByEarnMoney() {
		List<EarnMoney> resultList = stockService.getAllStockOrderByEarnMoney();
		return new Gson().toJson(resultList);
	}

	@RequestMapping(value = "/activeStockCron")
	public @ResponseBody
	void activeStockCron() throws IOException {
		stockCron.getStock();
	}

	@RequestMapping(value = "/activeAnalysisCron")
	public @ResponseBody
	void activeAnalysisCron() throws IOException {
		analysisCron.analysisStock();
	}

}
