package eu.sia.demo.mem.usage.core;

public interface DisplayBuffer {
	
	int getContainerSize();
	int getCacheSize();
	PerformanceStatistic getOneSecPerformanceStatistic();
	PerformanceStatistic getFiveSecPerformanceStatistic();
	PerformanceStatistic getOneMinutePerformanceStatistic();
}
