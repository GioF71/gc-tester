package eu.sia.demo.mem.usage.core.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.Collector;
import eu.sia.demo.mem.usage.core.StatisticEntry;
import eu.sia.demo.mem.usage.core.StatisticEntryCreator;

@Component
public class CollectorImpl implements Collector {
	
	@Autowired
	private StatisticEntryCreator creator;

	private final TreeSet<StatisticEntry> entryList = new TreeSet<>();

	@Override
	public void add(StatisticEntry entry) {
		synchronized(entryList) {
			entryList.add(entry);
		}
	}

	@Override
	public List<StatisticEntry> getLastEntries(int timeDelta, TimeUnit timeunit) {
		long lowest = System.nanoTime();
		lowest -= timeunit.toMicros(timeDelta) * 1000;
		List<StatisticEntry> list = new ArrayList<>();
		synchronized(entryList) {
			Iterator<StatisticEntry> it = entryList.descendingIterator();
			while (it.hasNext()) {
				StatisticEntry current = it.next();
				if (current.getCreationNanotime() >= lowest) {
					list.add(current);
				}
			}
		}
		return list;
	}

	@Override
	public void purgeBefore(int timeDelta, TimeUnit timeunit) {
		long lowest = System.nanoTime();
		lowest -= timeunit.toMicros(timeDelta) * 1000;
		purgeBefore(lowest);
	}

	@Override
	public void purgeBefore(long nanotime) {
		synchronized(entryList) {
			Iterator<StatisticEntry> it = entryList.iterator();
			boolean found = false;
			while (!found && it.hasNext()) {
				StatisticEntry current = it.next();
				if (current.getCreationNanotime() >= nanotime) {
					found = true;
				} else {
					it.remove();
				}
			}
		}
	}

	@Override
	public int size() {
		synchronized(entryList) {
			return entryList.size();
		}
	}

	@Override
	public StatisticEntryCreator getEntryCreator() {
		return creator;
	}
}
