package com.giof71.demo.mem.usage.util.impl;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.giof71.demo.mem.usage.util.TextToIntegerOrZero;
import com.giof71.demo.mem.usage.util.TextToPositiveIntegerOrZero;

@Component
public class TextToPositiveIntegerOrZeroImpl implements TextToPositiveIntegerOrZero {

	@Autowired
	private TextToIntegerOrZero underlying;
	
	private final Function<String, Integer> f = new Function<String, Integer>() {
		
		@Override
		public Integer apply(String t) {
			return Optional.of(underlying.convert(t))
				.filter(x -> x >= 0)
				.orElse(Integer.valueOf(0));
		}
	};
	
	@Override
	public Function<String, Integer> getFunction() {
		return f;
	}

	@Override
	public int convert(String text) {
		return getFunction().apply(text);
	}

}
