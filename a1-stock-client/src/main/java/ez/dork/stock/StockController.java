package ez.dork.stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ez.dork.stock.util.PriceUtil;

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

		Double[] ma5 = new Double[5];
		List<Stock> stockList = stockService.selectByCode(stockCode);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (Stock stock : stockList) {
			i++;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", stock.getDate());
			map.put("code", stock.getCode());
			map.put("open", stock.getOpen());
			map.put("high", stock.getHigh());
			map.put("low", stock.getLow());
			map.put("volumn", stock.getVolumn());
			map.put("close", stock.getClose());

			map.put("ma5", PriceUtil.average(ma5));
			ma5[i % 5] = stock.getClose();

			resultList.add(map);
		}
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
		if (StockCron.STOCK_QUEUE.isEmpty()) {
			stockCron.getStock();
		}
	}

	@RequestMapping(value = "/activeAnalysisCron")
	public @ResponseBody
	void activeAnalysisCron() throws IOException {
		if (AnalysisCron.CODE_QUEUE.isEmpty()) {
			analysisCron.analysisStock();
		}
	}

}
