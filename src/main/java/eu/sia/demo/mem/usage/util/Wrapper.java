package eu.sia.demo.mem.usage.util;

public class Wrapper<T> {
	
	private T value;
	
	void set(T value) {
		this.value = value;
	}
	
	T get() {
		return this.value;
	}
}
