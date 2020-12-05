package com.giof71.demo.mem.usage.core.impl;

import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CollectorTest {
	
	@Test
	public void t1() throws InterruptedException {
		Random random = new Random();
		CollectorImpl c = new CollectorImpl();
		int statCount = 100;
		int delayNanoSec = 1;
		int keepUntil = 45;
		Long keepUntilTimestamp = null;
		for (int i = 0; i < statCount; ++i) {
			MetricEntryImpl entry = new MetricEntryImpl(random.nextFloat(), random.nextFloat());
			c.add(entry);
			Thread.sleep(0, delayNanoSec);
			if (i == keepUntil) {
				keepUntilTimestamp = entry.getCreationNanotime();
			}
		}
		c.purgeOlder(keepUntilTimestamp);
		int residualSize = c.size();
		Assert.assertTrue(residualSize == (statCount - keepUntil));
	}
}
