package eu.sia.demo.mem.usage.view.behaviour;

import java.util.concurrent.TimeUnit;

import eu.sia.demo.mem.usage.view.RefreshableComponent;

public interface StatisticLayoutCreator {
	RefreshableComponent create(long removeDelta, TimeUnit timeunit);
}
