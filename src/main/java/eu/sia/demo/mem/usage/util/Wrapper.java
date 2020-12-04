package eu.sia.demo.mem.usage.util;

public class Wrapper<T> {
	
	private T value;
	
	public void set(T value) {
		this.value = value;
	}
	
	public T get() {
		return this.value;
	}
}
