package ez.dork.stock.domain;

public class EarnMoney {
	private String code;
	private Double earnMoney;
	private Double fee;

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

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "EarnMoney [code=" + code + ", earnMoney=" + earnMoney + ", fee=" + fee + "]";
	}

}
