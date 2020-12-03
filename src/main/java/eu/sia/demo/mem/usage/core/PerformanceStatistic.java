package eu.sia.demo.mem.usage.core;

import java.util.Calendar;

public interface PerformanceStatistic {
	String getName();
	Calendar creationTimeStamp();
	int getCount();
	Float getElapsedMin();
	Float getElapsedMax();
	Float getElapsedAvg();
}
