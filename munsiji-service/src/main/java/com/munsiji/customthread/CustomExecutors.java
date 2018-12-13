package com.munsiji.customthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomExecutors {
	static final ExecutorService executor = Executors.newFixedThreadPool(100);
	public static void executeThread(Runnable th, String purpose){
        System.out.println("Running new Thread to server purpose of "+purpose);
		executor.execute(th);
	}

}
