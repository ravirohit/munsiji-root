package com.munsiji.reqresObject;

import java.util.ArrayList;
import java.util.List;

public class ExpenseForAllAccType implements GenericResponse{
    List<ExpenseWithAccType> expenseWithAccTypeList;

	public List<ExpenseWithAccType> getExpenseWithAccTypeList() {
		if(expenseWithAccTypeList == null)
			expenseWithAccTypeList = new ArrayList<ExpenseWithAccType>();
		return expenseWithAccTypeList;
	}

	public void setExpenseWithAccTypeList(List<ExpenseWithAccType> expenseWithAccTypeList) {
		this.expenseWithAccTypeList = expenseWithAccTypeList;
	}

	
    
}
