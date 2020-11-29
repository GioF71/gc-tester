package eu.sia.demo.mem.usage.core;

public interface WorkerConfigurationProvider {
	WorkerConfiguration create(int sleepTimeMillisec);
}
