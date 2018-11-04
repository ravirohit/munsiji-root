package org.munsiji.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.munsiji.reqresObject.GenericResponse;

public class UserTotalExepense implements GenericResponse{
	Map<String,ExpensePerAccount> expenseforAlAcc;

	public Map<String,ExpensePerAccount> getExpenseforAlAcc() {
		expenseforAlAcc = expenseforAlAcc == null ? new HashMap<String,ExpensePerAccount>():expenseforAlAcc;
		return expenseforAlAcc;
	}

	public void setExpenseforAlAcc(Map<String,ExpensePerAccount> expenseforAlAcc) {
		this.expenseforAlAcc = expenseforAlAcc;
	}
	
	
}
