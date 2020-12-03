package eu.sia.demo.mem.usage.view.behaviour.impl;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import eu.sia.demo.mem.usage.core.DisplayBuffer;
import eu.sia.demo.mem.usage.core.KeyContainer;
import eu.sia.demo.mem.usage.util.TextToIntegerOrZero;
import eu.sia.demo.mem.usage.view.Refreshable;
import eu.sia.demo.mem.usage.view.RefreshableComponent;
import eu.sia.demo.mem.usage.view.behaviour.KeyAddLayoutCreator;

@Component
public class KeyAddLayoutCreatorImpl implements KeyAddLayoutCreator {

	@Autowired
	private KeyContainer keyContainer;

	@Autowired
	private DisplayBuffer displayBuffer;
	
	@Autowired
	private TextToIntegerOrZero textToIntegerOrZero;
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	@Override
	public RefreshableComponent create() {
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setPadding(false);
		vLayout.setSpacing(false);
		vLayout.setMargin(false);
		HorizontalLayout currentSizeLayout = new HorizontalLayout();
		currentSizeLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		TextField keySizeTextField = new TextField("Current Number of Keys");
		keySizeTextField.setValue(Integer.valueOf(keyContainer.size()).toString());
		keySizeTextField.setReadOnly(true);
		Button refreshCurrentKeySize = new Button("Refresh");
		refreshCurrentKeySize.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				keySizeTextField.setReadOnly(false);
				keySizeTextField.setValue(Integer.valueOf(displayBuffer.getContainerSize()).toString());
				keySizeTextField.setReadOnly(true);
			}
		});
		currentSizeLayout.add(keySizeTextField, refreshCurrentKeySize);
		HorizontalLayout addLayout = new HorizontalLayout();
		addLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		Button addButton = new Button("Add");
		TextField addHowMany = new TextField("Add Keys");
		addHowMany.setValue("1");
		addLayout.add(addHowMany);
		addLayout.add(addButton);
		addButton.addClickListener(createOnAdd(addHowMany));
		Refreshable refreshable = new Refreshable() {
			
			@Override
			public void refresh() {
				keySizeTextField.setReadOnly(false);
				keySizeTextField.setValue(Integer.valueOf(keyContainer.size()).toString());
				keySizeTextField.setReadOnly(true);
			}
		};
		vLayout.add(currentSizeLayout);
		vLayout.add(addLayout);
		
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
	
	private ComponentEventListener<ClickEvent<Button>> createOnAdd(TextField addHowMany) {

		return new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				executorService.submit(() -> {
					int howMany = Optional.ofNullable(addHowMany)
						.map(tf -> tf.getValue())
						.filter(s -> s.length() > 0)
						.map(textToIntegerOrZero.getFunction())
						.orElse(0);
					for (int i = 0; i < howMany; ++i) {
						keyContainer.put(UUID.randomUUID().toString());
					}
				});
			}
		};
	}
}
