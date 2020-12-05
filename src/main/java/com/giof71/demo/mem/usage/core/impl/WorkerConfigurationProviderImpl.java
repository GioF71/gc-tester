package com.giof71.demo.mem.usage.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.core.Collector;
import com.giof71.demo.mem.usage.core.CoreCache;
import com.giof71.demo.mem.usage.core.KeyContainer;
import com.giof71.demo.mem.usage.core.WorkerConfiguration;
import com.giof71.demo.mem.usage.core.WorkerConfigurationProvider;

@Component
public class WorkerConfigurationProviderImpl implements WorkerConfigurationProvider {
	
	@Autowired
	private KeyContainer keyContainer;
	
	@Autowired
	private CoreCache coreCache;
	
	@Autowired
	private Collector collector;
	
	@Override
	public WorkerConfiguration create(int sleepTimeMillisec) {
		return new WorkerConfiguration() {
			
			@Override
			public int sleepTimeMillisec() {
				return sleepTimeMillisec;
			}
			
			@Override
			public KeyContainer getKeyContainer() {
				return keyContainer;
			}
			
			@Override
			public CoreCache getCoreCache() {
				return coreCache;
			}

			@Override
			public Collector getCollector() {
				return collector;
			}
		};
	}

}
