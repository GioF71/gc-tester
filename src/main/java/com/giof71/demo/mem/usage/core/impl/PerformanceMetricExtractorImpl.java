package com.giof71.demo.mem.usage.core.impl;

import java.util.Calendar;
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

	class LocalPerformanceStatistic implements PerformanceStatistic {

		private final Calendar creationTimeStamp = Calendar.getInstance();
		private final int count;
		private final Float elapsedMin;
		private final Float elapsedMax;
		private final Float elapsedAvg;
		private final long oldestNanoTime;
		
		LocalPerformanceStatistic(
				int count, 
				Float elapsedMin, 
				Float elapsedMax, 
				Float elapsedAvg, 
				long oldestNanoTime) {
			this.count = count;
			this.elapsedMin = elapsedMin;
			this.elapsedMax = elapsedMax;
			this.elapsedAvg = elapsedAvg;
			this.oldestNanoTime = oldestNanoTime;
		}

		@Override
		public Calendar creationTimeStamp() {
			return creationTimeStamp;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public Float getElapsedMin() {
			return elapsedMin;
		}

		@Override
		public Float getElapsedMax() {
			return elapsedMax;
		}

		@Override
		public Float getElapsedAvg() {
			return elapsedAvg;
		}

		@Override
		public long getOldestNanoTime() {
			return oldestNanoTime;
		}
	}

	@Override
	public PerformanceStatistic calculate(List<MetricEntry> metricEntryList, long timeDelta, TimeUnit timeUnit) {
		long lowest = timeUtil.getLowest(timeDelta, timeUnit);
		Float avg = null, min = null, max = null;
		Float totalElapsed = 0.0f;
		int count = 0;
		long oldestNanoTime = -1;
		boolean keepAdding = true;
		for (int i = metricEntryList.size() - 1; keepAdding && i >= 0; --i) {
			MetricEntry e = metricEntryList.get(i);
			keepAdding = e.getCreationNanotime() >= lowest;
			if (keepAdding) {
				++count;
				Float currentElapsed = e.getElapsed();
				totalElapsed += currentElapsed;
				min = update(min, currentElapsed, (c, n) -> n < c);
				max = update(max, currentElapsed, (c, n) -> n > c);
				long currentNanoTime = e.getCreationNanotime();
				if (oldestNanoTime == -1 || oldestNanoTime > currentNanoTime) {
					oldestNanoTime = currentNanoTime;
				}
			}
		}
		avg = count > 0 ? totalElapsed / count : null;
		return new LocalPerformanceStatistic(count, min, max, avg, oldestNanoTime);
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
