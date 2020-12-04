package eu.sia.demo.mem.usage.view.behaviour.statisticlayout.impl;

class RequestTextField {
	
	private final String name;
	private final String caption;
	private final boolean processReadOnly;

	RequestTextField(String name, String caption) {
		this(name, caption, true);
	}
	
	RequestTextField(String name, String caption, boolean processReadOnly) {
		this.name = name;
		this.caption = caption;
		this.processReadOnly = processReadOnly;
	}

	String getName() {
		return name;
	}

	String getCaption() {
		return caption;
	}

	boolean isProcessReadOnly() {
		return processReadOnly;
	}
}
