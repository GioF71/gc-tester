package eu.sia.demo.mem.usage.util.impl;

import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import eu.sia.demo.mem.usage.util.TimeUtil;

@Component
public class TimeUtilImpl implements TimeUtil {

	private final Function<Calendar, String> toTimeStampFunction = new Function<Calendar, String>() {

		@Override
		public String apply(Calendar t) {
			return String.format("%02d:%02d:%02d.%03d", 
				t.get(Calendar.HOUR_OF_DAY),
				t.get(Calendar.MINUTE),
				t.get(Calendar.SECOND),
				t.get(Calendar.MILLISECOND));
		}
	};

	@Override
	public Float nanoToMilli(Long nanoDelta) {
		return Optional.ofNullable(nanoDelta)
			.map(n -> n / 1000000.0f)
			.orElse(null);
	}
	
	@Override
	public long getLowest(int timeDelta, TimeUnit timeunit) {
		long lowest = System.nanoTime();
		lowest -= timeunit.toMicros(timeDelta) * 1000;
		return lowest;
	}

	@Override
	public String toTimeStamp(Calendar calendar) {
		return toTimeStampFunction.apply(calendar);
	}

	@Override
	public Function<Calendar, String> getToTimeStampFunction() {
		return toTimeStampFunction;
	}
}
