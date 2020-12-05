package com.giof71.demo.mem.usage.core.statistic.impl;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.giof71.demo.mem.usage.core.PerformanceStatistic;

class LocalPerformanceStatistic implements PerformanceStatistic {

	private final Calendar creationTimeStamp;
	private final Integer count;
	private final long timeDelta;
	private final TimeUnit timeUnit;
	private final Float elapsedMin;
	private final Float elapsedMax;
	private final Float elapsedAvg;
	private final Long oldestNanoTime;
	private final Long newestNanoTime;
	
	private LocalPerformanceStatistic(Builder builder) {
		this.creationTimeStamp = builder.creationTimeStamp;
		this.timeDelta = builder.timeDelta;
		this.timeUnit = builder.timeUnit;
		this.count = builder.count;
		this.elapsedAvg = builder.elapsedAvg;
		this.elapsedMax = builder.elapsedMax;
		this.elapsedMin = builder.elapsedMin;
		this.oldestNanoTime = builder.oldestNanoTime;
		this.newestNanoTime = builder.newestNanoTime;
	}

	@Deprecated
	LocalPerformanceStatistic(long timeDelta, TimeUnit timeUnit, int count, Float elapsedMin, Float elapsedMax,
			Float elapsedAvg, Long oldestNanoTime, Long newestNanoTime) {
		this.creationTimeStamp = Calendar.getInstance();
		this.timeDelta = timeDelta;
		this.timeUnit = timeUnit;
		this.count = count;
		this.elapsedMin = elapsedMin;
		this.elapsedMax = elapsedMax;
		this.elapsedAvg = elapsedAvg;
		this.oldestNanoTime = oldestNanoTime;
		this.newestNanoTime = newestNanoTime;
	}

	@Override
	public Calendar creationTimeStamp() {
		return creationTimeStamp;
	}

	@Override
	public long getTimeDelta() {
		return timeDelta;
	}

	@Override
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	@Override
	public Integer getCount() {
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
	public Long getOldestNanoTime() {
		return oldestNanoTime;
	}

	@Override
	public Long getNewestNanoTime() {
		return newestNanoTime;
	}
	
	static Builder builder(long timeDelta, TimeUnit timeUnit) {
		return new Builder(timeDelta, timeUnit);
	}
	
	static class Builder {
		
		private final Calendar creationTimeStamp = Calendar.getInstance();
		private final long timeDelta;
		private final TimeUnit timeUnit;
		private Integer count;
		private Float elapsedMin;
		private Float elapsedMax;
		private Float elapsedAvg;
		private Long oldestNanoTime;
		private Long newestNanoTime;
		
		private Builder(long timeDelta, TimeUnit timeUnit) {
			this.timeDelta = timeDelta;
			this.timeUnit = timeUnit;
		}
		
		Builder count(Integer value) {
			this.count = value;
			return this;
		}

		Builder elapsedMin(Float value) {
			this.elapsedMin = value;
			return this;
		}

		Builder elapsedMax(Float value) {
			this.elapsedMax = value;
			return this;
		}

		Builder elapsedAvg(Float value) {
			this.elapsedAvg = value;
			return this;
		}

		Builder oldestNanoTime(Long value) {
			this.oldestNanoTime = value;
			return this;
		}

		Builder newestNanoTime(Long value) {
			this.newestNanoTime = value;
			return this;
		}
		
		LocalPerformanceStatistic build() {
			return new LocalPerformanceStatistic(this);
		}
	}
}
