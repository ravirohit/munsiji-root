package org.munsiji.batch;

import java.util.Date;

public class ExpenseResult {
	
	String accType;
	String accName;
	Date dateOfExp;
	float amount;
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public Date getDateOfExp() {
		return dateOfExp;
	}
	public void setDateOfExp(Date dateOfExp) {
		this.dateOfExp = dateOfExp;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	

}
