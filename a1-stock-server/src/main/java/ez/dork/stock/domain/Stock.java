package ez.dork.stock.domain;

public class Stock extends StockKey {
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column stock.open
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	private Double open;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column stock.high
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	private Double high;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column stock.low
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	private Double low;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column stock.close
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	private Double close;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column stock.volumn
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	private Integer volumn;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column stock.open
	 * @return  the value of stock.open
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public Double getOpen() {
		return open;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column stock.open
	 * @param open  the value for stock.open
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void setOpen(Double open) {
		this.open = open;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column stock.high
	 * @return  the value of stock.high
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public Double getHigh() {
		return high;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column stock.high
	 * @param high  the value for stock.high
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void setHigh(Double high) {
		this.high = high;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column stock.low
	 * @return  the value of stock.low
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public Double getLow() {
		return low;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column stock.low
	 * @param low  the value for stock.low
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void setLow(Double low) {
		this.low = low;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column stock.close
	 * @return  the value of stock.close
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public Double getClose() {
		return close;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column stock.close
	 * @param close  the value for stock.close
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void setClose(Double close) {
		this.close = close;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column stock.volumn
	 * @return  the value of stock.volumn
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public Integer getVolumn() {
		return volumn;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column stock.volumn
	 * @param volumn  the value for stock.volumn
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void setVolumn(Integer volumn) {
		this.volumn = volumn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((close == null) ? 0 : close.hashCode());
		result = prime * result + ((high == null) ? 0 : high.hashCode());
		result = prime * result + ((low == null) ? 0 : low.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((volumn == null) ? 0 : volumn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		if (!getCode().equals(other.getCode()) || !getDate().equals(other.getDate())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Stock [open=" + open + ", high=" + high + ", low=" + low + ", close=" + close + ", volumn=" + volumn
				+", code=" + getCode()+", date=" + getDate()+ "]";
	}

}