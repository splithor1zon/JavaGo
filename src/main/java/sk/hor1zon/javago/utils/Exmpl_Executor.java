package sk.hor1zon.javago.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exmpl_Executor {
	static final int THREAD_COUNT = 8;

	ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
	//exec.submit(new Exapl_Runnable());
	
	
}
