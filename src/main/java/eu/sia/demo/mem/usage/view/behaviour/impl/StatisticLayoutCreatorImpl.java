package eu.sia.demo.mem.usage.view.behaviour.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import eu.sia.demo.mem.usage.core.Collector;
import eu.sia.demo.mem.usage.core.Collector.ExtractAction;
import eu.sia.demo.mem.usage.core.PerformanceMetric;
import eu.sia.demo.mem.usage.core.PerformanceMetricExtractor;
import eu.sia.demo.mem.usage.core.StatisticEntry;
import eu.sia.demo.mem.usage.view.Refreshable;
import eu.sia.demo.mem.usage.view.RefreshableComponent;
import eu.sia.demo.mem.usage.view.behaviour.StatisticLayoutCreator;

@Component
public class StatisticLayoutCreatorImpl implements StatisticLayoutCreator {
	
	private final List<BiConsumer<PerformanceMetric, Controls>> metricConsumers = Arrays.asList(
		(m, c) -> c.getCnt().setValue(Optional.ofNullable(m.getCount()).map(i -> Integer.valueOf(i).toString()).orElse("---")),
		(m, c) -> updateValue(m, c, Controls::getAvg, PerformanceMetric::getElapsedAvg),
		(m, c) -> updateValue(m, c, Controls::getMax, PerformanceMetric::getElapsedMax),
		(m, c) -> updateValue(m, c, Controls::getMin, PerformanceMetric::getElapsedMin));

	@Autowired
	private Collector collector;
	
	@Autowired
	private PerformanceMetricExtractor performanceMetricExtractor;
	
	class Controls {
		
		private final TextField cnt;
		private final TextField avg;
		private final TextField max;
		private final TextField min;
		private final List<TextField> readOnlyChangeList;
		
		Controls(TextField cnt, TextField avg, TextField max, TextField min) {
			this.cnt = cnt;
			this.avg = avg;
			this.max = max;
			this.min = min;
			this.readOnlyChangeList = Arrays.asList(
				this.cnt, 
				this.avg, 
				this.max, 
				this.min);
		}
		
		TextField getCnt() {
			return cnt;
		}

		TextField getAvg() {
			return avg;
		}

		TextField getMax() {
			return max;
		}

		TextField getMin() {
			return min;
		}

		void changeReadOnly(boolean readOnly) {
			this.readOnlyChangeList.forEach(x -> x.setReadOnly(readOnly));
		}
	}
	
	@Override
	public RefreshableComponent create(int removeDelta, TimeUnit timeUnit) {
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		TextField cnt = new TextField("Count");
		TextField avg = new TextField("Avg");
		TextField max = new TextField("Max");
		TextField min = new TextField("Min");
		hLayout.add(cnt);
		hLayout.add(avg);
		hLayout.add(max);
		hLayout.add(min);
		Controls controls = new Controls(cnt, avg, max, min);
		
		refreshStats(controls, removeDelta, timeUnit);
		Refreshable refreshable = new Refreshable() {

			@Override
			public void refresh() {
				refreshStats(controls, removeDelta, timeUnit);
			}
		};
		return new RefreshableComponent() {
			
			@Override
			public Refreshable getRefreshable() {
				return refreshable;
			}
			
			@Override
			public com.vaadin.flow.component.Component getComponent() {
				return hLayout;
			}
		};
	}
	
	private final BiFunction<PerformanceMetric, Function<PerformanceMetric, Float>, String> floatFormatFunction = new BiFunction<PerformanceMetric, Function<PerformanceMetric, Float>, String>() {

		@Override
		public String apply(PerformanceMetric performanceMetric, Function<PerformanceMetric, Float> f) {
			return Optional.ofNullable(performanceMetric)
				.map(f)
				.map(floatToString)
				.orElse("---");
		}
	};
	
	private final Function<Float, String> floatToString = new Function<Float, String>() {

		@Override
		public String apply(Float f) {
			return Optional.ofNullable(f)
				.map(x -> String.format("%.3f", x))
				.orElse(null);
		}
	};
	
	private void updateValue(PerformanceMetric metric, Controls c, Function<Controls, TextField> textFieldExtractor, Function<PerformanceMetric, Float> metricExtractor) {
		textFieldExtractor.apply(c).setValue(floatFormatFunction.apply(metric, metricExtractor));
	}

	private void refreshStats(Controls c, int removeDelta, TimeUnit timeunit) {
		List<StatisticEntry> seList = collector.getLastEntries(1000, TimeUnit.MILLISECONDS, ExtractAction.NONE);
		PerformanceMetric metric = performanceMetricExtractor.get("One Sec", seList);
		c.changeReadOnly(false);
		for (BiConsumer<PerformanceMetric, Controls> bc : metricConsumers) {
			bc.accept(metric, c);
		}
		c.changeReadOnly(true);
		collector.purgeBefore(removeDelta, timeunit);
	}
}
