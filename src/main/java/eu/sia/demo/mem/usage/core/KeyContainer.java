package eu.sia.demo.mem.usage.core;

public interface KeyContainer {
	String getRandomKey();
	int size();
	void put(String key);
}
