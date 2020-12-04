package eu.sia.demo.mem.usage.view.behaviour.statisticlayout.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import eu.sia.demo.mem.usage.core.DisplayBuffer;
import eu.sia.demo.mem.usage.core.PerformanceStatistic;
import eu.sia.demo.mem.usage.core.RequestedStatistic;
import eu.sia.demo.mem.usage.core.StatisticConfiguration;
import eu.sia.demo.mem.usage.util.TimeUtil;
import eu.sia.demo.mem.usage.view.Refreshable;
import eu.sia.demo.mem.usage.view.RefreshableComponent;
import eu.sia.demo.mem.usage.view.behaviour.StatisticLayoutCreator;

@Component
public class StatisticLayoutCreatorImpl implements StatisticLayoutCreator {
	
	@Autowired
	private StatisticConfiguration statisticConfiguration;
	
	@Autowired
	private DisplayBuffer displayBuffer;
	
	@Autowired
	private TimeUtil timeUtil;
	
	private Collection<RequestedStatistic> requestedStatistics;
	
	private final List<BiConsumer<PerformanceStatistic, ControlContainer>> metricConsumers = Arrays.asList(
		(m, c) -> c.getCnt().setValue(Optional.ofNullable(m).map(PerformanceStatistic::getCount).map(i -> Integer.valueOf(i).toString()).orElse("---")),
		(m, c) -> c.getOldestAge().setValue(Optional.ofNullable(m).map(PerformanceStatistic::getOldestNanoTime).filter(o -> o >= 0).map(o -> System.nanoTime() - o).map(o -> String.format("%.3f msec ago", (float) o / (1000000.0f))).orElse("---")),
		(m, c) -> updateValue(m, c, ControlContainer::getAvg, PerformanceStatistic::getElapsedAvg),
		(m, c) -> updateValue(m, c, ControlContainer::getMax, PerformanceStatistic::getElapsedMax),
		(m, c) -> updateValue(m, c, ControlContainer::getMin, PerformanceStatistic::getElapsedMin),
		(m, c) -> c.getCreationTime().setValue(Optional.ofNullable(m).filter(x -> x.getCount() > 0).map(PerformanceStatistic::creationTimeStamp).map(timeUtil.getToTimeStampFunction()).orElse("---")));
	
	
	@PostConstruct
	private void postConstruct() {
		requestedStatistics = Optional.ofNullable(statisticConfiguration.getRequestedStatistics()).orElse(Collections.emptyList());
		for (RequestedStatistic current : requestedStatistics) {
			displayBuffer.requestStatistic(current.getName(), current.getDelta(), current.getTimeUnit());
		}
	}
	
	@Override
	public RefreshableComponent create(long removeDelta, TimeUnit timeUnit) {
		VerticalLayout vLayout = new VerticalLayout();
		Map<String, ControlContainer> controlContainerMap = new HashMap<>();
		for (RequestedStatistic current : requestedStatistics) {
			ControlContainer controls = createControlContainer(current.getName());
			HorizontalLayout layout = addControlsToLayout(controls);
			vLayout.add(layout);
			controlContainerMap.put(current.getName(), controls);
		}
		Refreshable refreshable = new Refreshable() {

			@Override
			public void refresh() {
				for (RequestedStatistic current : requestedStatistics) {
					ControlContainer controls = controlContainerMap.get(current.getName());
					refreshStats(controls, db -> db.getPerformanceStatistic(current.getName()));
				}
			}
		};
		return new RefreshableComponent() {
			
			@Override
			public Refreshable getRefreshable() {
				return refreshable;
			}
			
			@Override
			public com.vaadin.flow.component.Component getComponent() {
				return vLayout;
			}
		};
	}

	private HorizontalLayout addControlsToLayout(ControlContainer controlContainer) {
		HorizontalLayout target = new HorizontalLayout();
		target.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		target.add(controlContainer.getName());
		target.add(controlContainer.getCreationTime());
		target.add(controlContainer.getOldestAge());
		target.add(controlContainer.getCnt());
		target.add(controlContainer.getAvg());
		target.add(controlContainer.getMax());
		target.add(controlContainer.getMin());
		return target;
	}

	private ControlContainer createControlContainer(String statisticName) {
		TextField name = new TextField(statisticName);
		name.setValue(statisticName);
		name.setReadOnly(true);
		TextField creationTime = new TextField("Creation Time");
		TextField oldestAge = new TextField("Oldest item age");
		TextField cnt = new TextField("Count");
		TextField avg = new TextField("Avg");
		TextField max = new TextField("Max");
		TextField min = new TextField("Min");
		ControlContainer controls = new ControlContainer(name, creationTime, oldestAge, cnt, avg, max, min);
		return controls;
	}
	
	private final BiFunction<PerformanceStatistic, Function<PerformanceStatistic, Float>, String> floatFormatFunction = new BiFunction<PerformanceStatistic, Function<PerformanceStatistic, Float>, String>() {

		@Override
		public String apply(PerformanceStatistic performanceMetric, Function<PerformanceStatistic, Float> f) {
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
	
	private void updateValue(PerformanceStatistic metric, ControlContainer c, Function<ControlContainer, TextField> textFieldExtractor, Function<PerformanceStatistic, Float> metricExtractor) {
		textFieldExtractor.apply(c).setValue(floatFormatFunction.apply(metric, metricExtractor));
	}

	private void refreshStats(ControlContainer c, Function<DisplayBuffer, PerformanceStatistic> statisticRetriever) {
		PerformanceStatistic metric = statisticRetriever.apply(displayBuffer);
		c.changeReadOnly(false);
		for (BiConsumer<PerformanceStatistic, ControlContainer> bc : metricConsumers) {
			bc.accept(metric, c);
		}
		c.changeReadOnly(true);
	}
}
