package eu.sia.demo.mem.usage.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.KeyContainer;

@Component
public class KeyContainerImpl implements KeyContainer {
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor(); 
	private final List<String> keyList = new ArrayList<>();
	
	@Override
	public int size() {
		synchronized(keyList) {
			return keyList.size();
		}
	}

	@Override
	public void put(Collection<String> keyCollection) {
		executorService.submit(() -> {
			for (String current : Optional.ofNullable(keyCollection).orElse(Collections.emptyList())) {
				synchronized(keyList) {
					keyList.add(current);
				}
			}
		});
	}

	@Override
	public void put(String key) {
		executorService.submit(() -> {
			synchronized(keyList) {
				keyList.add(key);
			}
		});
	}

	@Override
	public String getRandomKey() {
		synchronized(keyList) {
			int sz = keyList.size();
			return Optional.of(keyList)
				.map(List::size)
				.filter(s -> s > 0)
				.map(s -> new Random().nextInt(sz))
				.map(r -> keyList.get(r))
				.orElse(null);
		}
	}
}
