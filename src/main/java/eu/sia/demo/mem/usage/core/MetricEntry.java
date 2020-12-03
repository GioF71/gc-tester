package eu.sia.demo.mem.usage.core;

public interface MetricEntry extends Comparable<MetricEntry> {
	long getCreationNanotime();
	float getElapsed();
	float getLatency();
}