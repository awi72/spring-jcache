/**
 * 
 */
package de.bitech.jcache;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author awi
 * @date 26.01.2017
 * @since 0.0.1-SNAPSHOT
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class FirstCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(FirstCache.class);

	private MySlowlyService service = new MySlowlyService();

	@Test
	public void foo() {
		Caching.getCachingProviders().forEach(x -> LOGGER.info("default caching provider:{}", x.getDefaultURI()));

		// +++++++++++++++++++++ CachingProvider +++++++++++++++++++++++++++++++++++++++
		CachingProvider cachingProvider = Caching.getCachingProvider();
		// CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
		// CachingProvider cachingProvider = Caching.getCachingProvider("org.infinispan.jcache.embedded.JCachingProvider");

		// +++++++++++++++++++++ CacheManager +++++++++++++++++++++++++++++++++++++++
		CacheManager cacheManager = cachingProvider.getCacheManager();

		MutableConfiguration<Integer, List<Integer>> configuration = new MutableConfiguration<>();
		// configuration.setTypes(Integer.class, List.class);
		configuration.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 3L)));
		CacheEntryListenerConfiguration<Integer, List<Integer>> cacheEntryListenerConfiguration = new MutableCacheEntryListenerConfiguration<Integer, List<Integer>>(
				FactoryBuilder.factoryOf(MyCacheEntryListener.class), null, false, false);
		configuration.addCacheEntryListenerConfiguration(cacheEntryListenerConfiguration);

		// +++++++++++++++++++++ Cache +++++++++++++++++++++++++++++++++++++++
		Cache<Integer, List<Integer>> cache = cacheManager.createCache("myCache", configuration);

		Integer key1 = 2147483578;
		serviceCallWithCache(cache, key1);

		Integer key2 = 12345;
		serviceCallWithCache(cache, key2);

		cache.forEach(entry -> LOGGER.info("Key:{}, Value:{}", entry.getKey(), entry.getValue()));
		Assert.assertEquals("Value 1 from Cache", Arrays.asList(2, 1073741789), cache.get(key1));
		Assert.assertEquals("Value 2 from Cache", Arrays.asList(3, 5, 823), cache.get(key2));

		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertNull("Cache has expired", cache.get(key1));
		Assert.assertNull("Cache has expired", cache.get(key2));
		
		LOGGER.info("+++ end +++");
	}

	private void serviceCallWithCache(Cache<Integer, List<Integer>> cache, Integer key1) {
		for (int i = 1; i < 4; i++) {
			List<Integer> val1 = cache.get(key1);
			if (val1 == null) {
				val1 = this.service.factorization(key1);
				cache.put(key1, val1);
			}
			LOGGER.info("loop:{}, number:{}, primes:{}", i, key1, val1);
		}
	}

}
