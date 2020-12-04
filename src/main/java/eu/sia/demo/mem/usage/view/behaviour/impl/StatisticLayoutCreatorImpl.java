package eu.sia.demo.mem.usage.view.behaviour.impl;

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
	
	private final List<BiConsumer<PerformanceStatistic, Controls>> metricConsumers = Arrays.asList(
		(m, c) -> c.getCnt().setValue(Optional.ofNullable(m).map(PerformanceStatistic::getCount).map(i -> Integer.valueOf(i).toString()).orElse("---")),
		(m, c) -> updateValue(m, c, Controls::getAvg, PerformanceStatistic::getElapsedAvg),
		(m, c) -> updateValue(m, c, Controls::getMax, PerformanceStatistic::getElapsedMax),
		(m, c) -> updateValue(m, c, Controls::getMin, PerformanceStatistic::getElapsedMin),
		(m, c) -> c.getCreationTime().setValue(Optional.ofNullable(m).filter(x -> x.getCount() > 0).map(PerformanceStatistic::creationTimeStamp).map(timeUtil.getToTimeStampFunction()).orElse("---")),
		(m, c) -> c.getName().setValue(Optional.ofNullable(m).map(PerformanceStatistic::getName).orElse("---")));
	
	class Controls {
		
		private final TextField name;
		private final TextField creationTime;
		private final TextField cnt;
		private final TextField avg;
		private final TextField max;
		private final TextField min;
		private final List<TextField> readOnlyChangeList;
		
		Controls(TextField name, TextField creationTime, TextField cnt, TextField avg, TextField max, TextField min) {
			this.name = name;
			this.creationTime = creationTime;
			this.cnt = cnt;
			this.avg = avg;
			this.max = max;
			this.min = min;
			this.readOnlyChangeList = Arrays.asList(
				this.name,
				this.creationTime,
				this.cnt, 
				this.avg, 
				this.max, 
				this.min);
		}
		
		TextField getName() {
			return name;
		}
		
		TextField getCreationTime() {
			return creationTime;
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
		Map<String, Controls> controlsMap = new HashMap<>();
		for (RequestedStatistic current : requestedStatistics) {
			Controls controls = createControlSet(current.getName());
			HorizontalLayout layout = addControlsToLayout(controls);
			vLayout.add(layout);
			controlsMap.put(current.getName(), controls);
		}
		
		
//		Controls controlsOneSec = createControlSet("OneSec");
//		HorizontalLayout oneSecLayout = addControlsToLayout(controlsOneSec);
//		Controls controlsFiveSec = createControlSet("FiveSec");
//		HorizontalLayout fiveSecLayout = addControlsToLayout(controlsFiveSec);
//		Controls controlsOneMin = createControlSet("OneMinute");
//		HorizontalLayout oneMinLayout = addControlsToLayout(controlsOneMin);
//		vLayout.add(oneSecLayout);
//		vLayout.add(fiveSecLayout);
//		vLayout.add(oneMinLayout);
		Refreshable refreshable = new Refreshable() {

			@Override
			public void refresh() {
//				refreshStats(controlsOneSec, db -> db.getPerformanceStatistic("OneSec"));
//				refreshStats(controlsFiveSec, db -> db.getPerformanceStatistic("FiveSec"));
//				refreshStats(controlsOneMin, db -> db.getPerformanceStatistic("OneMinute"));
				for (RequestedStatistic current : requestedStatistics) {
					Controls controls = controlsMap.get(current.getName());
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

	private HorizontalLayout addControlsToLayout(Controls controls) {
		HorizontalLayout target = new HorizontalLayout();
		target.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		target.add(controls.getName());
		target.add(controls.getCreationTime());
		target.add(controls.getCnt());
		target.add(controls.getAvg());
		target.add(controls.getMax());
		target.add(controls.getMin());
		return target;
	}

	private Controls createControlSet(String statisticName) {
		TextField name = new TextField(statisticName);
		TextField creationTime = new TextField("Creation Time");
		TextField cnt = new TextField("Count");
		TextField avg = new TextField("Avg");
		TextField max = new TextField("Max");
		TextField min = new TextField("Min");
		Controls controls = new Controls(name, creationTime, cnt, avg, max, min);
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
	
	private void updateValue(PerformanceStatistic metric, Controls c, Function<Controls, TextField> textFieldExtractor, Function<PerformanceStatistic, Float> metricExtractor) {
		textFieldExtractor.apply(c).setValue(floatFormatFunction.apply(metric, metricExtractor));
	}

	private void refreshStats(Controls c, Function<DisplayBuffer, PerformanceStatistic> statisticRetriever) {
		PerformanceStatistic metric = statisticRetriever.apply(displayBuffer);
		c.changeReadOnly(false);
		for (BiConsumer<PerformanceStatistic, Controls> bc : metricConsumers) {
			bc.accept(metric, c);
		}
		c.changeReadOnly(true);
	}
}
