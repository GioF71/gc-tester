package com.giof71.demo.mem.usage.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.core.WorkerConfiguration;
import com.giof71.demo.mem.usage.core.WorkerManager;
import com.giof71.demo.mem.usage.core.worker.Worker;
import com.giof71.demo.mem.usage.core.worker.impl.WorkerImpl;

@Component
public class WorkerManagerImpl implements WorkerManager {
	
	private final List<Worker> workerList = new ArrayList<>();
	private final List<Runnable> runnableList = new ArrayList<>();

	@Override
	public int getNumberOfWorkers() {
		synchronized(workerList) {
			return workerList.size();
		}
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

	@Override
	public void reset() {
		for (Worker currentWorker : Optional.ofNullable(workerList).orElse(Collections.emptyList())) {
			currentWorker.stop();
		}
		synchronized(workerList) {
			workerList.clear();
		}
	}
}


