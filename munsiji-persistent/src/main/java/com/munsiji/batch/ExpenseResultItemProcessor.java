package com.munsiji.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ExpenseResultItemProcessor  implements ItemProcessor<ExpenseResult, ExpenseResult>{

	@Override
	public ExpenseResult process(ExpenseResult result) throws Exception {
		System.out.println("ExpenseResultItemProcessor method invoked:"+result.getAccName());
		
		// here we can put some condition on the result then return the value;
		return result;
	}

}
