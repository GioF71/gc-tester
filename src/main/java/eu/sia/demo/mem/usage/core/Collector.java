package eu.sia.demo.mem.usage.core;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Collector {
	StatisticEntryCreator getEntryCreator();
	int size();
	List<StatisticEntry> getLastEntries(int timeDelta, TimeUnit timeunit);
	void purgeBefore(long nanotime);
	void purgeBefore(int timeDelta, TimeUnit timeunit);
	void add(StatisticEntry entry);
}
