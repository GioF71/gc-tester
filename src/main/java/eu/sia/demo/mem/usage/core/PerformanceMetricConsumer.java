package eu.sia.demo.mem.usage.core;

@FunctionalInterface
public interface PerformanceMetricConsumer {
	void accept(Float latencyMillisec, Float elapsedMillisec);
}
