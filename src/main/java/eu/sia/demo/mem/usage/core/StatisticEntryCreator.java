package eu.sia.demo.mem.usage.core;

public interface StatisticEntryCreator {
	StatisticEntry create(Float latency, Float elapsed);
}
