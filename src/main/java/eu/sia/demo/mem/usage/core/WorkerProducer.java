package eu.sia.demo.mem.usage.core;

import eu.sia.demo.mem.usage.core.worker.Worker;

public interface WorkerProducer {
	Worker createWorker(WorkerConfiguration configuration);
}
