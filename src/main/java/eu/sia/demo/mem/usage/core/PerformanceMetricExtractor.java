package eu.sia.demo.mem.usage.core;

import java.util.concurrent.TimeUnit;

public interface PerformanceMetricExtractor {
	PerformanceMetric get(String name, int timeDelta, TimeUnit timeunit);
}
