package eu.sia.demo.mem.usage.core.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.Collector;
import eu.sia.demo.mem.usage.core.Collector.ExtractAction;
import eu.sia.demo.mem.usage.core.CoreCache;
import eu.sia.demo.mem.usage.core.DisplayBuffer;
import eu.sia.demo.mem.usage.core.KeyContainer;
import eu.sia.demo.mem.usage.core.MetricEntry;
import eu.sia.demo.mem.usage.core.PerformanceMetricExtractor;
import eu.sia.demo.mem.usage.core.PerformanceStatistic;
import eu.sia.demo.mem.usage.util.Wrapper;

@Component
public class DisplayBufferImpl implements DisplayBuffer {

	@Autowired
	private KeyContainer keyContainer;

	@Autowired
	private Collector collector;

	@Autowired
	private PerformanceMetricExtractor performanceMetricExtractor;

	@Autowired
	private CoreCache coreCache;

	class RequestedStatistic {

		private final String name;
		private Long delta;
		private TimeUnit timeUnit;

		RequestedStatistic(String name, Long delta, TimeUnit timeUnit) {
			super();
			this.name = name;
			this.delta = delta;
			this.timeUnit = timeUnit;
		}

		String getName() {
			return name;
		}

		Long getDelta() {
			return delta;
		}

		TimeUnit getTimeUnit() {
			return timeUnit;
		}
	}

	private final Wrapper<Long> maxDeltaMilliSec = new Wrapper<>();
	private final Map<String, RequestedStatistic> requestedStatisticList = new HashMap<>();
	private final Map<String, PerformanceStatistic> currentPerformanceStatisticMap = new HashMap<>();
	private Wrapper<Integer> keyContainerSizeWrapper = new Wrapper<>();
	private Wrapper<Integer> cacheSizeWrapper = new Wrapper<>();
	private Wrapper<PerformanceStatistic> oneSecWrapper = new Wrapper<>();
	private Wrapper<PerformanceStatistic> fiveSecWrapper = new Wrapper<>();
	private Wrapper<PerformanceStatistic> oneMinuteWrapper = new Wrapper<>();

	private static final long ADDITIONAL_DELTA_MILLISEC = 1000;

	@PostConstruct
	private void postConstruct() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				while (true) {
					collectKeyCount();
					collectCacheSize();
					collectStatistics();
					try {
						Thread.sleep(250);
						Thread.yield();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	@Override
	public void requestStatistic(String name, long delta, TimeUnit timeUnit) {
		if (delta <= 0) {
			throw new IllegalArgumentException("The delta parameter must be positive");
		}
		synchronized (requestedStatisticList) {
			if (!requestedStatisticList.containsKey(name)) {
				requestedStatisticList.put(name, new RequestedStatistic(name, delta, timeUnit));
			} else {
				throw new IllegalArgumentException(String.format("%s with name %s already available",
						RequestedStatistic.class.getSimpleName(), name));
			}
		}
		synchronized (maxDeltaMilliSec) {
			long deltaMilliSec = timeUnit.toMillis(delta);
			long current = Optional.ofNullable(maxDeltaMilliSec).map(Wrapper::get).orElse(0L);
			if (current == 0 || current < deltaMilliSec) {
				maxDeltaMilliSec.set(deltaMilliSec);
			}
		}
	}

	@Override
	public int getContainerSize() {
		synchronized (keyContainerSizeWrapper) {
			return Optional.of(keyContainerSizeWrapper).map(Wrapper::get).orElse(0);
		}
	}

	@Override
	public int getCacheSize() {
		synchronized (cacheSizeWrapper) {
			return Optional.of(cacheSizeWrapper).map(Wrapper::get).orElse(0);
		}
	}

	@Override
	public PerformanceStatistic getPerformanceStatistic(String name) {
		synchronized (currentPerformanceStatisticMap) {
			return currentPerformanceStatisticMap.get(name);
		}
	}

	@Override
	public PerformanceStatistic getOneSecPerformanceStatistic() {
		synchronized (oneSecWrapper) {
			return Optional.of(oneSecWrapper).map(Wrapper::get).orElse(null);
		}
	}

	@Override
	public PerformanceStatistic getFiveSecPerformanceStatistic() {
		synchronized (fiveSecWrapper) {
			return Optional.of(fiveSecWrapper).map(Wrapper::get).orElse(null);
		}
	}

	@Override
	public PerformanceStatistic getOneMinutePerformanceStatistic() {
		synchronized (oneMinuteWrapper) {
			return Optional.of(oneMinuteWrapper).map(Wrapper::get).orElse(null);
		}
	}

	private void collectStatistics() {
		Long deltaMilliSec = null;
		synchronized(maxDeltaMilliSec) {
			deltaMilliSec = Optional.ofNullable(maxDeltaMilliSec).map(Wrapper::get).orElse(0L);
		}
		Collection<RequestedStatistic> toCalculateList = null;
		synchronized(requestedStatisticList) {
			toCalculateList = requestedStatisticList.values();
		}
		List<MetricEntry> list = collector.getLastEntries(deltaMilliSec, TimeUnit.MILLISECONDS, ExtractAction.NONE);
		for (RequestedStatistic requestedStatistic : toCalculateList) {
			PerformanceStatistic statistic = performanceMetricExtractor.calculate(
				list, 
				requestedStatistic.getDelta(), 
				requestedStatistic.getTimeUnit());
			synchronized(currentPerformanceStatisticMap) {
				currentPerformanceStatisticMap.put(requestedStatistic.getName(), statistic);
			}
		}
		collector.purgeBefore(deltaMilliSec + ADDITIONAL_DELTA_MILLISEC, TimeUnit.MILLISECONDS);
	}

	private void collectKeyCount() {
		int sz = keyContainer.size();
		synchronized (keyContainerSizeWrapper) {
			keyContainerSizeWrapper.set(sz);
		}
	}

	private void collectCacheSize() {
		int sz = coreCache.size();
		synchronized (cacheSizeWrapper) {
			cacheSizeWrapper.set(sz);
		}
	}
}
