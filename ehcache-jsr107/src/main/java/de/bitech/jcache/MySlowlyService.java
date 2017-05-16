/**
 * 
 */
package de.bitech.jcache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author awi
 * @date 11.03.2017
 * @since 0.0.1-SNAPSHOT
 *
 */
public class MySlowlyService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MySlowlyService.class);
	
	public List<Integer> factorization(int pNumber) {
		LOGGER.info("zerlege in Primfaktoren {} ...", pNumber);
		List<Integer> ret = fractionize(pNumber);
		LOGGER.info("Primefaktoren fuer {} sind {}", pNumber, ret);
		return ret;
	}

	private List<Integer> fractionize(int pNumber) {
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
