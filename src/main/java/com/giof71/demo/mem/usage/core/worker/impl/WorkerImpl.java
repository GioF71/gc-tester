package com.giof71.demo.mem.usage.core.worker.impl;

import com.giof71.demo.mem.usage.core.PerformanceMetricConsumer;
import com.giof71.demo.mem.usage.core.MetricEntry;
import com.giof71.demo.mem.usage.core.WorkerConfiguration;
import com.giof71.demo.mem.usage.core.worker.Worker;

public class WorkerImpl implements Worker {

	private final WorkerConfiguration workerConfiguration;
	private Boolean requestStop = Boolean.FALSE; 
	private final PerformanceMetricConsumer performanceMetricConsumer = new PerformanceMetricConsumer() {

		@Override
		public void accept(Float latencyMillisec, Float elapsedMillisec) {
			MetricEntry entry = workerConfiguration.getCollector().getEntryCreator().create(
				latencyMillisec,
				elapsedMillisec);
			workerConfiguration.getCollector().add(entry);
		}
	};

	public WorkerImpl(WorkerConfiguration workerConfiguration) {
		this.workerConfiguration = workerConfiguration;
	}

	@Override
	public void run() {
		boolean goOn = true;
		while (goOn) {
			action();
			try {
				if (workerConfiguration.sleepTimeMillisec() > 0) {
					Thread.sleep(workerConfiguration.sleepTimeMillisec());
				}
				Thread.yield();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			synchronized(this) {
				goOn = !requestStop;
			}
		}
	}

	@Override
	public void stop() {
		synchronized(this) {
			this.requestStop = Boolean.TRUE;
		}
	}

	private void action() {
		String key = getRandomKey();
		// update data
		if (key != null) {
			workerConfiguration.getCoreCache().update(key, performanceMetricConsumer);
		}
	}

	private String getRandomKey() {
		return workerConfiguration.getKeyContainer().getRandomKey();
	}
}
