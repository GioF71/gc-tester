package com.giof71.demo.mem.usage.core;

import java.util.Calendar;

public interface PerformanceStatistic {
	Calendar creationTimeStamp();
	long getOldestNanoTime();
	int getCount();
	Float getElapsedMin();
	Float getElapsedMax();
	Float getElapsedAvg();
}
