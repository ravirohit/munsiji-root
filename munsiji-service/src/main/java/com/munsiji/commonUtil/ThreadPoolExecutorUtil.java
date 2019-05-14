package com.munsiji.commonUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutorUtil {
	
	private static final ExecutorService es= Executors.newFixedThreadPool(50);
	public static ExecutorService getExecutorService(){
		return es;
	}

}
