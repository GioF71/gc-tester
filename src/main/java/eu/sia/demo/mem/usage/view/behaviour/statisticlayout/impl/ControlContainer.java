package eu.sia.demo.mem.usage.view.behaviour.statisticlayout.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vaadin.flow.component.textfield.TextField;

class ControlContainer {

	private final List<TextField> textFieldList = new ArrayList<>();
	private final Map<String, TextField> map = new HashMap<>();
	private final List<String> readOnlyChangeList = new ArrayList<>();

	ControlContainer(Collection<RequestTextField> requestTextFieldArray) {
		for (RequestTextField current : Optional.ofNullable(requestTextFieldArray).orElse(Collections.emptyList())) {
			TextField textField = new TextField(current.getCaption());
			put(current.getName(), textField);
			if (current.isProcessReadOnly()) {
				this.readOnlyChangeList.add(current.getName());
			}
		}
	}
	
	private void put(String name, TextField textField) {
		this.textFieldList.add(textField);
		this.map.put(name, textField);
	}
	
	Collection<TextField> getTextFieldCollection() {
		return Collections.unmodifiableList(textFieldList);
	}
	
	TextField byName(String name) {
		return map.get(name);
	}

	void changeReadOnly(boolean readOnly) {
		this.readOnlyChangeList.forEach(x -> map.get(x).setReadOnly(readOnly));
	}
}
