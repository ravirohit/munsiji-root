package org.munsiji.batch;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RunScheduler {
	private static int i = 1;
	@Autowired
	@Qualifier("jobLauncher")
	SimpleJobLauncher jobLauncher;
	@Autowired
	@Qualifier("expenseResultJob")
	Job job;
	
	public void run() {
        System.out.println("schulder method run is getting called time:"+i++);
	    try {

		String dateParam = new Date().toString();
		JobParameters param = 
		  new JobParametersBuilder().addString("date", dateParam).toJobParameters();
				
		System.out.println(dateParam);
				
		JobExecution execution = jobLauncher.run(job, param);
		System.out.println("Exit Status : " + execution.getStatus());

	    } catch (Exception e) {
		e.printStackTrace();
	    }

	  }

}
