package ez.dork.stock.queue;

import java.util.Calendar;

public class StockQueue {

	private String code;
	private Calendar calendar;
	/**
	 * 0:GOV, 1:ORG
	 */
	private int kind;
	private int emptyTimes;

	public StockQueue(String code, Calendar calendar, int kind) {
		super();
		this.code = code;
		this.calendar = calendar;
		this.kind = kind;
	}

	public int getEmptyTimes() {
		return emptyTimes;
	}

	public void setEmptyTimes(int emptyTimes) {
		this.emptyTimes = emptyTimes;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
}
