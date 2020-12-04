package eu.sia.demo.mem.usage.core;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface PerformanceMetricExtractor {
	PerformanceStatistic calculate(String name, List<MetricEntry> list);
	PerformanceStatistic calculate(String name, List<MetricEntry> list, long timeDelta, TimeUnit timeUnit);
}
