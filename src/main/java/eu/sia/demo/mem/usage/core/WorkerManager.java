package eu.sia.demo.mem.usage.core;

import eu.sia.demo.mem.usage.core.worker.Worker;

public interface WorkerManager {
	int getNumberOfWorkers();
	Worker createWorker(WorkerConfiguration workerManagerConfiguration);
}
