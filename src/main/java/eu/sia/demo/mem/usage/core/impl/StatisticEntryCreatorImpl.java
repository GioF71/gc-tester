package eu.sia.demo.mem.usage.core.impl;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.MetricEntry;
import eu.sia.demo.mem.usage.core.StatisticEntryCreator;

@Component
public class StatisticEntryCreatorImpl implements StatisticEntryCreator {

	@Override
	public MetricEntry create(Float latency, Float elapsed) {
		return new MetricEntryImpl(elapsed, latency);
	}

}
