package eu.sia.demo.mem.usage.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public interface TimeUtil {
	long getLowest(int timeDelta, TimeUnit timeunit);
	Float nanoToMilli(Long nanoDelta);
	String toTimeStamp(Calendar calendar);
	Function<Calendar, String> getToTimeStampFunction();
}
