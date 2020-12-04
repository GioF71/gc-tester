package eu.sia.demo.mem.usage.view.behaviour.statisticlayout.impl;

import eu.sia.demo.mem.usage.core.PerformanceStatistic;

class TransformerArgument {
	
	private final PerformanceStatistic performanceStatistic;

	TransformerArgument(PerformanceStatistic performanceStatistic) {
		this.performanceStatistic = performanceStatistic;
	}

	PerformanceStatistic getPerformanceStatistic() {
		return performanceStatistic;
	}
}
