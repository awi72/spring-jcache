/**
 * 
 */
package de.bitech.javaday.spring.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.event.CacheEntryListener;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author awi
 * @date 17.02.2017
 * @since 0.0.1-SNAPSHOT
 *
 */
public class JCacheManagerFactoryBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(JCacheManagerFactoryBean.class);
	public static final String CACHE_NAME = "myCache";

	public CacheManager buildJCacheCacheManager() {
		CachingProvider cachingProvider = Caching.getCachingProvider();
		CacheManager ret = cachingProvider.getCacheManager();

		MutableConfiguration<String, List<Integer>> configuration = new MutableConfiguration<>();
		// configuration.setTypes(String.class, Integer.class);
		configuration.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.HOURS, 1L)));
		
		CacheEntryListenerConfiguration<String, List<Integer>> cacheEntryListenerConfiguration = 
				new MutableCacheEntryListenerConfiguration<String, List<Integer>>(
						(Factory<? extends CacheEntryListener<? super String, ? super List<Integer>>>) FactoryBuilder.factoryOf(MyCacheEntryListener.class), null, false, false);
		configuration.addCacheEntryListenerConfiguration(cacheEntryListenerConfiguration);

		Cache<String, List<Integer>> myCache = ret.createCache(CACHE_NAME, configuration);
		LOGGER.info("Cache:{} created", myCache.getName());

		return ret;
	}

}
