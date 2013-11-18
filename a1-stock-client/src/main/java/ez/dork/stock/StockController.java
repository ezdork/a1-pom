package ez.dork.stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
		Double[] ma10 = new Double[10];
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

			map.put("ma10", PriceUtil.average(ma10));
			ma10[i % 10] = stock.getClose();

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

	@RequestMapping(value = "/getLatestStockDate")
	public @ResponseBody
	String getLatestStockDate() {
		return stockService.getLatestStockDate();
	};

	private Map<String, String> wantedStockMap = new HashMap<String, String>();

	@RequestMapping(value = "/getWantedStockList")
	public @ResponseBody
	String getWantedStockList(@RequestParam("date") String date, @RequestParam("clearCache") Boolean clearCache) {

		if (clearCache) {
			wantedStockMap.clear();
		} else {
			String string = wantedStockMap.get(date);
			if (string != null) {
				return string;
			}
		}

		List<Stock> resultList1 = stockService.selectHeighestStockList(getWantedCalendar(date), 1);
		List<Stock> resultList2 = stockService.selectHeighestStockList(getWantedCalendar(date), 2);
		List<Stock> resultList3 = stockService.selectHeighestStockList(getWantedCalendar(date), 3);
		List<Stock> resultList5 = stockService.selectHeighestStockList(getWantedCalendar(date), 5);
		List<Stock> resultList10 = stockService.selectHeighestStockList(getWantedCalendar(date), 10);
		List<Stock> resultListAll = stockService.selectHeighestStockList(getWantedCalendar(date), null);

		resultList1.removeAll(resultList2);
		resultList1.removeAll(resultList3);
		resultList1.removeAll(resultList5);
		resultList1.removeAll(resultList10);
		resultList1.removeAll(resultListAll);

		resultList2.removeAll(resultList3);
		resultList2.removeAll(resultList5);
		resultList2.removeAll(resultList10);
		resultList2.removeAll(resultListAll);

		resultList3.removeAll(resultList5);
		resultList3.removeAll(resultList10);
		resultList3.removeAll(resultListAll);

		resultList5.removeAll(resultList10);
		resultList5.removeAll(resultListAll);

		resultList10.removeAll(resultListAll);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("resultList1", formatList(resultList1));
		result.put("resultList2", formatList(resultList2));
		result.put("resultList3", formatList(resultList3));
		result.put("resultList5", formatList(resultList5));
		result.put("resultList10", formatList(resultList10));
		result.put("resultListAll", formatList(resultListAll));
		String json = new Gson().toJson(result);
		wantedStockMap.put(date, json);
		return json;
	}

	private static List<Map<String, Object>> formatList(List<Stock> stockList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Stock stock : stockList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", stock.getCode());
			map.put("date", stock.getDate());
			Double nextHighestPrice = PriceUtil.getNextHighestPrice(stock.getClose());
			map.put("nextHighestPrice", nextHighestPrice);
			map.put("buyAmount", Math.floor(200 / nextHighestPrice));
			list.add(map);
		}
		return list;
	}

	private static Calendar getWantedCalendar(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.valueOf(date.substring(0, 4)));
		calendar.set(Calendar.MONTH, Integer.valueOf(date.substring(4, 6)) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.substring(6, 8)));
		return calendar;
	}
}
