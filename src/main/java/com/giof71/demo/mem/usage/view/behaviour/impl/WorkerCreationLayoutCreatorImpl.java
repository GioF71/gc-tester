package com.giof71.demo.mem.usage.view.behaviour.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.giof71.demo.mem.usage.core.WorkerConfiguration;
import com.giof71.demo.mem.usage.core.WorkerConfigurationProvider;
import com.giof71.demo.mem.usage.core.WorkerManager;
import com.giof71.demo.mem.usage.util.TextToPositiveIntegerOrZero;
import com.giof71.demo.mem.usage.view.Refreshable;
import com.giof71.demo.mem.usage.view.RefreshableComponent;
import com.giof71.demo.mem.usage.view.behaviour.WorkerCreationLayoutCreator;

@Component
public class WorkerCreationLayoutCreatorImpl implements WorkerCreationLayoutCreator {

	@Autowired 
	private WorkerManager workerManager;
	
	@Autowired
	private WorkerConfigurationProvider workerConfigurationProvider;

	@Autowired
	private TextToPositiveIntegerOrZero textToPositiveIntegerOrZero;

	@Override
	public RefreshableComponent create() {
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout workerLayout = new HorizontalLayout();
		TextField howMany = new TextField("Add Workers");
		howMany.setValue("1");
		TextField pauseMillisec = new TextField("Worker Pause in millisec");
		pauseMillisec.setValue("10");
		workerLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		layout.add(workerLayout);
		Button createWorkers = new Button("Create workers");
		createWorkers.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = -2039028744374624585L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				int vHowMany = textToPositiveIntegerOrZero.convert(howMany.getValue());
				int vPauseMillisec = textToPositiveIntegerOrZero.convert(pauseMillisec.getValue());
				WorkerConfiguration config = workerConfigurationProvider.create(vPauseMillisec);
				for (int i = 0; i < vHowMany; ++i) {
					workerManager.createWorker(config);
				}
			}
		});
		Button removeAllWorkers = new Button("Remove all workers");
		removeAllWorkers.addClickListener(event -> workerManager.reset());
		workerLayout.add(howMany, pauseMillisec, createWorkers, removeAllWorkers);
		HorizontalLayout currentStatusLayout = new HorizontalLayout();
		currentStatusLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		TextField currentWorkers = new TextField("Current Workers");
		currentStatusLayout.add(currentWorkers);
		currentWorkers.setValue(Integer.valueOf(workerManager.getNumberOfWorkers()).toString());
		currentWorkers.setReadOnly(true);
		Button refreshCurrentWorkers = new Button("Refresh");
		currentStatusLayout.add(refreshCurrentWorkers);
		refreshCurrentWorkers.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				currentWorkers.setReadOnly(false);
				currentWorkers.setValue(Integer.valueOf(workerManager.getNumberOfWorkers()).toString());
				currentWorkers.setReadOnly(true);
			}
		});
		layout.add(currentStatusLayout);
		
		Refreshable refreshable = new Refreshable() {
			
			@Override
			public void refresh() {
				currentWorkers.setReadOnly(false);
				currentWorkers.setValue(Integer.valueOf(workerManager.getNumberOfWorkers()).toString());
				currentWorkers.setReadOnly(true);
			}
		};
		return new RefreshableComponent() {
			
			@Override
			public Refreshable getRefreshable() {
				return refreshable;
			}
			
			@Override
			public com.vaadin.flow.component.Component getComponent() {
				return layout;
			}
		};
	}
}
