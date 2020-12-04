package eu.sia.demo.mem.usage.view.behaviour.statisticlayout.impl;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.textfield.TextField;

class ControlContainer {

	private final TextField name;
	private final TextField creationTime;
	private final TextField oldestAge;
	private final TextField cnt;
	private final TextField avg;
	private final TextField max;
	private final TextField min;
	private final List<TextField> readOnlyChangeList;

	ControlContainer(
			TextField name, 
			TextField creationTime, 
			TextField oldestAge, 
			TextField cnt, 
			TextField avg, 
			TextField max,
			TextField min) {
		this.name = name;
		this.creationTime = creationTime;
		this.oldestAge = oldestAge;
		this.cnt = cnt;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.readOnlyChangeList = Arrays.asList(
			this.name, 
			this.creationTime, 
			this.oldestAge, 
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

	TextField getOldestAge() {
		return oldestAge;
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
