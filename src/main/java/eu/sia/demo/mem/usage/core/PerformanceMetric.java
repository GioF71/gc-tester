package eu.sia.demo.mem.usage.core;

public interface PerformanceMetric {
	String getName();
	int getCount();
	Float getElapsedMin();
	Float getElapsedMax();
	Float getElapsedAvg();
}
