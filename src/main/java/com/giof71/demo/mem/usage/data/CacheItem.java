package com.giof71.demo.mem.usage.data;

import java.util.Calendar;

public final class CacheItem {
	
	private final String key;
	private Calendar creationTimestamp;
	private Calendar updateTimestamp;
	private int updateCount = 0;

	private CacheItem(String key, Calendar creationTimestamp) {
		this.key = key;
		this.creationTimestamp = creationTimestamp;
		this.updateTimestamp = creationTimestamp;
	}

	public CacheItem(String key) {
		this(key, Calendar.getInstance());
	}

	public String getKey() {
		return key;
	}

	public Calendar getCreationTimestamp() {
		return creationTimestamp;
	}

	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public CacheItem update() {
		CacheItem updated = new CacheItem(key, creationTimestamp);
		updated.updateTimestamp = Calendar.getInstance();
		updated.updateCount = updateCount + 1;
		return updated;
	}
}
