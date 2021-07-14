package com.giof71.demo.mem.usage.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.core.RequestedStatistic;
import com.giof71.demo.mem.usage.core.StatisticConfiguration;

@Component
public class StatisticConfigurationImpl implements StatisticConfiguration {
	
	private final Collection<RequestedStatistic> requestedStatistics = new ArrayList<>();
	
	@PostConstruct
	private void postConstruct() {
		requestedStatistics.add(buildRequestedStatistic("OneSec", 1, TimeUnit.SECONDS));
		requestedStatistics.add(buildRequestedStatistic("FiveSec", 5, TimeUnit.SECONDS));
		requestedStatistics.add(buildRequestedStatistic("OneMinute", 1, TimeUnit.MINUTES));
	}

	@Override
	public Collection<RequestedStatistic> getRequestedStatistics() {
		return Collections.unmodifiableCollection(requestedStatistics);
	}

	private RequestedStatistic buildRequestedStatistic(String name, long delta, TimeUnit timeUnit) {
		return new RequestedStatistic() {
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public long getDelta() {
				return delta;
			}
			
			@Override
			public TimeUnit getTimeUnit() {
				return timeUnit;
			}
		};
	}
}
