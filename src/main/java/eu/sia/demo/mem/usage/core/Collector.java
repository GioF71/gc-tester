package eu.sia.demo.mem.usage.core;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Collector {
	
	public enum ExtractAction {
		CLEAN,
		NONE
	}
	
	StatisticEntryCreator getEntryCreator();
	int size();
	List<MetricEntry> getLastEntries(int timeDelta, TimeUnit timeunit, ExtractAction clean);
	void purgeBefore(long nanotime);
	void purgeBefore(int timeDelta, TimeUnit timeunit);
	void add(MetricEntry entry);
}
