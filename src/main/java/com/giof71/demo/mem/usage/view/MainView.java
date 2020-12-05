package com.giof71.demo.mem.usage.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.PollEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.giof71.demo.mem.usage.view.behaviour.CacheSizeLayoutCreator;
import com.giof71.demo.mem.usage.view.behaviour.KeyAddLayoutCreator;
import com.giof71.demo.mem.usage.view.behaviour.StatisticLayoutCreator;
import com.giof71.demo.mem.usage.view.behaviour.WorkerCreationLayoutCreator;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = -8858078649247130202L;
		
	@Autowired
	private KeyAddLayoutCreator keyContainerActionContainer;
	
	@Autowired
	private CacheSizeLayoutCreator cacheSizeLayoutCreator;
	
	@Autowired
	private WorkerCreationLayoutCreator workerCreationLayoutCreator;
	
	@Autowired
	private StatisticLayoutCreator statisticLayoutCreator;
	
	private final List<Refreshable> refreshableList = new ArrayList<>();
		
	@PostConstruct
	private void postConstruct() {
		this.add(buildDataLayout());
		this.add(buildCreateWorkersLayout());
		this.add(buildStatLayout());
		UI.getCurrent().setPollInterval(500);
		UI.getCurrent().addPollListener(pollListener);
	}
	
	private final ComponentEventListener<PollEvent> pollListener = new ComponentEventListener<PollEvent>() {
		
		private static final long serialVersionUID = -9102986628178310139L;

		@Override
		public void onComponentEvent(PollEvent event) {
			for (Refreshable refreshable : refreshableList) {
				refreshable.refresh();
			}
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
	
	private HorizontalLayout buildStatLayout() {
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		RefreshableComponent rc = statisticLayoutCreator.create(5, TimeUnit.SECONDS);
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
