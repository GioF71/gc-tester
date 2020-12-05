package com.giof71.demo.mem.usage.core;

public interface WorkerConfigurationProvider {
	WorkerConfiguration create(int sleepTimeMillisec);
}
