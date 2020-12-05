package com.giof71.demo.mem.usage.util;

import java.util.function.Function;

public interface TextToPositiveIntegerOrZero {
	Function<String, Integer> getFunction();
	int convert(String text);
}
