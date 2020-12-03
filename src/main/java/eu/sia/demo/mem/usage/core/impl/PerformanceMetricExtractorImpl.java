package eu.sia.demo.mem.usage.core.impl;

import java.util.List;
import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.PerformanceMetric;
import eu.sia.demo.mem.usage.core.PerformanceMetricExtractor;
import eu.sia.demo.mem.usage.core.MetricEntry;

@Component
public class PerformanceMetricExtractorImpl implements PerformanceMetricExtractor {

	class Metric implements PerformanceMetric {

		private final String name;
		private final int count;
		private final Float elapsedMin;
		private final Float elapsedMax;
		private final Float elapsedAvg;
		
		Metric(String name, int count, Float elapsedMin, Float elapsedMax, Float elapsedAvg) {
			this.name = name;
			this.count = count;
			this.elapsedMin = elapsedMin;
			this.elapsedMax = elapsedMax;
			this.elapsedAvg = elapsedAvg;
		}

		@Override
		public String getName() {
			return name;
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
	}

	@Override
	public PerformanceMetric get(String name, List<MetricEntry> list) {
		Float avg = null, min = null, max = null;
		Float totalElapsed = 0.0f;
		int count = 0;
		for (MetricEntry e : list) {
			++count;
			Float currentElapsed = e.getElapsed();
			totalElapsed += currentElapsed;
			min = update(min, currentElapsed, (c, n) -> n < c);
			max = update(max, currentElapsed, (c, n) -> n > c);
		}
		avg = count > 0 ? totalElapsed / count : null;
		return new Metric(name, count, min, max, avg);
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
