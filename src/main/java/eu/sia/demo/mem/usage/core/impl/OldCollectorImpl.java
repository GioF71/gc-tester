package eu.sia.demo.mem.usage.core.impl;

//@Component
public class OldCollectorImpl {}
//public class OldCollectorImpl implements Collector {
//	
//	private final Logger logger = Logger.getLogger(OldCollectorImpl.class.getName());
//
//	@Autowired
//	private StatisticEntryCreator creator;
//
//	private final TreeSet<StatisticEntry> entryList = new TreeSet<>();
//
//	@Override
//	public void add(StatisticEntry entry) {
//		boolean notOverwriting = true;
//		synchronized(entryList) {
//			notOverwriting = entryList.add(entry);
//		}
//		if (!notOverwriting) {
//			logger.severe("Overwriting");
//		}
//	}
//
//	@Override
//	public List<StatisticEntry> getLastEntries(int timeDelta, TimeUnit timeunit, boolean clean) {
//		long lowest = System.nanoTime();
//		lowest -= timeunit.toMicros(timeDelta) * 1000;
//		List<StatisticEntry> list = new ArrayList<>();
//		synchronized(entryList) {
//			Iterator<StatisticEntry> it = entryList.descendingIterator();
//			while (it.hasNext()) {
//				StatisticEntry current = it.next();
//				if (current.getCreationNanotime() >= lowest) {
//					list.add(current);
//				}
//			}
//		}
//		return list;
//	}
//
//	@Override
//	public void purgeBefore(int timeDelta, TimeUnit timeunit) {
//		long lowest = System.nanoTime();
//		lowest -= timeunit.toMicros(timeDelta) * 1000;
//		purgeBefore(lowest);
//	}
//
//	@Override
//	public void purgeBefore(long nanotime) {
//		synchronized(entryList) {
//			Iterator<StatisticEntry> it = entryList.iterator();
//			boolean found = false;
//			while (!found && it.hasNext()) {
//				StatisticEntry current = it.next();
//				if (current.getCreationNanotime() >= nanotime) {
//					found = true;
//				} else {
//					it.remove();
//				}
//			}
//		}
//	}
//
//	@Override
//	public int size() {
//		synchronized(entryList) {
//			return entryList.size();
//		}
//	}
//
//	@Override
//	public StatisticEntryCreator getEntryCreator() {
//		return creator;
//	}
//}
