package com.giof71.demo.mem.usage.core.impl;

import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.core.WorkerConfiguration;
import com.giof71.demo.mem.usage.core.WorkerProducer;
import com.giof71.demo.mem.usage.core.worker.Worker;
import com.giof71.demo.mem.usage.core.worker.impl.WorkerImpl;

@Component
public class WorkerProducerImpl implements WorkerProducer {

	@Override
	public Worker createWorker(WorkerConfiguration configuration) {
		return new WorkerImpl(configuration);
	}
}
