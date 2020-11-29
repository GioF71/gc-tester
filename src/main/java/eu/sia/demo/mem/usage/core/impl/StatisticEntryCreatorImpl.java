package eu.sia.demo.mem.usage.core.impl;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.StatisticEntry;
import eu.sia.demo.mem.usage.core.StatisticEntryCreator;

@Component
public class StatisticEntryCreatorImpl implements StatisticEntryCreator {

	@Override
	public StatisticEntry create(Float latency, Float elapsed) {
		// TODO Auto-generated method stub
		return new StatisticEntryImpl(elapsed, latency);
	}

}
