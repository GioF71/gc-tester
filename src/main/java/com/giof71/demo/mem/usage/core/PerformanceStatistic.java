package com.giof71.demo.mem.usage.core;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public interface PerformanceStatistic {
	long getTimeDelta();
	TimeUnit getTimeUnit();
	Calendar creationTimeStamp();
	Long getOldestNanoTime();
	Long getNewestNanoTime();
	Integer getCount();
	Float getElapsedMin();
	Float getElapsedMax();
	Float getElapsedAvg();
}
