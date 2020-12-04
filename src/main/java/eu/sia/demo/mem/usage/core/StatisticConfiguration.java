package eu.sia.demo.mem.usage.core;

import java.util.Collection;

public interface StatisticConfiguration {
	Collection<RequestedStatistic> getRequestedStatistics();
}
