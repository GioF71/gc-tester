package com.giof71.demo.mem.usage.core;

import com.giof71.demo.mem.usage.core.worker.Worker;

public interface WorkerManager {
	int getNumberOfWorkers();
	Worker createWorker(WorkerConfiguration workerManagerConfiguration);
	void reset();
}
