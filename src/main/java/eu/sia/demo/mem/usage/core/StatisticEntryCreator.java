package eu.sia.demo.mem.usage.core;

public interface StatisticEntryCreator {
	MetricEntry create(Float latency, Float elapsed);
}
