package eu.sia.demo.mem.usage.core.impl;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.WorkerConfiguration;
import eu.sia.demo.mem.usage.core.WorkerProducer;
import eu.sia.demo.mem.usage.core.worker.Worker;
import eu.sia.demo.mem.usage.core.worker.impl.WorkerImpl;

@Component
public class WorkerProducerImpl implements WorkerProducer {

	@Override
	public Worker createWorker(WorkerConfiguration configuration) {
		return new WorkerImpl(configuration);
	}
}
