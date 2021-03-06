package com.giof71.demo.mem.usage.view.behaviour.statisticlayout.impl;

enum StatisticField {
	
	NAME("Name"),
	CREATION_TIME("Creation Time"),
	OLDEST_AGE("Oldest Item Age"),
	NEWEST_AGE("Newest Item Age"),
	CNT("Count"),
	AVG("Avg"),
	SPEED("Speed"),
	MAX("Max"),
	MIN("Min");
	
	private String caption;
	private boolean processReadOnly;
	
	StatisticField(String caption, boolean processReadOnly) {
		this.caption = caption;
		this.processReadOnly = processReadOnly;
	}

	StatisticField(String caption) {
		this(caption, true);
	}

	String getCaption() {
		return caption;
	}

	boolean isProcessReadOnly() {
		return processReadOnly;
	}
}
