package eu.sia.demo.mem.usage.core;

import java.util.List;

public interface PerformanceMetricExtractor {
	PerformanceMetric get(String name, List<MetricEntry> list);
}
