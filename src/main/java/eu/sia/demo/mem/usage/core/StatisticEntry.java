package eu.sia.demo.mem.usage.core;

public interface StatisticEntry extends Comparable<StatisticEntry> {
	long getCreationNanotime();
	float getElapsed();
	float getLatency();
}