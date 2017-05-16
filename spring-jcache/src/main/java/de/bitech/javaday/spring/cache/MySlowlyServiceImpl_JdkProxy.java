/**
 * 
 */
package de.bitech.javaday.spring.cache;

import java.util.ArrayList;
import java.util.List;

import javax.cache.annotation.CacheResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author awi
 * @since 03.07.2016
 * @version 0.0.1-SNAPSHOT
 *
 */
public class MySlowlyServiceImpl_JdkProxy implements MySlowlyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySlowlyServiceImpl_JdkProxy.class);

	// @Cacheable(cacheNames = { JCacheManagerFactoryBean.CACHE_NAME }, condition="#pNumber > 1000000")
	@CacheResult(cacheName = JCacheManagerFactoryBean.CACHE_NAME)
	@Override
	public List<Integer> factorization(int pNumber) {
		LOGGER.info("zerlege in Primfaktoren {} ...", pNumber);
		List<Integer> ret = fractionize(pNumber);
		LOGGER.info("Primefaktoren fuer {} sind {}", pNumber, ret);
		return ret;
	}

	// @CacheResult(cacheName = JCacheManagerFactoryBean.CACHE_NAME)
	private List<Integer> fractionize(int pNumber) {
		LOGGER.info("MARKE");
		List<Integer> ret = new ArrayList<>();
		int tmpValue = pNumber;

		for (int divisor = 2; tmpValue > 1; divisor++) {
			while (tmpValue % divisor == 0) {
				ret.add(divisor);
				tmpValue /= divisor;
			}
		}

		return ret;
	}
}
