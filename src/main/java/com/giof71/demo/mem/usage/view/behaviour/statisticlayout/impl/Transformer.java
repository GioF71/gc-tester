package com.giof71.demo.mem.usage.view.behaviour.statisticlayout.impl;

import com.giof71.demo.mem.usage.core.PerformanceStatistic;

@FunctionalInterface
interface Transformer {
	void accept(PerformanceStatistic performanceStatistic, ControlContainer controlContainer);
}
