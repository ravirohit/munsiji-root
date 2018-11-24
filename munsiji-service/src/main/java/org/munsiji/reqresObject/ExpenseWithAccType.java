package org.munsiji.reqresObject;

import java.util.ArrayList;
import java.util.List;

public class ExpenseWithAccType{
	String accType;
	List<AccExpenseData> accExpList;
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public List<AccExpenseData> getAccExpList() {
		if(accExpList == null)
			accExpList = new ArrayList<AccExpenseData>();
		return accExpList;
	}
	public void setAccExpList(List<AccExpenseData> accExpList) {
		this.accExpList = accExpList;
	}
	
	

}
