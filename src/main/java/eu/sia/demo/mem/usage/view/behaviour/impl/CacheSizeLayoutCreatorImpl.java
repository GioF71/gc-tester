package eu.sia.demo.mem.usage.view.behaviour.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import eu.sia.demo.mem.usage.core.DisplayBuffer;
import eu.sia.demo.mem.usage.view.Refreshable;
import eu.sia.demo.mem.usage.view.RefreshableComponent;
import eu.sia.demo.mem.usage.view.behaviour.CacheSizeLayoutCreator;

@Component
public class CacheSizeLayoutCreatorImpl implements CacheSizeLayoutCreator {
	
	@Autowired
	private DisplayBuffer displayBuffer;
	
	@Override
	public RefreshableComponent create() {
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		TextField coreCacheSize = new TextField("Core Cache Size");
		hLayout.add(coreCacheSize);
		refreshCacheSize(coreCacheSize);
		Button refresh = new Button("Refresh");
		refresh.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = 3332368562535218096L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				refreshCacheSize(coreCacheSize);
				
			}

		});
		hLayout.add(refresh);
		Refreshable refreshable = new Refreshable() {

			@Override
			public void refresh() {
				refreshCacheSize(coreCacheSize);
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
	
	private void refreshCacheSize(TextField coreCacheSize) {
		coreCacheSize.setReadOnly(false);
		coreCacheSize.setValue(Integer.valueOf(displayBuffer.getCacheSize()).toString());
		coreCacheSize.setReadOnly(true);
	}
}
