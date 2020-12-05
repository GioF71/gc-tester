package com.giof71.demo.mem.usage.util.impl;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.util.TextToIntegerOrZero;

@Component
public class TextToIntegerOrZeroImpl implements TextToIntegerOrZero {
	
	private final Function<String, Integer> f = new Function<String, Integer>() {
		
		@Override
		public Integer apply(String t) {
			Integer result = 0;
			try {
				result = Integer.parseInt(t);
			} catch (NumberFormatException nfExc) {
			}
			return result;
		}
	};

	@Override
	public int convert(String text) {
		return getFunction().apply(text);
	}

	@Override
	public Function<String, Integer> getFunction() {
		return f;
	}

}
