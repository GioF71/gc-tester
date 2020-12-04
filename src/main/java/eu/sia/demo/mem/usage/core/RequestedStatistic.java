package eu.sia.demo.mem.usage.core;

import java.util.concurrent.TimeUnit;

public interface RequestedStatistic {
	String getName();
	long getDelta();
 	TimeUnit getTimeUnit(); 
}
