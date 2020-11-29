package eu.sia.demo.mem.usage.core;

public interface WorkerConfiguration {
	CoreCache getCoreCache();
	KeyContainer getKeyContainer();
	Collector getCollector();
	int sleepTimeMillisec();
}
