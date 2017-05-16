/**
 * 
 */
package de.bitech.jcache;

import java.util.List;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListenerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author awi
 * @date 10.02.2017
 * @since 0.0.1-SNAPSHOT
 *
 */
public class MyCacheEntryListener implements CacheEntryCreatedListener<Integer, List<Integer>>, CacheEntryExpiredListener<Integer, List<Integer>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(MyCacheEntryListener.class);

	@Override
	public void onCreated(Iterable<CacheEntryEvent<? extends Integer, ? extends List<Integer>>> pEvents) 
			throws CacheEntryListenerException {
		pEvents.forEach(event -> LOGGER.info("listen to {}, key:{}, value:{}", event.getEventType(), event.getKey(), event.getValue()));
	};
	
	@Override
	public void onExpired(Iterable<CacheEntryEvent<? extends Integer, ? extends List<Integer>>> pEvents)
			throws CacheEntryListenerException {
		pEvents.forEach(event -> LOGGER.info("listen to {}, key:{}, value:{}", event.getEventType(), event.getKey(), event.getValue()));
	}
}
