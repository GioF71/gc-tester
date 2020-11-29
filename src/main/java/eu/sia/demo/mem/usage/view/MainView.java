package eu.sia.demo.mem.usage.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.PollEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import eu.sia.demo.mem.usage.view.behaviour.CacheSizeLayoutCreator;
import eu.sia.demo.mem.usage.view.behaviour.KeyAddLayoutCreator;
import eu.sia.demo.mem.usage.view.behaviour.WorkerCreationLayoutCreator;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = -8858078649247130202L;
		
	@Autowired
	private KeyAddLayoutCreator keyContainerActionContainer;
	
	@Autowired
	private CacheSizeLayoutCreator cacheSizeLayoutCreator;
	
	@Autowired
	private WorkerCreationLayoutCreator workerCreationLayoutCreator;
	
	private final List<Refreshable> refreshableList = new ArrayList<>();
		
	@PostConstruct
	private void postConstruct() {
		this.add(buildDataLayout());
		this.add(buildCreateWorkersLayout());
		UI.getCurrent().setPollInterval(1000);
		UI.getCurrent().addPollListener(pollListener);
	}
	
	private final ComponentEventListener<PollEvent> pollListener = new ComponentEventListener<PollEvent>() {
		
		private static final long serialVersionUID = -9102986628178310139L;

		@Override
		public void onComponentEvent(PollEvent event) {
			for (Refreshable refreshable : refreshableList) {
				refreshable.refresh();
			}
//			PerformanceMetric performanceMetric = performanceMetricExtractor.get("One sec", 1, TimeUnit.SECONDS);
//			if (performanceMetric.getCount() > 0) {
//				logger.log(Level.INFO, String.format("Metric [%s] Count [%d] Avg [%.3f] Max [%.3f] Min [%.3f]", 
//					performanceMetric.getName(), 
//					performanceMetric.getCount(),
//					performanceMetric.getElapsedAvg(),
//					performanceMetric.getElapsedMax(),
//					performanceMetric.getElapsedMin()));
//			}
//			long purgeStart = System.nanoTime();
//			collector.purgeBefore(1, TimeUnit.SECONDS);
//			long purgeElapsed = System.nanoTime() - purgeStart;
//			logger.log(Level.INFO, String.format("Purge took [%.3f] millisec", nanotimeConverter.nanoToMilli(purgeElapsed)));
		}
	};

	private VerticalLayout buildDataLayout() {
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout keysLayout = createKeysLayout();
		layout.add(keysLayout);
		HorizontalLayout cacheSizeLayout = createCacheSizeLayout();
		layout.add(cacheSizeLayout);
		return layout;
	}

	private HorizontalLayout createKeysLayout() {
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		RefreshableComponent rc = keyContainerActionContainer.create();
		hLayout.add(rc.getComponent());
		refreshableList.add(rc.getRefreshable());
		return hLayout;
	}
	
	private HorizontalLayout createCacheSizeLayout() {
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		RefreshableComponent rc = cacheSizeLayoutCreator.create();
		hLayout.add(rc.getComponent());
		refreshableList.add(rc.getRefreshable());
		return hLayout;
	}
	
	private HorizontalLayout buildCreateWorkersLayout() {
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		RefreshableComponent rc = workerCreationLayoutCreator.create();
		hLayout.add(rc.getComponent());
		refreshableList.add(rc.getRefreshable());
		return hLayout;
	}
}
