package ez.dork.stock;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import ez.dork.stock.batch.AnalysisCron;
import ez.dork.stock.batch.StockCron;
import ez.dork.stock.batch.StockNameCron;
import ez.dork.stock.domain.EarnMoney;
import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.StockName;
import ez.dork.stock.domain.Strategy;
import ez.dork.stock.service.StockService;
import ez.dork.stock.thread.WantedThread;
import ez.dork.stock.util.PriceUtil;

@Controller
public class StockController {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private StockService stockService;
	@Autowired
	private StockCron stockCron;
	@Autowired
	private AnalysisCron analysisCron;
	@Autowired
	private StockNameCron stockNameCron;

	@RequestMapping(value = "/selectAllStockName")
	public @ResponseBody
	String selectAllStockName() throws UnsupportedEncodingException {
		List<StockName> selectAllStockName = stockService.selectAllStockName();
		for (StockName stockName : selectAllStockName) {
			stockName.setName(java.net.URLEncoder.encode(stockName.getName().trim(), "UTF8"));
		}
		return new Gson().toJson(selectAllStockName);
	}

	@RequestMapping(value = "/getStockName")
	public @ResponseBody
	void getStockName(@RequestParam(required = false, value = "wantKnowStockCode") String wantKnowStockCode)
			throws IOException {
		stockNameCron.getStockName(wantKnowStockCode);
	}

	@RequestMapping(value = "/getData")
	public @ResponseBody
	String getData(@RequestParam("stockCode") String stockCode) {

		Double[] ma5 = new Double[5];
		Double[] ma10 = new Double[10];

		int closeInt = 240;
		Double[] close = new Double[closeInt];

		List<Stock> stockList = stockService.selectByCode(stockCode);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		Stock yesterdayStock = null;
		for (int i = 0; i < stockList.size(); i++) {
			Stock stock = stockList.get(i);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", stock.getDate());
			map.put("code", stock.getCode());
			map.put("open", stock.getOpen());
			map.put("high", stock.getHigh());
			map.put("low", stock.getLow());
			map.put("volumn", stock.getVolumn());
			map.put("close", stock.getClose());

			Double before5days = (i - 5 >= 0 && stockList.get(i - 5).getClose() > 0) ? stock.getHigh()
					/ stockList.get(i - 5).getClose() : 0d;
			map.put("before5days", before5days);

			close[i % closeInt] = stock.getClose();
			map.put("high240", PriceUtil.highest(close));
			map.put("low240", PriceUtil.lowest(close));

			map.put("ma5", PriceUtil.average(ma5));
			ma5[i % 5] = stock.getClose();

			map.put("ma10", PriceUtil.average(ma10));
			ma10[i % 10] = stock.getClose();

			if (yesterdayStock != null) {
				if (PriceUtil.getNextHighestPrice(yesterdayStock.getClose()).compareTo(stock.getHigh()) == 0) {
					map.put("highest", true);
				}
				if (PriceUtil.getNextLowestPrice(yesterdayStock.getClose()).compareTo(stock.getLow()) == 0) {
					map.put("lowest", true);
				}
			}

			resultList.add(map);

			yesterdayStock = stock;
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
	void activeStockCron(@RequestParam(required = false, value = "wantScanStockCode") String wantScanStockCode)
			throws IOException {
		if (StockCron.STOCK_QUEUE.isEmpty()) {
			stockCron.getStock(wantScanStockCode);
			activeAnalysisCron();
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
	String getWantedStockList(@RequestParam("date") String date,
			@RequestParam(required = false, value = "clearCache", defaultValue = "false") Boolean clearCache)
			throws InterruptedException, ParseException {

		if (clearCache) {
			wantedStockMap.clear();
		} else {
			String string = wantedStockMap.get(date);
			if (string != null) {
				return string;
			}
		}

		WantedThread wantedThread1 = ctx.getBean(WantedThread.class);
		wantedThread1.setCalendar(getWantedCalendar(date));
		wantedThread1.setHowManyYears(1);
		wantedThread1.start();

		WantedThread wantedThread2 = ctx.getBean(WantedThread.class);
		wantedThread2.setCalendar(getWantedCalendar(date));
		wantedThread2.setHowManyYears(2);
		wantedThread2.start();

		WantedThread wantedThread3 = ctx.getBean(WantedThread.class);
		wantedThread3.setCalendar(getWantedCalendar(date));
		wantedThread3.setHowManyYears(3);
		wantedThread3.start();

		WantedThread wantedThread5 = ctx.getBean(WantedThread.class);
		wantedThread5.setCalendar(getWantedCalendar(date));
		wantedThread5.setHowManyYears(5);
		wantedThread5.start();

		WantedThread wantedThread10 = ctx.getBean(WantedThread.class);
		wantedThread10.setCalendar(getWantedCalendar(date));
		wantedThread10.setHowManyYears(10);
		wantedThread10.start();

		WantedThread wantedThreadAll = ctx.getBean(WantedThread.class);
		wantedThreadAll.setCalendar(getWantedCalendar(date));
		wantedThreadAll.setHowManyYears(null);
		wantedThreadAll.start();

		wantedThread1.join();
		wantedThread2.join();
		wantedThread3.join();
		wantedThread5.join();
		wantedThread10.join();
		wantedThreadAll.join();

		List<Stock> resultList1 = wantedThread1.getResultList();
		List<Stock> resultList2 = wantedThread2.getResultList();
		List<Stock> resultList3 = wantedThread3.getResultList();
		List<Stock> resultList5 = wantedThread5.getResultList();
		List<Stock> resultList10 = wantedThread10.getResultList();
		List<Stock> resultListAll = wantedThreadAll.getResultList();

		// 移除重複的
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

		List<Strategy> currentBuyList = stockService.selectCurrentBuyList(date);
		List<Map<String, Object>> buyList = new ArrayList<Map<String, Object>>();
		for (Strategy strategy : currentBuyList) {
			Map<String, Object> map = new HashMap<String, Object>();
			String code = strategy.getCode();
			map.put("code", code);
			map.put("buyDate", strategy.getBuyDate());
			map.put("buyPrice", strategy.getBuyPrice());
			map.put("buyAmount", strategy.getBuyAmount());
			map.put("sellDate", strategy.getSellDate());
			map.put("sellPrice", strategy.getSellPrice());
			map.put("sellAmount", strategy.getSellAmount());
			List<Stock> resultList = stockService.selectLast5(code, date);
			Double[] doubleArray = new Double[5];
			for (int i = 0; i < 5; i++) {
				if (i < resultList.size()) {
					doubleArray[i] = resultList.get(i).getClose();
				}
			}
			map.put("ma5", PriceUtil.getLowerPrice(PriceUtil.average(doubleArray)));
			Double close = resultList.get(0).getClose();
			map.put("lowestPrice", PriceUtil.getNextLowestPrice(close));
			map.put("nowPrice", close);

			buyList.add(map);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("resultList1", formatList(resultList1, currentBuyList));
		result.put("resultList2", formatList(resultList2, currentBuyList));
		result.put("resultList3", formatList(resultList3, currentBuyList));
		result.put("resultList5", formatList(resultList5, currentBuyList));
		result.put("resultList10", formatList(resultList10, currentBuyList));
		result.put("resultListAll", formatList(resultListAll, currentBuyList));
		result.put("currentBuyList", buyList);
		String json = new Gson().toJson(result);
		wantedStockMap.put(date, json);
		return json;
	}

	private static List<Map<String, Object>> formatList(List<Stock> stockList, List<Strategy> currentBuyList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Stock stock : stockList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", stock.getCode());
			map.put("date", stock.getDate());
			Double nextHighestPrice = PriceUtil.getNextHighestPrice(stock.getClose());
			map.put("nextHighestPrice", nextHighestPrice);
			map.put("buyAmount", Math.floor(200 / nextHighestPrice));
			for (Strategy currentBuy : currentBuyList) {
				if (stock.getCode().equals(currentBuy.getCode())) {
					map.put("alreadyBuy", true);
					break;
				}
			}
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
