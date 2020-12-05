package com.giof71.demo.mem.usage.core.impl;

import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.core.MetricEntry;
import com.giof71.demo.mem.usage.core.StatisticEntryCreator;

@Component
public class StatisticEntryCreatorImpl implements StatisticEntryCreator {

	@Override
	public MetricEntry create(Float latency, Float elapsed) {
		return new MetricEntryImpl(elapsed, latency);
	}

}
