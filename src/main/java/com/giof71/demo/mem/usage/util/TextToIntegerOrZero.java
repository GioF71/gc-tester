package com.giof71.demo.mem.usage.util;

import java.util.function.Function;

public interface TextToIntegerOrZero {
	Function<String, Integer> getFunction();
	int convert(String text);
}
