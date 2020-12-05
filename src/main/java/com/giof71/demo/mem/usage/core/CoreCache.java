package com.giof71.demo.mem.usage.core;

import com.giof71.demo.mem.usage.data.CacheItem;

public interface CoreCache {
	int size();
	boolean containsKey(String key);
	CacheItem get(String key);
	CacheItem put(String key, CacheItem cacheItem);
	CacheItem update(String key);
	CacheItem update(String key, PerformanceMetricConsumer performanceMetricConsumer);
}
