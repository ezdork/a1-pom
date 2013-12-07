package ez.dork.stock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import ez.dork.stock.domain.Stock;
import ez.dork.stock.domain.StockKey;
import ez.dork.stock.domain.StockExample;

public interface StockMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int countByExample(StockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int deleteByExample(StockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int deleteByPrimaryKey(StockKey key);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int insert(Stock record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int insertSelective(Stock record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	List<Stock> selectByExample(StockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	Stock selectByPrimaryKey(StockKey key);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int updateByExampleSelective(@Param("record") Stock record, @Param("example") StockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int updateByExample(@Param("record") Stock record, @Param("example") StockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int updateByPrimaryKeySelective(Stock record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	int updateByPrimaryKey(Stock record);

	List<Stock> selectHighestStockList(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

	List<Stock> selectByCode(@Param("code") String code);

	List<String> selectGroupByCode();

	Double getHighestPrice(@Param("code") String code, @Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

	String getLatestStockDate();

	List<Stock> selectLast5(@Param("code") String code, @Param("date") String date);
}