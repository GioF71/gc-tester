package com.giof71.demo.mem.usage.view.behaviour.statisticlayout.impl;

import com.giof71.demo.mem.usage.core.PerformanceStatistic;

class TransformerArgument {
	
	private final PerformanceStatistic performanceStatistic;

	TransformerArgument(PerformanceStatistic performanceStatistic) {
		this.performanceStatistic = performanceStatistic;
	}

	PerformanceStatistic getPerformanceStatistic() {
		return performanceStatistic;
	}
}
