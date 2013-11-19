package ez.dork.stock.thread;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.service.StockService;

/**
 * 
 * @author tim
 * 
 */
@Component
@Scope("prototype")
// The bean scope must be “prototype“, so that each request will return a new
// instance, to run each individual thread.
public class WantedThread extends Thread {

	private Calendar calendar;
	private Integer howManyYears;
	private List<Stock> resultList;

	@Autowired
	private StockService stockService;

	@Override
	public void run() {
		resultList = stockService.selectHeighestStockList(calendar, howManyYears);
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Integer getHowManyYears() {
		return howManyYears;
	}

	public void setHowManyYears(Integer howManyYears) {
		this.howManyYears = howManyYears;
	}

	public List<Stock> getResultList() {
		return resultList;
	}

	public void setResultList(List<Stock> resultList) {
		this.resultList = resultList;
	}

}
