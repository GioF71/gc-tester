package com.giof71.demo.mem.usage.core;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public interface PerformanceStatistic {
	long getTimeDelta();
	TimeUnit getTimeUnit();
	Calendar creationTimeStamp();
	long getOldestNanoTime();
	long getNewestNanoTime();
	int getCount();
	Float getElapsedMin();
	Float getElapsedMax();
	Float getElapsedAvg();
}
