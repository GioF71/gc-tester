package eu.sia.demo.mem.usage.core.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.CoreCache;
import eu.sia.demo.mem.usage.core.PerformanceMetricConsumer;
import eu.sia.demo.mem.usage.data.CacheItem;
import eu.sia.demo.mem.usage.util.TimeUtil;

@Component
public class CoreCacheImpl implements CoreCache {
	
	@Autowired
	private TimeUtil timeUtil;
	
	private final Map<String, CacheItem> data = new HashMap<>();

	@Override
	public int size() {
		synchronized(data) {
			return data.size();
		}
	}

	@Override
	public boolean containsKey(String key) {
		synchronized(data) {
			return data.containsKey(key);
		}
	}

	@Override
	public CacheItem put(String key, CacheItem cacheItem) {
		synchronized(data) {
			return data.put(key, cacheItem);
		}
	}

	@Override
	public CacheItem get(String key) {
		synchronized(data) {
			return data.get(key);
		}
	}

	@Override
	public CacheItem update(String key) {
		return update(key, null);
	}

	@Override
	public CacheItem update(String key, PerformanceMetricConsumer performanceMetricConsumer) {
		CacheItem result;
		Long latencyNanoTime;
		Long begin = System.nanoTime();
		synchronized(data) {
			latencyNanoTime = System.nanoTime() - begin; 
			CacheItem item = data.get(key);
			if (item == null) {
				item = new CacheItem(key);
			} else {
				item = item.update();
			}
			result = data.put(key, item);
		}
		if (performanceMetricConsumer != null) {
			Long elapsed = System.nanoTime() - begin;
			performanceMetricConsumer.accept(
				timeUtil.nanoToMilli(latencyNanoTime), 
				timeUtil.nanoToMilli(elapsed));
		}
		return result;
	}
}

