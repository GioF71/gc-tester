package eu.sia.demo.mem.usage.core;

import java.util.Collection;

public interface KeyContainer {
	String getRandomKey();
	int size();
	void put(Collection<String> key);
	void put(String key);
}
