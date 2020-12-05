package com.giof71.demo.mem.usage.core;

public interface StatisticEntryCreator {
	MetricEntry create(Float latencyMilliSec, Float elapsedMilliSec);
}
