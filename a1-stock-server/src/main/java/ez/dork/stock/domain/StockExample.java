package ez.dork.stock.domain;

import java.util.ArrayList;
import java.util.List;

public class StockExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public StockExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andCodeIsNull() {
			addCriterion("code is null");
			return (Criteria) this;
		}

		public Criteria andCodeIsNotNull() {
			addCriterion("code is not null");
			return (Criteria) this;
		}

		public Criteria andCodeEqualTo(String value) {
			addCriterion("code =", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotEqualTo(String value) {
			addCriterion("code <>", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeGreaterThan(String value) {
			addCriterion("code >", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeGreaterThanOrEqualTo(String value) {
			addCriterion("code >=", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeLessThan(String value) {
			addCriterion("code <", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeLessThanOrEqualTo(String value) {
			addCriterion("code <=", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeLike(String value) {
			addCriterion("code like", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotLike(String value) {
			addCriterion("code not like", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeIn(List<String> values) {
			addCriterion("code in", values, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotIn(List<String> values) {
			addCriterion("code not in", values, "code");
			return (Criteria) this;
		}

		public Criteria andCodeBetween(String value1, String value2) {
			addCriterion("code between", value1, value2, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotBetween(String value1, String value2) {
			addCriterion("code not between", value1, value2, "code");
			return (Criteria) this;
		}

		public Criteria andDateIsNull() {
			addCriterion("date is null");
			return (Criteria) this;
		}

		public Criteria andDateIsNotNull() {
			addCriterion("date is not null");
			return (Criteria) this;
		}

		public Criteria andDateEqualTo(String value) {
			addCriterion("date =", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateNotEqualTo(String value) {
			addCriterion("date <>", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateGreaterThan(String value) {
			addCriterion("date >", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateGreaterThanOrEqualTo(String value) {
			addCriterion("date >=", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateLessThan(String value) {
			addCriterion("date <", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateLessThanOrEqualTo(String value) {
			addCriterion("date <=", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateLike(String value) {
			addCriterion("date like", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateNotLike(String value) {
			addCriterion("date not like", value, "date");
			return (Criteria) this;
		}

		public Criteria andDateIn(List<String> values) {
			addCriterion("date in", values, "date");
			return (Criteria) this;
		}

		public Criteria andDateNotIn(List<String> values) {
			addCriterion("date not in", values, "date");
			return (Criteria) this;
		}

		public Criteria andDateBetween(String value1, String value2) {
			addCriterion("date between", value1, value2, "date");
			return (Criteria) this;
		}

		public Criteria andDateNotBetween(String value1, String value2) {
			addCriterion("date not between", value1, value2, "date");
			return (Criteria) this;
		}

		public Criteria andOpenIsNull() {
			addCriterion("open is null");
			return (Criteria) this;
		}

		public Criteria andOpenIsNotNull() {
			addCriterion("open is not null");
			return (Criteria) this;
		}

		public Criteria andOpenEqualTo(Double value) {
			addCriterion("open =", value, "open");
			return (Criteria) this;
		}

		public Criteria andOpenNotEqualTo(Double value) {
			addCriterion("open <>", value, "open");
			return (Criteria) this;
		}

		public Criteria andOpenGreaterThan(Double value) {
			addCriterion("open >", value, "open");
			return (Criteria) this;
		}

		public Criteria andOpenGreaterThanOrEqualTo(Double value) {
			addCriterion("open >=", value, "open");
			return (Criteria) this;
		}

		public Criteria andOpenLessThan(Double value) {
			addCriterion("open <", value, "open");
			return (Criteria) this;
		}

		public Criteria andOpenLessThanOrEqualTo(Double value) {
			addCriterion("open <=", value, "open");
			return (Criteria) this;
		}

		public Criteria andOpenIn(List<Double> values) {
			addCriterion("open in", values, "open");
			return (Criteria) this;
		}

		public Criteria andOpenNotIn(List<Double> values) {
			addCriterion("open not in", values, "open");
			return (Criteria) this;
		}

		public Criteria andOpenBetween(Double value1, Double value2) {
			addCriterion("open between", value1, value2, "open");
			return (Criteria) this;
		}

		public Criteria andOpenNotBetween(Double value1, Double value2) {
			addCriterion("open not between", value1, value2, "open");
			return (Criteria) this;
		}

		public Criteria andHighIsNull() {
			addCriterion("high is null");
			return (Criteria) this;
		}

		public Criteria andHighIsNotNull() {
			addCriterion("high is not null");
			return (Criteria) this;
		}

		public Criteria andHighEqualTo(Double value) {
			addCriterion("high =", value, "high");
			return (Criteria) this;
		}

		public Criteria andHighNotEqualTo(Double value) {
			addCriterion("high <>", value, "high");
			return (Criteria) this;
		}

		public Criteria andHighGreaterThan(Double value) {
			addCriterion("high >", value, "high");
			return (Criteria) this;
		}

		public Criteria andHighGreaterThanOrEqualTo(Double value) {
			addCriterion("high >=", value, "high");
			return (Criteria) this;
		}

		public Criteria andHighLessThan(Double value) {
			addCriterion("high <", value, "high");
			return (Criteria) this;
		}

		public Criteria andHighLessThanOrEqualTo(Double value) {
			addCriterion("high <=", value, "high");
			return (Criteria) this;
		}

		public Criteria andHighIn(List<Double> values) {
			addCriterion("high in", values, "high");
			return (Criteria) this;
		}

		public Criteria andHighNotIn(List<Double> values) {
			addCriterion("high not in", values, "high");
			return (Criteria) this;
		}

		public Criteria andHighBetween(Double value1, Double value2) {
			addCriterion("high between", value1, value2, "high");
			return (Criteria) this;
		}

		public Criteria andHighNotBetween(Double value1, Double value2) {
			addCriterion("high not between", value1, value2, "high");
			return (Criteria) this;
		}

		public Criteria andLowIsNull() {
			addCriterion("low is null");
			return (Criteria) this;
		}

		public Criteria andLowIsNotNull() {
			addCriterion("low is not null");
			return (Criteria) this;
		}

		public Criteria andLowEqualTo(Double value) {
			addCriterion("low =", value, "low");
			return (Criteria) this;
		}

		public Criteria andLowNotEqualTo(Double value) {
			addCriterion("low <>", value, "low");
			return (Criteria) this;
		}

		public Criteria andLowGreaterThan(Double value) {
			addCriterion("low >", value, "low");
			return (Criteria) this;
		}

		public Criteria andLowGreaterThanOrEqualTo(Double value) {
			addCriterion("low >=", value, "low");
			return (Criteria) this;
		}

		public Criteria andLowLessThan(Double value) {
			addCriterion("low <", value, "low");
			return (Criteria) this;
		}

		public Criteria andLowLessThanOrEqualTo(Double value) {
			addCriterion("low <=", value, "low");
			return (Criteria) this;
		}

		public Criteria andLowIn(List<Double> values) {
			addCriterion("low in", values, "low");
			return (Criteria) this;
		}

		public Criteria andLowNotIn(List<Double> values) {
			addCriterion("low not in", values, "low");
			return (Criteria) this;
		}

		public Criteria andLowBetween(Double value1, Double value2) {
			addCriterion("low between", value1, value2, "low");
			return (Criteria) this;
		}

		public Criteria andLowNotBetween(Double value1, Double value2) {
			addCriterion("low not between", value1, value2, "low");
			return (Criteria) this;
		}

		public Criteria andCloseIsNull() {
			addCriterion("close is null");
			return (Criteria) this;
		}

		public Criteria andCloseIsNotNull() {
			addCriterion("close is not null");
			return (Criteria) this;
		}

		public Criteria andCloseEqualTo(Double value) {
			addCriterion("close =", value, "close");
			return (Criteria) this;
		}

		public Criteria andCloseNotEqualTo(Double value) {
			addCriterion("close <>", value, "close");
			return (Criteria) this;
		}

		public Criteria andCloseGreaterThan(Double value) {
			addCriterion("close >", value, "close");
			return (Criteria) this;
		}

		public Criteria andCloseGreaterThanOrEqualTo(Double value) {
			addCriterion("close >=", value, "close");
			return (Criteria) this;
		}

		public Criteria andCloseLessThan(Double value) {
			addCriterion("close <", value, "close");
			return (Criteria) this;
		}

		public Criteria andCloseLessThanOrEqualTo(Double value) {
			addCriterion("close <=", value, "close");
			return (Criteria) this;
		}

		public Criteria andCloseIn(List<Double> values) {
			addCriterion("close in", values, "close");
			return (Criteria) this;
		}

		public Criteria andCloseNotIn(List<Double> values) {
			addCriterion("close not in", values, "close");
			return (Criteria) this;
		}

		public Criteria andCloseBetween(Double value1, Double value2) {
			addCriterion("close between", value1, value2, "close");
			return (Criteria) this;
		}

		public Criteria andCloseNotBetween(Double value1, Double value2) {
			addCriterion("close not between", value1, value2, "close");
			return (Criteria) this;
		}

		public Criteria andVolumnIsNull() {
			addCriterion("volumn is null");
			return (Criteria) this;
		}

		public Criteria andVolumnIsNotNull() {
			addCriterion("volumn is not null");
			return (Criteria) this;
		}

		public Criteria andVolumnEqualTo(Integer value) {
			addCriterion("volumn =", value, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnNotEqualTo(Integer value) {
			addCriterion("volumn <>", value, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnGreaterThan(Integer value) {
			addCriterion("volumn >", value, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnGreaterThanOrEqualTo(Integer value) {
			addCriterion("volumn >=", value, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnLessThan(Integer value) {
			addCriterion("volumn <", value, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnLessThanOrEqualTo(Integer value) {
			addCriterion("volumn <=", value, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnIn(List<Integer> values) {
			addCriterion("volumn in", values, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnNotIn(List<Integer> values) {
			addCriterion("volumn not in", values, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnBetween(Integer value1, Integer value2) {
			addCriterion("volumn between", value1, value2, "volumn");
			return (Criteria) this;
		}

		public Criteria andVolumnNotBetween(Integer value1, Integer value2) {
			addCriterion("volumn not between", value1, value2, "volumn");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table stock
	 * @mbggenerated  Fri Dec 06 23:23:50 CST 2013
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table stock
     *
     * @mbggenerated do_not_delete_during_merge Tue Nov 12 15:09:34 CST 2013
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}