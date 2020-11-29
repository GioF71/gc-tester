package eu.sia.demo.mem.usage.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.WorkerConfiguration;
import eu.sia.demo.mem.usage.core.WorkerManager;
import eu.sia.demo.mem.usage.core.worker.Worker;
import eu.sia.demo.mem.usage.core.worker.impl.WorkerImpl;

@Component
public class WorkerManagerImpl implements WorkerManager {
	
	private final List<Worker> workerList = new ArrayList<>();
	private final List<Runnable> runnableList = new ArrayList<>();

	@Override
	public synchronized int getNumberOfWorkers() {
		return workerList.size();
	}

	@Override
	public Worker createWorker(WorkerConfiguration workerManagerConfiguration) {
		Worker worker = new WorkerImpl(workerManagerConfiguration);
		workerList.add(worker);
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				worker.run();
			}
		};
		Thread t = new Thread(runnable);
		runnableList.add(runnable);
		t.start();
		return worker;
	}
}
