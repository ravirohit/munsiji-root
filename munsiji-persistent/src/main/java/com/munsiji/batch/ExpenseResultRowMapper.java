package com.munsiji.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class ExpenseResultRowMapper implements RowMapper<ExpenseResult>{

	@Override
	public ExpenseResult mapRow(ResultSet arg0, int arg1) throws SQLException {
		ExpenseResult expenseResult = new ExpenseResult();
		expenseResult.setAccName("record1");
		expenseResult.setAccType("acctype");
		expenseResult.setDateOfExp(new Date());
		expenseResult.setAmount(1500);
		return expenseResult;
	}

}
