package eu.sia.demo.mem.usage.view.behaviour.statisticlayout.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
	private final Collection<StatisticFieldMapping> statisticFieldMappingList = new ArrayList<>();
	
	@PostConstruct
	private void postConstruct() {
		requestedStatistics = Optional.ofNullable(statisticConfiguration.getRequestedStatistics()).orElse(Collections.emptyList());
		for (RequestedStatistic current : requestedStatistics) {
			displayBuffer.requestStatistic(current.getName(), current.getDelta(), current.getTimeUnit());
		}
		statisticFieldMappingList.add(new StatisticFieldMapping(StatisticField.NAME, (m, c) -> {}));
		statisticFieldMappingList.add(new StatisticFieldMapping(StatisticField.CREATION_TIME, creationTimeTransformer));
		statisticFieldMappingList.add(new StatisticFieldMapping(StatisticField.OLDEST_AGE, oldestAgeTransformer));
		statisticFieldMappingList.add(new StatisticFieldMapping(StatisticField.CNT, countTransformer));
		statisticFieldMappingList.add(new StatisticFieldMapping(StatisticField.AVG, floatTransformer(StatisticField.AVG.name(), PerformanceStatistic::getElapsedAvg)));
		statisticFieldMappingList.add(new StatisticFieldMapping(StatisticField.MAX, floatTransformer(StatisticField.MAX.name(), PerformanceStatistic::getElapsedMax)));
		statisticFieldMappingList.add(new StatisticFieldMapping(StatisticField.MIN, floatTransformer(StatisticField.MIN.name(), PerformanceStatistic::getElapsedMin)));
	}
	
	private final Transformer creationTimeTransformer = new Transformer() {
		
		@Override
		public void accept(PerformanceStatistic m, ControlContainer c) {
			c.byName(StatisticField.CREATION_TIME.name()).setValue(Optional.ofNullable(m).filter(x -> x.getCount() > 0).map(PerformanceStatistic::creationTimeStamp).map(timeUtil.getToTimeStampFunction()).orElse("---"));
		}
	}; 

	private final Transformer oldestAgeTransformer = new Transformer() {
		
		@Override
		public void accept(PerformanceStatistic m, ControlContainer c) {
			c.byName(StatisticField.OLDEST_AGE.name()).setValue(Optional.ofNullable(m).map(PerformanceStatistic::getOldestNanoTime).filter(o -> o >= 0).map(o -> System.nanoTime() - o).map(o -> String.format("%.3f msec ago", (float) o / (1000000.0f))).orElse("---"));
		}
	}; 
	
	private final Transformer countTransformer = new Transformer() {
		
		@Override
		public void accept(PerformanceStatistic m, ControlContainer c) {
			c.byName(StatisticField.CNT.name()).setValue(Optional.ofNullable(m).map(PerformanceStatistic::getCount).map(i -> Integer.valueOf(i).toString()).orElse("---"));
		}
	}; 
	
	private Transformer floatTransformer(String name, Function<PerformanceStatistic, Float> floatExtractor) {
		return (m, c) -> updateValue(m, floatExtractor, c, x -> x.byName(name));
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
		for (TextField current : controlContainer.getTextFieldCollection()) {
			target.add(current);
		}
		return target;
	}

	private ControlContainer createControlContainer(String statisticName) {
		List<RequestTextField> list = new ArrayList<>();
		for (StatisticFieldMapping current : statisticFieldMappingList) {
			RequestTextField rtf = new RequestTextField(
				current.getStatisticField().name(), 
				current.getStatisticField().getCaption(),
				current.getStatisticField().isProcessReadOnly());
			list.add(rtf);
		}
		ControlContainer controls = new ControlContainer(list);
		controls.byName(StatisticField.NAME.name()).setReadOnly(false);
		controls.byName(StatisticField.NAME.name()).setValue(statisticName);
		controls.byName(StatisticField.NAME.name()).setReadOnly(true);
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
	
	private void updateValue(
			PerformanceStatistic metric, 
			Function<PerformanceStatistic, Float> metricExtractor, 
			ControlContainer controlContainer, 
			Function<ControlContainer, TextField> textFieldExtractor) {
		String valueAsString = floatFormatFunction.apply(metric, metricExtractor);
		textFieldExtractor.apply(controlContainer).setValue(valueAsString);
	}

	private void refreshStats(ControlContainer c, Function<DisplayBuffer, PerformanceStatistic> statisticRetriever) {
		PerformanceStatistic metric = statisticRetriever.apply(displayBuffer);
		c.changeReadOnly(false);
		for (StatisticFieldMapping current : statisticFieldMappingList) {
			current.getTransformer().accept(metric, c);
		}
		c.changeReadOnly(true);
	}
}
