package eu.sia.demo.mem.usage.core.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helger.commons.wrapper.Wrapper;

import eu.sia.demo.mem.usage.core.Collector;
import eu.sia.demo.mem.usage.core.Collector.ExtractAction;
import eu.sia.demo.mem.usage.core.CoreCache;
import eu.sia.demo.mem.usage.core.DisplayBuffer;
import eu.sia.demo.mem.usage.core.KeyContainer;
import eu.sia.demo.mem.usage.core.MetricEntry;
import eu.sia.demo.mem.usage.core.PerformanceMetricExtractor;
import eu.sia.demo.mem.usage.core.PerformanceStatistic;

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

	private Wrapper<Integer> keyContainerSizeWrapper = new Wrapper<>();
	private Wrapper<Integer> cacheSizeWrapper = new Wrapper<>();
	private Wrapper<PerformanceStatistic> oneSecWrapper = new Wrapper<>();
	private Wrapper<PerformanceStatistic> fiveSecWrapper = new Wrapper<>();
	private Wrapper<PerformanceStatistic> oneMinuteWrapper = new Wrapper<>();
	
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

			private void collectKeyCount() {
				int sz = keyContainer.size();
				synchronized(keyContainerSizeWrapper) {
					keyContainerSizeWrapper.set(sz);
				}
			}
			private void collectCacheSize() {
				int sz = coreCache.size();
				synchronized(cacheSizeWrapper) {
					cacheSizeWrapper.set(sz);
				}
			}
			
			private void collectStatistics() {
				List<MetricEntry> list = collector.getLastEntries(60, TimeUnit.SECONDS, ExtractAction.NONE);
				PerformanceStatistic oneSecStatistic = performanceMetricExtractor.calculate("One sec", list, 1, TimeUnit.SECONDS);
				synchronized(oneSecWrapper) {
					oneSecWrapper.set(oneSecStatistic);
				}
				PerformanceStatistic fiveSecStatistic = performanceMetricExtractor.calculate("Five sec", list, 5, TimeUnit.SECONDS);
				synchronized(fiveSecWrapper) {
					fiveSecWrapper.set(fiveSecStatistic);
				}
				PerformanceStatistic oneMinuteStatistic = performanceMetricExtractor.calculate("Sixty sec", list, 60, TimeUnit.SECONDS);
				synchronized(oneMinuteWrapper) {
					oneMinuteWrapper.set(oneMinuteStatistic);
				}
				collector.purgeBefore(90, TimeUnit.SECONDS);
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	@Override
	public int getContainerSize() {
		synchronized(keyContainerSizeWrapper) {
			return Optional.of(keyContainerSizeWrapper)
				.map(Wrapper::get)
				.orElse(0);
		}
	}

	@Override
	public int getCacheSize() {
		synchronized(cacheSizeWrapper) {
			return Optional.of(cacheSizeWrapper)
				.map(Wrapper::get)
				.orElse(0);
		}
	}

	@Override
	public PerformanceStatistic getOneSecPerformanceStatistic() {
		synchronized(oneSecWrapper) {
			return Optional.of(oneSecWrapper)
				.map(Wrapper::get)
				.orElse(null);
		}
	}

	@Override
	public PerformanceStatistic getFiveSecPerformanceStatistic() {
		synchronized(fiveSecWrapper) {
			return Optional.of(fiveSecWrapper)
				.map(Wrapper::get)
				.orElse(null);
		}
	}

	@Override
	public PerformanceStatistic getOneMinutePerformanceStatistic() {
		synchronized(oneMinuteWrapper) {
			return Optional.of(oneMinuteWrapper)
				.map(Wrapper::get)
				.orElse(null);
		}
	}
}
