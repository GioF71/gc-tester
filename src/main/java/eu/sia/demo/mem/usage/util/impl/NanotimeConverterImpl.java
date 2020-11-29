package eu.sia.demo.mem.usage.util.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.util.NanotimeConverter;

@Component
public class NanotimeConverterImpl implements NanotimeConverter {

	@Override
	public Float nanoToMilli(Long nanoDelta) {
		return Optional.ofNullable(nanoDelta)
			.map(n -> n / 1000000.0f)
			.orElse(null);
	}
}
