package eu.sia.demo.mem.usage.view.behaviour.statisticlayout.impl;

import eu.sia.demo.mem.usage.core.PerformanceStatistic;

@FunctionalInterface
interface Transformer {
	void accept(PerformanceStatistic p, ControlContainer c);
}
