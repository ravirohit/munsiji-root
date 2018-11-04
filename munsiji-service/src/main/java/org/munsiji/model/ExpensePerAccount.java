package org.munsiji.model;

import java.util.HashMap;
import java.util.Map;

public class ExpensePerAccount {
	
	Map<String,Float> expensePerAcc;

	public Map<String, Float> getExpensePerAcc() {
		expensePerAcc = expensePerAcc == null? new HashMap<String,Float>():expensePerAcc;;
		return expensePerAcc;
	}

	public void setExpensePerAcc(Map<String, Float> expensePerAcc) {
		this.expensePerAcc = expensePerAcc;
	}
	
	

}
