package com.giof71.demo.mem.usage.core;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Collector {
	
	public enum ExtractAction {
		CLEAN,
		NONE
	}
	
	StatisticEntryCreator getEntryCreator();
	int size();
	List<MetricEntry> getLastEntries(long timeDelta, TimeUnit timeunit, ExtractAction clean);
	void purgeOlder(long nanotime);
	void purgeBefore(long timeDelta, TimeUnit timeunit);
	void add(MetricEntry entry);
}
