package eu.sia.demo.mem.usage.core.worker.impl;

import eu.sia.demo.mem.usage.core.PerformanceMetricConsumer;
import eu.sia.demo.mem.usage.core.MetricEntry;
import eu.sia.demo.mem.usage.core.WorkerConfiguration;
import eu.sia.demo.mem.usage.core.worker.Worker;

public class WorkerImpl implements Worker {

	private final WorkerConfiguration workerConfiguration;
	private final PerformanceMetricConsumer performanceMetricConsumer = new PerformanceMetricConsumer() {

		@Override
		public void accept(Float latencyMillisec, Float elapsedMillisec) {
			MetricEntry entry = workerConfiguration.getCollector().getEntryCreator().create(latencyMillisec,
					elapsedMillisec);
			workerConfiguration.getCollector().add(entry);
		}
	};

	public WorkerImpl(WorkerConfiguration workerConfiguration) {
		this.workerConfiguration = workerConfiguration;
	}

	@Override
	public void run() {
		while (true) {
			action();
			try {
				if (workerConfiguration.sleepTimeMillisec() > 0) {
					Thread.sleep(workerConfiguration.sleepTimeMillisec());
				}
				Thread.yield();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
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
