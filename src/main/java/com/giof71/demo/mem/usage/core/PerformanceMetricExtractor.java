package com.giof71.demo.mem.usage.core;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface PerformanceMetricExtractor {
	PerformanceStatistic calculate(List<MetricEntry> list, long timeDelta, TimeUnit timeUnit);
}
