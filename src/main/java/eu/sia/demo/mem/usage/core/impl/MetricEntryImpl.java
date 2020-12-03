package eu.sia.demo.mem.usage.core.impl;

import eu.sia.demo.mem.usage.core.MetricEntry;

class MetricEntryImpl implements MetricEntry {

	private Long creationNanoTime = System.nanoTime();
	private float elapsed;
	private float latency = 0.0f;
		
	public MetricEntryImpl(float elapsed, float latency) {
		this.elapsed = elapsed;
		this.latency = latency;
	}

	@Override
	public float getElapsed() {
		return elapsed;
	}

	@Override
	public float getLatency() {
		return latency;
	}

	@Override
	public int compareTo(MetricEntry o) {
		if (creationNanoTime < o.getCreationNanotime()) {
			return -1;
		} else if (creationNanoTime > o.getCreationNanotime()) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getCreationNanotime() {
		return creationNanoTime;
	}
}
