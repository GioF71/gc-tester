package com.giof71.demo.mem.usage.view.behaviour.statisticlayout.impl;

class StatisticFieldMapping {
	
	private final StatisticField statisticField;
	private final Transformer transformer;
	
	StatisticFieldMapping(
		StatisticField statisticField,
		Transformer transformer) {
		this.statisticField = statisticField;
		this.transformer = transformer;
	}

	StatisticField getStatisticField() {
		return statisticField;
	}

	Transformer getTransformer() {
		return transformer;
	}
}
