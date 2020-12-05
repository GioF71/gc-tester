package com.giof71.demo.mem.usage.core;

import com.giof71.demo.mem.usage.core.worker.Worker;

public interface WorkerProducer {
	Worker createWorker(WorkerConfiguration configuration);
}
