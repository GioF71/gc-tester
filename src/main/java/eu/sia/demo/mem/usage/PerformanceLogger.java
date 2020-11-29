package eu.sia.demo.mem.usage;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.core.Collector;
import eu.sia.demo.mem.usage.core.PerformanceMetric;
import eu.sia.demo.mem.usage.core.PerformanceMetricExtractor;
import eu.sia.demo.mem.usage.util.NanotimeConverter;

@Component
public class PerformanceLogger {
	
	@Autowired
	private PerformanceMetricExtractor performanceMetricExtractor;
	
	@Autowired
	private Collector collector;
	
	@Autowired
	private NanotimeConverter nanotimeConverter;

	private final Logger logger = Logger.getLogger(PerformanceLogger.class.getCanonicalName());
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			while (true) {
				iteration();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

		}

		private void iteration() {
			PerformanceMetric performanceMetric = performanceMetricExtractor.get("One sec", 1, TimeUnit.SECONDS);
			logMetric(performanceMetric);
			PerformanceMetric performanceMetric10Sec = performanceMetricExtractor.get("Ten sec", 10, TimeUnit.SECONDS);
			logMetric(performanceMetric10Sec);
			long purgeStart = System.nanoTime();
			collector.purgeBefore(10, TimeUnit.SECONDS);
			long purgeElapsed = System.nanoTime() - purgeStart;
			logger.log(Level.INFO, String.format("Purge took [%.3f] millisec", nanotimeConverter.nanoToMilli(purgeElapsed)));
		}

		private void logMetric(PerformanceMetric performanceMetric) {
			if (performanceMetric.getCount() > 0) {
				logger.log(Level.INFO, String.format("Metric [%s] Count [%d] Avg [%.3f] Max [%.3f] Min [%.3f]", 
					performanceMetric.getName(), 
					performanceMetric.getCount(),
					performanceMetric.getElapsedAvg(),
					performanceMetric.getElapsedMax(),
					performanceMetric.getElapsedMin()));
			}
		}
	};

	@PostConstruct
	private void init() {
		logger.info(getClass().getSimpleName() + " init");
		Thread t = new Thread(runnable);
		t.start();
	}
}
