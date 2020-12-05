package com.giof71.demo.mem.usage.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.core.Collector;
import com.giof71.demo.mem.usage.core.MetricEntry;
import com.giof71.demo.mem.usage.core.StatisticEntryCreator;
import com.giof71.demo.mem.usage.util.TimeUtil;

@Component
public class CollectorImpl implements Collector {
	
	@Autowired
	private StatisticEntryCreator creator;
	
	@Autowired
	private TimeUtil timeUtil;

	private final List<MetricEntry> entryList = new ArrayList<>(2000000);
	private Long lastNanoTime = null;
	
	private boolean canEnqueue(MetricEntry entry) {
		return lastNanoTime == null || entry.getCreationNanotime() >= lastNanoTime;
	}

	@Override
	public void add(MetricEntry entry) {
		synchronized(entryList) {
			if (canEnqueue(entry)) {
				entryList.add(entry);
				lastNanoTime = entry.getCreationNanotime();
			} else {
				// find position
				int foundIndex = -1;
				for (int i = entryList.size() - 1; foundIndex == -1 && i >= 0; --i) {
					MetricEntry current = entryList.get(i);
					if (current.getCreationNanotime() >= entry.getCreationNanotime()) {
						foundIndex = i;
					}
				}
				entryList.add(foundIndex, entry);
			}
		}
	}
	
	private final Comparator<MetricEntry> comparator = new Comparator<MetricEntry>() {

		@Override
		public int compare(MetricEntry left, MetricEntry right) {
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
	public List<MetricEntry> getLastEntries(long timeDelta, TimeUnit timeUnit, ExtractAction clean) {
		long lowest = timeUtil.getLowest(timeDelta, timeUnit);
		List<MetricEntry> list = new ArrayList<>();
		synchronized(entryList) {
			//entryList.sort(comparator);
			boolean keepAdding = true;
			for (int i = entryList.size() - 1; keepAdding && i >= 0; --i) {
				MetricEntry current = entryList.get(i);
				keepAdding = current.getCreationNanotime() >= lowest;
				if (keepAdding) {
					list.add(current);
				}
			}
			if (ExtractAction.CLEAN.equals(clean)) {
				entryList.clear();
			}
		}
		Collections.reverse(list);
		return list;
	}

	@Override
	public void purgeAll() {
		synchronized(entryList) {
			entryList.clear();
			lastNanoTime = null;
		}
	}

	@Override
	public void purgeBefore(long timeDelta, TimeUnit timeUnit) {
		long lowest = timeUtil.getLowest(timeDelta, timeUnit);
		purgeOlder(lowest);
	}

	@Override
	public void purgeOlder(long nanotime) {
		synchronized(entryList) {
			entryList.sort(comparator);
			Iterator<MetricEntry> it = entryList.iterator();
			boolean found = false;
			while (!found && it.hasNext()) {
				MetricEntry current = it.next();
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
