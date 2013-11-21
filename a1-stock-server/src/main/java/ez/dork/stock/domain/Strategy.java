package ez.dork.stock.domain;

public class Strategy extends StrategyKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column strategy.buy_price
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    private Double buyPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column strategy.buy_amount
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    private Integer buyAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column strategy.sell_date
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    private String sellDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column strategy.sell_price
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    private Double sellPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column strategy.sell_amount
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    private Integer sellAmount;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column strategy.buy_price
     *
     * @return the value of strategy.buy_price
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public Double getBuyPrice() {
        return buyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column strategy.buy_price
     *
     * @param buyPrice the value for strategy.buy_price
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column strategy.buy_amount
     *
     * @return the value of strategy.buy_amount
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public Integer getBuyAmount() {
        return buyAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column strategy.buy_amount
     *
     * @param buyAmount the value for strategy.buy_amount
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public void setBuyAmount(Integer buyAmount) {
        this.buyAmount = buyAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column strategy.sell_date
     *
     * @return the value of strategy.sell_date
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public String getSellDate() {
        return sellDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column strategy.sell_date
     *
     * @param sellDate the value for strategy.sell_date
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column strategy.sell_price
     *
     * @return the value of strategy.sell_price
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public Double getSellPrice() {
        return sellPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column strategy.sell_price
     *
     * @param sellPrice the value for strategy.sell_price
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column strategy.sell_amount
     *
     * @return the value of strategy.sell_amount
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public Integer getSellAmount() {
        return sellAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column strategy.sell_amount
     *
     * @param sellAmount the value for strategy.sell_amount
     *
     * @mbggenerated Sun Nov 17 11:56:09 CST 2013
     */
    public void setSellAmount(Integer sellAmount) {
        this.sellAmount = sellAmount;
    }

	@Override
	public String toString() {
		return "Strategy [buyPrice=" + buyPrice + ", buyAmount=" + buyAmount + ", sellDate=" + sellDate
				+ ", sellPrice=" + sellPrice + ", sellAmount=" + sellAmount + "]";
	}
    
}