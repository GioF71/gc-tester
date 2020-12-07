package com.giof71.demo.mem.usage.core.statistic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.core.MetricEntry;
import com.giof71.demo.mem.usage.core.PerformanceMetricExtractor;
import com.giof71.demo.mem.usage.core.PerformanceStatistic;
import com.giof71.demo.mem.usage.util.TimeUtil;

@Component
public class PerformanceMetricExtractorImpl implements PerformanceMetricExtractor {
	
	@Autowired
	private TimeUtil timeUtil;

	@Override
	public PerformanceStatistic calculate(List<MetricEntry> metricEntryList, long timeDelta, TimeUnit timeUnit) {
		long lowest = timeUtil.getLowest(timeDelta, timeUnit);
		Float avg = null, min = null, max = null;
		Float totalElapsed = 0.0f;
		int count = 0;
		Long oldestNanoTime = null;
		Long newestNanoTime = null;
		boolean keepAdding = true;
		List<Float> values = new ArrayList<>();
		for (int i = metricEntryList.size() - 1; keepAdding && i >= 0; --i) {
			MetricEntry e = metricEntryList.get(i);
			keepAdding = e.getCreationNanotime() >= lowest;
			if (keepAdding) {
				++count;
				Float currentElapsed = e.getElapsed();
				values.add(currentElapsed);
				totalElapsed += currentElapsed;
				min = update(min, currentElapsed, (c, n) -> n < c);
				max = update(max, currentElapsed, (c, n) -> n > c);
				long currentNanoTime = e.getCreationNanotime();
				if (oldestNanoTime == null || oldestNanoTime > currentNanoTime) {
					oldestNanoTime = currentNanoTime;
				}
				if (newestNanoTime == null || newestNanoTime < currentNanoTime) {
					newestNanoTime = currentNanoTime;
				}
			}
		}
		avg = count > 0 ? totalElapsed / count : null;
		// std dev
		float numerator = 0.0f;
		for (Float currentValue : values) {
			numerator += Math.pow((currentValue - avg), 2);
		}
		Double stdDev = Math.sqrt(numerator / ((float) values.size()));
		return LocalPerformanceStatistic.builder(timeDelta, timeUnit)
			.count(count)
			.elapsedAvg(avg)
			.elapsedMax(max)
			.elapsedMin(min)
			.elapsedStdDev(stdDev)
			.oldestNanoTime(oldestNanoTime)
			.newestNanoTime(newestNanoTime)
			.build();
	}

	private Float update(Float current, Float newer, BiFunction<Float, Float, Boolean> needsUpdate) {
		if (current != null) {
			if (needsUpdate.apply(current, newer)) {
				return newer;
			} else {
				return current;
			}
		} else {
			return newer;
		}
	}
}
