package ez.dork.stock.domain;

public class EarnMoney {
	private String code;
	private Double earnMoney;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getEarnMoney() {
		return earnMoney;
	}

	public void setEarnMoney(Double earnMoney) {
		this.earnMoney = earnMoney;
	}

	@Override
	public String toString() {
		return "EarnMoney [code=" + code + ", earnMoney=" + earnMoney + "]";
	}

}
