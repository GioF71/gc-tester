package eu.sia.demo.mem.usage.core;

import java.util.concurrent.TimeUnit;

public interface DisplayBuffer {
	int getContainerSize();
	int getCacheSize();
	void requestStatistic(String name, long delta, TimeUnit timeUnit);
	PerformanceStatistic getPerformanceStatistic(String name);
	@Deprecated
	PerformanceStatistic getOneSecPerformanceStatistic();
	@Deprecated
	PerformanceStatistic getFiveSecPerformanceStatistic();
	@Deprecated
	PerformanceStatistic getOneMinutePerformanceStatistic();
}
