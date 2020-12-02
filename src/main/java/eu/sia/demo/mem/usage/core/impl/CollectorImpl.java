package eu.sia.demo.mem.usage.core.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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

	private final List<StatisticEntry> entryList = new ArrayList<>(2000000);

	@Override
	public void add(StatisticEntry entry) {
		synchronized(entryList) {
			entryList.add(entry);
		}
	}
	
	private final Comparator<StatisticEntry> comparator = new Comparator<StatisticEntry>() {

		@Override
		public int compare(StatisticEntry left, StatisticEntry right) {
			long l = left.getCreationNanotime();
			long r = right.getCreationNanotime();
			if (l == r) {
				return 0;
			} else if (l < r) {
				return -1;
			}
			return 1;
		}
	};

	@Override
	public List<StatisticEntry> getLastEntries(int timeDelta, TimeUnit timeunit, ExtractAction clean) {
		long lowest = getLowest(timeDelta, timeunit);
		List<StatisticEntry> list = new ArrayList<>();
		synchronized(entryList) {
			entryList.sort(comparator);
			boolean keepAdding = true;
			for (int i = entryList.size() - 1; keepAdding && i >= 0; --i) {
				StatisticEntry current = entryList.get(i);
				keepAdding = current.getCreationNanotime() >= lowest;
				if (keepAdding) {
					list.add(current);
				}
			}
			if (ExtractAction.CLEAN.equals(clean)) {
				entryList.clear();
			}
		}
		return list;
	}

	@Override
	public void purgeBefore(int timeDelta, TimeUnit timeunit) {
		long lowest = getLowest(timeDelta, timeunit);
		purgeBefore(lowest);
	}

	@Override
	public void purgeBefore(long nanotime) {
		synchronized(entryList) {
			entryList.sort(comparator);
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

	private long getLowest(int timeDelta, TimeUnit timeunit) {
		long lowest = System.nanoTime();
		lowest -= timeunit.toMicros(timeDelta) * 1000;
		return lowest;
	}
}
